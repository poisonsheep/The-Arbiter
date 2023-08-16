package io.github.poisonsheep.thearbiter;

import io.github.poisonsheep.thearbiter.Item.ItemRegistry;
import io.github.poisonsheep.thearbiter.client.particle.ParticlesRegistry;
import io.github.poisonsheep.thearbiter.client.sound.SoundRegistry;
import io.github.poisonsheep.thearbiter.entity.EntityRegistry;
import io.github.poisonsheep.thearbiter.event.ForgeEvent;
import io.github.poisonsheep.thearbiter.event.blueprint.LearnEvent;
import io.github.poisonsheep.thearbiter.potion.MobEffectRegistry;
import io.github.poisonsheep.thearbiter.recipe.BlueprintSerializer;
import io.github.poisonsheep.thearbiter.recipe.RecipeRegistry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib3.GeckoLib;

@Mod(TheArbiter.MODID)
public class TheArbiter
{
    public static final IEventBus modBusEvent = FMLJavaModLoadingContext.get().getModEventBus();
    public final static String MODID = "the_arbiter";
    public TheArbiter()
    {
        MinecraftForge.EVENT_BUS.register(new ForgeEvent());
        MinecraftForge.EVENT_BUS.register(new LearnEvent());
        modBusEvent.addListener(this::setup);
        modBusEvent.addListener(this::enqueueIMC);
        modBusEvent.addListener(this::processIMC);
        MinecraftForge.EVENT_BUS.register(this);
        ItemRegistry.ITEMS.register(modBusEvent);
        EntityRegistry.ENTITY_TYPE.register(modBusEvent);
        MobEffectRegistry.EFFECT.register(modBusEvent);
        ParticlesRegistry.register(modBusEvent);
        SoundRegistry.register(modBusEvent);
        RecipeRegistry.register(modBusEvent);
        GeckoLib.initialize();
    }

    private void setup(final FMLCommonSetupEvent event) {
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {}

    private void processIMC(final InterModProcessEvent event) {}
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {}
}
