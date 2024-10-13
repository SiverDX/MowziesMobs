package com.bobmowzie.mowziesmobs.server.message.mouse;

import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.ability.PlayerAbility;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.PlayerCapability;
import com.bobmowzie.mowziesmobs.server.power.Power;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

/**
 * Created by BobMowzie on 5/25/2017.
 */
public record MessageRightMouseUp() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<MessageRightMouseUp> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "message_right_mouse_up"));
    public static final StreamCodec<ByteBuf, MessageRightMouseUp> STREAM_CODEC = StreamCodec.unit(new MessageRightMouseUp());

    public static void handleServer(final MessageRightMouseUp packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            PlayerCapability.IPlayerCapability capability = CapabilityHandler.getCapability(player, CapabilityHandler.PLAYER_CAPABILITY);

            if (capability != null) {
                capability.setMouseRightDown(false);

                for (Power power : capability.getPowers()) {
                    power.onRightMouseUp(player);
                }
            }

            AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);

            if (abilityCapability != null) {
                for (Ability<?> ability : abilityCapability.getAbilities()) {
                    if (ability instanceof PlayerAbility playerAbility) {
                        playerAbility.onRightMouseUp(player);
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