package io.github.poisonsheep.thearbiter.Item;

import io.github.poisonsheep.thearbiter.client.particle.ParticlesRegistry;
import io.github.poisonsheep.thearbiter.client.particle.ShelterParticles;
import io.github.poisonsheep.thearbiter.client.render.item.RenderArbiterSword;
import io.github.poisonsheep.thearbiter.client.sound.SoundRegistry;
import io.github.poisonsheep.thearbiter.potion.MobEffectRegistry;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.network.GeckoLibNetwork;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class ArbiterSword extends SwordItem implements IAnimatable, ISyncable {

    public AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public String RUNE_CONTROLLER = "rune";
    public static final int No_ENCHANTED = 0;
    private int divide;
    private double radius;
    private int max;
    private double divdAngle;
    private static final Tier tier = new Tier(){

       @Override
       public int getUses() {
           return 2000;
       }

       @Override
       public float getSpeed() {
           return 0;
       }

       @Override
       public float getAttackDamageBonus() {
           return 0;
       }

       @Override
       public int getLevel() {
           return 0;
       }

       @Override
       public int getEnchantmentValue() {
           return 0;
       }

       @Override
       public Ingredient getRepairIngredient() {
           return null;
       }

       @Nullable
       @Override
       public TagKey<Block> getTag() {
           return Tier.super.getTag();
       }
   };

    public ArbiterSword() {
        super(tier,14,-2.4F,new Properties().tab(CreativeModeTab.TAB_COMBAT));
        GeckoLibNetwork.registerSyncable(this);
        update_radius(2,48);
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IItemRenderProperties() {
            private final BlockEntityWithoutLevelRenderer renderer = new RenderArbiterSword();

            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return renderer;
            }
        });
    }



    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player player, InteractionHand hand) {
       //进行使用动作
        player.startUsingItem(hand);
        if(!worldIn.isClientSide){
            BlockPos blockpos = player.blockPosition();
            List<LivingEntity> entities = worldIn.getEntitiesOfClass(LivingEntity.class,new AABB(blockpos).inflate(20));
            for (LivingEntity entity:entities){
                if(entity!=player){
                    LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(worldIn);
                    lightningbolt.moveTo(Vec3.atBottomCenterOf(entity.blockPosition()));
                    lightningbolt.setCause(player instanceof ServerPlayer ? (ServerPlayer)player : null);
                    lightningbolt.setVisualOnly(true);
                    worldIn.addFreshEntity(lightningbolt);
                }
                if(entity.getHealth() >=1 ){
                    entity.setHealth(1);
                }
            }
            //player.getCooldowns().addCooldown(this, 800);
            //为玩家添加调停者的庇护
            player.addEffect(new MobEffectInstance(MobEffectRegistry.UNDYING.get(), 200));
            ItemStack itemstack = player.getItemInHand(hand);
            final int id = GeckoLibUtil.guaranteeIDForStack(itemstack, (ServerLevel) worldIn);
            final PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF
                    .with(() -> player);
            GeckoLibNetwork.syncAnimation(target, this, id, No_ENCHANTED);
        }
        if(worldIn.isClientSide){
            player.playSound(SoundRegistry.ARBITER_SWORD_USE.get(),1f,1f);
            player.playSound(SoundRegistry.ZOOEY.get(),1f,1f);
            for (int i = 0; i < max; i++) {
                Position position = getPosition(i);
                player.level.addParticle(ParticlesRegistry.SHELTER_PARTICLES.get(),player.getX() + position.x,player.getY() + position.y + 1.2,player.getZ() + position.z,player.getX(),player.getY(),player.getZ());
            }
        }
        return super.use(worldIn, player, hand);
    }

    public class Position {
        public double x;
        public double y;
        public double z;
    }
    public void update_radius(double pR, int pDivd) {
        radius = pR;
        if (pR < 0)
        {
            radius = 1;
        }
        if (pDivd < 4)
        {
            divide = 4;
        }
        else if (pDivd % 4 != 0)
        {
            divide = pDivd + 4 - pDivd % 4;
        }
        else
        {
            divide = pDivd;
        }
        this.max = divide * (divide - 3) + 2;
        divdAngle = Math.PI/(divide/2);
    }
    public Position getPosition(int i){
        Position position = new Position();
        double tR,tL;
        int group,pos;
        if( i < 0 || i > max-1) {
            i = 0;
        }
        if(i == 0)  {   // 球顶
            position.x = 0;
            position.z = 0;
            position.y = radius;
        } else if (i == (max - 1)) { // 球底
            position.x = 0;
            position.z = 0;
            position.y = -radius;
        } else {    // 序列号除球顶和球底以外，每次切一个圆，分成divd份
            group = ((i-1) / divide) + 1;
            pos = i % divide;
            tR = radius * Math.cos(divdAngle * group);
            tL = radius * Math.sin(divdAngle * group);
            position.y = tR;
            position.x = tL * Math.cos(divdAngle * pos);
            position.z = tL * Math.sin(divdAngle * pos);
        }
        return position;
    }
    //动画部分
    public <P extends Item & IAnimatable> PlayState predicate1(AnimationEvent<P> event) {
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, RUNE_CONTROLLER, 4, this::predicate1));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void onAnimationSync(int id, int state) {
        if(state == No_ENCHANTED){
            final AnimationController<?> controller = GeckoLibUtil.getControllerForID(this.factory, id, RUNE_CONTROLLER);
            if (controller.getAnimationState() == AnimationState.Stopped) {
                controller.markNeedsReload();
                controller.setAnimation(new AnimationBuilder().addAnimation("rune", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
            }
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft){
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        //单位应该是tick
        return 20;
    }
}

