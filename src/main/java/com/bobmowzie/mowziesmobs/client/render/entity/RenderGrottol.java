package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelGrottol;
import com.bobmowzie.mowziesmobs.server.entity.grottol.EntityGrottol;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Created by BobMowzie on 5/8/2017.
 */
public class RenderGrottol extends MobRenderer<EntityGrottol, ModelGrottol<EntityGrottol>> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(MowziesMobs.MODID, "textures/entity/grottol.png");
    private static final ResourceLocation TEXTURE_DEEPSLATE = ResourceLocation.fromNamespaceAndPath(MowziesMobs.MODID, "textures/entity/grottol_deepslate.png");

    public RenderGrottol(EntityRendererProvider.Context mgr) {
        super(mgr, new ModelGrottol<>(), 0.6f);
    }

    @Override
    protected float getFlipDegrees(EntityGrottol entity) {
        return 0;
    }

    @Override
    public ResourceLocation getTextureLocation(EntityGrottol entity) {
        return entity.getDeepslate() ? RenderGrottol.TEXTURE_DEEPSLATE : RenderGrottol.TEXTURE;
    }

    /*@Override
    public void doRender(EntityGrottol entity, double x, double y, double z, float yaw, float delta) {
        if (entity.hasMinecartBlockDisplay()) {
            if (!renderOutlines) {
                renderName(entity, x, y, z);
            }
        } else {
            super.doRender(entity, x, y, z, yaw, delta);
        }
    }

    @Override
    public void doRenderShadowAndFire(Entity entity, double x, double y, double z, float yaw, float delta) {
        if (!(entity instanceof EntityGrottol) || !((EntityGrottol) entity).hasMinecartBlockDisplay()) {
            super.doRenderShadowAndFire(entity, x, y, z, yaw, delta);
        }
    }*/
}
