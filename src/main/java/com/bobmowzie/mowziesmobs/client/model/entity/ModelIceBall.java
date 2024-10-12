package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.server.entity.effects.EntityIceBall;
import com.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import com.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import com.ilexiconn.llibrary.client.model.tools.BasicModelRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class ModelIceBall<T extends EntityIceBall> extends AdvancedModelBase<T> {
	private final AdvancedModelRenderer body1;
	private final AdvancedModelRenderer body2;
	private final AdvancedModelRenderer core;

	public ModelIceBall() {
		textureWidth = 128;
		textureHeight = 64;

		body1 = new AdvancedModelRenderer(this, 32, 0);
		body1.setRotationPoint(0.0F, 16.0F, 0.0F);
		body1.addBox(-12.0F, -12.0F, -12.0F, 24, 24, 24, 0.0F, false);

		body2 = new AdvancedModelRenderer(this, 32, 0);
		body2.setRotationPoint(0.0F, 16.0F, 0.0F);
		setRotationAngle(body2, 0.7854F, 0.0F, 0.7854F);
		body2.addBox(-12.0F, -12.0F, -12.0F, 24, 24, 24, 0.0F, false);

		core = new AdvancedModelRenderer(this, 8, 0);
		core.setRotationPoint(0.0F, 16.0F, 0.0F);
		core.addBox( -4.0F, -4.0F, -4.0F, 8, 8, 8, 0.0F, false);

		body1.setOpacity(0.65f);
		body2.setOpacity(0.65f);
	}

	@Override
	public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		body1.rotateAngleX = 0;
		body1.rotateAngleY = 0;
		body1.rotateAngleZ = -ageInTicks * 0.4f;
		body2.rotateAngleX = 0.7854F;
		body2.rotateAngleY = 0.7854F + ageInTicks * 0.4f;
		body2.rotateAngleZ = 0.7854F + ageInTicks * 0.4f;
		body2.showModel = true;

		core.rotateAngleZ = ageInTicks * 0.4f;
	}

	@Override
	public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, int color) {
		core.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, color);
		body1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, color);
		body2.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, color);
	}

	public void setRotationAngle(BasicModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}