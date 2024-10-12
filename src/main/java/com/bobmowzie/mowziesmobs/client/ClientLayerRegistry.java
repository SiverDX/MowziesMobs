package com.bobmowzie.mowziesmobs.client;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.render.entity.FrozenRenderHandler;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.SunblockLayer;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoPlayer;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import java.util.List;
import java.util.stream.Collectors;

// From https://github.com/Alex-the-666/AlexsMobs/blob/1.19/src/main/java/com/github/alexthe666/alexsmobs/client/ClientLayerRegistry.java
public class ClientLayerRegistry {
    @SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        List<EntityType<? extends LivingEntity>> entityTypes = ImmutableList.copyOf(
                Registries.ENTITY_TYPE.getValues().stream() // FIXME 1.21 :: registry retrieval without lookup
                        .filter(DefaultAttributes::hasSupplier)
                        .map(entityType -> (EntityType<? extends LivingEntity>) entityType)
                        .collect(Collectors.toList()));
        entityTypes.forEach((entityType -> {
            addLayerIfApplicable(entityType, event);
        }));
        for (PlayerSkin.Model skin : event.getSkins()){ // FIXME 1.21 :: add layers some other way
            event.getSkin(skin).addLayer(new FrozenRenderHandler.LayerFrozen(event.getSkin(skin)));
            event.getSkin(skin).addLayer(new SunblockLayer(event.getSkin(skin)));
        }

        GeckoPlayer.GeckoPlayerThirdPerson.initRenderer();
    }

    private static void addLayerIfApplicable(EntityType<? extends LivingEntity> entityType, EntityRenderersEvent.AddLayers event) {
        LivingEntityRenderer renderer = null;
        if(entityType != EntityType.ENDER_DRAGON){
            try{
                renderer = event.getRenderer(entityType);
            }catch (Exception e){
                if (!entityType.getBaseClass().isAssignableFrom(MowzieEntity.class)) {
                    MowziesMobs.LOGGER.warn("Could not apply layer to " + Registries.ENTITY_TYPE.getKey(entityType) + ", has custom renderer that is not LivingEntityRenderer.");
                }
            }
            if(renderer != null){
                renderer.addLayer(new FrozenRenderHandler.LayerFrozen(renderer));
                renderer.addLayer(new SunblockLayer(renderer));
            }
        }
    }
}
