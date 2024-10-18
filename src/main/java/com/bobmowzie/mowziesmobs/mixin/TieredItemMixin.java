package com.bobmowzie.mowziesmobs.mixin;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.item.ItemEarthrendGauntlet;
import com.bobmowzie.mowziesmobs.server.item.ItemSculptorStaff;
import com.bobmowzie.mowziesmobs.server.item.ItemWroughtAxe;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.lang.invoke.MethodHandles;

/** Don't override manually set durability (in properties) with default durability from the tier (alternative would be custom tier which is a copy of vanilla with just the durability changed) */
@Mixin(TieredItem.class)
public abstract class TieredItemMixin extends Item {
    public TieredItemMixin(Properties properties) {
        super(properties);
    }

    @WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item$Properties;durability(I)Lnet/minecraft/world/item/Item$Properties;"))
    private static Item.Properties mowziesmobs$skipSetDurability(Item.Properties instance, int maxDamage, Operation<Item.Properties> original) {
        Class<?> thisClass = MethodHandles.lookup().lookupClass();

        // Setting the durability marks the item as damageable through certain data components
        if (thisClass == ItemWroughtAxe.class) {
            return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.breakable.get() ? instance.durability(Tiers.IRON.getUses()) : instance.stacksTo(1);
        }

        if (thisClass == ItemEarthrendGauntlet.class) {
            return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHREND_GAUNTLET.breakable.get() ? instance.durability(ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHREND_GAUNTLET.durabilityValue) : instance.stacksTo(1);
        }

        if (thisClass == ItemSculptorStaff.class) {
            return instance;
        }

        return original.call(instance, maxDamage);
    }
}
