package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.PlayerCapability;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import java.util.List;

public class ItemWroughtAxe extends AxeItem {

    public ItemWroughtAxe(Item.Properties properties) {
        super(Tiers.IRON, properties);
    }

    @Override
    public boolean isValidRepairItem(ItemStack itemStack, ItemStack itemStackMaterial) {
        return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.breakable.get() && super.isValidRepairItem(itemStack, itemStackMaterial);
    }

    @Override
    public boolean isEnchantable(ItemStack p_77616_1_) {
        return true;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        PlayerCapability.Capability playerCapability = CapabilityHandler.getCapability(player, CapabilityHandler.PLAYER_CAPABILITY);
        return playerCapability == null || (!playerCapability.getAxeCanAttack() && playerCapability.getUntilAxeSwing() > 0);
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity, InteractionHand hand) { // FIXME 1.21 :: now has the hand -> the point where this is called seems to be the same though ('LivingEntity#swing' always had hand context)
        PlayerCapability.Capability playerCapability = CapabilityHandler.getCapability(entity, CapabilityHandler.PLAYER_CAPABILITY);
        return playerCapability != null && playerCapability.getUntilAxeSwing() > 0;
    }

    @Override
    public boolean hurtEnemy(ItemStack heldItemStack, LivingEntity entityHit, LivingEntity attacker) {
        if (ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.breakable.get()) heldItemStack.hurtAndBreak(2, attacker, LivingEntity.getSlotForHand(InteractionHand.MAIN_HAND));
        if (!entityHit.level().isClientSide) {
            entityHit.playSound(SoundEvents.ANVIL_LAND, 0.3F, 0.5F);
        }
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (hand == InteractionHand.MAIN_HAND && player.getAttackStrengthScale(0.5F) == 1.0f) {
            PlayerCapability.Capability playerCapability = CapabilityHandler.getCapability(player, CapabilityHandler.PLAYER_CAPABILITY);
            if (playerCapability != null && playerCapability.getUntilAxeSwing() <= 0) {
                boolean verticalAttack = player.isShiftKeyDown() && player.onGround();
                if (verticalAttack)
                    AbilityHandler.INSTANCE.sendAbilityMessage(player, AbilityHandler.WROUGHT_AXE_SLAM_ABILITY);
                else
                    AbilityHandler.INSTANCE.sendAbilityMessage(player, AbilityHandler.WROUGHT_AXE_SWING_ABILITY);
                playerCapability.setVerticalSwing(verticalAttack);
                playerCapability.setUntilAxeSwing(30);
                player.startUsingItem(hand);
                if (ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.breakable.get() && !player.getAbilities().instabuild) player.getItemInHand(hand).hurtAndBreak(2, player, LivingEntity.getSlotForHand(hand));
            }
            return new InteractionResultHolder<ItemStack>(InteractionResult.SUCCESS, player.getItemInHand(hand));
        }
        return super.use(world, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, context, tooltip, flagIn);
        tooltip.add(Component.translatable(getDescriptionId() + ".text.0").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(Component.translatable(getDescriptionId() + ".text.1").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(Component.translatable(getDescriptionId() + ".text.2").setStyle(ItemHandler.TOOLTIP_STYLE));
    }
}
