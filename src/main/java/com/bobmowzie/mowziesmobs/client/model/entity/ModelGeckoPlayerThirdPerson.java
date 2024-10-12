package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import net.minecraft.world.entity.player.Player;

public class ModelGeckoPlayerThirdPerson extends ModelGeckoBiped {

	public MowzieGeoBone bipedLeftArmwear() {
		return getMowzieBone("LeftArmLayer");
	}

	public MowzieGeoBone bipedRightArmwear() {
		return getMowzieBone("RightArmLayer");
	}

	public MowzieGeoBone bipedLeftLegwear() {
		return getMowzieBone("LeftLegLayer");
	}

	public MowzieGeoBone bipedRightLegwear() {
		return getMowzieBone("RightLegLayer");
	}

	public MowzieGeoBone bipedBodywear() {
		return getMowzieBone("BodyLayer");
	}

	public void setVisible(boolean visible) {
		super.setVisible(visible);
		this.bipedLeftArmwear().setHidden(!visible);
		this.bipedRightArmwear().setHidden(!visible);
		this.bipedLeftLegwear().setHidden(!visible);
		this.bipedRightLegwear().setHidden(!visible);
		this.bipedBodywear().setHidden(!visible);
	}

	@Override
	public void setRotationAngles(Player entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float partialTick) {
		super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTick);
		MowzieGeoBone rightArmLayerClassic = getMowzieBone("RightArmLayerClassic");
		MowzieGeoBone leftArmLayerClassic = getMowzieBone("LeftArmLayerClassic");
		MowzieGeoBone rightArmLayerSlim = getMowzieBone("RightArmLayerSlim");
		MowzieGeoBone leftArmLayerSlim = getMowzieBone("LeftArmLayerSlim");
		if (useSmallArms) {
			rightArmLayerClassic.setHidden(true);
			leftArmLayerClassic.setHidden(true);
			rightArmLayerSlim.setHidden(false);
			leftArmLayerSlim.setHidden(false);
		}
		else {
			rightArmLayerSlim.setHidden(true);
			leftArmLayerSlim.setHidden(true);
			rightArmLayerClassic.setHidden(false);
			leftArmLayerClassic.setHidden(false);
		}
	}
}