package com.bobmowzie.mowziesmobs.server.ai;

import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.entity.MowzieGeckoEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class UseAbilityAI<T extends MowzieGeckoEntity> extends Goal {

    protected final T entity;

    protected AbilityType abilityType;

    public UseAbilityAI(T entity, AbilityType ability) {
        this(entity, ability, true);
    }

    public UseAbilityAI(T entity, AbilityType ability, boolean interruptsAI) {
        this.entity = entity;
        this.abilityType = ability;
        if (interruptsAI) this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (entity.getActiveAbility() == null) return false;
        return entity.getActiveAbility().getAbilityType() == abilityType;
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
        Ability<?>ability = entity.getActiveAbility();
        if (ability != null && ability.getAbilityType() == abilityType) {
            AbilityHandler.INSTANCE.sendInterruptAbilityMessage(entity, ability.getAbilityType());
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}
