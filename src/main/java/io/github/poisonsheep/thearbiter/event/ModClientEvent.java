package io.github.poisonsheep.thearbiter.event;

import io.github.poisonsheep.thearbiter.TheArbiter;
import io.github.poisonsheep.thearbiter.client.render.entity.RenderZooey;
import io.github.poisonsheep.thearbiter.entity.EntityRegistry;
import io.github.poisonsheep.thearbiter.entity.Zooey;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TheArbiter.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModClientEvent {
    @SubscribeEvent
    public static void onRegisterRenderer(EntityRenderersEvent.RegisterRenderers event) {
        //添加渲染注册语句
        event.registerEntityRenderer(EntityRegistry.ZOOEY.get(), RenderZooey::new);
        //event.registerEntityRenderer(EntityInit.DETONATOR_ENTITY.get(), RenderDetonatorEntity::new);

    }
    @SubscribeEvent
    public static void onAttributeCreate(EntityAttributeCreationEvent event){
        event.put(EntityRegistry.ZOOEY.get(), Zooey.prepareAttributes().build());
    }

}
