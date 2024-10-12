package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoModel;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoPlayer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.core.animation.AnimationState;

@OnlyIn(Dist.CLIENT)
public class ModelGeckoPlayerFirstPerson extends MowzieGeoModel<GeckoPlayer> {
	private ResourceLocation textureLocation;

	public HumanoidModel.ArmPose leftArmPose = HumanoidModel.ArmPose.EMPTY;
	public HumanoidModel.ArmPose rightArmPose = HumanoidModel.ArmPose.EMPTY;

	protected boolean useSmallArms;
	
	@Override
	public ResourceLocation getAnimationResource(GeckoPlayer animatable) {
		return ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "animations/animated_player_first_person.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(GeckoPlayer animatable) {
		return ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "geo/animated_player_first_person.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(GeckoPlayer animatable) {
		return textureLocation;
	}

	public void setUseSmallArms(boolean useSmallArms) {
		this.useSmallArms = useSmallArms;
	}

	public boolean isUsingSmallArms() {
		return useSmallArms;
	}

	@Override
	public void setCustomAnimations(GeckoPlayer animatable, long instanceId, AnimationState<GeckoPlayer> animationState) {
		super.setCustomAnimations(animatable, instanceId, animationState);
		if (isInitialized()) {
			MowzieGeoBone rightArmLayerClassic = getMowzieBone("RightArmLayerClassic");
			MowzieGeoBone leftArmLayerClassic = getMowzieBone("LeftArmLayerClassic");
			MowzieGeoBone rightArmLayerSlim = getMowzieBone("RightArmLayerSlim");
			MowzieGeoBone leftArmLayerSlim = getMowzieBone("LeftArmLayerSlim");
			MowzieGeoBone rightArmClassic = getMowzieBone("RightArmClassic");
			MowzieGeoBone leftArmClassic = getMowzieBone("LeftArmClassic");
			MowzieGeoBone rightArmSlim = getMowzieBone("RightArmSlim");
			MowzieGeoBone leftArmSlim = getMowzieBone("LeftArmSlim");
			getMowzieBone("LeftHeldItem").setHidden(true);
			getMowzieBone("RightHeldItem").setHidden(true);
			rightArmClassic.setHidden(true);
			leftArmClassic.setHidden(true);
			rightArmLayerClassic.setHidden(true);
			leftArmLayerClassic.setHidden(true);
			rightArmSlim.setHidden(true);
			leftArmSlim.setHidden(true);
			rightArmLayerSlim.setHidden(true);
			leftArmLayerSlim.setHidden(true);
		}
	}

	public void setTextureFromPlayer(AbstractClientPlayer player) {
		this.textureLocation = player.getSkinTextureLocation();
	}
}