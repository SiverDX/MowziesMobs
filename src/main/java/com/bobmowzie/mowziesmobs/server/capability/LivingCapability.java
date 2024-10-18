package com.bobmowzie.mowziesmobs.server.capability;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LivingCapability {
    public static class Capability implements INBTSerializable<CompoundTag> {
        float lastDamage = 0;
        boolean hasSunblock;

        public void setLastDamage(float damage) {
            lastDamage = damage;
        }

        public float getLastDamage() {
            return lastDamage;
        }

        public void setHasSunblock(boolean hasSunblock) {
            this.hasSunblock = hasSunblock;
        }

        public boolean getHasSunblock() {
            return hasSunblock;
        }

        public void tick(LivingEntity entity) {
//            if (!hasSunblock && entity.isPotionActive(EffectHandler.SUNBLOCK)) hasSunblock = true;
        }

        @Override
        public CompoundTag serializeNBT(@NotNull HolderLookup.Provider lookup) {
            return new CompoundTag();
        }

        @Override
        public void deserializeNBT(@NotNull HolderLookup.Provider lookup, @NotNull CompoundTag nbt) {
        }
    }

    public static class Provider implements ICapabilityProvider<Entity, Void, LivingCapability.Capability> {
        @Override
        public @Nullable LivingCapability.Capability getCapability(@NotNull Entity entity, Void context) {
            if (entity instanceof LivingEntity) {
                return entity.getCapability(CapabilityHandler.LIVING_CAPABILITY);
            }

            return null;
        }
    }
}
