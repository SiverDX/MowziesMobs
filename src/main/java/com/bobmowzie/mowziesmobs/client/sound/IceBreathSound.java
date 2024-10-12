package com.bobmowzie.mowziesmobs.client.sound;

import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.ilexiconn.llibrary.client.model.tools.ControlledAnimation;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;

public class IceBreathSound extends AbstractTickableSoundInstance {
    private final Entity iceBreath;
    int ticksExisted = 0;
    ControlledAnimation volumeControl;
    boolean active = true;

    public IceBreathSound(Entity icebreath) {
        super(MMSounds.ENTITY_FROSTMAW_ICEBREATH.get(), SoundSource.NEUTRAL, SoundInstance.createUnseededRandom());
        this.iceBreath = icebreath;
        volume = 3F;
        pitch = 1f;
        x = (float) icebreath.getX();
        y = (float) icebreath.getY();
        z = (float) icebreath.getZ();
        volumeControl = new ControlledAnimation(10);
        looping = true;
    }

    @Override
    public void tick() {
        if (active) volumeControl.increaseTimer();
        else volumeControl.decreaseTimer();
        volume = volumeControl.getAnimationFraction();
        if (volumeControl.getAnimationFraction() <= 0.05)
            stop();
        if (iceBreath != null) {
            active = true;
            x = (float) iceBreath.getX();
            y = (float) iceBreath.getY();
            z = (float) iceBreath.getZ();
            if (!iceBreath.isAlive()) {
                active = false;
            }
        }
        else {
            active = false;
        }
        ticksExisted++;
    }
}
