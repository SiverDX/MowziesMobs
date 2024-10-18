package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.bobmowzie.mowziesmobs.server.util.EnchantmentUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.EventHooks;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Predicate;

public class ItemBlowgun extends BowItem {
    public static final Predicate<ItemStack> DARTS = (p_220002_0_) -> p_220002_0_.getItem() == ItemHandler.DART.get();

    public ItemBlowgun(Item.Properties properties) {
        super(properties);
    }

    // FIXME 1.21 :: maybe it can be simplified with the context changes ('createArrow' has weapon stack, same for 'customArrow')?
    // FIXME 1.21 :: if not needs to be adapted to vanilla changes (e.g. 'draw' call which uses 'EnchantmentHelper.processProjectileCount' to support multiple projectiles)
    // FIXME 1.21 :: flame e.g. gets applied in the abstract arrow constructor through 'EnchantmentHelper.onProjectileSpawned' (pierce is checked there too)
    // FIXME 1.21 :: knockback (and related enchantments) are handled in the 'doKnockback' method (called in 'onHitEntity' from abstract arrow)
    @Override
    public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft)
    {
        if (entityLiving instanceof Player player) {
            boolean flag = player.getAbilities().instabuild || EnchantmentUtils.getLevel(Enchantments.INFINITY, player.level(), stack) > 0;
            ItemStack itemstack = player.getProjectile(stack);

            int i = this.getUseDuration(stack, player) - timeLeft;
            i = EventHooks.onArrowLoose(stack, worldIn, player, i, !itemstack.isEmpty() || flag);
            if (i < 0) return;

            if (!itemstack.isEmpty() || flag) {
                if (itemstack.isEmpty()) {
                    itemstack = new ItemStack(Items.ARROW);
                }

                float f = getArrowVelocity(i);
                if (!((double)f < 0.1D)) {
                    boolean flag1 = player.getAbilities().instabuild || (itemstack.getItem() instanceof ItemDart && ((ItemDart)itemstack.getItem()).isInfinite(itemstack, stack, player));
                    if (!worldIn.isClientSide) {
                        ArrowItem arrowitem = (ArrowItem)(itemstack.getItem() instanceof ItemDart ? itemstack.getItem() : ItemHandler.DART.get());
                        AbstractArrow abstractarrowentity = arrowitem.createArrow(worldIn, itemstack, player, stack);
                        abstractarrowentity = customArrow(abstractarrowentity, itemstack, stack);
                        abstractarrowentity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, f * 1.1F /*ALTERED FROM PARENT*/, 1.0F);
                        if (f == 1.0F) {
                            abstractarrowentity.setCritArrow(true);
                        }

                        int j = EnchantmentUtils.getLevel(Enchantments.POWER, player.level(), stack);
                        if (j > 0) {
                            abstractarrowentity.setBaseDamage(abstractarrowentity.getBaseDamage() + (double)j * 0.5D + 0.5D);
                        }

                        int k = EnchantmentUtils.getLevel(Enchantments.PUNCH, player.level(), stack);
                        if (k > 0) {
                            abstractarrowentity.setKnockback(k);
                        }

                        if (EnchantmentUtils.getLevel(Enchantments.FLAME, player.level(), stack) > 0) {
                            abstractarrowentity.igniteForSeconds(100);
                        }

                        stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(entityLiving.getUsedItemHand()));
                        if (flag1 || player.getAbilities().instabuild && (itemstack.getItem() == Items.SPECTRAL_ARROW || itemstack.getItem() == Items.TIPPED_ARROW)) {
                            abstractarrowentity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                        }

                        worldIn.addFreshEntity(abstractarrowentity);
                    }

                    worldIn.playSound(null, player.getX(), player.getY(), player.getZ(), MMSounds.ENTITY_UMVUTHANA_BLOWDART.get(), SoundSource.PLAYERS, 1.0F, 1.0F / (player.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F); //CHANGED FROM PARENT CLASS
                    if (!flag1 && !player.getAbilities().instabuild) {
                        itemstack.shrink(1);
                        if (itemstack.isEmpty()) {
                            player.getInventory().removeItem(itemstack);
                        }
                    }

                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }

    public static float getArrowVelocity(int charge) {
        float f = (float)charge / 5.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return 72000;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, context, tooltip, flagIn);
        tooltip.add(Component.translatable(getDescriptionId() + ".text.0").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(Component.translatable(getDescriptionId() + ".text.1").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(Component.translatable(getDescriptionId() + ".text.2").setStyle(ItemHandler.TOOLTIP_STYLE));
    }

    /**
     * Get the predicate to match ammunition when searching the player's inventory, not their main/offhand
     */
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return DARTS;
    }

    @Override
    public Predicate<ItemStack> getSupportedHeldProjectiles() {
        return DARTS;
    }
}
