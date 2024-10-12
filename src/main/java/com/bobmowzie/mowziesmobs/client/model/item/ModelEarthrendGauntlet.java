package com.bobmowzie.mowziesmobs.client.model.item;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.item.ItemEarthrendGauntlet;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ModelEarthrendGauntlet extends GeoModel<ItemEarthrendGauntlet> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(MowziesMobs.MODID, "textures/item/earthrend_gauntlet.png");

    @Override
    public ResourceLocation getModelResource(ItemEarthrendGauntlet object) {
        return ResourceLocation.fromNamespaceAndPath(MowziesMobs.MODID, "geo/earthrend_gauntlet.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ItemEarthrendGauntlet object) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(ItemEarthrendGauntlet animatable) {
        return ResourceLocation.fromNamespaceAndPath(MowziesMobs.MODID, "animations/earthrend_gauntlet.animation.json");
    }
}