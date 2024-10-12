package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.client.model.tools.RigUtils;
import com.bobmowzie.mowziesmobs.client.model.tools.dynamics.GeckoDynamicChain;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoModel;
import com.bobmowzie.mowziesmobs.server.entity.sculptor.EntitySculptor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.data.EntityModelData;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ModelSculptor extends MowzieGeoModel<EntitySculptor> {
    private GeckoDynamicChain tail;

    public MowzieGeoBone[] beardOriginal;
    public MowzieGeoBone[] beardDynamic;
    
    public MowzieGeoBone[] armOriginal;
    public MowzieGeoBone[] armDynamic;

    public ModelSculptor() {
        super();
    }

    @Override
    public ResourceLocation getModelResource(EntitySculptor object) {
        return ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "geo/sculptor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(EntitySculptor object) {
        return ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "textures/entity/sculptor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(EntitySculptor object) {
        return ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "animations/sculptor.animation.json");
    }

    @Override
    public void setCustomAnimations(EntitySculptor entity, long instanceId, AnimationState<EntitySculptor> animationState) {
        GeoBone head = this.getBone("head").get();
        GeoBone chestJoint = this.getBone("chestJoint").get();
        GeoBone handClosedL = this.getBone("handClosedLeft").get();
        GeoBone handClosedR = this.getBone("handClosedRight").get();
        GeoBone handOpenL = this.getBone("handOpenLeft").get();
        GeoBone handOpenR = this.getBone("handOpenRight").get();
        GeoBone backCloth = this.getBone("clothBack").get();

        EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        float torsoAimController = getControllerValue("torsoAimController");
        head.setRotX(head.getRotX() + entityData.headPitch() * ((float) Math.PI / 180F) * (1.0f - torsoAimController));
        head.setRotY(head.getRotY() + entityData.netHeadYaw() * ((float) Math.PI / 180F) * (1.0f - torsoAimController));
        chestJoint.setRotX(chestJoint.getRotX() + entityData.headPitch() * ((float) Math.PI / 180F) * torsoAimController);
        chestJoint.setRotY(chestJoint.getRotY() + entityData.netHeadYaw() * ((float) Math.PI / 180F) * torsoAimController);

        if (entity.isAlive()) {
            idleAnim(entity, animationState);
        }

        float handControllerLeft = getControllerValue("handControllerLeft");
        float handControllerRight = getControllerValue("handControllerRight");
        boolean handLOpen = handControllerLeft == 0;
        boolean handROpen = handControllerRight == 0;

        handClosedL.setHidden(handLOpen);
        handClosedR.setHidden(handROpen);
        handOpenL.setHidden(!handLOpen);
        handOpenR.setHidden(!handROpen);

        backCloth.setHidden(false);

        beadsCorrections(entity);
        skirtCorrections(entity);

        staffRendering(entity);
        gauntletVisibility();

        if (beardOriginal == null || beardDynamic == null) {
            beardOriginal = new MowzieGeoBone[]{getMowzieBone("beard3"), getMowzieBone("beard4"), getMowzieBone("beard5Rot"), getMowzieBone("beard6")};
            beardDynamic = new MowzieGeoBone[beardOriginal.length];
        }
        if (entity.beardChain != null) entity.beardChain.setChainArrays(beardOriginal, beardDynamic);
    }

    private void beadsCorrections(EntitySculptor entity) {
        Map<MowzieGeoBone, Vec3> beadsDirections = new HashMap<>();
        beadsDirections.put(this.getMowzieBone("bead1"), new Vec3(0, -1, 0));
        beadsDirections.put(this.getMowzieBone("bead2"), new Vec3(0, -1, 0));
        beadsDirections.put(this.getMowzieBone("bead3"), new Vec3(0, -1, 0));
        beadsDirections.put(this.getMowzieBone("bead4"), new Vec3(0, -0.5, 0.25));
        beadsDirections.put(this.getMowzieBone("bead5"), new Vec3(0, -0.5, 0.25));
        beadsDirections.put(this.getMowzieBone("bead6"), new Vec3(0, -0.5, 1));
        beadsDirections.put(this.getMowzieBone("bead7"), new Vec3(0, -0.5, 1));
        beadsDirections.put(this.getMowzieBone("bead8"), new Vec3(0, -0.25, 0.75));
        beadsDirections.put(this.getMowzieBone("bead9"), new Vec3(0, -0.25, 0.75));

        GeoBone headJoint = this.getBone("headJoint").get();
        Vec3 headPos = new Vec3(headJoint.getPivotX(), headJoint.getPivotY(), headJoint.getPivotZ());
        GeoBone head = this.getBone("head").get();
        Vec3 headDir = new Vec3(0, 0, -1).normalize();
        headDir = headDir.yRot(head.getRotY()).xRot(head.getRotX());
        for (Map.Entry<MowzieGeoBone, Vec3> beadDir : beadsDirections.entrySet()) {
            MowzieGeoBone bead = beadDir.getKey();
            Vec3 beadPos = new Vec3(bead.getPivotX(), bead.getPivotY(), bead.getPivotZ());
            Vec3 dir = beadPos.subtract(headPos).normalize().multiply(1, -1, 1);
            double dot = dir.dot(headDir);
            dot = Math.pow(Math.max(dot, 0), 3.5);
            Vec3 moveDir = beadDir.getValue().normalize();
            bead.addPos(moveDir.scale(dot * 3));
        }
    }

    private void skirtCorrections(EntitySculptor entity) {
        if (
                entity.calfRPos == null ||
                entity.calfLPos == null ||
                entity.thighRPos == null ||
                entity.thighLPos == null ||
                entity.skirtEndRPos == null ||
                entity.skirtEndLPos == null ||
                entity.skirtLocFrontRPos == null ||
                entity.skirtLocFrontLPos == null ||
                entity.skirtLocBackRPos == null ||
                entity.skirtLocBackLPos == null ||
                entity.frontClothRot == null
        ) {
            return;
        }
        MowzieGeoBone headJoint = this.getMowzieBone("headJoint");
        MowzieGeoBone thighR = this.getMowzieBone("thighRight");
        MowzieGeoBone thighJointR = this.getMowzieBone("thighJointRight");
        MowzieGeoBone thighJointL = this.getMowzieBone("thighJointLeft");
        MowzieGeoBone calfR = this.getMowzieBone("calfRight");
        MowzieGeoBone thighL = this.getMowzieBone("thighLeft");
        MowzieGeoBone calfL = this.getMowzieBone("calfLeft");
        MowzieGeoBone footR = this.getMowzieBone("footRight");

        MowzieGeoBone skirtBack = this.getMowzieBone("skirtBack");
        MowzieGeoBone skirtFront = this.getMowzieBone("skirtFront");
        MowzieGeoBone skirtL = this.getMowzieBone("skirtLeft");
        MowzieGeoBone skirtR = this.getMowzieBone("skirtRight");
        MowzieGeoBone skirtJointL = this.getMowzieBone("skirtJointLeft");
        MowzieGeoBone skirtJointR = this.getMowzieBone("skirtJointRight");
        MowzieGeoBone skirtJoint2L = this.getMowzieBone("skirtJoint2Left");
        MowzieGeoBone skirtJoint2R = this.getMowzieBone("skirtJoint2Right");
        MowzieGeoBone skirtEndR = this.getMowzieBone("skirtEndRight");
        MowzieGeoBone skirtEndL = this.getMowzieBone("skirtEndLeft");

        MowzieGeoBone skirtLocFrontR = this.getMowzieBone("skirtFrontLocRight");
        MowzieGeoBone skirtLocFrontL = this.getMowzieBone("skirtFrontLocLeft");
        MowzieGeoBone skirtLocBackR = this.getMowzieBone("skirtBackLocRight");
        MowzieGeoBone skirtLocBackL = this.getMowzieBone("skirtBackLocLeft");

        headJoint.setHidden(false);

        Vec3 thighToKneeR = Vec3(entity.calfRPos).subtract(Vec3(entity.thighRPos)).normalize();
        Vec3 thighToKneeL = Vec3(entity.calfLPos).subtract(Vec3(entity.thighLPos)).normalize();

        skirtEndL.addPos(-0.2f, -1.5f, 0);
        skirtEndR.addPos( 0.2f, -1.5f, 0);
        Vec3 thighToSkirtEndR = Vec3(entity.skirtEndRPos).subtract(Vec3(entity.thighRPos)).normalize();
        Vec3 thighToSkirtEndL = Vec3(entity.skirtEndLPos).subtract(Vec3(entity.thighLPos)).normalize();
        float rightDot = (float) thighToKneeR.dot(new Vec3(0, -1, 0));
        rightDot = (float) Math.pow(Math.max(rightDot, 0), 3);
        float leftDot = (float) thighToKneeL.dot(new Vec3(0, -1, 0));
        leftDot = (float) Math.pow(Math.max(leftDot, 0), 3);
        skirtJointR.addPos(Math.max(-0.9f * rightDot, -0.7f), 0, Math.max(-0.7f * rightDot, -0.5f));
        skirtJointL.addPos(-Math.max(-0.9f * leftDot, -0.7f), 0, Math.max(-0.7f * leftDot, -0.5f));



        Quaternionf rightRot = RigUtils.betweenVectors(thighToSkirtEndR, thighToKneeR);
        Quaternionf leftRot = RigUtils.betweenVectors(thighToSkirtEndL, thighToKneeL);
        Matrix4f rightMat = (new Matrix4f()).rotate(rightRot);
        Matrix4f leftMat = (new Matrix4f()).rotate(leftRot);
        skirtJoint2R.setModelXformOverride(rightMat);
        skirtJoint2L.setModelXformOverride(leftMat);

        Vec3 average = thighToKneeL.add(thighToKneeR).scale(2).multiply(0, 1, 1).normalize();
        float angleAv = (float) Mth.atan2(average.y(), average.z());
        skirtBack.setRotX(skirtBack.getRotX() - angleAv + 3.48f);
        skirtFront.setRotX(skirtFront.getRotX() - Math.min(angleAv, -2) + 3.48f);
        Vec3 skirtFrontDiff = Vec3(entity.skirtLocFrontLPos).subtract(Vec3(entity.skirtLocFrontRPos));
        skirtFront.setScaleX(Math.max((float) (0.3f + 0.15f * skirtFrontDiff.length()), 0.4f));
        Vec3 skirtBackDiff = Vec3(entity.skirtLocBackLPos).subtract(Vec3(entity.skirtLocBackRPos));
        skirtBack.setScaleX((float) (0.15f + 0.1f * skirtBackDiff.length()));
        float angleF = (float) Mth.atan2(skirtFrontDiff.normalize().z(), skirtFrontDiff.normalize().x());
        if (angleF < 0.001 || angleF > 3.141) angleF = 0;
        skirtFront.setRotY(angleF * 0.6f);
        skirtFront.addPos(0.5f * (float) Vec3(entity.skirtLocFrontRPos).add(skirtFrontDiff.scale(0.5)).x(), 0, 0);
        float angleB = (float) Mth.atan2(skirtBackDiff.normalize().z(), skirtBackDiff.normalize().x());
        skirtBack.setRotY(angleB * 0.5f);
        skirtBack.addPos(0.5f * (float) Vec3(entity.skirtLocBackRPos).add(skirtBackDiff.scale(0.5)).x(), 0, 0);
        float bothDots = (float) Math.pow(rightDot * leftDot, 1);
        float f = Math.min(1, bothDots * 2);
        skirtR.addRot(0, Mth.clamp(angleF, -0.5f, 0.5f) * (1 - f) - bothDots * 0.4f, 0);
        skirtL.addRot(0, Mth.clamp(angleF, -0.5f, 0.5f) * (1 - f) + bothDots * 0.4f, 0);

        MowzieGeoBone frontCloth = getMowzieBone("clothFront");
        MowzieGeoBone frontCloth2 = getMowzieBone("clothFront2");

        frontCloth.setRot(skirtFront.getRot());
        Matrix4f mat = entity.frontClothRot;
        mat.invert();
        frontCloth2.setModelXformOverride(mat);
    }

    private void staffRendering(EntitySculptor entity) {
        MowzieGeoBone itemHandLeft = this.getMowzieBone("itemHandLeft");
        MowzieGeoBone itemHandRight = this.getMowzieBone("itemHandRight");
        MowzieGeoBone backItem = this.getMowzieBone("backItem");

        itemHandLeft.setHidden(true);
        itemHandRight.setHidden(true);
        backItem.setHidden(true);

        MowzieGeoBone staffController = this.getMowzieBone("staffController");

        switch ((int) staffController.getPosX()){
            case -1:
                itemHandRight.setHidden(false);
                break;
            case 0:
                backItem.setHidden(false);
                break;
            case 1:
                itemHandLeft.setHidden(false);
                break;
        }

        itemHandLeft.setScale(1.2f);
        itemHandRight.setScale(1.2f);
        backItem.setScale(1.2f);
    }

    private void idleAnim(EntitySculptor entity, AnimationState<?> animationState) {
        float frame = entity.frame + animationState.getPartialTick();

        MowzieGeoBone eyebrowRight = this.getMowzieBone("eyebrowRight");
        MowzieGeoBone eyebrowLeft = this.getMowzieBone("eyebrowLeft");
        MowzieGeoBone clothFront2 = this.getMowzieBone("clothFront2");
        MowzieGeoBone clothBack = this.getMowzieBone("clothBack");
        MowzieGeoBone footLeft = this.getMowzieBone("footLeft");
        MowzieGeoBone calfLeft = this.getMowzieBone("calfLeft");
        MowzieGeoBone thighLeft = this.getMowzieBone("thighLeft");
        MowzieGeoBone headJoint = this.getMowzieBone("headJoint");
        MowzieGeoBone chest = this.getMowzieBone("chest");
        MowzieGeoBone footRight = this.getMowzieBone("footRight");
        MowzieGeoBone calfRight = this.getMowzieBone("calfRight");
        MowzieGeoBone thighRight = this.getMowzieBone("thighRight");
        MowzieGeoBone mouth = this.getMowzieBone("mouth");
        MowzieGeoBone handLeft = this.getMowzieBone("handLeft");
        MowzieGeoBone lowerArmLeft = this.getMowzieBone("lowerArmLeft");
        MowzieGeoBone shoulderLeft = this.getMowzieBone("shoulderLeft");
        MowzieGeoBone handRight = this.getMowzieBone("handRight");
        MowzieGeoBone lowerArmRight = this.getMowzieBone("lowerArmRight");
        MowzieGeoBone shoulderRight = this.getMowzieBone("shoulderRight");
        MowzieGeoBone body = this.getMowzieBone("body");

        float idleAnim = getControllerValue("idleAnimController");
        float idleAnimDisableArms = getControllerValueInverted("idleAnimDisableArmsController");
        float idleSpeed = 0.08f;

        eyebrowRight.addPosY(idleAnim * (float) (Math.sin((frame * idleSpeed + 0.4)) * 0.1));
        eyebrowLeft.addPosY(idleAnim * (float) (Math.sin((frame * idleSpeed + 0.4)) * 0.1));
        clothFront2.addRotX(idleAnim * (float) (Math.sin((frame * idleSpeed - 1)) * 0.05));
        clothBack.addRotX(idleAnim * (float) (-Math.sin((frame * idleSpeed - 1)) * 0.05));
        footLeft.addRotZ(idleAnim * (float) (-0.175 - Math.sin((frame * idleSpeed+0.5)) * 0.035));
        calfLeft.addRotZ(idleAnim * (float) (0.175 + Math.sin((frame * idleSpeed+0.5)) * 0.035));
        thighLeft.addRotZ(idleAnim * (float) (0.035-Math.sin((frame * idleSpeed+1.5)) * 0.035));
        headJoint.addRotX(idleAnim * (float) (Math.sin((frame * idleSpeed + 1.5)) * 0.017));
        chest.addRotX(idleAnim * (float) (-Math.sin((frame * idleSpeed+1)*idleSpeed)*0.017));
        footRight.addRotY(-0.1309f);
        footRight.addRotZ(idleAnim * (float) (-0.35 + Math.sin((frame * idleSpeed+0.5)) * 0.035));
        calfRight.addRotZ(idleAnim * (float) (0.567 - Math.sin((frame * idleSpeed+0.5)) * 0.035));
        thighRight.addRotZ(idleAnim * (float) (-0.297 + Math.sin((frame * idleSpeed+1.5)) * 0.035));
        mouth.addPosY(idleAnim * (float) (-Math.sin((frame * idleSpeed+0.5)) * 0.1f));
        mouth.setScaleY((float) (mouth.getScaleZ() + idleAnim * Math.sin((frame * idleSpeed+0.5)) * 0.05f));
        handLeft.addRotY(idleAnim * idleAnimDisableArms * (float) (-Math.sin((frame * idleSpeed+1)) * 0.05));
        lowerArmLeft.addRotX(idleAnim * idleAnimDisableArms * (float) (-Math.sin((frame * idleSpeed+0.5)) * 0.05));
        lowerArmLeft.addRotY(idleAnim * idleAnimDisableArms * (float) (-Math.sin((frame * idleSpeed+0.5)) * 0.05));
        shoulderLeft.addRotZ(idleAnim * idleAnimDisableArms * (float) (Math.sin((frame * idleSpeed - 0.5)) * 0.05));
        handRight.addRotY(idleAnim * idleAnimDisableArms * (float) (Math.sin((frame * idleSpeed+1)) * 0.05));
        lowerArmRight.addRotX(idleAnim * idleAnimDisableArms * (float) (-Math.sin((frame * idleSpeed+0.5)) * 0.05));
        lowerArmRight.addRotY(idleAnim * idleAnimDisableArms * (float) (Math.sin((frame * idleSpeed+0.5)) * 0.05));
        shoulderRight.addRotZ(idleAnim * idleAnimDisableArms * (float) (-Math.sin((frame * idleSpeed - 0.5)) * 0.05));
        body.addRotX(idleAnim * (float) (-Math.cos((frame * idleSpeed+0.5)) * 0.017));
        body.addPosY(idleAnim * (float) (Math.sin(frame * idleSpeed) * 1));
    }

    private static final String[] GAUNTLET_ASSEMBLE_ORDER = new String[] {
            "centerRock",
            "sleeve",
            "rightRock1",
            "wrist",
            "pinky",
            "index",
            "thumb",
            "leftRock1",
            "leftRock2",
            "rightRock2"
    };

    private void gauntletVisibility() {
        float gauntletProgress = getControllerValue("gauntletProgressController");

        MowzieGeoBone gauntlet = getMowzieBone("gauntlet");
        MowzieGeoBone gauntletUnparented = getMowzieBone("gauntletUnparented");

        if (gauntletProgress <= 0.0 || gauntletProgress > 1.15) {
            gauntlet.setHidden(true);
            gauntletUnparented.setHidden(true);
        }
        else {
            gauntlet.setHidden(false);
            gauntletUnparented.setHidden(false);

            for (int i = 0; i < GAUNTLET_ASSEMBLE_ORDER.length; i++) {
                String boneName = GAUNTLET_ASSEMBLE_ORDER[i];
                Optional<GeoBone> bone = getBone(boneName);
                float waitForControllerValue = (float) i / (float) GAUNTLET_ASSEMBLE_ORDER.length + 0.2f;
                if (bone.isPresent()) {
                    bone.get().setHidden(gauntletProgress < waitForControllerValue);
                }
                else {
                    System.out.println("Missing bone " + boneName);
                }

                Optional<GeoBone> boneUnparented = getBone(boneName + "Unparented");
                if (boneUnparented.isPresent()) {
                    boneUnparented.get().setHidden(gauntletProgress >= waitForControllerValue || gauntletProgress < waitForControllerValue - 0.1);
                }
                else {
                    System.out.println("Missing bone " + boneName + "Unparented");
                }
            }
        }
    }

    private static Vec3 Vec3(Vector3d vec) {
        return new Vec3(vec.x, vec.y, vec.z);
    }
}