package io.github.poisonsheep.thearbiter.event;

import io.github.poisonsheep.thearbiter.TheArbiter;
import io.github.poisonsheep.thearbiter.client.particle.ParticlesRegistry;
import io.github.poisonsheep.thearbiter.client.particle.ShelterParticles;
import io.github.poisonsheep.thearbiter.loottable.BlueprintLootModifier;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
//Mod事件总线是用来处理和模组加载相关的事件
@Mod.EventBusSubscriber(modid = TheArbiter.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModServerEvent {
    @SubscribeEvent
    public static void registerParticleFactory(final ParticleFactoryRegisterEvent event){
        Minecraft.getInstance().particleEngine.register(ParticlesRegistry.SHELTER_PARTICLES.get(), ShelterParticles.Factory::new);
    }
    @SubscribeEvent
    public static void registerLootData(RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
        event.getRegistry().register(new BlueprintLootModifier.Serializer().setRegistryName(new ResourceLocation(TheArbiter.MODID,"blueprint_modifier")));
    }
}
