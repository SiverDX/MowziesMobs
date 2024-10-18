package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoModel;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoPlayer;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import software.bernie.geckolib.cache.object.GeoBone;

public class ModelGeckoBiped extends MowzieGeoModel<GeckoPlayer> {
	private ResourceLocation textureLocation;

	public boolean isSitting = false;
	public boolean isChild = true;
	public float swingProgress;
	public boolean isSneak;
	public float swimAnimation;

	public HumanoidModel.ArmPose leftArmPose = HumanoidModel.ArmPose.EMPTY;
	public HumanoidModel.ArmPose rightArmPose = HumanoidModel.ArmPose.EMPTY;

	protected boolean useSmallArms = false;
	
	@Override
	public ResourceLocation getAnimationResource(GeckoPlayer animatable) {
		return ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "animations/animated_player.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(GeckoPlayer animatable) {
		return ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "geo/animated_player.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(GeckoPlayer animatable) {
		return textureLocation;
	}

	public void setTextureFromPlayer(AbstractClientPlayer player) {
		this.textureLocation = player.getSkin().texture();
	}

	public void setUseSmallArms(boolean useSmallArms) {
		this.useSmallArms = useSmallArms;
	}

	public boolean isUsingSmallArms() {
		return useSmallArms;
	}

	public MowzieGeoBone bipedHead() {
		return getMowzieBone("Head");
	}

	public MowzieGeoBone bipedHeadwear() {
		return getMowzieBone("HatLayer");
	}

	public MowzieGeoBone bipedBody() {
		return getMowzieBone("Body");
	}

	public MowzieGeoBone bipedRightArm() {
		return getMowzieBone("RightArm");
	}

	public MowzieGeoBone bipedLeftArm() {
		return getMowzieBone("LeftArm");
	}

	public MowzieGeoBone bipedRightLeg() {
		return getMowzieBone("RightLeg");
	}

	public MowzieGeoBone bipedLeftLeg() {
		return getMowzieBone("LeftLeg");
	}

	public void setVisible(boolean visible) {
		this.bipedHead().setHidden(!visible);
		this.bipedHeadwear().setHidden(!visible);
		this.bipedBody().setHidden(!visible);
		this.bipedRightArm().setHidden(!visible);
		this.bipedLeftArm().setHidden(!visible);
		this.bipedRightLeg().setHidden(!visible);
		this.bipedLeftLeg().setHidden(!visible);
	}

	public void setRotationAngles() {
		MowzieGeoBone head = getMowzieBone("Head");
		MowzieGeoBone neck = getMowzieBone("Neck");
		float yaw = 0;
		float pitch = 0;
		float roll = 0;
		GeoBone parent = neck.getParent();
		while (parent != null) {
			pitch += parent.getRotX();
			yaw += parent.getRotY();
			roll += parent.getRotZ();
			parent = parent.getParent();
		}
		neck.addRot(-yaw, -pitch, -roll);
	}

	public void setRotationAngles(Player entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float partialTick) {
		if (!isInitialized()) return;
		if (Minecraft.getInstance().isPaused()) return;

		MowzieGeoBone rightArmClassic = getMowzieBone("RightArmClassic");
		MowzieGeoBone leftArmClassic = getMowzieBone("LeftArmClassic");
		MowzieGeoBone rightArmSlim = getMowzieBone("RightArmSlim");
		MowzieGeoBone leftArmSlim = getMowzieBone("LeftArmSlim");
		if (useSmallArms) {
			rightArmClassic.setHidden(true);
			leftArmClassic.setHidden(true);
			rightArmSlim.setHidden(false);
			leftArmSlim.setHidden(false);
		}
		else {
			rightArmSlim.setHidden(true);
			leftArmSlim.setHidden(true);
			rightArmClassic.setHidden(false);
			leftArmClassic.setHidden(false);
		}
		
		this.swimAnimation = entityIn.getSwimAmount(partialTick);

		float headLookAmount = getControllerValueInverted("HeadLookController");
		float armLookAmount = 1f - getControllerValueInverted("ArmPitchController");
		float armLookAmountRight = getBone("ArmPitchController").get().getPosY();
		float armLookAmountLeft = getBone("ArmPitchController").get().getPosZ();
		boolean flag = entityIn.getFallFlyingTicks() > 4;
		boolean flag1 = entityIn.isVisuallySwimming();
		this.bipedHead().addRotY(headLookAmount * -netHeadYaw * ((float)Math.PI / 180F));
		this.getMowzieBone("LeftClavicle").addRotY(Math.min(armLookAmount + armLookAmountLeft, 1) * -netHeadYaw * ((float)Math.PI / 180F));
		this.getMowzieBone("RightClavicle").addRotY(Math.min(armLookAmount + armLookAmountRight, 1) * -netHeadYaw * ((float)Math.PI / 180F));
		if (flag) {
			this.bipedHead().addRotX((-(float)Math.PI / 4F));
		} else if (this.swimAnimation > 0.0F) {
			if (flag1) {
				this.bipedHead().addRotX(headLookAmount * this.rotLerpRad(this.swimAnimation, this.bipedHead().getRotX(), (-(float)Math.PI / 4F)));
			} else {
				this.bipedHead().addRotX(headLookAmount * this.rotLerpRad(this.swimAnimation, this.bipedHead().getRotX(), headPitch * ((float)Math.PI / 180F)));
			}
		} else {
			this.bipedHead().addRotX(headLookAmount * -headPitch * ((float)Math.PI / 180F));
			this.getMowzieBone("LeftClavicle").addRotX(Math.min(armLookAmount + armLookAmountLeft, 1) * -headPitch * ((float)Math.PI / 180F));
			this.getMowzieBone("RightClavicle").addRotX(Math.min(armLookAmount + armLookAmountRight, 1) * -headPitch * ((float)Math.PI / 180F));
		}
		
		float f = 1.0F;
		if (flag) {
			f = (float)entityIn.getDeltaMovement().lengthSqr();
			f = f / 0.2F;
			f = f * f * f;
		}

		if (f < 1.0F) {
			f = 1.0F;
		}

		float legWalkAmount = getControllerValueInverted("LegWalkController");
		float armSwingAmount = getControllerValueInverted("ArmSwingController");
		float armSwingAmountRight = 1.0f - getBone("ArmSwingController").get().getPosY();
		float armSwingAmountLeft = 1.0f - getBone("ArmSwingController").get().getPosZ();
		this.bipedRightArm().addRotX(armSwingAmount * armSwingAmountRight *Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 2.0F * limbSwingAmount * 0.5F / f);
		this.bipedLeftArm().addRotX(armSwingAmount * armSwingAmountLeft * Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F / f);
		this.bipedRightLeg().addRotX(legWalkAmount * Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount / f);
		this.bipedLeftLeg().addRotX(legWalkAmount * Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount / f);

		if (this.isSitting) {
			this.bipedRightArm().setRotX(bipedRightArm().getRotX() + (-(float)Math.PI / 5F));
			this.bipedLeftArm().setRotX(bipedRightArm().getRotX() + (-(float)Math.PI / 5F));
			this.bipedRightLeg().setRotX(-1.4137167F);
			this.bipedRightLeg().setRotY(((float)Math.PI / 10F));
			this.bipedRightLeg().setRotZ(0.07853982F);
			this.bipedLeftLeg().setRotX(-1.4137167F);
			this.bipedLeftLeg().setRotY((-(float)Math.PI / 10F));
			this.bipedLeftLeg().setRotZ(-0.07853982F);
			getMowzieBone("Waist").setRot(0, 0, 0);
			getMowzieBone("Root").setRot(0, 0, 0);
		}

		boolean flag2 = entityIn.getMainArm() == HumanoidArm.RIGHT;
		boolean flag3 = flag2 ? this.leftArmPose.isTwoHanded() : this.rightArmPose.isTwoHanded();
		if (flag2 != flag3) {
			this.poseLeftArm(entityIn);
			this.poseRightArm(entityIn);
		} else {
			this.poseRightArm(entityIn);
			this.poseLeftArm(entityIn);
		}

//		this.swingAnim(entityIn, ageInTicks);

		float sneakController = getControllerValueInverted("CrouchController");
		if (this.isSneak) {
			this.bipedBody().addRotX(-0.5F * sneakController);
			this.getMowzieBone("Neck").addRotX(0.5F * sneakController);
			this.bipedRightArm().addRot(0.4F * sneakController, 0, 0);
			this.bipedLeftArm().addRot(0.4F * sneakController, 0, 0);
			this.bipedHead().addPosY(-1F * sneakController);
			this.bipedBody().addPos(0, -1.5F * sneakController, 1.7f * sneakController);
			this.getMowzieBone("Waist").addPos(0, -0.2f * sneakController, 4F * sneakController);
			this.bipedLeftArm().addRotX(-0.4f * sneakController);
			this.bipedLeftArm().addPos(0, 0.2f * sneakController, -1f * sneakController);
			this.bipedRightArm().addRotX(-0.4f * sneakController);
			this.bipedRightArm().addPos(0, 0.2f * sneakController, -1f * sneakController);

			this.getMowzieBone("Waist").addPosY(2f * (1f - sneakController));
		}

		float armBreathAmount = getControllerValueInverted("ArmBreathController");
		breathAnim(this.bipedRightArm(), this.bipedLeftArm(), ageInTicks, armBreathAmount);

//		if (this.swimAnimation > 0.0F) {
//			float f1 = limbSwing % 26.0F;
//			HandSide handside = this.getMainHand(entityIn);
//			float f2 = handside == HandSide.RIGHT && this.swingProgress > 0.0F ? 0.0F : this.swimAnimation;
//			float f3 = handside == HandSide.LEFT && this.swingProgress > 0.0F ? 0.0F : this.swimAnimation;
//			if (f1 < 14.0F) {
//				this.bipedLeftArm().setRotX(this.rotLerpRad(f3, this.bipedLeftArm().getRotX(), 0.0F));
//				this.bipedRightArm().setRotX(MathHelper.lerp(f2, this.bipedRightArm().getRotX(), 0.0F));
//				this.bipedLeftArm().setRotY(this.rotLerpRad(f3, this.bipedLeftArm().getRotY(), (float)Math.PI));
//				this.bipedRightArm().setRotY(MathHelper.lerp(f2, this.bipedRightArm().getRotY(), (float)Math.PI));
//				this.bipedLeftArm().setRotZ(this.rotLerpRad(f3, this.bipedLeftArm().getRotZ(), (float)Math.PI + 1.8707964F * this.getArmAngleSq(f1) / this.getArmAngleSq(14.0F)));
//				this.bipedRightArm().setRotZ(MathHelper.lerp(f2, this.bipedRightArm().getRotZ(), (float)Math.PI - 1.8707964F * this.getArmAngleSq(f1) / this.getArmAngleSq(14.0F)));
//			} else if (f1 >= 14.0F && f1 < 22.0F) {
//				float f6 = (f1 - 14.0F) / 8.0F;
//				this.bipedLeftArm().setRotX(this.rotLerpRad(f3, this.bipedLeftArm().getRotX(), ((float)Math.PI / 2F) * f6));
//				this.bipedRightArm().setRotX(MathHelper.lerp(f2, this.bipedRightArm().getRotX(), ((float)Math.PI / 2F) * f6));
//				this.bipedLeftArm().setRotY(this.rotLerpRad(f3, this.bipedLeftArm().getRotY(), (float)Math.PI));
//				this.bipedRightArm().setRotY(MathHelper.lerp(f2, this.bipedRightArm().getRotY(), (float)Math.PI));
//				this.bipedLeftArm().setRotZ(this.rotLerpRad(f3, this.bipedLeftArm().getRotZ(), 5.012389F - 1.8707964F * f6));
//				this.bipedRightArm().setRotZ(MathHelper.lerp(f2, this.bipedRightArm().getRotZ(), 1.2707963F + 1.8707964F * f6));
//			} else if (f1 >= 22.0F && f1 < 26.0F) {
//				float f4 = (f1 - 22.0F) / 4.0F;
//				this.bipedLeftArm().setRotX(this.rotLerpRad(f3, this.bipedLeftArm().getRotX(), ((float)Math.PI / 2F) - ((float)Math.PI / 2F) * f4));
//				this.bipedRightArm().setRotX(MathHelper.lerp(f2, this.bipedRightArm().getRotX(), ((float)Math.PI / 2F) - ((float)Math.PI / 2F) * f4));
//				this.bipedLeftArm().setRotY(this.rotLerpRad(f3, this.bipedLeftArm().getRotY(), (float)Math.PI));
//				this.bipedRightArm().setRotY(MathHelper.lerp(f2, this.bipedRightArm().getRotY(), (float)Math.PI));
//				this.bipedLeftArm().setRotZ(this.rotLerpRad(f3, this.bipedLeftArm().getRotZ(), (float)Math.PI));
//				this.bipedRightArm().setRotZ(MathHelper.lerp(f2, this.bipedRightArm().getRotZ(), (float)Math.PI));
//			}
//
//			float f7 = 0.3F;
//			float f5 = 0.33333334F;
//			this.bipedLeftLeg().setRotX(MathHelper.lerp(this.swimAnimation, this.bipedLeftLeg().getRotX(), 0.3F * MathHelper.cos(limbSwing * 0.33333334F + (float)Math.PI)));
//			this.bipedRightLeg().setRotX(MathHelper.lerp(this.swimAnimation, this.bipedRightLeg().getRotX(), 0.3F * MathHelper.cos(limbSwing * 0.33333334F)));
//		}

		AbilityCapability.Capability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(entityIn);
		if (abilityCapability != null && abilityCapability.getActiveAbility() != null) {
			abilityCapability.codeAnimations(this, partialTick);
		}
	}

	protected MowzieGeoBone getArmForSide(HumanoidArm side) {
		return side == HumanoidArm.LEFT ? this.bipedLeftArm() : this.bipedRightArm();
	}

	protected float rotLerpRad(float angleIn, float maxAngleIn, float mulIn) {
		float f = (mulIn - maxAngleIn) % ((float)Math.PI * 2F);
		if (f < -(float)Math.PI) {
			f += ((float)Math.PI * 2F);
		}

		if (f >= (float)Math.PI) {
			f -= ((float)Math.PI * 2F);
		}

		return maxAngleIn + angleIn * f;
	}

	private float getArmAngleSq(float limbSwing) {
		return -65.0F * limbSwing + limbSwing * limbSwing;
	}

	protected HumanoidArm getMainHand(Player entityIn) {
		HumanoidArm handside = entityIn.getMainArm();
		return entityIn.swingingArm == InteractionHand.MAIN_HAND ? handside : handside.getOpposite();
	}

	public static void breathAnim(MowzieGeoBone rightArm, MowzieGeoBone leftArm, float ageInTicks, float armBreathAmount) {
		rightArm.addRotZ(armBreathAmount * Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F);
		leftArm.addRotZ(armBreathAmount * -Mth.cos(ageInTicks * 0.09F) * 0.05F - 0.05F);
		rightArm.addRotX(armBreathAmount * Mth.sin(ageInTicks * 0.067F) * 0.05F);
		leftArm.addRotX(armBreathAmount * -Mth.sin(ageInTicks * 0.067F) * 0.05F);
	}

	private void poseRightArm(Player p_241654_1_) {
		float armSwingAmount = getControllerValueInverted("ArmSwingController");
		switch(this.rightArmPose) {
			case EMPTY:
				break;
			case BLOCK:
				this.bipedRightArm().addRotX(0.9424779F * armSwingAmount);
				break;
			case ITEM:
				this.bipedRightArm().addRotX( ((float)Math.PI / 10F) * armSwingAmount);
				break;
		}

	}

	private void poseLeftArm(Player p_241655_1_) {
		float armSwingAmount = getControllerValueInverted("ArmSwingController");
		switch(this.leftArmPose) {
			case EMPTY:
				break;
			case BLOCK:
				this.bipedLeftArm().addRotX(0.9424779F * armSwingAmount);
				break;
			case ITEM:
				this.bipedLeftArm().addRotX(((float)Math.PI / 10F) * armSwingAmount);
				break;
		}
	}
}