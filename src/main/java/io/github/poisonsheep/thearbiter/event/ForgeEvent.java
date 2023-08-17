package io.github.poisonsheep.thearbiter.event;


import io.github.poisonsheep.thearbiter.TheArbiter;
import io.github.poisonsheep.thearbiter.potion.MobEffectRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

//Forge事件总线是用来处理和游戏运行相关的事件
@Mod.EventBusSubscriber(modid = TheArbiter.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvent {
    public static Player player = null;
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
    public void onPlayerCrafting(PlayerContainerEvent.Open event) {
        System.out.println("open");
        this.player = event.getPlayer();
    }
}
