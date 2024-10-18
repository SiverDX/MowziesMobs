package com.bobmowzie.mowziesmobs.server.world.feature.structure;

import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthanaMinion;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthi;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.MaskType;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.item.ItemUmvuthanaMask;
import com.bobmowzie.mowziesmobs.server.loot.LootTableHandler;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.material.FluidState;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UmvuthanaGrovePieces {
    private static final Set<Block> BLOCKS_NEEDING_POSTPROCESSING = ImmutableSet.<Block>builder().add(Blocks.NETHER_BRICK_FENCE).add(Blocks.TORCH).add(Blocks.WALL_TORCH).add(Blocks.OAK_FENCE).add(Blocks.SPRUCE_FENCE).add(Blocks.DARK_OAK_FENCE).add(Blocks.ACACIA_FENCE).add(Blocks.BIRCH_FENCE).add(Blocks.JUNGLE_FENCE).add(Blocks.MANGROVE_FENCE).add(Blocks.LADDER).add(Blocks.SKELETON_SKULL).build();

    public static final ResourceLocation PLATFORM_1 = ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "umvuthana/umvuthana_platform_1");
    public static final ResourceLocation PLATFORM_2 = ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "umvuthana/umvuthana_platform_2");
    public static final ResourceLocation[] PLATFORMS = new ResourceLocation[] {
            PLATFORM_1,
            PLATFORM_2
    };
    public static final ResourceLocation PLATFORM_EXTEND = ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "umvuthana/umvuthana_platform_extend");
    public static final ResourceLocation FIREPIT = ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "umvuthana/umvuthana_firepit");
    public static final ResourceLocation FIREPIT_SMALL_1 = ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "umvuthana/umvuthana_firepit_small_1");
    public static final ResourceLocation FIREPIT_SMALL_2 = ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "umvuthana/umvuthana_firepit_small_2");
    public static final ResourceLocation[] FIREPIT_SMALL = new ResourceLocation[] {
            FIREPIT_SMALL_1,
            FIREPIT_SMALL_2
    };
    public static final ResourceLocation TREE_1 = ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "umvuthana/umvuthana_tree_1");
    public static final ResourceLocation TREE_2 = ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "umvuthana/umvuthana_tree_2");
    public static final ResourceLocation TREE_3 = ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "umvuthana/umvuthana_tree_3");
    public static final ResourceLocation[] TREES = new ResourceLocation[] {
            TREE_1,
            TREE_2,
            TREE_3
    };
    public static final ResourceLocation SPIKE_1 = ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "umvuthana/umvuthana_spike_1");
    public static final ResourceLocation SPIKE_2 = ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "umvuthana/umvuthana_spike_2");
    public static final ResourceLocation SPIKE_3 = ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "umvuthana/umvuthana_spike_3");
    public static final ResourceLocation SPIKE_4 = ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "umvuthana/umvuthana_spike_4");
    public static final ResourceLocation[] SPIKES = new ResourceLocation[] {
            SPIKE_1,
            SPIKE_2,
            SPIKE_3,
            SPIKE_4
    };
    public static final ResourceLocation THRONE = ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "umvuthana/umvuthi_throne");

    private static final Map<ResourceLocation, BlockPos> OFFSET = ImmutableMap.<ResourceLocation, BlockPos>builder()
            .put(PLATFORM_1, new BlockPos(-5, 0, -5))
            .put(PLATFORM_2, new BlockPos(0, 0, -5))
            .put(PLATFORM_EXTEND, new BlockPos(8, 1, -2))
            .put(FIREPIT, new BlockPos(-3, -2, -3))
            .put(FIREPIT_SMALL_1, new BlockPos(-1, 0, -1))
            .put(FIREPIT_SMALL_2, new BlockPos(-1, 0, -1))
            .put(TREE_1, new BlockPos(-5, 1, -3))
            .put(TREE_2, new BlockPos(-3, 1, -3))
            .put(TREE_3, new BlockPos(-3, 1, -3))
            .put(SPIKE_1, new BlockPos(-1, 1, 0))
            .put(SPIKE_2, new BlockPos(0, 1, 0))
            .put(SPIKE_3, new BlockPos(0, 1, 0))
            .put(SPIKE_4, new BlockPos(0, 1, 0))
            .put(THRONE, new BlockPos(-9, 0, 0))
            .build();

    private static final Map<ResourceLocation, Pair<BlockPos, BlockPos>> BOUNDS_OFFSET = ImmutableMap.<ResourceLocation, Pair<BlockPos, BlockPos>>builder()
            .put(PLATFORM_1, new Pair<>(new BlockPos(1, 0, 0), new BlockPos(-3, 0, -3)))
            .put(PLATFORM_2, new Pair<>(new BlockPos(0, 0, 0), new BlockPos(0, 0, -3)))
            .put(PLATFORM_EXTEND, new Pair<>(new BlockPos(0, 0, 0), new BlockPos(0, 0, 0)))
            .put(FIREPIT, new Pair<>(new BlockPos(0, 0, 0), new BlockPos(0, 0, 0)))
            .put(FIREPIT_SMALL_1, new Pair<>(new BlockPos(0, 0, 0), new BlockPos(0, 0, 0)))
            .put(FIREPIT_SMALL_2, new Pair<>(new BlockPos(0, 0, 0), new BlockPos(0, 0, 0)))
            .put(TREE_1, new Pair<>(new BlockPos(1, 0, 1), new BlockPos(-3, 0, -3)))
            .put(TREE_2, new Pair<>(new BlockPos(2, 0, 1), new BlockPos(-1, 0, -3)))
            .put(TREE_3, new Pair<>(new BlockPos(2, 0, 2), new BlockPos(-2, 0, -2)))
            .put(SPIKE_1, new Pair<>(new BlockPos(0, 0, 0), new BlockPos(0, 0, 0)))
            .put(SPIKE_2, new Pair<>(new BlockPos(0, 0, 0), new BlockPos(0, 0, 0)))
            .put(SPIKE_3, new Pair<>(new BlockPos(0, 0, 0), new BlockPos(0, 0, 0)))
            .put(SPIKE_4, new Pair<>(new BlockPos(0, 0, 0), new BlockPos(0, 0, 0)))
            .put(THRONE, new Pair<>(new BlockPos(4, 0, 1), new BlockPos(-4, 0, -3)))
            .build();

    public static StructurePiece addPiece(ResourceLocation resourceLocation, StructureTemplateManager manager, BlockPos pos, Rotation rot, StructurePieceAccessor pieces, WorldgenRandom rand) {
        StructurePiece newPiece = new UmvuthanaGrovePieces.Piece(manager, resourceLocation, rot, pos);
        pieces.addPiece(newPiece);
        return newPiece;
    }

    public static StructurePiece addPieceCheckBounds(ResourceLocation resourceLocation, StructureTemplateManager manager, BlockPos pos, Rotation rot, StructurePieceAccessor pieces, WorldgenRandom rand, List<StructurePiece> ignore) {
        UmvuthanaGrovePieces.Piece newPiece = new UmvuthanaGrovePieces.Piece(manager, resourceLocation, rot, pos);
        StructurePiece collisionPiece = pieces.findCollisionPiece(newPiece.getCollisionBoundingBox());
        if (collisionPiece != null && !ignore.contains(collisionPiece)) return null;
        pieces.addPiece(newPiece);
        return newPiece;
    }

    public static StructurePiece addPlatform(StructureTemplateManager manager, BlockPos pos, Rotation rot, StructurePiecesBuilder builder, WorldgenRandom rand) {
        int whichPlatform = rand.nextInt(PLATFORMS.length);
        Piece newPiece = new Piece(manager, PLATFORMS[whichPlatform], rot, pos);
        if (findCollisionPiece(builder.pieces, newPiece.getCollisionBoundingBox()) != null) return null;
        builder.addPiece(newPiece);
        if (whichPlatform == 1) {
            Piece extension = new Piece(manager, PLATFORM_EXTEND, rot, pos);
            if (findCollisionPiece(builder.pieces, extension.getCollisionBoundingBox(), newPiece) != null) return newPiece;
            builder.addPiece(extension);
        }
        return newPiece;
    }

    public static StructurePiece addPieceCheckBounds(ResourceLocation resourceLocation, StructureTemplateManager manager, BlockPos pos, Rotation rot, StructurePiecesBuilder pieces, WorldgenRandom rand) {
       return addPieceCheckBounds(resourceLocation, manager, pos, rot, pieces, rand, Collections.emptyList());
    }

    @Nullable
    public static StructurePiece findCollisionPiece(List<StructurePiece> pieces, BoundingBox bounds, StructurePiece ignore) {
        for(StructurePiece structurePiece : pieces) {
            if (structurePiece == ignore) continue;
            if (structurePiece instanceof Piece && ((Piece)structurePiece).getCollisionBoundingBox().intersects(bounds)) {
                return structurePiece;
            }
            else if (structurePiece.getBoundingBox().intersects(bounds)) {
                return structurePiece;
            }
        }
        return null;
    }

    @Nullable
    public static StructurePiece findCollisionPiece(List<StructurePiece> pieces, BoundingBox bounds) {
        return findCollisionPiece(pieces, bounds, null);
    }

    public static class Piece extends TemplateStructurePiece {
        protected ResourceLocation resourceLocation;
        public BoundingBox collisionBoundingBox;

        public Piece(StructurePieceType pieceType, StructureTemplateManager manager, ResourceLocation resourceLocationIn, Rotation rotation, BlockPos pos) {
            super(pieceType, 0, manager, resourceLocationIn, resourceLocationIn.toString(), makeSettings(rotation, resourceLocationIn), makePosition(resourceLocationIn, pos, rotation));
            this.resourceLocation = resourceLocationIn;
            this.collisionBoundingBox = makeCollisionBoundingBox();
            if (resourceLocation == THRONE || resourceLocation == FIREPIT) boundingBox = getBoundingBox().moved(0, 1, 0);
        }

        public Piece(StructurePieceType pieceType, StructurePieceSerializationContext context, CompoundTag tagCompound) {
            super(pieceType, tagCompound, context.structureTemplateManager(), (resourceLocation) -> makeSettings(Rotation.valueOf(tagCompound.getString("Rot")), resourceLocation));
            this.collisionBoundingBox = makeCollisionBoundingBox();
            if (resourceLocation == THRONE || resourceLocation == FIREPIT) boundingBox = getBoundingBox().moved(0, 1, 0);
        }

        public Piece(StructureTemplateManager manager, ResourceLocation resourceLocationIn, Rotation rotation, BlockPos pos) {
            this(StructureTypeHandler.UMVUTHANA_GROVE_PIECE.get(), manager, resourceLocationIn, rotation, pos);
        }

        public Piece(StructurePieceSerializationContext context, CompoundTag tagCompound) {
            this(StructureTypeHandler.UMVUTHANA_GROVE_PIECE.get(), context, tagCompound);
        }

        private static StructurePlaceSettings makeSettings(Rotation rotation, ResourceLocation resourceLocation) {
            return (new StructurePlaceSettings()).setRotation(rotation).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
        }

        private static BlockPos makePosition(ResourceLocation resourceLocation, BlockPos pos, Rotation rotation) {
            return pos.offset(UmvuthanaGrovePieces.OFFSET.get(resourceLocation).rotate(rotation));
        }

        public BoundingBox makeCollisionBoundingBox() {
            StructureTemplate structuretemplate = this.template;
            BlockPos boundsMinOffset, boundsMaxOffset;
            boundsMinOffset = boundsMaxOffset = new BlockPos(0, 0, 0);
            Pair<BlockPos, BlockPos> boundsOffset = BOUNDS_OFFSET.get(resourceLocation);
            if (boundsOffset != null) {
                boundsMinOffset = boundsOffset.getFirst();
                boundsMaxOffset = boundsOffset.getSecond();
            }

            Vec3i sizeVec = structuretemplate.getSize().offset(-1, -1, -1);
            BlockPos blockpos = StructureTemplate.transform(BlockPos.ZERO.offset(boundsMinOffset), placeSettings.getMirror(), placeSettings.getRotation(), placeSettings.getRotationPivot());
            BlockPos blockpos1 = StructureTemplate.transform(BlockPos.ZERO.offset(sizeVec).offset(boundsMaxOffset), placeSettings.getMirror(), placeSettings.getRotation(), placeSettings.getRotationPivot());
            return BoundingBox.fromCorners(blockpos, blockpos1).move(templatePosition);
        }

        public BoundingBox getCollisionBoundingBox() {
            return collisionBoundingBox;
        }

        /**
         * (abstract) Helper method to read subclass data from NBT
         */
        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tagCompound) {
            super.addAdditionalSaveData(context, tagCompound);
            tagCompound.putString("Rot", this.placeSettings.getRotation().name());
        }

        @Override
        public void postProcess(WorldGenLevel p_192682_, StructureManager p_192683_, ChunkGenerator p_192684_, RandomSource p_192685_, BoundingBox p_192686_, ChunkPos p_192687_, BlockPos p_192688_) {
            super.postProcess(p_192682_, p_192683_, p_192684_, p_192685_, p_192686_, p_192687_, p_192688_);
        }

        /*
         * If you added any data marker structure blocks to your structure, you can access and modify them here. In this case,
         * our structure has a data maker with the string "chest" put into it. So we check to see if the incoming function is
         * "chest" and if it is, we now have that exact position.
         *
         * So what is done here is we replace the structure block with a chest and we can then set the loottable for it.
         *
         * You can set other data markers to do other behaviors such as spawn a random mob in a certain spot, randomize what
         * rare block spawns under the floor, or what item an Item Frame will have.
         */
        @Override
        protected void handleDataMarker(String function, BlockPos pos, ServerLevelAccessor worldIn, RandomSource rand, BoundingBox sbb) {
            Rotation rotation = this.placeSettings.getRotation();
            if (function.equals("support")) {
                worldIn.setBlock(pos, Blocks.OAK_FENCE.defaultBlockState(), 3);
                fillAirLiquidDown(worldIn, Blocks.OAK_FENCE.defaultBlockState(), pos.below());
            }
            else if (function.equals("trunk")) {
                fillAirLiquidDownTrunk(worldIn, pos, rand);
            }
            else if (function.equals("leg")) {
                fillAirLiquidDown(worldIn, Blocks.STRIPPED_MANGROVE_LOG.defaultBlockState(), pos);
            }
            else if (function.equals("base")) {
                fillAirLiquidDownBase(worldIn, pos, rand);
            }
            else if (function.equals("umvuthi")) {
                setBlockState(worldIn, pos, Blocks.AIR.defaultBlockState());
                EntityUmvuthi barako = new EntityUmvuthi(EntityHandler.UMVUTHI.get(), worldIn.getLevel());
                barako.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                int i = rotation.rotate(3, 4);
                barako.setDirection(i);
                barako.finalizeSpawn(worldIn, worldIn.getCurrentDifficultyAt(barako.blockPosition()), MobSpawnType.STRUCTURE, null);
                BlockPos offset = new BlockPos(0, 0, -18);
                offset = offset.rotate(rotation);
                BlockPos firePitPos = pos.offset(offset);
                firePitPos = worldIn.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, firePitPos);
                barako.restrictTo(firePitPos, -1);
                worldIn.addFreshEntity(barako);
            }
            else if ("chest".equals(function)) {
                Direction facing = Direction.NORTH;
                facing = rotation.rotate(facing);
                createChest(worldIn, sbb, rand, pos, LootTableHandler.UMVUTHANA_GROVE_CHEST, Blocks.CHEST.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, facing));
            }
            else if ("skull".equals(function)) {
                BlockPos groundPos = getGroundPos(worldIn, pos);
                setBlockState(worldIn, groundPos.above(), Blocks.SKELETON_SKULL.defaultBlockState().setValue(BlockStateProperties.ROTATION_16, rand.nextInt(16)));
            }
            else if ("campfire".equals(function)) {
                BlockPos groundPos = getGroundPos(worldIn, pos);
                setBlockState(worldIn, groundPos.above(), Blocks.CAMPFIRE.defaultBlockState());
            }
            else if (function.length() > 5 && "spike".equals(function.substring(0, 5))) {
                String[] split = function.split("_");
                int logCount = 2;
                int fenceCount = 1;
                int barCount = 1;
                int skullCount = 0;
                if (split.length > 1) logCount = Integer.parseInt(split[1]);
                if (split.length > 2) fenceCount = Integer.parseInt(split[2]);
                if (split.length > 3) barCount = Integer.parseInt(split[3]);
                if (split.length > 4) skullCount = Integer.parseInt(split[4]);
                genSpike(worldIn, pos, rand, logCount, fenceCount, barCount, skullCount);
            }
            else if (function.length() > 6 && "stairs".equals(function.substring(0, 6))) {
                String[] split = function.split("_");
                Direction stairDirection = Direction.EAST;
                Direction newDirection = null;
                if (split.length > 1) newDirection = Direction.byName(split[1]);
                if (newDirection != null) stairDirection = newDirection;
                stairDirection = rotation.rotate(stairDirection);
                genStairs(worldIn, pos, rand, stairDirection);
            }
            else if ("chest_under".equals(function)) {
                if (rand.nextFloat() < 0.5) worldIn.removeBlock(pos, false);
                else {
                    BlockPos groundPos = getGroundPos(worldIn, pos);
                    Direction facing = rand.nextFloat() < 0.5 ? Direction.NORTH : Direction.EAST;
                    facing = rotation.rotate(facing);
                    createChest(worldIn, sbb, rand, groundPos.above(), LootTableHandler.UMVUTHANA_GROVE_CHEST, Blocks.CHEST.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, facing));
                }
            }
            else if (function.length() > 4 && "mask".equals(function.substring(0, 4))) {
                worldIn.removeBlock(pos, false);
                String[] split = function.split("_");
                Direction direction = Direction.NORTH;
                if (split.length > 1) direction = Direction.byName(split[1]);
                ItemFrame itemFrame = new ItemFrame(worldIn.getLevel(), pos, rotation.rotate(direction));
                int i = rand.nextInt(MaskType.values().length);
                MaskType type = MaskType.values()[i];
                ItemUmvuthanaMask mask = ItemHandler.UMVUTHANA_MASK_FURY.get();
                switch (type) {
                    case BLISS:
                        mask = ItemHandler.UMVUTHANA_MASK_BLISS.get();
                        break;
                    case FEAR:
                        mask = ItemHandler.UMVUTHANA_MASK_FEAR.get();
                        break;
                    case FURY:
                        mask = ItemHandler.UMVUTHANA_MASK_FURY.get();
                        break;
                    case MISERY:
                        mask = ItemHandler.UMVUTHANA_MASK_MISERY.get();
                        break;
                    case RAGE:
                        mask = ItemHandler.UMVUTHANA_MASK_RAGE.get();
                        break;
                    case FAITH:
                        mask = ItemHandler.UMVUTHANA_MASK_FAITH.get();
                        break;
                }
                ItemStack stack = new ItemStack(mask);
                itemFrame.setItem(stack, false);
                worldIn.addFreshEntity(itemFrame);
            }
            else {
                worldIn.removeBlock(pos, false);
            }
        }

        protected void setBlockState(LevelAccessor worldIn, BlockPos pos, BlockState state) {
            FluidState ifluidstate = worldIn.getFluidState(pos);
            if (!ifluidstate.isEmpty()) {
                worldIn.getFluidTicks().willTickThisTick(pos, ifluidstate.getType());
                if (state.hasProperty(BlockStateProperties.WATERLOGGED)) state = state.setValue(BlockStateProperties.WATERLOGGED, true);
            }
            worldIn.setBlock(pos, state, 2);
            if (BLOCKS_NEEDING_POSTPROCESSING.contains(state.getBlock())) {
                worldIn.getChunk(pos).markPosForPostprocessing(pos);
            }
        }

        public BlockPos getGroundPos(LevelAccessor worldIn, BlockPos startPos) {
            while(!Block.canSupportRigidBlock(worldIn, startPos) && startPos.getY() > worldIn.getMinBuildHeight()) {
                startPos = startPos.below();
            }
            return startPos;
        }

        public void fillAirLiquidDown(LevelAccessor worldIn, BlockState state, BlockPos startPos) {
            int i = startPos.getX();
            int j = startPos.getY();
            int k = startPos.getZ();
            while(!Block.canSupportRigidBlock(worldIn, new BlockPos(i, j, k)) && j > 1) {
                BlockPos pos = new BlockPos(i, j, k);
                setBlockState(worldIn, pos, state);
                --j;
            }
        }

        public void fillAirLiquidDownTrunk(LevelAccessor worldIn, BlockPos startPos, RandomSource rand) {
            int i = startPos.getX();
            int j = startPos.getY();
            int k = startPos.getZ();
            while(!Block.canSupportRigidBlock(worldIn, new BlockPos(i, j, k)) && j > 1) {
                BlockPos pos = new BlockPos(i, j, k);
                setBlockState(worldIn, pos, rand.nextFloat() < 0.2 ? BlockHandler.CLAWED_LOG.get().defaultBlockState() : Blocks.STRIPPED_JUNGLE_WOOD.defaultBlockState());
                --j;
            }
        }

        public void fillAirLiquidDownBase(LevelAccessor worldIn, BlockPos startPos, RandomSource rand) {
            int i = startPos.getX();
            int j = startPos.getY();
            int k = startPos.getZ();
            while(!Block.canSupportRigidBlock(worldIn, new BlockPos(i, j, k)) && j > 1) {
                BlockPos pos = new BlockPos(i, j, k);
                setBlockState(worldIn, pos, rand.nextFloat() < 0.5 ? Blocks.STRIPPED_MANGROVE_LOG.defaultBlockState() : Blocks.RED_TERRACOTTA.defaultBlockState());
                --j;
            }
        }

        public void genStairs(LevelAccessor worldIn, BlockPos pos, RandomSource rand, Direction direction) {
            for (int i = 1; i < 5; i++) {
                if (!Block.canSupportRigidBlock(worldIn, pos)) {
                    BlockState state = rand.nextFloat() > 0.5 ? Blocks.ACACIA_SLAB.defaultBlockState() : Blocks.SMOOTH_RED_SANDSTONE_SLAB.defaultBlockState();
                    if (i % 2 == 1) state = state.setValue(SlabBlock.TYPE, SlabType.TOP);
                    setBlockState(worldIn, pos, state);

                    pos = pos.relative(direction);
                    if (i % 2 == 0) pos = pos.relative(Direction.DOWN);
                } else {
                    return;
                }
            }
            pos = pos.relative(direction.getOpposite());
            fillAirLiquidDown(worldIn, Blocks.STRIPPED_MANGROVE_LOG.defaultBlockState(), pos);
            fillAirLiquidDown(worldIn, Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, direction), pos.relative(direction));
        }

        public void genSpike(LevelAccessor worldIn, BlockPos startPos, RandomSource rand, int numLogs, int numFence, int numBars, int numSkulls) {
            int groundPos = worldIn.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, startPos.getX(), startPos.getZ());
            BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(startPos.getX(), groundPos - 1, startPos.getZ());
            for (int i = 0; i < numLogs; i++) {
                setBlockState(worldIn, pos, Blocks.STRIPPED_MANGROVE_LOG.defaultBlockState());
                pos.move(Direction.UP);
            }
            for (int i = 0; i < numFence; i++) {
                setBlockState(worldIn, pos, Blocks.MANGROVE_FENCE.defaultBlockState());
                pos.move(Direction.UP);
            }
            for (int i = 0; i < numBars; i++) {
                setBlockState(worldIn, pos, Blocks.IRON_BARS.defaultBlockState());
                pos.move(Direction.UP);
            }
            if (rand.nextFloat() < 0.1 && numSkulls > 0) {
                setBlockState(worldIn, pos, Blocks.SKELETON_SKULL.defaultBlockState().setValue(BlockStateProperties.ROTATION_16, rand.nextInt(16)));
            }
        }
    }

    public static class FirepitPiece extends Piece {

        public FirepitPiece(StructureTemplateManager manager, Rotation rotation, BlockPos pos) {
            super(StructureTypeHandler.UMVUTHANA_FIREPIT.get(), manager, FIREPIT, rotation, pos);
        }

        public FirepitPiece(StructurePieceSerializationContext context, CompoundTag tagCompound) {
            super(StructureTypeHandler.UMVUTHANA_FIREPIT.get(), context, tagCompound);
        }

        public BlockPos findGround(LevelAccessor worldIn, int x, int z) {
            int i = this.getWorldX(x, z);
            int k = this.getWorldZ(x, z);
            int j = worldIn.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, i, k);
            return new BlockPos(i, j, k);
        }

        @Override
        public void postProcess(WorldGenLevel worldIn, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource randomIn, BoundingBox p_230383_5_, ChunkPos p_230383_6_, BlockPos p_230383_7_) {
            super.postProcess(worldIn, structureManager, chunkGenerator, randomIn, p_230383_5_, p_230383_6_, p_230383_7_);
            BlockPos centerPos = findGround(worldIn, 4, 4);

            // Spawn Umvuthana
            int numUmvuthana = randomIn.nextInt(5) + 5;
            for (int i = 1; i <= numUmvuthana; i++) {
                int distance;
                int angle;
                EntityUmvuthanaMinion umvuthana = new EntityUmvuthanaMinion(EntityHandler.UMVUTHANA_MINION.get(), worldIn.getLevel());
                for (int j = 1; j <= 20; j++) {
                    distance = randomIn.nextInt(10) + 2;
                    angle = randomIn.nextInt(360);
                    int x = (int) (distance * Math.sin(Math.toRadians(angle))) + 4;
                    int z = (int) (distance * Math.cos(Math.toRadians(angle))) + 4;
                    BlockPos bPos = findGround(worldIn, x, z);
                    umvuthana.setPos(bPos.getX(), bPos.getY(), bPos.getZ());
                    if (bPos.getY() > 0 && umvuthana.checkSpawnRules(worldIn, MobSpawnType.STRUCTURE) && worldIn.noCollision(umvuthana.getBoundingBox())) {
                        umvuthana.finalizeSpawn(worldIn, worldIn.getCurrentDifficultyAt(umvuthana.blockPosition()), MobSpawnType.STRUCTURE, null, null);
                        umvuthana.restrictTo(centerPos, 25);
                        worldIn.addFreshEntity(umvuthana);
                        break;
                    }
                }
            }
        }
    }
}
