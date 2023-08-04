package io.github.poisonsheep.thearbiter.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.Random;

public class Zooey extends Monster implements IAnimatable {

    private AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public static final EntityDataAccessor<Integer> STATE = SynchedEntityData.defineId(Zooey.class, EntityDataSerializers.INT);
    protected static final AnimationBuilder WALK_ANIM = new AnimationBuilder().addAnimation("walk", ILoopType.EDefaultLoopTypes.LOOP);
    protected static final AnimationBuilder ATTACK = new AnimationBuilder().addAnimation("attack1", ILoopType.EDefaultLoopTypes.LOOP);

    protected Zooey(EntityType<? extends Zooey> worldIn, Level level) {
        super(worldIn, level);
        this.xpReward = 200;
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, 3.0)
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.FOLLOW_RANGE, 40.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2);
    }

    //该状态播放器控制生物平时的各种动作
    protected <E extends Zooey> PlayState walkAnimController(final AnimationEvent<E> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(WALK_ANIM);
            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState attackAnimController(AnimationEvent<E> event) {
        //如果生物攻击状态为1并且没有死亡，就执行这个攻击动画
        if (this.entityData.get(STATE) == 1 && !(this.dead || this.getHealth() < 0.01 || this.isDeadOrDying())) {
            event.getController().setAnimation(ATTACK);
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }
    @Override
    protected void registerGoals(){

        this.goalSelector.addGoal(8,new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(3,new LookAtPlayerGoal(this, Player.class,15.0f));
        this.goalSelector.addGoal(2,new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.goalSelector.addGoal(2,new AttackGoal(this));
        this.goalSelector.addGoal(1,new FloatGoal(this));
    }


    @Override
    public void registerControllers(final AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "walk", 5, this::walkAnimController));
        data.addAnimationController(new AnimationController<>(this, "attack1", 5, this::attackAnimController));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    public int getAttckingState() {
        return this.entityData.get(STATE);
    }

    public void setAttackingState(int time) {
        this.entityData.set(STATE, time);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(STATE, 0);
    }

    static class AttackGoal extends Goal{

        private final Zooey parentEntity;

        protected int attackTimer = 0;

        public AttackGoal(Zooey mob) {
            this.parentEntity = mob;
        }

        @Override
        public boolean canUse() {
            return this.parentEntity.getTarget() != null;
        }

        public void tick(){
            LivingEntity livingentity = this.parentEntity.getTarget();
            if (this.parentEntity.hasLineOfSight(livingentity)) {
                ++this.attackTimer;

                this.parentEntity.getNavigation().moveTo(livingentity, 1.5D);
                // 将该生物攻击状态设置为1
                this.parentEntity.setAttackingState(1);
                //计时器到头后，将攻击状态设置为0，同时计时器清0
                if (this.attackTimer == 30) {
                    this.parentEntity.setAttackingState(0);
                    this.attackTimer = -5;
                }
            }
            else if (this.attackTimer > 0) {
                --this.attackTimer;
            }
            this.parentEntity.lookAt(livingentity, 30.0F, 30.0F);
        }
    }
}
