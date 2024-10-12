package com.bobmowzie.mowziesmobs.client.sound;

import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public final class BlackPinkSound extends AbstractTickableSoundInstance {
    private final AbstractMinecart minecart;

    public BlackPinkSound(AbstractMinecart minecart) {
        super(MMSounds.MUSIC_BLACK_PINK.get(), SoundSource.NEUTRAL, SoundInstance.createUnseededRandom());
        this.minecart = minecart;
    }

    @Override
    public void tick() {
        if (minecart.isAlive()) {
            x = (float) minecart.getX();
            y = (float) minecart.getY();
            z = (float) minecart.getZ();
        } else {
            stop();
        }
    }
}
