package com.bobmowzie.mowziesmobs.server.loot;

import com.bobmowzie.mowziesmobs.MMCommon;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class LootTableHandler {
    // Mob drops
    public static final ResourceKey<LootTable> FERROUS_WROUGHTNAUT = register("entities/ferrous_wroughtnaut");
    public static final ResourceKey<LootTable> LANTERN = register("entities/lantern");
    public static final ResourceKey<LootTable> NAGA = register("entities/naga");
    public static final ResourceKey<LootTable> FOLIAATH = register("entities/foliaath");
    public static final ResourceKey<LootTable> GROTTOL = register("entities/grottol");
    public static final ResourceKey<LootTable> FROSTMAW = register("entities/frostmaw");
    public static final ResourceKey<LootTable> UMVUTHANA_FURY = register("entities/umvuthana_fury");
    public static final ResourceKey<LootTable> UMVUTHANA_MISERY = register("entities/umvuthana_misery");
    public static final ResourceKey<LootTable> UMVUTHANA_BLISS = register("entities/umvuthana_bliss");
    public static final ResourceKey<LootTable> UMVUTHANA_RAGE = register("entities/umvuthana_rage");
    public static final ResourceKey<LootTable> UMVUTHANA_FEAR = register("entities/umvuthana_fear");
    public static final ResourceKey<LootTable> UMVUTHANA_FAITH = register("entities/umvuthana_faith");
    public static final ResourceKey<LootTable> UMVUTHI = register("entities/umvuthi");
    public static final ResourceKey<LootTable> UMVUTHANA_GROVE_CHEST = register("chests/umvuthana_grove_chest");
    public static final ResourceKey<LootTable> MONASTERY_CHEST = register("chests/monastery_chest");

    public static final DeferredRegister<LootItemFunctionType<?>> LOOT_FUNCTION_TYPE_REG = DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, MMCommon.MODID);
    public static final DeferredRegister<LootItemConditionType> LOOT_CONDITION_TYPE_REG = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, MMCommon.MODID);
    // FIXME 1.21 :: is this now a datapack registry?
    public static DeferredHolder<LootItemConditionalFunction, LootFunctionGrottolDeathType> GROTTOL_DEATH_TYPE = registerFunction("grottol_death_type", new LootFunctionGrottolDeathType.FunctionSerializer());

    public static DeferredHolder<LootItemConditionType, LootItemConditionType> FROSTMAW_HAS_CRYSTAL = registerCondition("has_crystal", new LootConditionFrostmawHasCrystal.ConditionSerializer());

    private static ResourceKey<LootTable> register(String id) {
        return ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, id));
    }

    private static DeferredHolder<LootItemFunctionType<?>, LootItemFunctionType<?>> registerFunction(String name, Serializer<? extends LootItemFunction> serializer) {
        return LOOT_FUNCTION_TYPE_REG.register(name, () -> new LootItemFunctionType(serializer));
    }

    private static DeferredHolder<LootItemConditionType, LootItemConditionType> registerCondition(String registryName, Serializer<? extends LootItemCondition> serializer) {
        return LOOT_CONDITION_TYPE_REG.register(registryName, () -> new LootItemConditionType(serializer));
    }
}
