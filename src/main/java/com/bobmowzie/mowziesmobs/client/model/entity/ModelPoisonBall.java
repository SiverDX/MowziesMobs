package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.server.entity.effects.EntityPoisonBall;
import com.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import com.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.phys.Vec3;

public class ModelPoisonBall<T extends EntityPoisonBall> extends AdvancedModelBase<T> {
	private final AdvancedModelRenderer inner;
	private final AdvancedModelRenderer outer;

	public ModelPoisonBall() {
		textureWidth = 32;
		textureHeight = 32;

		inner = new AdvancedModelRenderer(this, 0, 16);
		inner.setRotationPoint(0.0F, 3.5F, 0.0F);
		inner.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6, 0.0F, false);

		outer = new AdvancedModelRenderer(this, 0, 0);
		outer.setRotationPoint(0.0F, 3.5F, 0.0F);
		outer.addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8, 0.0F, false);

		inner.setOpacity(1f);
		outer.setOpacity(0.6f);
	}

	@Override
	public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, int color) {
		inner.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, color);
		outer.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, color);
	}

	@Override
	public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		EntityPoisonBall poisonBall = entityIn;
		float delta = ageInTicks - entityIn.tickCount;
		Vec3 prevV = new Vec3(poisonBall.prevMotionX, poisonBall.prevMotionY, poisonBall.prevMotionZ);
		Vec3 dv = prevV.add(poisonBall.getDeltaMovement().subtract(prevV).scale(delta));
		double d = Math.sqrt(dv.x * dv.x + dv.y * dv.y + dv.z * dv.z);
		if (d != 0) {
			double a = dv.y / d;
			a = Math.max(-1, Math.min(1, a));
			float pitch = -(float) Math.asin(a);
			inner.rotateAngleX = pitch + (float)Math.PI / 2f;
			outer.rotateAngleX = pitch + (float)Math.PI / 2f;
		}
	}

	public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
		modelRenderer.x = x;
		modelRenderer.y = y;
		modelRenderer.z = z;
	}
}