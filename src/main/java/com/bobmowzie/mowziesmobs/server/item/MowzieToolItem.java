package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.google.common.collect.ImmutableMultimap;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.Block;

public abstract class MowzieToolItem extends DiggerItem {
    public MowzieToolItem(float attackDamageIn, float attackSpeedIn, Tier tier, TagKey<Block> effectiveBlocksIn, Properties builderIn) {
        super(attackDamageIn, attackSpeedIn, tier, effectiveBlocksIn, builderIn);
    }

    public void getAttributesFromConfig() {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE.value(), new AttributeModifier(BASE_ATTACK_DAMAGE_ID, getConfig().attackDamage.get() - 1.0, AttributeModifier.Operation.ADD_VALUE));
        builder.put(Attributes.ATTACK_SPEED.value(), new AttributeModifier(BASE_ATTACK_SPEED_ID, getConfig().attackSpeed.get() - 4.0, AttributeModifier.Operation.ADD_VALUE));
        this.defaultModifiers = builder.build();
    }

    @Override
    public float getAttackDamage() {
        return getConfig().attackDamage.get().floatValue();
    }

    public abstract ConfigHandler.ToolConfig getConfig();
}
