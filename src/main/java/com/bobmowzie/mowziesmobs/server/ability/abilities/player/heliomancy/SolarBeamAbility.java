package com.bobmowzie.mowziesmobs.server.ability.abilities.player.heliomancy;

import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySolarBeam;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.animation.RawAnimation;

public class SolarBeamAbility extends HeliomancyAbilityBase {
    protected EntitySolarBeam solarBeam;

    public SolarBeamAbility(AbilityType<Player, SolarBeamAbility> abilityType, Player user) {
        super(abilityType, user, new AbilitySection[] {
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, 20),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.ACTIVE, 55),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, 20)
        });
    }

    private static final RawAnimation SOLAR_BEAM_CHARGE_ANIM = RawAnimation.begin().thenPlay("solar_beam_charge");

    @Override
    public void start() {
        super.start();
        LivingEntity user = getUser();
        EntitySolarBeam solarBeam = new EntitySolarBeam(EntityHandler.SOLAR_BEAM.get(), user.level(), user, user.getX(), user.getY() + 1.2f, user.getZ(), (float) ((user.yHeadRot + 90) * Math.PI / 180), (float) (-user.getXRot() * Math.PI / 180), 55);
        this.solarBeam = solarBeam;
        if (!getUser().level().isClientSide()) {
            solarBeam.setHasPlayer(true);
            user.level().addFreshEntity(solarBeam);
            user.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 2, false, false));
        }
        else {
            heldItemMainHandVisualOverride = ItemStack.EMPTY;
            heldItemOffHandVisualOverride = ItemStack.EMPTY;
            firstPersonOffHandDisplay = HandDisplay.FORCE_RENDER;
            firstPersonMainHandDisplay = HandDisplay.FORCE_RENDER;
        }
        playAnimation(SOLAR_BEAM_CHARGE_ANIM);
    }
    
    @Override
    protected void beginSection(AbilitySection section) {
        super.beginSection(section);
        if (section.sectionType == AbilitySection.AbilitySectionType.ACTIVE) {
            if (!getLevel().isClientSide()) {
                MobEffectInstance sunsBlessingInstance = getUser().getEffect(EffectHandler.SUNS_BLESSING);
                if (sunsBlessingInstance != null) {
                    int duration = sunsBlessingInstance.getDuration();
                    getUser().removeEffect(EffectHandler.SUNS_BLESSING);
                    int solarBeamCost = ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SUNS_BLESSING.solarBeamCost.get() * 60 * 20;
                    if (duration - solarBeamCost > 0) {
                        getUser().addEffect(new MobEffectInstance(EffectHandler.SUNS_BLESSING, duration - solarBeamCost, 0, false, false));
                    }
                }
            }
        }
    }

    @Override
    public void end() {
        super.end();
        if (solarBeam != null) {
            solarBeam.discard();
        }
    }
}
