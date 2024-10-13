package com.bobmowzie.mowziesmobs.server.message;

import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.LivingCapability;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

/**
 * Created by BobMowzie on 5/31/2017.
 */
public record MessageSunblockEffect(int entityId, boolean hasSunBlock) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<MessageSunblockEffect> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "message_sunblock_effect"));

    public static final StreamCodec<ByteBuf, MessageSunblockEffect> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            MessageSunblockEffect::entityId,
            ByteBufCodecs.BOOL,
            MessageSunblockEffect::hasSunBlock,
            MessageSunblockEffect::new
    );

    public static void handleClient(final MessageSunblockEffect packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().level.getEntity(packet.entityId()) instanceof LivingEntity entity) {
                LivingCapability.ILivingCapability livingCapability = CapabilityHandler.getCapability(entity, CapabilityHandler.LIVING_CAPABILITY);

                if (livingCapability != null) {
                    livingCapability.setHasSunblock(packet.hasSunBlock());
                }
            }
        });
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
