package com.bobmowzie.mowziesmobs.server.message;

import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySolarBeam;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

// FIXME 1.21 :: unused?
public record MessagePlayerSolarBeam() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<MessagePlayerSolarBeam> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "message_player_solar_beam"));
    public static final StreamCodec<ByteBuf, MessagePlayerSolarBeam> STREAM_CODEC = StreamCodec.unit(new MessagePlayerSolarBeam());

    public static void handleServer(final MessagePlayerSolarBeam packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();

            EntitySolarBeam solarBeam = new EntitySolarBeam(EntityHandler.SOLAR_BEAM.get(), player.level(), player, player.getX(), player.getY() + 1.2f, player.getZ(), (float) ((player.yHeadRot + 90) * Math.PI / 180), (float) (-player.getXRot() * Math.PI / 180), 55);
            solarBeam.setHasPlayer(true);
            player.level().addFreshEntity(solarBeam);

            if (player.hasEffect(EffectHandler.SUNS_BLESSING)) {
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 2, false, false));
                int duration = player.getEffect(EffectHandler.SUNS_BLESSING).getDuration();
                player.removeEffect(EffectHandler.SUNS_BLESSING);

                int solarBeamCost = ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SUNS_BLESSING.solarBeamCost.get() * 60 * 20;

                if (duration - solarBeamCost > 0) {
                    player.addEffect(new MobEffectInstance(EffectHandler.SUNS_BLESSING, duration - solarBeamCost, 0, false, false));
                }
            }
        });
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
