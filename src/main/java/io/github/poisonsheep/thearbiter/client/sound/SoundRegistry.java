package io.github.poisonsheep.thearbiter.client.sound;


import io.github.poisonsheep.thearbiter.TheArbiter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundRegistry {
    public static final DeferredRegister<SoundEvent> SOUND_EVENT = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, TheArbiter.MODID);

    public static final RegistryObject<SoundEvent> ARBITER_SWORD_USE = registerSoundEvent("arbiter_sword_use");

    public static final RegistryObject<SoundEvent> ZOOEY = registerSoundEvent("zooey");

    public static final RegistryObject<SoundEvent> ARBITER_SWORD_CHARGED = registerSoundEvent("arbiter_sword_charged");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name){
        return SOUND_EVENT.register(name , () -> new SoundEvent(new ResourceLocation(TheArbiter.MODID , name)));
    }

    public static void register(IEventBus bus) {
        SOUND_EVENT.register(bus);
    }
}
