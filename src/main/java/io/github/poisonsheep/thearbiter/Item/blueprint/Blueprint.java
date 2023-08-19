package io.github.poisonsheep.thearbiter.Item.blueprint;

import io.github.poisonsheep.thearbiter.TheArbiter;
import io.github.poisonsheep.thearbiter.capability.PlayerBlueprint;
import io.github.poisonsheep.thearbiter.capability.PlayerBlueprintProvider;
import io.github.poisonsheep.thearbiter.event.blueprint.ReadEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;
import java.util.List;

public class Blueprint extends Item {
    public static final ResourceLocation UNKNOWN_BLUEPRINT = new ResourceLocation(TheArbiter.MODID, "unknown");
    public Blueprint() {
        super(new Properties().stacksTo(1));
        /*
        如果用指令直接give玩家该物品会导致游戏崩溃，bing建议添加这一行，但是没用，还是会崩
        setBluePrint(this.getDefaultInstance(), UNKNOWN_BLUEPRINT);
         */
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
        ItemStack stack = player.getItemInHand(hand);
        ResourceLocation name = getBlueprint(stack);
        if(!level.isClientSide){
            PlayerBlueprint playerBlueprint = player.getCapability(PlayerBlueprintProvider.PLAYER_BLUEPRINT_CAPABILITY).orElseThrow(() -> new RuntimeException("Player does not have PlayerBlueprint capability"));
            // 获取玩家的能力列表
            List<String> blueprints = playerBlueprint.getBlueprints();
            // 检查玩家的能力列表中是否包含当前物品的蓝图名称
            if (!blueprints.contains(name.toString())) {
                // 如果不包含，那么表示玩家没有阅读过这个物品
                CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) player, stack);
                MinecraftForge.EVENT_BUS.post(new ReadEvent((ServerPlayer)player,stack));
                player.awardStat(Stats.ITEM_USED.get(this));
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            } else {
                // 如果包含，那么表示玩家已经阅读过这个物品，不要再触发阅读事件和消耗物品
                // 给玩家一个提示信息
                player.sendMessage(new TranslatableComponent("message.the_arbiter.already_read", name).withStyle(ChatFormatting.GOLD), Util.NIL_UUID);
            }
        }
        if(level.isClientSide){
            playSound(player);
            addParticle();
        }
        player.getCooldowns().addCooldown(this, 10);
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    public void playSound(Player player){
        player.playSound(SoundEvents.PLAYER_LEVELUP,1.0F,1.0F);
    }

    public void addParticle(){}

}
