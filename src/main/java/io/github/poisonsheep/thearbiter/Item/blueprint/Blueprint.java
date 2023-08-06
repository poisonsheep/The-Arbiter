package io.github.poisonsheep.thearbiter.Item.blueprint;

import io.github.poisonsheep.thearbiter.TheArbiter;
import io.github.poisonsheep.thearbiter.event.blueprint.ReadEvent;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;

public class Blueprint extends Item {
    public static final ResourceLocation UNKNOWN_BLUEPRINT = new ResourceLocation(TheArbiter.MODID, "unknown");
    public Blueprint() {
        super(new Properties().stacksTo(1));
    }
    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> itemStackNonNullList) {
        if (tab == CreativeModeTab.TAB_MISC) {
            for (String blueprint : BlueprintList.INSTANCE.blueprints) {
                ItemStack stack = new ItemStack(this);
                setBluePrint(stack, new ResourceLocation(blueprint));
                itemStackNonNullList.add(stack);
            }
        }
    }
    public static void setBluePrint(ItemStack itemStack, ResourceLocation name) {
        itemStack.getOrCreateTag().putString("blueprint", name.toString());
    }
    @Nullable
    public static ResourceLocation getBlueprint(ItemStack itemStack) {
        if (itemStack.getTag() != null && itemStack.getTag().contains("blueprint")) {
            return new ResourceLocation(itemStack.getTag().getString("blueprint"));
        }
        return null;
    }
    @Override
    public String getDescriptionId(ItemStack stack) {
        ResourceLocation name = getBlueprint(stack);
        if (name != null) {
            return name.toString().replace(":", ".");
        }
        return super.getDescriptionId(stack);
    }
    @Override
    public  InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if(level.isClientSide){
            playSound(player);
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
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        Player player = entity instanceof Player ? (Player) entity : null;
        if(!level.isClientSide){
            if (player instanceof ServerPlayer) {
                CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) player, stack);
                MinecraftForge.EVENT_BUS.post(new ReadEvent((ServerPlayer)player,stack));
            }
        }
        if(player != null){
            player.awardStat(Stats.ITEM_USED.get(this));
            stack.shrink(1);
        }
        return stack;
    }
    public void playSound(Player player){
        player.playSound(SoundEvents.PLAYER_LEVELUP,1.0F,1.0F);
    }
    public void addParticle(){}
}
