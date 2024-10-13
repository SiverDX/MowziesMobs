package com.bobmowzie.mowziesmobs.client.particle;

import com.bobmowzie.mowziesmobs.client.model.tools.MathUtils;
import com.bobmowzie.mowziesmobs.client.render.MMRenderType;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.math.Axis;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Locale;

/**
 * Created by BobMowzie on 6/2/2017.
 */
public class ParticleRing extends TextureSheetParticle {
    public float r, g, b;
    public float opacity;
    public boolean facesCamera;
    public float yaw, pitch;
    public float size;

    private final EnumRingBehavior behavior;

    public enum EnumRingBehavior {
        SHRINK,
        GROW,
        CONSTANT,
        GROW_THEN_SHRINK
    }

    public ParticleRing(ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ, float yaw, float pitch, int duration, float r, float g, float b, float opacity, float size, boolean facesCamera, EnumRingBehavior behavior) {
        super(world, x, y, z);
        setSize(1, 1);
        this.size = size * 0.1f;
        lifetime = duration;
        alpha = 1;
        this.r = r;
        this.g = g;
        this.b = b;
        this.opacity = opacity;
        this.yaw = yaw;
        this.pitch = pitch;
        this.facesCamera = facesCamera;
        this.xd = motionX;
        this.yd = motionY;
        this.zd = motionZ;
        this.behavior = behavior;
    }

    @Override
    public int getLightColor(float delta) {
        return 240 | super.getLightColor(delta) & 0xFF0000;
    }

    @Override
    public void tick() {
        super.tick();
        if (age >= lifetime) {
            remove();
        }
        age++;
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        float var = (age + partialTicks)/lifetime;
        if (behavior == EnumRingBehavior.GROW) {
            quadSize = size * var;
        }
        else if (behavior == EnumRingBehavior.SHRINK) {
            quadSize = size * (1 - var);
        }
        else if (behavior == EnumRingBehavior.GROW_THEN_SHRINK) {
            quadSize = (float) (size * (1 - var - Math.pow(2000, -var)));
        }
        else {
            quadSize = size;
        }
        alpha = opacity * 0.95f * (1 - (age + partialTicks)/lifetime) + 0.05f;
        rCol = r;
        gCol = g;
        bCol = b;

        Vec3 Vector3d = renderInfo.getPosition();
        float f = (float)(Mth.lerp(partialTicks, this.xo, this.x) - Vector3d.x());
        float f1 = (float)(Mth.lerp(partialTicks, this.yo, this.y) - Vector3d.y());
        float f2 = (float)(Mth.lerp(partialTicks, this.zo, this.z) - Vector3d.z());
        Quaternionf quaternionf = new Quaternionf(0.0F, 0.0F, 0.0F, 1.0F);
        if (facesCamera) {
            if (this.roll == 0.0F) {
                quaternionf = renderInfo.rotation();
            } else {
                quaternionf = new Quaternionf(renderInfo.rotation());
                float f3 = Mth.lerp(partialTicks, this.oRoll, this.roll);
                quaternionf.mul(Axis.ZP.rotation(f3));
            }
        }
        else {
            Quaternionf quatX = MathUtils.quatFromRotationXYZ(pitch, 0, 0, false);
            Quaternionf quatY = MathUtils.quatFromRotationXYZ(0, yaw, 0, false);
            quaternionf.mul(quatY);
            quaternionf.mul(quatX);
        }

        Vector3f vector3f1 = new Vector3f(-1.0F, -1.0F, 0.0F);
        quaternionf.transform(vector3f1);
        Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        float f4 = this.getQuadSize(partialTicks);

        for(int i = 0; i < 4; ++i) {
            Vector3f vector3f = avector3f[i];
            quaternionf.transform(vector3f);
            vector3f.mul(f4);
            vector3f.add(f, f1, f2);
        }

        float f7 = this.getU0();
        float f8 = this.getU1();
        float f5 = this.getV0();
        float f6 = this.getV1();
        int j = this.getLightColor(partialTicks);
        buffer.vertex(avector3f[0].x(), avector3f[0].y(), avector3f[0].z()).uv(f8, f6).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        buffer.vertex(avector3f[1].x(), avector3f[1].y(), avector3f[1].z()).uv(f8, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        buffer.vertex(avector3f[2].x(), avector3f[2].y(), avector3f[2].z()).uv(f7, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        buffer.vertex(avector3f[3].x(), avector3f[3].y(), avector3f[3].z()).uv(f7, f6).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return MMRenderType.PARTICLE_SHEET_TRANSLUCENT_NO_DEPTH;
    }

    public static final class RingFactory implements ParticleProvider<RingData> {
        private final SpriteSet spriteSet;

        public RingFactory(SpriteSet sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle createParticle(RingData typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            ParticleRing particle = new ParticleRing(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, typeIn.getYaw(), typeIn.getPitch(), typeIn.getDuration(), typeIn.getR(), typeIn.getG(), typeIn.getB(), typeIn.getA(), typeIn.getScale(), typeIn.getFacesCamera(), typeIn.getBehavior());
            particle.setSpriteFromAge(spriteSet);
            return particle;
        }
    }

    public static class RingData implements ParticleOptions {
        public static final ParticleOptions.Deserializer<ParticleRing.RingData> DESERIALIZER = new ParticleOptions.Deserializer<ParticleRing.RingData>() {
            public ParticleRing.RingData fromCommand(ParticleType<ParticleRing.RingData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
                reader.expect(' ');
                float yaw = (float) reader.readDouble();
                reader.expect(' ');
                float pitch = (float) reader.readDouble();
                reader.expect(' ');
                float r = (float) reader.readDouble();
                reader.expect(' ');
                float g = (float) reader.readDouble();
                reader.expect(' ');
                float b = (float) reader.readDouble();
                reader.expect(' ');
                float a = (float) reader.readDouble();
                reader.expect(' ');
                float scale = (float) reader.readDouble();
                reader.expect(' ');
                int duration = reader.readInt();
                reader.expect(' ');
                boolean facesCamera = reader.readBoolean();
                return new ParticleRing.RingData(yaw, pitch, duration, r, g, b, a, scale, facesCamera, EnumRingBehavior.GROW);
            }

            public ParticleRing.RingData fromNetwork(ParticleType<ParticleRing.RingData> particleTypeIn, FriendlyByteBuf buffer) {
                return new ParticleRing.RingData(buffer.readFloat(), buffer.readFloat(), buffer.readInt(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readBoolean(), EnumRingBehavior.GROW);
            }
        };

        private final float yaw;
        private final float pitch;
        private final float r;
        private final float g;
        private final float b;
        private final float a;
        private final float scale;
        private final int duration;
        private final boolean facesCamera;
        private final EnumRingBehavior behavior;

        public RingData(float yaw, float pitch, int duration, float r, float g, float b, float a, float scale, boolean facesCamera, EnumRingBehavior behavior) {
            this.yaw = yaw;
            this.pitch = pitch;
            this.r = r;
            this.g = g;
            this.b = b;
            this.a = a;
            this.scale = scale;
            this.duration = duration;
            this.facesCamera = facesCamera;
            this.behavior = behavior;
        }

        @Override
        public void writeToNetwork(FriendlyByteBuf buffer) {
            buffer.writeFloat(this.r);
            buffer.writeFloat(this.g);
            buffer.writeFloat(this.b);
            buffer.writeFloat(this.scale);
            buffer.writeInt(this.duration);
        }

        @SuppressWarnings("deprecation")
        @Override
        public String writeToString() {
            return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %.2f %.2f %.2f %d %b", BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()),
                    this.yaw, this.pitch, this.r, this.g, this.b, this.scale, this.a, this.duration, this.facesCamera);
        }

        @Override
        public ParticleType<ParticleRing.RingData> getType() {
            return ParticleHandler.RING.get();
        }

        public float getYaw() {
            return this.yaw;
        }

        public float getPitch() {
            return this.pitch;
        }

        public float getR() {
            return this.r;
        }

        public float getG() {
            return this.g;
        }

        public float getB() {
            return this.b;
        }

        public float getA() {
            return this.a;
        }

        public float getScale() {
            return this.scale;
        }

        public int getDuration() {
            return this.duration;
        }

        public boolean getFacesCamera() {
            return this.facesCamera;
        }

        public EnumRingBehavior getBehavior() {
            return this.behavior;
        }

        public static Codec<RingData> CODEC(ParticleType<RingData> particleType) {
            return RecordCodecBuilder.create((codecBuilder) -> codecBuilder.group(
                    Codec.FLOAT.fieldOf("yaw").forGetter(RingData::getYaw),
                    Codec.FLOAT.fieldOf("pitch").forGetter(RingData::getPitch),
                    Codec.FLOAT.fieldOf("r").forGetter(RingData::getR),
                    Codec.FLOAT.fieldOf("g").forGetter(RingData::getG),
                    Codec.FLOAT.fieldOf("b").forGetter(RingData::getB),
                    Codec.FLOAT.fieldOf("a").forGetter(RingData::getA),
                    Codec.FLOAT.fieldOf("scale").forGetter(RingData::getScale),
                    Codec.INT.fieldOf("duration").forGetter(RingData::getDuration),
                    Codec.BOOL.fieldOf("facesCamera").forGetter(RingData::getFacesCamera),
                    Codec.STRING.fieldOf("behavior").forGetter((ringData) -> ringData.getBehavior().toString())
                    ).apply(codecBuilder, (yaw, pitch, r, g, b, a, scale, duration, facesCamera, behavior) ->
                            new RingData(yaw, pitch, duration, r, g, b, a, scale, facesCamera, EnumRingBehavior.valueOf(behavior)))
            );
        }
    }
}
