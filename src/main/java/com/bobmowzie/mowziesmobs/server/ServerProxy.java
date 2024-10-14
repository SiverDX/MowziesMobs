package com.bobmowzie.mowziesmobs.server;

import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySolarBeam;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySunstrike;
import com.bobmowzie.mowziesmobs.server.entity.naga.EntityNaga;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.trade.Trade;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ServerProxy {
    private static final StreamCodec<RegistryFriendlyByteBuf, Optional<Trade>> OPTIONAL_TRADE_CODEC = new StreamCodec<>() {
        public @NotNull Optional<Trade> decode(@NotNull RegistryFriendlyByteBuf buffer) {
            boolean hasTrade = buffer.readBoolean();

            if (!hasTrade) {
                return Optional.empty();
            }

            return Optional.of(new Trade(ItemStack.OPTIONAL_STREAM_CODEC.decode(buffer), ItemStack.OPTIONAL_STREAM_CODEC.decode(buffer), buffer.readInt()));
        }

        public void encode(@NotNull RegistryFriendlyByteBuf buffer, @NotNull Optional<Trade> optional) {
            optional.ifPresentOrElse(trade -> {
                buffer.writeBoolean(true);
                ItemStack.OPTIONAL_STREAM_CODEC.encode(buffer, trade.getInput());
                ItemStack.OPTIONAL_STREAM_CODEC.encode(buffer, trade.getOutput());
                buffer.writeInt(trade.getWeight());
            }, () -> buffer.writeBoolean(false));
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
