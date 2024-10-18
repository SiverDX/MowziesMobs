package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.client.model.LayerHandler;
import com.bobmowzie.mowziesmobs.client.model.armor.WroughtHelmModel;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class ItemWroughtHelm extends MowzieArmorItem {
    public ItemWroughtHelm(Item.Properties properties) {
        super(MaterialHandler.ARMOR_WROUGHT_HELM.value(), Type.HELMET, properties);
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        if (ConfigHandler.COMMON.TOOLS_AND_ABILITIES.WROUGHT_HELM.breakable.get()) return super.isValidRepairItem(toRepair, repair);
        return false;
    }

    @Override
    public boolean isEnchantable(ItemStack p_77616_1_) {
        return true;
    }

    @Override
    public boolean canBeDepleted() {
        return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.WROUGHT_HELM.breakable.get();
    }

    @Override
    public int getDamage(ItemStack stack) {
        return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.WROUGHT_HELM.breakable.get() ? super.getDamage(stack): 0;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.WROUGHT_HELM.breakable.get() ? super.getMaxDamage(stack): 0;
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        if (ConfigHandler.COMMON.TOOLS_AND_ABILITIES.WROUGHT_HELM.breakable.get()) super.setDamage(stack, damage);
    }

    @Override
    public @Nullable ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
        return ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "textures/item/wrought_helmet.png");
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, context, tooltip, flagIn);
        tooltip.add(Component.translatable(getDescriptionId() + ".text.0").setStyle(ItemHandler.TOOLTIP_STYLE));
    }

    @Override
    public ConfigHandler.ArmorConfig getConfig() {
        return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.WROUGHT_HELM.armorConfig;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(ArmorRender.INSTANCE);
    }

    private static final class ArmorRender implements IClientItemExtensions {
        private static final ArmorRender INSTANCE = new ArmorRender();
        private static HumanoidModel<?> MODEL;

        @Override
        public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> _default) {
            if (MODEL == null) {
                EntityModelSet models = Minecraft.getInstance().getEntityModels();
                ModelPart root = models.bakeLayer(LayerHandler.WROUGHT_HELM_LAYER);
                MODEL = new WroughtHelmModel<>(root);
            }
            return MODEL;
        }
    }
}
