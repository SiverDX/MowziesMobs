package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelSuperNova;
import com.bobmowzie.mowziesmobs.client.render.MMRenderType;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySuperNova;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class RenderSuperNova extends EntityRenderer<EntitySuperNova> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "textures/effects/super_nova.png");
    public static final ResourceLocation[] TEXTURES = new ResourceLocation[] {
            ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "textures/effects/super_nova_1.png"),
            ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "textures/effects/super_nova_2.png"),
            ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "textures/effects/super_nova_3.png"),
            ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "textures/effects/super_nova_4.png"),
            ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "textures/effects/super_nova_5.png"),
            ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "textures/effects/super_nova_6.png"),
            ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "textures/effects/super_nova_7.png"),
            ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "textures/effects/super_nova_8.png"),
            ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "textures/effects/super_nova_9.png"),
            ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "textures/effects/super_nova_10.png"),
            ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "textures/effects/super_nova_11.png"),
            ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "textures/effects/super_nova_12.png"),
            ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "textures/effects/super_nova_13.png"),
            ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "textures/effects/super_nova_14.png"),
            ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "textures/effects/super_nova_15.png"),
            ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "textures/effects/super_nova_16.png")
    };
    public ModelSuperNova<EntitySuperNova> model;

    public RenderSuperNova(EntityRendererProvider.Context mgr) {
        super(mgr);
        model = new ModelSuperNova<>();
    }

    @Override
    public ResourceLocation getTextureLocation(EntitySuperNova entity) {
        int index = entity.tickCount % TEXTURES.length;
        return TEXTURES[index];
    }

    @Override
    public void render(EntitySuperNova entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(MMRenderType.getGlowingEffect(this.getTextureLocation(entityIn)));
        model.setupAnim(entityIn, 0, 0, entityIn.tickCount + partialTicks, 0, 0);
        model.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        matrixStackIn.popPose();
    }
}
