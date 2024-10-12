package com.bobmowzie.mowziesmobs.client.sound;

import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;

/**
 * Created by BobMowzie on 1/9/2019.
 */
public class NagaSwoopSound extends AbstractTickableSoundInstance {
    private final Entity naga;
    int ticksExisted = 0;
    boolean active = true;

    public NagaSwoopSound(Entity naga) {
        super(MMSounds.ENTITY_NAGA_SWOOP.get(), SoundSource.HOSTILE, SoundInstance.createUnseededRandom());
        this.naga = naga;
        volume = 2F;
        pitch = 1.2f;
        x = (float) naga.getX();
        y = (float) naga.getY();
        z = (float) naga.getZ();
        looping = false;
    }

    @Override
    public void tick() {
        if (naga != null) {
            active = true;
            x = (float) naga.getX();
            y = (float) naga.getY();
            z = (float) naga.getZ();
            if (!naga.isAlive()) {
                active = false;
                stop();
            }
        }
        ticksExisted++;
    }
}
