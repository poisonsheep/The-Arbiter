package io.github.poisonsheep.thearbiter;

import io.github.poisonsheep.thearbiter.Item.ItemRegistry;
import io.github.poisonsheep.thearbiter.advancement.AdvancementTriggerRegistry;
import io.github.poisonsheep.thearbiter.client.particle.ParticlesRegistry;
import io.github.poisonsheep.thearbiter.client.sound.SoundRegistry;
import io.github.poisonsheep.thearbiter.event.ForgeEvent;
import io.github.poisonsheep.thearbiter.event.blueprint.LearnEvent;
import io.github.poisonsheep.thearbiter.network.ModNetwork;
import io.github.poisonsheep.thearbiter.potion.MobEffectRegistry;
import io.github.poisonsheep.thearbiter.recipe.RecipeRegistry;
import io.github.poisonsheep.thearbiter.recipe.RecipeSerializerRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib3.GeckoLib;

@Mod(TheArbiter.MODID)
public class TheArbiter
{
    public static final IEventBus modBusEvent = FMLJavaModLoadingContext.get().getModEventBus();
    public final static String MODID = "the_arbiter";
    public static final String VERSION = "1.18.2-1.1.1";
    public TheArbiter()
    {
        MinecraftForge.EVENT_BUS.register(new ForgeEvent());
        MinecraftForge.EVENT_BUS.register(new LearnEvent());
        modBusEvent.addListener(this::setup);
        modBusEvent.addListener(this::enqueueIMC);
        modBusEvent.addListener(this::processIMC);
        MinecraftForge.EVENT_BUS.register(this);
        ItemRegistry.ITEMS.register(modBusEvent);
        MobEffectRegistry.EFFECT.register(modBusEvent);
        ParticlesRegistry.register(modBusEvent);
        SoundRegistry.register(modBusEvent);
        RecipeRegistry.register(modBusEvent);
        RecipeSerializerRegistry.register(modBusEvent);
        ModNetwork.register();
        GeckoLib.initialize();
    }

    private void setup(final FMLCommonSetupEvent event) {
        AdvancementTriggerRegistry.register();
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {}

    private void processIMC(final InterModProcessEvent event) {}
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {}
}
