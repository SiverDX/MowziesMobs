package com.bobmowzie.mowziesmobs.server.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.effects.*;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.*;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityBabyFoliaath;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityFoliaath;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrostmaw;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrozenController;
import com.bobmowzie.mowziesmobs.server.entity.grottol.EntityGrottol;
import com.bobmowzie.mowziesmobs.server.entity.lantern.EntityLantern;
import com.bobmowzie.mowziesmobs.server.entity.naga.EntityNaga;
import com.bobmowzie.mowziesmobs.server.entity.sculptor.EntitySculptor;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.*;
import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class EntityHandler {
    public static final DeferredRegister<EntityType<?>> REG = DeferredRegister.create(Registries.ENTITY_TYPE, MowziesMobs.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<EntityFoliaath>> FOLIAATH = REG.register("foliaath", () -> EntityType.Builder.of(EntityFoliaath::new, MobCategory.MONSTER).sized(0.5f, 2.5f).build(ResourceLocation.fromNamespaceAndPath(MowziesMobs.MODID, "foliaath").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<EntityBabyFoliaath>> BABY_FOLIAATH = REG.register("baby_foliaath", () -> EntityType.Builder.of(EntityBabyFoliaath::new, MobCategory.MONSTER).sized(0.4f, 0.4f).build(ResourceLocation.fromNamespaceAndPath(MowziesMobs.MODID, "baby_foliaath").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<EntityWroughtnaut>> WROUGHTNAUT = REG.register("ferrous_wroughtnaut", () -> EntityType.Builder.of(EntityWroughtnaut::new, MobCategory.MONSTER).sized(2.5f, 3.5f).setUpdateInterval(1).build(ResourceLocation.fromNamespaceAndPath(MowziesMobs.MODID, "ferrous_wroughtnaut").toString()));
    private static EntityType.Builder<EntityUmvuthanaFollowerToRaptor> umvuthanaFollowerToRaptorBuilder() {
        return EntityType.Builder.of(EntityUmvuthanaFollowerToRaptor::new, MobCategory.MONSTER);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntityUmvuthanaFollowerToRaptor>> UMVUTHANA_FOLLOWER_TO_RAPTOR = REG.register("umvuthana_follower_raptor", () -> umvuthanaFollowerToRaptorBuilder().sized(MaskType.FEAR.entityWidth, MaskType.FEAR.entityHeight).setUpdateInterval(1).build(ResourceLocation.fromNamespaceAndPath(MowziesMobs.MODID, "umvuthana_follower_raptor").toString()));
    private static EntityType.Builder<EntityUmvuthanaFollowerToPlayer> umvuthanaFollowerToPlayerBuilder() {
        return EntityType.Builder.of(EntityUmvuthanaFollowerToPlayer::new, MobCategory.MONSTER);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntityUmvuthanaFollowerToPlayer>> UMVUTHANA_FOLLOWER_TO_PLAYER = REG.register("umvuthana_follower_player", () -> umvuthanaFollowerToPlayerBuilder().sized(MaskType.FEAR.entityWidth, MaskType.FEAR.entityHeight).setUpdateInterval(1).build(ResourceLocation.fromNamespaceAndPath(MowziesMobs.MODID, "umvuthana_follower_player").toString()));
    private static EntityType.Builder<EntityUmvuthanaCraneToPlayer> umvuthanaCraneToPlayerBuilder() {
        return EntityType.Builder.of(EntityUmvuthanaCraneToPlayer::new, MobCategory.MONSTER);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntityUmvuthanaCraneToPlayer>> UMVUTHANA_CRANE_TO_PLAYER = REG.register("umvuthana_crane_player", () -> umvuthanaCraneToPlayerBuilder().sized(MaskType.FAITH.entityWidth, MaskType.FAITH.entityHeight).setUpdateInterval(1).build(ResourceLocation.fromNamespaceAndPath(MowziesMobs.MODID, "umvuthana_crane_player").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<EntityUmvuthanaMinion>> UMVUTHANA_MINION = REG.register("umvuthana", () -> EntityType.Builder.of(EntityUmvuthanaMinion::new, MobCategory.MONSTER).sized(MaskType.FEAR.entityWidth, MaskType.FEAR.entityHeight).setUpdateInterval(1).build(ResourceLocation.fromNamespaceAndPath(MowziesMobs.MODID, "umvuthana").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<EntityUmvuthanaRaptor>> UMVUTHANA_RAPTOR = REG.register("umvuthana_raptor", () -> EntityType.Builder.of(EntityUmvuthanaRaptor::new, MobCategory.MONSTER).sized(MaskType.FURY.entityWidth, MaskType.FURY.entityHeight).setUpdateInterval(1).build(ResourceLocation.fromNamespaceAndPath(MowziesMobs.MODID, "umvuthana_raptor").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<EntityUmvuthanaCrane>> UMVUTHANA_CRANE = REG.register("umvuthana_crane", () -> EntityType.Builder.of(EntityUmvuthanaCrane::new, MobCategory.MONSTER).sized(MaskType.FEAR.entityWidth, MaskType.FEAR.entityHeight).setUpdateInterval(1).build(ResourceLocation.fromNamespaceAndPath(MowziesMobs.MODID, "umvuthana_crane").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<EntityUmvuthi>> UMVUTHI = REG.register("umvuthi", () -> EntityType.Builder.of(EntityUmvuthi::new, MobCategory.MONSTER).sized(1.5f, 3.2f).setUpdateInterval(1).build(ResourceLocation.fromNamespaceAndPath(MowziesMobs.MODID, "umvuthi").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<EntityFrostmaw>> FROSTMAW = REG.register("frostmaw", () -> EntityType.Builder.of(EntityFrostmaw::new, MobCategory.MONSTER).sized(4f, 4f).setUpdateInterval(1).build(ResourceLocation.fromNamespaceAndPath(MowziesMobs.MODID, "frostmaw").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<EntityGrottol>> GROTTOL = REG.register("grottol", () -> EntityType.Builder.of(EntityGrottol::new, MobCategory.MONSTER).sized(0.9F, 1.2F).setUpdateInterval(1).build(ResourceLocation.fromNamespaceAndPath(MowziesMobs.MODID, "grottol").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<EntityLantern>> LANTERN = REG.register("lantern", () -> EntityType.Builder.of(EntityLantern::new, MobCategory.AMBIENT).sized(1.0f, 1.0f).setUpdateInterval(1).build(ResourceLocation.fromNamespaceAndPath(MowziesMobs.MODID, "lantern").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<EntityNaga>> NAGA = REG.register("naga", () -> EntityType.Builder.of(EntityNaga::new, MobCategory.MONSTER).sized(3.0f, 1.0f).setTrackingRange(128).setUpdateInterval(1).build(ResourceLocation.fromNamespaceAndPath(MowziesMobs.MODID, "naga").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<EntitySculptor>> SCULPTOR = REG.register("sculptor", () -> EntityType.Builder.of(EntitySculptor::new, MobCategory.MISC).sized(1.0f, 2.3f).setUpdateInterval(1).build(ResourceLocation.fromNamespaceAndPath(MowziesMobs.MODID, "sculptor").toString()));

    private static EntityType.Builder<EntitySunstrike> sunstrikeBuilder() {
        return EntityType.Builder.of(EntitySunstrike::new, MobCategory.MISC);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntitySunstrike>> SUNSTRIKE = REG.register("sunstrike", () -> sunstrikeBuilder().sized(0.1F, 0.1F).build(ResourceLocation.fromNamespaceAndPath(MowziesMobs.MODID, "sunstrike").toString()));
    private static EntityType.Builder<EntitySolarBeam> solarBeamBuilder() {
        return EntityType.Builder.of(EntitySolarBeam::new, MobCategory.MISC);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntitySolarBeam>> SOLAR_BEAM = REG.register("solar_beam", () -> solarBeamBuilder().sized(0.1F, 0.1F).setUpdateInterval(1).build(ResourceLocation.fromNamespaceAndPath(MowziesMobs.MODID, "solar_beam").toString()));
    private static EntityType.Builder<EntityBoulderProjectile> boulderProjectileBuilder() {
        return EntityType.Builder.of(EntityBoulderProjectile::new, MobCategory.MISC);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntityBoulderProjectile>> BOULDER_PROJECTILE = REG.register("boulder_projectile", () -> boulderProjectileBuilder().sized(1, 1).setUpdateInterval(1).build(ResourceLocation.fromNamespaceAndPath(MowziesMobs.MODID, "boulder_projectile").toString()));
    private static EntityType.Builder<EntityRockSling> rockSlingBuilder() {
        return EntityType.Builder.of(EntityRockSling::new, MobCategory.MISC);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntityRockSling>> ROCK_SLING = REG.register("rock_sling", () -> rockSlingBuilder().sized(1, 1).setUpdateInterval(1).build(ResourceLocation.fromNamespaceAndPath(MowziesMobs.MODID, "rock_sling").toString()));
    private static EntityType.Builder<EntityBoulderSculptor> boulderPlatformBuilder() {
        return EntityType.Builder.of(EntityBoulderSculptor::new, MobCategory.MISC);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntityBoulderSculptor>> BOULDER_SCULPTOR = REG.register("boulder_platform", () -> boulderPlatformBuilder().sized(1, 1).setUpdateInterval(1).build(ResourceLocation.fromNamespaceAndPath(MowziesMobs.MODID, "boulder_platform").toString()));
    private static EntityType.Builder<EntityPillar> pillarBuilder() {
        return EntityType.Builder.of(EntityPillar::new, MobCategory.MISC);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntityPillar>> PILLAR = REG.register("pillar", () -> pillarBuilder().sized(1f, 1f).setUpdateInterval(1).build(ResourceLocation.fromNamespaceAndPath(MowziesMobs.MODID, "pillar").toString()));
    private static EntityType.Builder<EntityPillar.EntityPillarSculptor> sculptorPillarBuilder() {
        return EntityType.Builder.of(EntityPillar.EntityPillarSculptor::new, MobCategory.MISC);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntityPillar.EntityPillarSculptor>> PILLAR_SCULPTOR = REG.register("pillar_sculptor", () -> sculptorPillarBuilder().sized(1f, 1f).setUpdateInterval(1).build(ResourceLocation.fromNamespaceAndPath(MowziesMobs.MODID, "pillar_sculptor").toString()));
    private static EntityType.Builder<EntityPillarPiece> pillarPieceBuilder() {
        return EntityType.Builder.of(EntityPillarPiece::new, MobCategory.MISC);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntityPillarPiece>> PILLAR_PIECE = REG.register("pillar_piece", () -> pillarPieceBuilder().sized(1f, 1f).setUpdateInterval(1).build(ResourceLocation.fromNamespaceAndPath(MowziesMobs.MODID, "pillar_piece").toString()));

    private static EntityType.Builder<EntityAxeAttack> axeAttackBuilder() {
        return EntityType.Builder.of(EntityAxeAttack::new, MobCategory.MISC);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntityAxeAttack>> AXE_ATTACK = REG.register("axe_attack", () -> axeAttackBuilder().sized(1f, 1f).setUpdateInterval(1).build(ResourceLocation.fromNamespaceAndPath(MowziesMobs.MODID, "axe_attack").toString()));
    private static EntityType.Builder<EntityIceBreath> iceBreathBuilder() {
        return EntityType.Builder.of(EntityIceBreath::new, MobCategory.MISC);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntityIceBreath>> ICE_BREATH = REG.register("ice_breath", () -> iceBreathBuilder().sized(0F, 0F).setUpdateInterval(1).build(ResourceLocation.fromNamespaceAndPath(MowziesMobs.MODID, "ice_breath").toString()));
    private static EntityType.Builder<EntityIceBall> iceBallBuilder() {
        return EntityType.Builder.of(EntityIceBall::new, MobCategory.MISC);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntityIceBall>> ICE_BALL = REG.register("ice_ball", () -> iceBallBuilder().sized(0.5F, 0.5F).setUpdateInterval(20).build(ResourceLocation.fromNamespaceAndPath(MowziesMobs.MODID, "ice_ball").toString()));
    private static EntityType.Builder<EntityFrozenController> frozenControllerBuilder() {
        return EntityType.Builder.of(EntityFrozenController::new, MobCategory.MISC);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntityFrozenController>> FROZEN_CONTROLLER = REG.register("frozen_controller", () -> frozenControllerBuilder().noSummon().sized(0, 0).build(ResourceLocation.fromNamespaceAndPath(MowziesMobs.MODID, "frozen_controller").toString()));
    private static EntityType.Builder<EntityDart> dartBuilder() {
        return EntityType.Builder.of(EntityDart::new, MobCategory.MISC);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntityDart>> DART = REG.register("dart", () -> dartBuilder().noSummon().sized(0.5F, 0.5F).setUpdateInterval(20).build(ResourceLocation.fromNamespaceAndPath(MowziesMobs.MODID, "dart").toString()));
    private static EntityType.Builder<EntityPoisonBall> poisonBallBuilder() {
        return EntityType.Builder.of(EntityPoisonBall::new, MobCategory.MISC);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntityPoisonBall>> POISON_BALL = REG.register("poison_ball", () -> poisonBallBuilder().sized(0.5F, 0.5F).setUpdateInterval(20).build(ResourceLocation.fromNamespaceAndPath(MowziesMobs.MODID, "poison_ball").toString()));
    private static EntityType.Builder<EntitySuperNova> superNovaBuilder() {
        return EntityType.Builder.of(EntitySuperNova::new, MobCategory.MISC);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntitySuperNova>> SUPER_NOVA = REG.register("super_nova", () -> superNovaBuilder().sized(1, 1).setUpdateInterval(Integer.MAX_VALUE).build(ResourceLocation.fromNamespaceAndPath(MowziesMobs.MODID, "super_nova").toString()));
    private static EntityType.Builder<EntityFallingBlock> fallingBlockBuilder() {
        return EntityType.Builder.of(EntityFallingBlock::new, MobCategory.MISC);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntityFallingBlock>> FALLING_BLOCK = REG.register("falling_block", () -> fallingBlockBuilder().sized(1, 1).build(ResourceLocation.fromNamespaceAndPath(MowziesMobs.MODID, "falling_block").toString()));
    private static EntityType.Builder<EntityBlockSwapper> blockSwapperBuilder() {
        return EntityType.Builder.of(EntityBlockSwapper::new, MobCategory.MISC);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntityBlockSwapper>> BLOCK_SWAPPER = REG.register("block_swapper", () -> blockSwapperBuilder().noSummon().sized(1, 1).setUpdateInterval(Integer.MAX_VALUE).build(ResourceLocation.fromNamespaceAndPath(MowziesMobs.MODID, "block_swapper").toString()));
    private static EntityType.Builder<EntityBlockSwapper.EntityBlockSwapperTunneling> blockSwapperTunnelingBuilder() {
        return EntityType.Builder.of(EntityBlockSwapper.EntityBlockSwapperTunneling::new, MobCategory.MISC);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntityBlockSwapper.EntityBlockSwapperTunneling>> BLOCK_SWAPPER_TUNNELING = REG.register("block_swapper_tunneling", () -> blockSwapperTunnelingBuilder().noSummon().sized(1, 1).setUpdateInterval(Integer.MAX_VALUE).build(ResourceLocation.fromNamespaceAndPath(MowziesMobs.MODID, "block_swapper_tunneling").toString()));
    private static EntityType.Builder<EntityCameraShake> cameraShakeBuilder() {
        return EntityType.Builder.of(EntityCameraShake::new, MobCategory.MISC);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntityCameraShake>> CAMERA_SHAKE = REG.register("camera_shake", () -> cameraShakeBuilder().sized(1, 1).setUpdateInterval(Integer.MAX_VALUE).build(ResourceLocation.fromNamespaceAndPath(MowziesMobs.MODID, "camera_shake").toString()));
//    private static EntityType.Builder<TestEntity> testEntityBuilder() {
//        return EntityType.Builder.of(TestEntity::new, MobCategory.MISC);
//    }
//    public static final DeferredHolder<EntityType<TestEntity>> TEST_ENTITY = REG.register("test_entity", () -> testEntityBuilder().sized(1f, 2f).setUpdateInterval(Integer.MAX_VALUE).build(ResourceLocation.fromNamespaceAndPath(MowziesMobs.MODID, "test_entity").toString()));

    @SubscribeEvent
    public static void onCreateAttributes(EntityAttributeCreationEvent event) {
        event.put(EntityHandler.FOLIAATH.get(), EntityFoliaath.createAttributes().build());
        event.put(EntityHandler.BABY_FOLIAATH.get(), EntityBabyFoliaath.createAttributes().build());
        event.put(EntityHandler.WROUGHTNAUT.get(), EntityWroughtnaut.createAttributes().build());
        event.put(EntityHandler.UMVUTHANA_RAPTOR.get(), EntityUmvuthanaRaptor.createAttributes().build());
        event.put(EntityHandler.UMVUTHANA_MINION.get(), EntityUmvuthana.createAttributes().build());
        event.put(EntityHandler.UMVUTHANA_FOLLOWER_TO_PLAYER.get(), EntityUmvuthanaFollowerToPlayer.createAttributes().build());
        event.put(EntityHandler.UMVUTHANA_CRANE_TO_PLAYER.get(), EntityUmvuthanaFollowerToPlayer.createAttributes().build());
        event.put(EntityHandler.UMVUTHANA_FOLLOWER_TO_RAPTOR.get(), EntityUmvuthana.createAttributes().build());
        event.put(EntityHandler.UMVUTHANA_CRANE.get(), EntityUmvuthana.createAttributes().build());
        event.put(EntityHandler.UMVUTHI.get(), EntityUmvuthi.createAttributes().build());
        event.put(EntityHandler.FROSTMAW.get(), EntityFrostmaw.createAttributes().build());
        event.put(EntityHandler.NAGA.get(), EntityNaga.createAttributes().build());
        event.put(EntityHandler.LANTERN.get(), EntityLantern.createAttributes().build());
        event.put(EntityHandler.GROTTOL.get(), EntityGrottol.createAttributes().build());
        event.put(EntityHandler.SCULPTOR.get(), EntitySculptor.createAttributes().build());
    }
}
