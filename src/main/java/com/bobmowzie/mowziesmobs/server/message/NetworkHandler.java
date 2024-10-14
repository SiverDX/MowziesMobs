package com.bobmowzie.mowziesmobs.server.message;

import com.bobmowzie.mowziesmobs.server.message.mouse.MessageLeftMouseDown;
import com.bobmowzie.mowziesmobs.server.message.mouse.MessageLeftMouseUp;
import com.bobmowzie.mowziesmobs.server.message.mouse.MessageRightMouseDown;
import com.bobmowzie.mowziesmobs.server.message.mouse.MessageRightMouseUp;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class NetworkHandler {
    public static final String PROTOCOL_VERSION = "1";

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION);

        // -> Client
        registrar.playToClient(MessageUseAbility.TYPE, MessageUseAbility.STREAM_CODEC, MessageUseAbility::handleClient);
        registrar.playToClient(MessageUpdateBossBar.TYPE, MessageUpdateBossBar.STREAM_CODEC, MessageUpdateBossBar::handleClient);
        registrar.playToClient(MessageSunblockEffect.TYPE, MessageSunblockEffect.STREAM_CODEC, MessageSunblockEffect::handleClient);
        registrar.playToClient(MessageLinkEntities.TYPE, MessageLinkEntities.STREAM_CODEC, MessageLinkEntities::handleClient);
        registrar.playToClient(MessageJumpToAbilitySection.TYPE, MessageJumpToAbilitySection.STREAM_CODEC, MessageJumpToAbilitySection::handleClient);
        registrar.playToClient(MessageInterruptAbility.TYPE, MessageInterruptAbility.STREAM_CODEC, MessageInterruptAbility::handleClient);
        registrar.playToClient(MessageFreezeEffect.TYPE, MessageFreezeEffect.STREAM_CODEC, MessageFreezeEffect::handleClient);
        registrar.playToClient(MessageBlackPinkInYourArea.TYPE, MessageBlackPinkInYourArea.STREAM_CODEC, MessageBlackPinkInYourArea::handleClient);

        // -> Server
        registrar.playToServer(MessageUmvuthiTrade.TYPE, MessageUmvuthiTrade.STREAM_CODEC, MessageUmvuthiTrade::handleServer);
        registrar.playToServer(MessageSculptorTrade.TYPE, MessageSculptorTrade.STREAM_CODEC, MessageSculptorTrade::handleServer);
        registrar.playToServer(MessagePlayerUseAbility.TYPE, MessagePlayerUseAbility.STREAM_CODEC, MessagePlayerUseAbility::handleServer);
        registrar.playToServer(MessagePlayerSummonSunstrike.TYPE, MessagePlayerSummonSunstrike.STREAM_CODEC, MessagePlayerSummonSunstrike::handleServer);
        registrar.playToServer(MessagePlayerSolarBeam.TYPE, MessagePlayerSolarBeam.STREAM_CODEC, MessagePlayerSolarBeam::handleServer);
        registrar.playToServer(MessagePlayerAttackMob.TYPE, MessagePlayerAttackMob.STREAM_CODEC, MessagePlayerAttackMob::handleServer);
        registrar.playToServer(MessageRightMouseUp.TYPE, MessageRightMouseUp.STREAM_CODEC, MessageRightMouseUp::handleServer);
        registrar.playToServer(MessageRightMouseDown.TYPE, MessageRightMouseDown.STREAM_CODEC, MessageRightMouseDown::handleServer);
        registrar.playToServer(MessageLeftMouseUp.TYPE, MessageLeftMouseUp.STREAM_CODEC, MessageLeftMouseUp::handleServer);
        registrar.playToServer(MessageLeftMouseDown.TYPE, MessageLeftMouseDown.STREAM_CODEC, MessageLeftMouseDown::handleServer);
    }
}