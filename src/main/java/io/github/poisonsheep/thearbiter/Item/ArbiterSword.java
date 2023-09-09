package io.github.poisonsheep.thearbiter.Item;

import io.github.poisonsheep.thearbiter.client.particle.ParticlesRegistry;
import io.github.poisonsheep.thearbiter.client.render.item.RenderArbiterSword;
import io.github.poisonsheep.thearbiter.client.sound.SoundRegistry;
import io.github.poisonsheep.thearbiter.potion.MobEffectRegistry;
import io.github.poisonsheep.thearbiter.util.BallUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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
import net.minecraftforge.common.Tags;
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
import java.util.function.Consumer;

public class ArbiterSword extends SwordItem implements IAnimatable, ISyncable {
    public AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public String RUNE_CONTROLLER = "rune";
    public static final int No_ENCHANTED = 0;
    public BallUtil ballUtil = new BallUtil();
    private static final Tier tier = new Tier(){

       @Override
       public int getUses() {
           return 2031;
       }

       @Override
       public float getSpeed() {
           return 1.6f;
       }

       @Override
       public float getAttackDamageBonus() {
           return 8.0f;
       }

       @Override
       public int getLevel() {
           return 4;
       }

       @Override
       public int getEnchantmentValue() {
           return 20;
       }

       @Override
       public Ingredient getRepairIngredient() {
           return Ingredient.of(Tags.Items.NETHER_STARS);
       }

       @Nullable
       @Override
       public TagKey<Block> getTag() {
           return Tier.super.getTag();
       }
   };
    public ArbiterSword() {
        super(tier,6,-2.4F,new Properties().tab(CreativeModeTab.TAB_COMBAT));
        GeckoLibNetwork.registerSyncable(this);
        ballUtil.update_radius(1.5,24);
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
                    //给命中生物添加发光药水效果
                    entity.addEffect(new MobEffectInstance(MobEffects.GLOWING,200));
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
            player.getCooldowns().addCooldown(this, 800);
            //为玩家添加调停者的庇护
            player.addEffect(new MobEffectInstance(MobEffectRegistry.UNDYING.get(), 200));
            ItemStack itemstack = player.getItemInHand(hand);
            final int id = GeckoLibUtil.guaranteeIDForStack(itemstack, (ServerLevel) worldIn);
            final PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF
                    .with(() -> player);
            GeckoLibNetwork.syncAnimation(target, this, id, No_ENCHANTED);
            worldIn.playSound((Player)null, player.getX(), player.getY(), player.getZ(),SoundRegistry.ARBITER_SWORD_USE.get(), SoundSource.PLAYERS, 1f,1f);
        }
        if(worldIn.isClientSide){
            player.playSound(SoundRegistry.ZOOEY.get(),1f,1f);
            for (int i = 0; i < BallUtil.max; i++) {
                BallUtil.Position position = BallUtil.getPosition(i);
                worldIn.addParticle(ParticlesRegistry.SHELTER_PARTICLES.get(),player.getX() + position.x,player.getY() + position.y + 1.2,player.getZ() + position.z,player.getX(),player.getY(),player.getZ());
            }
        }
        return super.use(worldIn, player, hand);
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
        //单位tick
        return 20;
    }
    @Override
    public void appendHoverText(ItemStack stack, @javax.annotation.Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(new TranslatableComponent("item.arbiter_sword.description"));
    }
}

