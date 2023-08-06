package io.github.poisonsheep.thearbiter.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


public class ShelterParticles extends SimpleAnimatedParticle {
    private final SpriteSet sprites;
    private Player player;

    private double ox;
    private double oy;
    private double oz;

    private  double deltY ;
    private final double speed = 0.01;
    private ShelterParticles(ClientLevel world, Player player, double x, double y, double z, double x0, double y0, double z0, SpriteSet sprites) {
        super(world, x, y, z, sprites, 0.0F);
        this.player = player;
        this.x = x;
        this.y = y;
        this.z = z;
        this.deltY = y-y0;
        this.sprites = sprites;
        this.quadSize = 0.03F + (float)Math.sin((double)this.age / 60.0 * Math.PI + (float)Math.random() * 2.0F) * 0.02F + (float)Math.random() * 0.01F - 0.006F;
        this.lifetime = 200 + this.random.nextInt(15);
        this.gravity = 0.0F;
        int color = 16777215;
        float lvt_18_1_ = (float) (color >> 16 &  255) /255.0F;
        float lvt_19_1_ = (float) (color >> 8 &  255) /255.0F;
        float lvt_20_1_ = (float) (color &  255) /255.0F;
        setColor(lvt_18_1_, lvt_19_1_, lvt_20_1_);
        this.setSpriteFromAge(sprites);
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
        ox = x0;
        oy = y0;
        oz = z0;
    }
    public int getLightColor(float p_189214_1_) {
        int lvt_2_1_ = super.getLightColor(p_189214_1_);
        int lvt_4_1_ = lvt_2_1_ >> 16 &  255;
        return 240 | lvt_4_1_ << 16;
    }
    public void tick() {
        super.tick();
        double xT = x - ox;
        double zT = z - oz;
        double tM = Math.sqrt(xT*xT+zT*zT);
        if(Math.abs(tM)<0.001){
            tM = 1;
        }
        double vX = -zT/tM * speed;
        double vZ = xT/tM * speed;
        this.xd = vX;
        this.yd = 0;
        this.zd = vZ;
        this.setPos(player.getX()+xT,player.getY()+deltY,player.getZ()+zT);

        float subAlpha = 1F;
        if(this.age > 5){
            subAlpha = 1 - (float)(this.age - 5) / (this.getLifetime() - 5);
        }
        this.alpha = subAlpha;
        this.setSpriteFromAge(this.sprites);
        this.quadSize = 0.06F + (float)Math.sin((double)this.age / 60.0 * Math.PI + (float)Math.random() * 2.0F) * 0.03F + (float)Math.random() * 0.015F - 0.0075F;
        ox = player.getX();
        oy = player.getY();
        oz = player.getZ();
    }
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double x0, double y0, double z0) {
            Player player = worldIn.getNearestPlayer(TargetingConditions.forNonCombat(), x0, y0, z0);
            ShelterParticles p = new ShelterParticles(worldIn, player, x, y, z, x0, y0, z0, spriteSet);
            return p;
        }
    }
}