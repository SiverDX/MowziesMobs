package com.bobmowzie.mowziesmobs.server.message;

import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySunstrike;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

// FIXME 1.21 :: unused?
public record MessagePlayerSummonSunstrike() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<MessagePlayerSummonSunstrike> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "message_player_summon_sunstrike"));
    public static final StreamCodec<ByteBuf, MessagePlayerSummonSunstrike> STREAM_CODEC = StreamCodec.unit(new MessagePlayerSummonSunstrike());

    private static final double REACH = 15;

    private static BlockHitResult rayTrace(LivingEntity entity, double reach) {
        Vec3 pos = entity.getEyePosition(0);
        Vec3 segment = entity.getLookAngle();
        segment = pos.add(segment.x * reach, segment.y * reach, segment.z * reach);
        return entity.level().clip(new ClipContext(pos, segment, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));
    }

    public static void handleServer(final MessagePlayerSummonSunstrike packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            BlockHitResult raytrace = rayTrace(player, REACH);

            if (raytrace.getType() == HitResult.Type.BLOCK && raytrace.getDirection() == Direction.UP && player.getInventory().getSelected().isEmpty() && player.hasEffect(EffectHandler.SUNS_BLESSING)) {
                BlockPos hit = raytrace.getBlockPos();
                EntitySunstrike sunstrike = new EntitySunstrike(EntityHandler.SUNSTRIKE.get(), player.level(), player, hit.getX(), hit.getY(), hit.getZ());
                sunstrike.onSummon();
                player.level().addFreshEntity(sunstrike);
            }
        });
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
