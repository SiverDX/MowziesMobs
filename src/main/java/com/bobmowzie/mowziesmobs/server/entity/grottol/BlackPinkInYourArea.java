package com.bobmowzie.mowziesmobs.server.entity.grottol;

import com.bobmowzie.mowziesmobs.server.message.MessageBlackPinkInYourArea;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.function.BiConsumer;

public final class BlackPinkInYourArea implements BiConsumer<Level, AbstractMinecart> {
    private BlackPinkInYourArea() {}

    @Override
    public void accept(Level world, AbstractMinecart minecart) {
        /*BlockState state = minecart.getDisplayTile();
        if (state.getBlock() != BlockHandler.GROTTOL.get()) {
            state = BlockHandler.GROTTOL.get().getDefaultState();
            minecart.setDisplayTileOffset(minecart.getDefaultDisplayTileOffset());
        }
        minecart.setDisplayTile(state.with(BlockGrottol.VARIANT, BlockGrottol.Variant.BLACK_PINK));*/
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(minecart, MessageBlackPinkInYourArea.fromMinecraft(minecart));
    }

    public static BlackPinkInYourArea create() {
        return new BlackPinkInYourArea();
    }
}
