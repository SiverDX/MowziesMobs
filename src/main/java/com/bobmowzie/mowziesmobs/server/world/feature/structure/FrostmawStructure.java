package com.bobmowzie.mowziesmobs.server.world.feature.structure;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.tag.TagHandler;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.HashMap;

public class FrostmawStructure extends MowzieStructure {
	public static final MapCodec<FrostmawStructure> CODEC = simpleCodec(FrostmawStructure::new);
	
    public FrostmawStructure(Structure.StructureSettings settings) {
        super(settings, ConfigHandler.COMMON.MOBS.FROSTMAW.generationConfig, StructureTypeHandler.FROSTMAW_BIOMES);
    }

    @Override
    public void generatePieces(StructurePiecesBuilder builder, GenerationContext context) {
        int x = context.chunkPos().getMinBlockX();
        int z = context.chunkPos().getMinBlockZ();
        int y = context.chunkGenerator().getFirstFreeHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());
        BlockPos blockpos = new BlockPos(x, y, z);
        Rotation rotation = Rotation.getRandom(context.random());
        FrostmawPieces.addPieces(context.structureTemplateManager(), blockpos, rotation, builder, context.random());
    }

	@Override
	public StructureType<?> type() {
		return StructureTypeHandler.FROSTMAW.get();
	}

    // FIXME 1.21 :: unused?
    public static FrostmawStructure buildStructureConfig(BootstrapContext<Structure> context) {
        return new FrostmawStructure(
                new Structure.StructureSettings(
                        context.lookup(Registries.BIOME).getOrThrow(TagHandler.HAS_MOWZIE_STRUCTURE),
                        new HashMap<>(),
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.BEARD_THIN
                )
        );
    }
}
