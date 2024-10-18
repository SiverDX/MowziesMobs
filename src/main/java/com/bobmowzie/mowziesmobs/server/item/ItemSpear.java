package com.bobmowzie.mowziesmobs.server.item;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class ItemSpear extends DiggerItem {
    public ItemSpear(Item.Properties properties) {
        super(Tiers.STONE, BlockTags.MINEABLE_WITH_HOE, properties);
    }

    public static LivingEntity raytraceEntities(Level world, Player player, double range) {
        ItemSpear.SpearHitResult result = new ItemSpear.SpearHitResult();
        Vec3 pos = new Vec3(player.getX(), player.getY() + player.getEyeHeight(), player.getZ());
        Vec3 segment = player.getLookAngle();
        segment = pos.add(segment.x * range, segment.y * range, segment.z * range);
        result.setBlockHit(world.clip(new ClipContext(pos, segment, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player)));
        double collidePosX, collidePosY, collidePosZ;
        if (result.blockHit != null) {
            collidePosX = result.blockHit.getLocation().x;
            collidePosY = result.blockHit.getLocation().y;
            collidePosZ = result.blockHit.getLocation().z;
        }
        else {
            Vec3 end = player.getLookAngle().scale(range).add(pos);
            collidePosX = end.x;
            collidePosY = end.y;
            collidePosZ = end.z;
        }

        List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class, new AABB(Math.min(pos.x, collidePosX), Math.min(pos.y, collidePosY), Math.min(pos.z, collidePosZ), Math.max(pos.x, collidePosX), Math.max(pos.y, collidePosY), Math.max(pos.z, collidePosZ)).inflate(1, 1, 1));
        LivingEntity closest = null;
        for (LivingEntity entity : entities) {
            if (entity == player) {
                continue;
            }
            float pad = entity.getPickRadius();
            AABB aabb = entity.getBoundingBox().inflate(pad, pad, pad);
            boolean hit = aabb.intersects(pos, segment);
            if (aabb.contains(pos) || hit) {
                result.addEntityHit(entity);
                if (closest == null || player.distanceTo(closest) > player.distanceTo(entity)) closest = entity;
            }
        }
        return closest;
    }

    public boolean canAttackBlock(BlockState p_43291_, Level p_43292_, BlockPos p_43293_, Player p_43294_) {
        return !p_43294_.isCreative();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, context, tooltip, flagIn);
        tooltip.add(Component.translatable(getDescriptionId() + ".text.0").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(Component.translatable(getDescriptionId() + ".text.1").setStyle(ItemHandler.TOOLTIP_STYLE));
    }

    public static class SpearHitResult {
        private HitResult blockHit;

        private final List<LivingEntity> entities = new ArrayList<>();

        public HitResult getBlockHit() {
            return blockHit;
        }

        public void setBlockHit(HitResult blockHit) {
            this.blockHit = blockHit;
        }

        public void addEntityHit(LivingEntity entity) {
            entities.add(entity);
        }
    }
}
