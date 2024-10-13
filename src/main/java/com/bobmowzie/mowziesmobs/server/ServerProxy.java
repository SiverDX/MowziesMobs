package com.bobmowzie.mowziesmobs.server;

import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySolarBeam;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySunstrike;
import com.bobmowzie.mowziesmobs.server.entity.naga.EntityNaga;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.trade.Trade;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ServerProxy {
    // FIXME 1.21 :: probably needs item instead of itemstack
    private static final StreamCodec<ByteBuf, Optional<Trade>> OPTIONAL_TRADE_CODEC = new StreamCodec<>() {
        public @NotNull Optional<Trade> decode(@NotNull ByteBuf buffer) {
            Item input = Item.byId(buffer.readInt());

            if (input == Items.AIR) {
                return Optional.empty();
            }

            return Optional.of(new Trade(input.getDefaultInstance(), Item.byId(buffer.readInt()).getDefaultInstance(), buffer.readInt()));
        }

        public void encode(@NotNull ByteBuf buffer, @NotNull Optional<Trade> optional) {
            optional.ifPresentOrElse(trade -> {
                buffer.writeInt(Item.getId(trade.getInput().getItem()));
                buffer.writeInt(Item.getId(trade.getOutput().getItem()));
                buffer.writeInt(trade.getWeight());
            }, () -> buffer.writeInt(Item.getId(Items.AIR)));
        }
    };

    public static final EntityDataSerializer<Optional<Trade>> OPTIONAL_TRADE = EntityDataSerializer.forValueType(OPTIONAL_TRADE_CODEC);

    public void init() {
        EntityDataSerializers.registerSerializer(OPTIONAL_TRADE);
    }

    public void playSunstrikeSound(EntitySunstrike strike) {
    }

    public void playIceBreathSound(Entity entity) {
    }

    public void playBoulderChargeSound(LivingEntity player) {
    }

    public void playNagaSwoopSound(EntityNaga naga) {
    }

    public void playBlackPinkSound(AbstractMinecart entity) {
    }

    public void playSunblockSound(LivingEntity entity) {
    }

    public void playSolarBeamSound(EntitySolarBeam entity) {
    }

    public void minecartParticles(ClientLevel world, AbstractMinecart minecart, float scale, double x, double y,
                                  double z, BlockState state, BlockPos pos) {
    }

    public void setTPS(float tickRate) {
    }

    public Entity getReferencedMob() {
        return null;
    }

    public void setReferencedMob(Entity referencedMob) {
    }

    public void sculptorMarkBlock(int id, BlockPos pos) {
    }

    public void updateMarkedBlocks() {
    }
}
