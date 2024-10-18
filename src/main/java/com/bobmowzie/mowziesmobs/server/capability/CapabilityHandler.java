package com.bobmowzie.mowziesmobs.server.capability;

import com.bobmowzie.mowziesmobs.MMCommon;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import javax.annotation.Nullable;

public final class CapabilityHandler {
    public static final EntityCapability<FrozenCapability.Capability, Void> FROZEN_CAPABILITY = EntityCapability.createVoid(ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "frozen_capability"), FrozenCapability.Capability.class);
    public static final EntityCapability<LivingCapability.Capability, Void> LIVING_CAPABILITY = EntityCapability.createVoid(ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "living_capability"), LivingCapability.Capability.class);
    public static final EntityCapability<PlayerCapability.Capability, Void> PLAYER_CAPABILITY = EntityCapability.createVoid(ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "player_capability"), PlayerCapability.Capability.class);
    public static final EntityCapability<AbilityCapability.Capability, Void> ABILITY_CAPABILITY = EntityCapability.createVoid(ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "ability_capability"), AbilityCapability.Capability.class);

    // FIXME 1.21 :: unsure if data attachments ore capabilities are needed (do capabilities retain data? there is no copy on death flag like in attachments so maybe not?)
//    public static final DeferredRegister<AttachmentType<?>> MM_ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES.key(), MMCommon.MODID);

//    public static final DeferredHolder<AttachmentType<?>, AttachmentType<FrozenCapability.Capability>> FROZEN_CAPABILITY = MM_ATTACHMENT_TYPES.register("frozen_capability", () -> AttachmentType.serializable(FrozenCapability.Capability::new).build());
//    public static final DeferredHolder<AttachmentType<?>, AttachmentType<LivingCapability.Capability>> LIVING_CAPABILITY = MM_ATTACHMENT_TYPES.register("living_capability", () -> AttachmentType.serializable(LivingCapability.Capability::new).copyOnDeath().build());
//    public static final DeferredHolder<AttachmentType<?>, AttachmentType<PlayerCapability.Capability>> PLAYER_CAPABILITY = MM_ATTACHMENT_TYPES.register("player_capability", () -> AttachmentType.serializable(PlayerCapability.Capability::new).copyOnDeath().build());
//    public static final DeferredHolder<AttachmentType<?>, AttachmentType<AbilityCapability.Capability>> ABILITY_CAPABILITY = MM_ATTACHMENT_TYPES.register("ability_capability", () -> AttachmentType.serializable(AbilityCapability.Capability::new).copyOnDeath().build());

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerEntity(PLAYER_CAPABILITY, EntityType.PLAYER, new PlayerCapability.Provider());
        event.registerEntity(ABILITY_CAPABILITY, EntityType.PLAYER, new AbilityCapability.Provider());

        MMCommon.getLivingEntityTypes().forEach(type -> {
            event.registerEntity(FROZEN_CAPABILITY, type, new FrozenCapability.Provider());
            event.registerEntity(LIVING_CAPABILITY, type, new LivingCapability.Provider());
        });
    }

    @Nullable
    public static <T> T getCapability(Entity entity, EntityCapability<T, Void> capability) {
        if (entity == null) return null;
        if (entity.isRemoved()) return null;
        return entity.getCapability(capability);
    }
}
