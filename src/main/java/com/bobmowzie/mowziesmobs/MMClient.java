package com.bobmowzie.mowziesmobs;

import com.bobmowzie.mowziesmobs.client.ClientEventHandler;
import com.bobmowzie.mowziesmobs.client.ClientLayerRegistry;
import com.bobmowzie.mowziesmobs.client.MMModels;
import com.bobmowzie.mowziesmobs.client.render.entity.FrozenRenderHandler;
import com.bobmowzie.mowziesmobs.server.ability.AbilityClientEventHandler;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = MMCommon.MODID, dist = Dist.CLIENT)
public class MMClient {
    public MMClient(IEventBus modBus, ModContainer container) {
        modBus.register(MMModels.class);
        modBus.addListener(ClientLayerRegistry::onAddLayers);
        modBus.addListener(this::init);

        NeoForge.EVENT_BUS.register(ClientEventHandler.INSTANCE);
        NeoForge.EVENT_BUS.register(FrozenRenderHandler.INSTANCE);
        NeoForge.EVENT_BUS.register(AbilityClientEventHandler.INSTANCE);

        container.registerConfig(ModConfig.Type.CLIENT, ConfigHandler.CLIENT_CONFIG);
    }

    private void init(FMLLoadCompleteEvent event) {
        ItemPropertyFunction pulling = ItemProperties.getProperty(Items.BOW.getDefaultInstance(), ResourceLocation.withDefaultNamespace("pulling"));
        ItemProperties.register(ItemHandler.BLOWGUN.get().asItem(), ResourceLocation.withDefaultNamespace("pulling"), pulling);
    }
}
