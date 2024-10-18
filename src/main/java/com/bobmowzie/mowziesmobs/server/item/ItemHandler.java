package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.MaskType;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.core.dispenser.ProjectileDispenseBehavior;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.DispenserBlock;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItemHandler {
    // Default attribute values
    private static final int NEGATE_ATTACK_DAMAGE = -1; // 1 as base set for players
    private static final int NEGATE_ATTACK_SPEED = -4; // 4 as base from the attribute

    public static Style TOOLTIP_STYLE = Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.GRAY));

    public static final DeferredRegister<Item> REG = DeferredRegister.create(Registries.ITEM, MMCommon.MODID);

    public static final DeferredHolder<Item, ItemFoliaathSeed> FOLIAATH_SEED = REG.register("foliaath_seed", () -> new ItemFoliaathSeed(new Item.Properties()));
    public static final DeferredHolder<Item, ItemMobRemover> MOB_REMOVER = REG.register("mob_remover", () -> new ItemMobRemover(new Item.Properties()));
    public static final DeferredHolder<Item, ItemWroughtAxe> WROUGHT_AXE = REG.register("wrought_axe", () -> new ItemWroughtAxe(new Item.Properties()
            .rarity(Rarity.UNCOMMON)
            .attributes(AxeItem.createAttributes(Tiers.IRON, NEGATE_ATTACK_DAMAGE + ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolConfig.attackDamageValue, NEGATE_ATTACK_SPEED + ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolConfig.attackSpeedValue))
    ));
    public static final DeferredHolder<Item, ItemWroughtHelm> WROUGHT_HELMET = REG.register("wrought_helmet", () -> new ItemWroughtHelm(() -> {
        Item.Properties properties = new Item.Properties().rarity(Rarity.UNCOMMON);

        if (ConfigHandler.COMMON.TOOLS_AND_ABILITIES.WROUGHT_HELM.breakable.get()) {
            properties.durability(/* Iron */ ArmorItem.Type.HELMET.getDurability(15));
        } else {
            properties.stacksTo(1);
        }

        return properties;
    }));
    public static final DeferredHolder<Item, ItemUmvuthanaMask> UMVUTHANA_MASK_FURY = REG.register("umvuthana_mask_fury", () -> new ItemUmvuthanaMask(MaskType.FURY, new Item.Properties()));
    public static final DeferredHolder<Item, ItemUmvuthanaMask> UMVUTHANA_MASK_FEAR = REG.register("umvuthana_mask_fear", () -> new ItemUmvuthanaMask(MaskType.FEAR, new Item.Properties()));
    public static final DeferredHolder<Item, ItemUmvuthanaMask> UMVUTHANA_MASK_RAGE = REG.register("umvuthana_mask_rage", () -> new ItemUmvuthanaMask(MaskType.RAGE, new Item.Properties()));
    public static final DeferredHolder<Item, ItemUmvuthanaMask> UMVUTHANA_MASK_BLISS = REG.register("umvuthana_mask_bliss", () -> new ItemUmvuthanaMask(MaskType.BLISS, new Item.Properties()));
    public static final DeferredHolder<Item, ItemUmvuthanaMask> UMVUTHANA_MASK_MISERY = REG.register("umvuthana_mask_misery", () -> new ItemUmvuthanaMask(MaskType.MISERY, new Item.Properties()));
    public static final DeferredHolder<Item, ItemUmvuthanaMask> UMVUTHANA_MASK_FAITH = REG.register("umvuthana_mask_faith", () -> new ItemUmvuthanaMask(MaskType.FAITH, new Item.Properties()));
    public static final DeferredHolder<Item, ItemSolVisage> SOL_VISAGE = REG.register("sol_visage", () -> new ItemSolVisage(() -> {
        Item.Properties properties = new Item.Properties().rarity(Rarity.RARE);

        if (ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SOL_VISAGE.breakable.get()) {
            properties.durability(/* Golden */ ArmorItem.Type.HELMET.getDurability(7));
        } else {
            properties.stacksTo(1);
        }

        return properties;
    }));
    public static final DeferredHolder<Item, ItemDart> DART = REG.register("dart", () -> new ItemDart(new Item.Properties()));
    public static final DeferredHolder<Item, ItemSpear> SPEAR = REG.register("spear", () -> new ItemSpear(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, ItemBlowgun> BLOWGUN = REG.register("blowgun", () -> new ItemBlowgun(new Item.Properties().stacksTo(1).durability(300)));
    public static final DeferredHolder<Item, ItemGrantSunsBlessing> GRANT_SUNS_BLESSING = REG.register("grant_suns_blessing", () -> new ItemGrantSunsBlessing(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
    public static final DeferredHolder<Item, ItemIceCrystal> ICE_CRYSTAL = REG.register("ice_crystal", () -> new ItemIceCrystal(new Item.Properties().durability(ConfigHandler.COMMON.TOOLS_AND_ABILITIES.ICE_CRYSTAL.durabilityValue).rarity(Rarity.RARE)));
    public static final DeferredHolder<Item, ItemCapturedGrottol> CAPTURED_GROTTOL = REG.register("captured_grottol", () -> new ItemCapturedGrottol(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, ItemGlowingJelly> GLOWING_JELLY = REG.register("glowing_jelly", () -> new ItemGlowingJelly(new Item.Properties().food(ItemGlowingJelly.GLOWING_JELLY_FOOD)));
    public static final DeferredHolder<Item, ItemNagaFang> NAGA_FANG = REG.register("naga_fang", () -> new ItemNagaFang(new Item.Properties()));
    public static final DeferredHolder<Item, ItemNagaFangDagger> NAGA_FANG_DAGGER = REG.register("naga_fang_dagger", () -> new ItemNagaFangDagger(new Item.Properties()));
    public static final DeferredHolder<Item, ItemEarthrendGauntlet> EARTHREND_GAUNTLET = REG.register("earthrend_gauntlet", () -> new ItemEarthrendGauntlet(new Item.Properties().rarity(Rarity.RARE)));
    public static final DeferredHolder<Item, ItemSculptorStaff> SCULPTOR_STAFF = REG.register("sculptor_staff", () -> new ItemSculptorStaff(new Item.Properties().durability(1000).rarity(Rarity.RARE)));
    public static final DeferredHolder<Item, ItemSandRake> SAND_RAKE = REG.register("sand_rake", () -> new ItemSandRake(new Item.Properties().durability(64)));
    public static final DeferredHolder<Item, ArmorItem> GEOMANCER_BEADS = REG.register("geomancer_beads", () -> new ItemGeomancerArmor(ArmorItem.Type.HELMET, new Item.Properties().rarity(Rarity.UNCOMMON).durability(/* Diamond */ ArmorItem.Type.HELMET.getDurability(33))));
    public static final DeferredHolder<Item, ArmorItem> GEOMANCER_ROBE = REG.register("geomancer_robe", () -> new ItemGeomancerArmor(ArmorItem.Type.CHESTPLATE, new Item.Properties().rarity(Rarity.UNCOMMON).durability(/* Diamond */ ArmorItem.Type.CHESTPLATE.getDurability(33))));
    public static final DeferredHolder<Item, ArmorItem> GEOMANCER_BELT = REG.register("geomancer_belt", () -> new ItemGeomancerArmor(ArmorItem.Type.LEGGINGS, new Item.Properties().rarity(Rarity.UNCOMMON).durability(/* Diamond */ ArmorItem.Type.LEGGINGS.getDurability(33))));
    public static final DeferredHolder<Item, ArmorItem> GEOMANCER_SANDALS = REG.register("geomancer_sandals", () -> new ItemGeomancerArmor(ArmorItem.Type.BOOTS, new Item.Properties().rarity(Rarity.UNCOMMON).durability(/* Diamond */ ArmorItem.Type.BOOTS.getDurability(33))));

    public static final DeferredHolder<Item, Item> LOGO = REG.register("logo", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> PETIOLE_MUSIC_DISC = REG.register("music_disc_petiole", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(MMSounds.JUKEBOX_PETIOLE)));

    public static final DeferredHolder<Item, DeferredSpawnEggItem> FOLIAATH_SPAWN_EGG = REG.register("foliaath_spawn_egg", () -> new DeferredSpawnEggItem(EntityHandler.FOLIAATH, 0x47CC3B, 0xC03BCC, new Item.Properties()));
    public static final DeferredHolder<Item, DeferredSpawnEggItem> WROUGHTNAUT_SPAWN_EGG = REG.register("wroughtnaut_spawn_egg", () -> new DeferredSpawnEggItem(EntityHandler.WROUGHTNAUT, 0x8C8C8C, 0xFFFFFF, new Item.Properties()));
    public static final DeferredHolder<Item, DeferredSpawnEggItem> UMVUTHANA_SPAWN_EGG = REG.register("umvuthana_spawn_egg", () -> new DeferredSpawnEggItem(EntityHandler.UMVUTHANA_MINION, 0xba5f1e, 0x3a2f2f, new Item.Properties()));
    public static final DeferredHolder<Item, DeferredSpawnEggItem> UMVUTHANA_RAPTOR_SPAWN_EGG = REG.register("umvuthana_raptor_spawn_egg", () -> new DeferredSpawnEggItem(EntityHandler.UMVUTHANA_RAPTOR, 0xba5f1e, 0xf6f2f1, new Item.Properties()));
    public static final DeferredHolder<Item, DeferredSpawnEggItem> UMVUTHANA_CRANE_SPAWN_EGG = REG.register("umvuthana_crane_spawn_egg", () -> new DeferredSpawnEggItem(EntityHandler.UMVUTHANA_CRANE, 0xba5f1e, 0xfddc76, new Item.Properties()));
    public static final DeferredHolder<Item, DeferredSpawnEggItem> UMVUTHI_SPAWN_EGG = REG.register("umvuthi_spawn_egg", () -> new DeferredSpawnEggItem(EntityHandler.UMVUTHI, 0xf6f2f1, 0xba5f1e, new Item.Properties()));
    public static final DeferredHolder<Item, DeferredSpawnEggItem> FROSTMAW_SPAWN_EGG = REG.register("frostmaw_spawn_egg", () -> new DeferredSpawnEggItem(EntityHandler.FROSTMAW, 0xf7faff, 0xafcdff, new Item.Properties()));
    public static final DeferredHolder<Item, DeferredSpawnEggItem> GROTTOL_SPAWN_EGG = REG.register("grottol_spawn_egg", () -> new DeferredSpawnEggItem(EntityHandler.GROTTOL, 0x777777, 0xbce0ff, new Item.Properties()));
    public static final DeferredHolder<Item, DeferredSpawnEggItem> LANTERN_SPAWN_EGG = REG.register("lantern_spawn_egg", () -> new DeferredSpawnEggItem(EntityHandler.LANTERN, 0x6dea00, 0x235a10, new Item.Properties()));
    public static final DeferredHolder<Item, DeferredSpawnEggItem> NAGA_SPAWN_EGG = REG.register("naga_spawn_egg", () -> new DeferredSpawnEggItem(EntityHandler.NAGA, 0x154850, 0x8dd759, new Item.Properties()));
    public static final DeferredHolder<Item, DeferredSpawnEggItem> SCULPTOR_SPAWN_EGG = REG.register("sculptor_spawn_egg", () -> new DeferredSpawnEggItem(EntityHandler.SCULPTOR, 0xc4a137, 0xfff5e7, new Item.Properties()));

    public static void initializeDispenserBehaviors() {
        DispenserBlock.registerBehavior(DART.get(), new ProjectileDispenseBehavior(DART.get()));
    }
}