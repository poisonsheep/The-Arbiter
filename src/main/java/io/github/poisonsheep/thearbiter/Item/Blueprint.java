package io.github.poisonsheep.thearbiter.Item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class Blueprint extends Item {
    public Blueprint() {
        super(new Properties().tab(CreativeModeTab.TAB_MISC));
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if(level.isClientSide){
            playSound();
            addParticle();
        }
        player.startUsingItem(hand);
        return super.use(level, player, hand);
    }
    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BLOCK;
    }
    @Override
    public int getUseDuration(ItemStack stack) {
        return 1;
    }
    @Override
    public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft){
    }
    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        Player player = entity instanceof Player ? (Player) entity : null;
        if (player instanceof ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) player, stack);
        }
        if(player != null){
            player.awardStat(Stats.ITEM_USED.get(this));
            stack.shrink(1);
        }
        return stack;
    }
    public void playSound(){}
    public void addParticle(){}
}
