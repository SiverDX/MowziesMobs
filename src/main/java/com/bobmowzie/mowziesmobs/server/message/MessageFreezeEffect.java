package com.bobmowzie.mowziesmobs.server.message;

import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.FrozenCapability;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

/**
 * Created by BobMowzie on 5/31/2017.
 */
public record MessageFreezeEffect(int entityId, boolean isFrozen) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<MessageFreezeEffect> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "message_freeze_effect"));
    public static final StreamCodec<ByteBuf, MessageFreezeEffect> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            MessageFreezeEffect::entityId,
            ByteBufCodecs.BOOL,
            MessageFreezeEffect::isFrozen,
            MessageFreezeEffect::new
    );

    public static void handleClient(final MessageFreezeEffect packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Entity entity = Minecraft.getInstance().level.getEntity(packet.entityId());

            if (entity instanceof LivingEntity living) {
                AbilityCapability.Capability abilityCapability = CapabilityHandler.getCapability(living, CapabilityHandler.ABILITY_CAPABILITY);

                if (abilityCapability != null) {
                    FrozenCapability.Capability capability = CapabilityHandler.getCapability(living, CapabilityHandler.FROZEN_CAPABILITY);

                    if (capability != null) {
                        if (packet.isFrozen()) {
                            capability.onFreeze(living);
                        } else {
                            capability.onUnfreeze(living);
                        }
                    }
                }
            }
        });
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
