package com.bobmowzie.mowziesmobs.server.ability.abilities.player;

import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoPlayer;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.ability.PlayerAbility;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import software.bernie.geckolib.animation.Animation;
import software.bernie.geckolib.animation.RawAnimation;

public class SimplePlayerAnimationAbility extends PlayerAbility {
    private String animationName;
    private boolean separateLeftAndRight;
    private boolean lockHeldItemMainHand;

    public SimplePlayerAnimationAbility(AbilityType<Player, SimplePlayerAnimationAbility> abilityType, Player user, String animationName, int duration) {
        super(abilityType, user, new AbilitySection[] {
                new AbilitySection.AbilitySectionInstant(AbilitySection.AbilitySectionType.ACTIVE),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, duration)
        });
        this.animationName = animationName;
    }

    public SimplePlayerAnimationAbility(AbilityType<Player, SimplePlayerAnimationAbility> abilityType, Player user, String animationName, int duration, boolean separateLeftAndRight, boolean lockHeldItemMainHand) {
        super(abilityType, user, new AbilitySection[] {
                new AbilitySection.AbilitySectionInstant(AbilitySection.AbilitySectionType.ACTIVE),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, duration)
        });
        this.animationName = animationName;
        this.separateLeftAndRight = separateLeftAndRight;
        this.lockHeldItemMainHand = lockHeldItemMainHand;
    }

    @Override
    public void start() {
        super.start();
        if (separateLeftAndRight) {
            boolean handSide = getUser().getMainArm() == HumanoidArm.RIGHT;
            playAnimation(animationName + "_" + (handSide ? "right" : "left"), GeckoPlayer.Perspective.THIRD_PERSON, Animation.LoopType.PLAY_ONCE);
            playAnimation(animationName, GeckoPlayer.Perspective.FIRST_PERSON, Animation.LoopType.PLAY_ONCE);
        }
        else {
            playAnimation(RawAnimation.begin().thenPlay(animationName));
        }
        if (lockHeldItemMainHand)
            heldItemMainHandVisualOverride = getUser().getMainHandItem();
    }
}
