package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityRockSling;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import software.bernie.geckolib.model.GeoModel;

import java.util.Map;
import java.util.TreeMap;

public class ModelRockSling extends GeoModel<EntityRockSling> {
    static Map<String, ResourceLocation> texMap;
    private static final ResourceLocation TEXTURE_DIRT = new ResourceLocation("textures/block/dirt.png");
    private static final ResourceLocation TEXTURE_STONE = new ResourceLocation("textures/block/stone.png");
    private static final ResourceLocation TEXTURE_SANDSTONE = new ResourceLocation("textures/block/sandstone.png");
    private static final ResourceLocation TEXTURE_CLAY = new ResourceLocation("textures/block/clay.png");

    public ModelRockSling(){
        super();
        texMap = new TreeMap<String, ResourceLocation>();
        texMap.put(Blocks.STONE.getDescriptionId(), TEXTURE_STONE);
        texMap.put(Blocks.DIRT.getDescriptionId(), TEXTURE_DIRT);
        texMap.put(Blocks.CLAY.getDescriptionId(), TEXTURE_CLAY);
        texMap.put(Blocks.SANDSTONE.getDescriptionId(), TEXTURE_SANDSTONE);
    }

    @Override
    public ResourceLocation getAnimationResource(EntityRockSling entity) {
        return ResourceLocation.fromNamespaceAndPath(MowziesMobs.MODID, "animations/rock_sling.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(EntityRockSling entity) {
        return ResourceLocation.fromNamespaceAndPath(MowziesMobs.MODID, "geo/rock_sling.geo.json");

    }

    @Override
    public ResourceLocation getTextureResource(EntityRockSling entity) {
        if (entity.storedBlock != null) {
            ResourceLocation tex = texMap.get(entity.storedBlock.getBlock().getDescriptionId());
            if (tex != null) return tex;
        }
        return TEXTURE_DIRT;
    }

    @Override
    public RenderType getRenderType(EntityRockSling animatable, ResourceLocation texture) {
        return RenderType.entityCutout(texture);
    }
}
