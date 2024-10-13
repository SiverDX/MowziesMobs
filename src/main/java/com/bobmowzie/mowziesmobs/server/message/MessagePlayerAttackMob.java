package com.bobmowzie.mowziesmobs.server.message;

import com.bobmowzie.mowziesmobs.MMCommon;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

/**
 * Created by BobMowzie on 10/28/2016.
 */
public record MessagePlayerAttackMob(int entityId) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<MessagePlayerAttackMob> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "message_player_attack_mob"));
    public static final StreamCodec<ByteBuf, MessagePlayerAttackMob> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            MessagePlayerAttackMob::entityId,
            MessagePlayerAttackMob::new
    );

    public static MessagePlayerAttackMob fromEntity(LivingEntity target) {
        return new MessagePlayerAttackMob(target.getId());
    }

    public static void handleServer(final MessagePlayerAttackMob packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            Entity entity = player.level().getEntity(packet.entityId());

            if (entity != null) {
                player.attack(entity);
            }
        });
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
