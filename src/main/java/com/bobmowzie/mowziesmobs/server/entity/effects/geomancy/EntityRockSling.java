package com.bobmowzie.mowziesmobs.server.entity.effects.geomancy;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;

public class EntityRockSling extends EntityBoulderProjectile implements GeoEntity {
    private Vec3 launchVec;

    public EntityRockSling(EntityType<? extends EntityRockSling> type, Level worldIn) {
        super(type, worldIn);
    }


    public EntityRockSling(EntityType<? extends EntityBoulderProjectile> type, Level world, LivingEntity caster, BlockState blockState, BlockPos pos, GeomancyTier tier) {
        super(type, world, caster, blockState, pos, tier);
    }

    @Override
    public void tick() {
        super.tick();

        if(tickCount > 30 + random.nextInt(35) && launchVec != null) {
            setDeltaMovement(launchVec.normalize().multiply(2f + random.nextFloat()/5, 2f, 2f + random.nextFloat()/5));

        }

    }

    public void setLaunchVec(Vec3 vec){
        this.launchVec = vec;
    }

    private static RawAnimation ROLL_ANIM = RawAnimation.begin().thenLoop("roll");

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        super.registerControllers(controllers);
        AnimationController<EntityRockSling> controller = new AnimationController<>(this, "controller", 0,
                event -> {
                    event.getController()
                            .setAnimation(ROLL_ANIM);
                    return PlayState.CONTINUE;
                });
        controllers.add(controller);
    }
}
