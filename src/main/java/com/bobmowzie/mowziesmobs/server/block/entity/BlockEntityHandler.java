package com.bobmowzie.mowziesmobs.server.block.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class BlockEntityHandler {

    public static final DeferredRegister<BlockEntityType<?>> REG = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MowziesMobs.MODID);

    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<GongBlockEntity>> GONG_BLOCK_ENTITY = REG.register("gong_entity", () -> BlockEntityType.Builder.of(GongBlockEntity::new, BlockHandler.GONG.get()).build(null));
}
