package io.github.poisonsheep.thearbiter.event;

import io.github.poisonsheep.thearbiter.TheArbiter;
import io.github.poisonsheep.thearbiter.client.particle.ParticlesRegistry;
import io.github.poisonsheep.thearbiter.client.particle.ShelterParticles;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

//Mod事件总线是用来处理和模组加载相关的事件
@Mod.EventBusSubscriber(modid = TheArbiter.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModServerEvent {
    @SubscribeEvent
    public static void registerParticleFactory(final ParticleFactoryRegisterEvent event){
        Minecraft.getInstance().particleEngine.register(ParticlesRegistry.SHELTER_PARTICLES.get(), ShelterParticles.Factory::new);
    }

}
