package com.bobmowzie.mowziesmobs.server.block;

import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class BlockHandler {

    public static final DeferredRegister<Block> REG = DeferredRegister.create(Registries.BLOCK, MMCommon.MODID);

    // FIXME 1.21 :: replace 'copy' with either 'ofFullCopy' or 'ofLegacyCopy'
    public static final DeferredHolder<Block, Block> PAINTED_ACACIA = registerBlockAndItem("painted_acacia", () -> new Block(Block.Properties.copy(Blocks.ACACIA_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final DeferredHolder<Block, Block> PAINTED_ACACIA_SLAB = registerBlockAndItem("painted_acacia_slab", () -> new SlabBlock(Block.Properties.copy(PAINTED_ACACIA.get())));
    public static final DeferredHolder<Block, Block> THATCH = registerBlockAndItem("thatch_block", () -> new HayBlock(BlockBehaviour.Properties.copy(Blocks.HAY_BLOCK)));
    public static final DeferredHolder<Block, Block> GONG = registerBlockAndItem("gong", () -> new GongBlock(BlockBehaviour.Properties.copy(Blocks.GOLD_BLOCK).requiresCorrectToolForDrops().strength(3.0F).sound(SoundType.ANVIL)));
    public static final DeferredHolder<Block, Block> GONG_PART = REG.register("gong_part", () -> new GongBlock.GongPartBlock(BlockBehaviour.Properties.copy(Blocks.GOLD_BLOCK).requiresCorrectToolForDrops().strength(3.0F).sound(SoundType.ANVIL)));
    public static final DeferredHolder<Block, Block> RAKED_SAND = registerBlockAndItem("raked_sand", () -> new RakedSandBlock(14406560, BlockBehaviour.Properties.copy(Blocks.SAND), Blocks.SAND.defaultBlockState()));
    public static final DeferredHolder<Block, Block> RED_RAKED_SAND = registerBlockAndItem("red_raked_sand", () -> new RakedSandBlock(11098145, BlockBehaviour.Properties.copy(Blocks.RED_SAND), Blocks.RED_SAND.defaultBlockState()));
    public static final DeferredHolder<Block, Block> CLAWED_LOG = registerBlockAndItem("clawed_log", () -> new Block(Block.Properties.copy(Blocks.ACACIA_PLANKS)));
    //public static final RegistryObject<BlockGrottol> GROTTOL = REG.register("grottol", () -> new BlockGrottol(Block.Properties.copy(Material.STONE).noDrops()));

    public static DeferredHolder<Block, Block> registerBlockAndItem(String name, Supplier<Block> block){
        DeferredHolder<Block, Block> blockObj = REG.register(name, block);
        ItemHandler.REG.register(name, () -> new BlockItem(blockObj.get(), new Item.Properties()));
        return blockObj;
    }

    public static void init() {
        FireBlock fireblock = (FireBlock)Blocks.FIRE;
        fireblock.setFlammable(THATCH.get(), 60, 20);
        fireblock.setFlammable(PAINTED_ACACIA.get(), 5, 20);
        fireblock.setFlammable(PAINTED_ACACIA_SLAB.get(), 5, 20);
        fireblock.setFlammable(CLAWED_LOG.get(), 5, 5);
    }
}