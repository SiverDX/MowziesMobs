package com.bobmowzie.mowziesmobs.server.message;

import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record MessageUseAbility(int entityId, int index) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<MessageUseAbility> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "message_use_ability"));
    public static final StreamCodec<ByteBuf, MessageUseAbility> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            MessageUseAbility::entityId,
            ByteBufCodecs.INT,
            MessageUseAbility::index,
            MessageUseAbility::new
    );

    public static void handleClient(final MessageUseAbility packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().level.getEntity(packet.entityId()) instanceof LivingEntity entity) {
                AbilityCapability.Capability abilityCapability = CapabilityHandler.getCapability(entity, CapabilityHandler.ABILITY_CAPABILITY);

                if (abilityCapability != null) {
                    abilityCapability.activateAbility(entity, abilityCapability.getAbilityTypesOnEntity(entity)[packet.index()]);
                }
            }
        });
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
