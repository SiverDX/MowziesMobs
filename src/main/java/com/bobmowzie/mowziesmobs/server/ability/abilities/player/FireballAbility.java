package com.bobmowzie.mowziesmobs.server.ability.abilities.player;

import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.ability.PlayerAbility;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.phys.Vec3;

public class FireballAbility extends PlayerAbility {
    public FireballAbility(AbilityType<Player, FireballAbility> abilityType, Player user) {
        super(abilityType, user, new AbilitySection[] {
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, 20),
                new AbilitySection.AbilitySectionInstant(AbilitySection.AbilitySectionType.ACTIVE)
        }, 20);
    }

    @Override
    public void tick() {
        super.tick();
        if (getTicksInUse() == 20) {
            LivingEntity user = getUser();
            Vec3 lookVec = user.getLookAngle();
            SmallFireball smallfireballentity = new SmallFireball(user.level(), user, lookVec);
            smallfireballentity.setPos(smallfireballentity.getX(), user.getY(0.5D) + 0.5D, smallfireballentity.getZ());
            user.level().addFreshEntity(smallfireballentity);
        }
    }
}
