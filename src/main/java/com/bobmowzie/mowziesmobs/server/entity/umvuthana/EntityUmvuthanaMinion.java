package com.bobmowzie.mowziesmobs.server.entity.umvuthana;

import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.server.ServerProxy;
import com.bobmowzie.mowziesmobs.server.ai.EntityAIUmvuthanaTrade;
import com.bobmowzie.mowziesmobs.server.ai.EntityAIUmvuthanaTradeLook;
import com.bobmowzie.mowziesmobs.server.ai.UmvuthanaHurtByTargetAI;
import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import com.bobmowzie.mowziesmobs.server.entity.LeaderSunstrikeImmune;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.trade.Trade;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.trade.TradeStore;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerUmvuthanaTrade;
import com.bobmowzie.mowziesmobs.server.item.UmvuthanaMask;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.world.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class EntityUmvuthanaMinion extends EntityUmvuthana implements LeaderSunstrikeImmune, Enemy {
    private static final TradeStore DEFAULT = new TradeStore.Builder()
        .addTrade(Items.GOLD_NUGGET, 4, BlockHandler.CLAWED_LOG.get().asItem(), 1, 9)
        .addTrade(Items.GOLD_NUGGET, 7, BlockHandler.CLAWED_LOG.get().asItem(), 2, 9)
        .addTrade(Items.GOLD_NUGGET, 5, Items.COOKED_CHICKEN, 2, 2)
        .addTrade(Items.GOLD_NUGGET, 4, Items.COOKED_CHICKEN, 1, 2)
        .addTrade(Items.GOLD_NUGGET, 7, Items.COOKED_PORKCHOP, 2, 2)
        .addTrade(Items.GOLD_NUGGET, 4, Items.COOKED_PORKCHOP, 1, 2)
        .addTrade(Items.GOLD_NUGGET, 1, Items.FEATHER, 4, 2)
        .addTrade(Items.GOLD_NUGGET, 1, Items.STICK, 12, 2)
        .addTrade(Items.GOLD_NUGGET, 3, Items.CAMPFIRE, 1, 2)

        .addTrade(Items.MELON_SLICE, 3, Items.GOLD_NUGGET, 5, 2)
        .addTrade(Items.CHICKEN, 1, Items.GOLD_NUGGET, 3, 2)
        .addTrade(Items.CHICKEN, 1, Items.GOLD_NUGGET, 4, 2)
        .addTrade(Items.PORKCHOP, 1, Items.GOLD_NUGGET, 6, 2)
        .addTrade(Items.BEETROOT, 3, Items.GOLD_NUGGET, 6, 2)
        .addTrade(Items.SALMON, 2, Items.GOLD_NUGGET, 8, 1)
        .addTrade(Items.COD, 2, Items.GOLD_NUGGET, 7, 1)
        .addTrade(Items.FLINT, 2, Items.FEATHER, 5, 2)
        .addTrade(Items.FEATHER, 5, Items.FLINT, 2, 2)
        .addTrade(Items.ACACIA_SAPLING, 2, Items.GOLD_NUGGET, 4, 2)
        .addTrade(Items.BONE, 3, Items.GOLD_NUGGET, 4, 1)
        .addTrade(Items.BONE, 2, Items.GOLD_NUGGET, 2, 1)
        .build();

    private static final EntityDataAccessor<Optional<Trade>> TRADE = SynchedEntityData.defineId(EntityUmvuthanaMinion.class, ServerProxy.OPTIONAL_TRADE);
    //    private static final DataParameter<Integer> NUM_SALES = EntityDataManager.createKey(EntityBarakoaya.class, DataSerializers.VARINT);
    private static final EntityDataAccessor<Optional<UUID>> MISBEHAVED_PLAYER = SynchedEntityData.defineId(EntityUmvuthanaMinion.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Boolean> IS_TRADING = SynchedEntityData.defineId(EntityUmvuthanaMinion.class, EntityDataSerializers.BOOLEAN);

    //TODO: Sale limits. After X sales, go out of stock and change trade.

    private static final int MIN_OFFER_TIME = 5 * 60 * 20;

    private static final int MAX_OFFER_TIME = 20 * 60 * 20;

    private TradeStore tradeStore = TradeStore.EMPTY;

    private int timeOffering;

//    private static final int SOLD_OUT_TIME = 5 * 60 * 20;
//    private static final int MAX_SALES = 5;

    private Player customer;

    public EntityUmvuthanaMinion(EntityType<? extends EntityUmvuthanaMinion> type, Level world) {
        super(type, world);
        setWeapon(0);
//        setNumSales(MAX_SALES);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(1, new EntityAIUmvuthanaTrade(this));
        goalSelector.addGoal(1, new EntityAIUmvuthanaTradeLook(this));
        this.goalSelector.addGoal(7, new MoveTowardsRestrictionGoal(this, 0.4));
    }

    @Override
    protected void registerTargetGoals() {
        targetSelector.addGoal(3, new UmvuthanaHurtByTargetAI(this, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<Player>(this, Player.class, 0, true, true, target -> {
            if (target instanceof Player) {
                if (this.level().getDifficulty() == Difficulty.PEACEFUL) return false;
                ItemStack headArmorStack = ((Player) target).getInventory().armor.get(3);
                return !(headArmorStack.getItem() instanceof UmvuthanaMask) || target == getMisbehavedPlayer();
            }
            return true;
        }){
            @Override
            public void stop() {
                super.stop();
                setMisbehavedPlayerId(null);
            }
        });
        targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Zombie.class, 0, true, true, null));
        targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, AbstractSkeleton.class, 0, true, false, null));
    }

    @Override
    protected void defineSynchedData(@NotNull SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(TRADE, Optional.empty());
        builder.define(MISBEHAVED_PLAYER, Optional.empty());
        builder.define(IS_TRADING, false);
//        getDataManager().register(NUM_SALES, MAX_SALES);
    }

    public void setOfferingTrade(Trade trade) {
        getEntityData().set(TRADE, Optional.ofNullable(trade));
    }

    public Trade getOfferingTrade() {
        return getEntityData().get(TRADE).orElse(null);
    }

    //    public int getNumSales() {
//        return getDataManager().get(NUM_SALES);
//    }
//
//    public void setNumSales(int numSales) {
//        getDataManager().set(NUM_SALES, numSales);
//    }

    public boolean isOfferingTrade() {
        if (getEntityData().get(TRADE) instanceof Optional) {
            return getEntityData().get(TRADE).isPresent();
        }
        else return false;
    }

    public void setCustomer(Player customer) {
        setTrading(customer != null);
        this.customer = customer;
    }

    public Player getCustomer() {
        return customer;
    }

    public void setTrading(boolean trading) {
        entityData.set(IS_TRADING, trading);
    }

    public boolean isTrading() {
        return entityData.get(IS_TRADING);
    }

    protected boolean canHoldVaryingWeapons() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (getTarget() instanceof Player) {
            if (((Player) getTarget()).isCreative() || getTarget().isSpectator()) setTarget(null);
        }
        if ((!isOfferingTrade() || timeOffering <= 0) && tradeStore.hasStock()) {
            setOfferingTrade(tradeStore.get(random));
            timeOffering = random.nextInt(MAX_OFFER_TIME - MIN_OFFER_TIME + 1) + MIN_OFFER_TIME;
        }
    }

    public void openGUI(Player playerEntity) {
        setCustomer(playerEntity);
        MMCommon.PROXY.setReferencedMob(this);
        if (!this.level().isClientSide && getTarget() == null && isAlive()) {
            playerEntity.openMenu(new MenuProvider() {
                @Override
                public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
                    return new ContainerUmvuthanaTrade(id, EntityUmvuthanaMinion.this, playerInventory);
                }

                @Override
                public Component getDisplayName() {
                    return EntityUmvuthanaMinion.this.getDisplayName();
                }
            });
        }
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (canTradeWith(player) && getTarget() == null && isAlive()) {
            openGUI(player);
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        return InteractionResult.PASS;
    }

    public boolean canTradeWith(Player player) {
        if (isTrading()) {
            return false;
        }
        ItemStack headStack = player.getInventory().armor.get(3);
        return headStack.getItem() instanceof UmvuthanaMask && isOfferingTrade();
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingData, @Nullable CompoundTag compound) {
        tradeStore = DEFAULT;
        if (reason == MobSpawnType.COMMAND) restrictTo(blockPosition(), 25);
        return super.finalizeSpawn(world, difficulty, reason, livingData, compound);
    }

    @Override
    public boolean requiresCustomPersistence() {
        return true;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.put("tradeStore", tradeStore.serialize());
        if (isOfferingTrade()) {
            compound.put("offeringTrade", getOfferingTrade().serialize());
        }
        compound.putInt("timeOffering", timeOffering);
        compound.putInt("HomePosX", this.getRestrictCenter().getX());
        compound.putInt("HomePosY", this.getRestrictCenter().getY());
        compound.putInt("HomePosZ", this.getRestrictCenter().getZ());
        compound.putInt("HomeDist", (int) this.getRestrictRadius());
        if (this.getMisbehavedPlayerId() != null) {
            compound.putUUID("MisbehavedPlayer", this.getMisbehavedPlayerId());
        }
//        compound.setInteger("numSales", getNumSales());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        tradeStore = TradeStore.deserialize(compound.getCompound("tradeStore"));
        setOfferingTrade(Trade.deserialize(compound.getCompound("offeringTrade")));
        timeOffering = compound.getInt("timeOffering");
        int i = compound.getInt("HomePosX");
        int j = compound.getInt("HomePosY");
        int k = compound.getInt("HomePosZ");
        int dist = compound.getInt("HomeDist");
        this.restrictTo(new BlockPos(i, j, k), dist);
        UUID uuid;
        if (compound.hasUUID("MisbehavedPlayer")) {
            uuid = compound.getUUID("MisbehavedPlayer");
        } else {
            String s = compound.getString("MisbehavedPlayer");
            uuid = OldUsersConverter.convertMobOwnerIfNecessary(this.getServer(), s);
        }

        if (uuid != null) {
            try {
                this.setMisbehavedPlayerId(uuid);
            } catch (Throwable ignored) {

            }
        }
//        setNumSales(compound.getInteger("numSales"));
    }

    @Nullable
    public UUID getMisbehavedPlayerId() {
        return this.entityData.get(MISBEHAVED_PLAYER).orElse((UUID)null);
    }

    public void setMisbehavedPlayerId(@Nullable UUID p_184754_1_) {
        this.entityData.set(MISBEHAVED_PLAYER, Optional.ofNullable(p_184754_1_));
    }

    @Nullable
    public LivingEntity getMisbehavedPlayer() {
        try {
            UUID uuid = this.getMisbehavedPlayerId();
            return uuid == null ? null : this.level().getPlayerByUUID(uuid);
        } catch (IllegalArgumentException illegalargumentexception) {
            return null;
        }
    }
}
