package com.bobmowzie.mowziesmobs.client.sound;

import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySunstrike;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;

public class SunstrikeSound extends AbstractTickableSoundInstance {
    private final EntitySunstrike sunstrike;

    public SunstrikeSound(EntitySunstrike sunstrike) {
        super(MMSounds.SUNSTRIKE.get(), SoundSource.NEUTRAL, SoundInstance.createUnseededRandom());
        this.sunstrike = sunstrike;
        volume = 1.5F;
        pitch = 1.1F;
        x = (float) sunstrike.getX();
        y = (float) sunstrike.getY();
        z = (float) sunstrike.getZ();
    }

    @Override
    public void tick() {
        if (!sunstrike.isAlive()) {
            stop();
        }
    }
}
