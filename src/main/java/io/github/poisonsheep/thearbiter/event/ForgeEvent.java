package io.github.poisonsheep.thearbiter.event;

import io.github.poisonsheep.thearbiter.Item.Blueprint;
import io.github.poisonsheep.thearbiter.Item.ItemRegistry;
import io.github.poisonsheep.thearbiter.TheArbiter;
import io.github.poisonsheep.thearbiter.potion.MobEffectRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

//Forge事件总线是用来处理和游戏运行相关的事件
@Mod.EventBusSubscriber(modid = TheArbiter.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvent {
    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        // 在生物受到伤害时执行的代码
        LivingEntity entity = event.getEntityLiving();
        // 判断生物是否有佐伊的庇护效果
        if (entity.hasEffect(MobEffectRegistry.UNDYING.get())) {
            // 取消伤害
            event.setCanceled(true);
        }
    }
    @SubscribeEvent
    public void onLivingFall(LivingFallEvent event) {
        // 获取落地的生物
        LivingEntity entity = event.getEntityLiving();
        // 判断生物是否有佐伊的庇护效果
        if (entity.hasEffect(MobEffectRegistry.UNDYING.get())) {
            // 取消摔落伤害
            event.setCanceled(true);
        }
    }
    @SubscribeEvent
    public void onAdvancement(AdvancementEvent event) {
        if(event.getAdvancement().getId().toString().equals("the_arbiter:the_embodiment_of_dream")) {
            Player player = event.getPlayer();
            ItemStack book = new ItemStack(ItemRegistry.BLUEPRINT_ANTHOLOGY.get());
            if (!player.getInventory().add(book)) {
                player.drop(book, false);
            }
        }
        if(event.getAdvancement().getId().toString().equals("the_arbiter:transform")) {
            Player player = event.getPlayer();
            ItemStack blueprint = new ItemStack(ItemRegistry.BLUEPRINT.get());
            Blueprint.setBluePrint(blueprint ,Blueprint.ARBITER_SWORD_BLUEPRINT);
            if (!player.getInventory().add(blueprint)) {
                player.drop(blueprint, false);
            }
        }
    }
}
