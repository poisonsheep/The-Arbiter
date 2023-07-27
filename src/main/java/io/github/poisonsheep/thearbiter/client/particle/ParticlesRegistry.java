package io.github.poisonsheep.thearbiter.client.particle;

import io.github.poisonsheep.thearbiter.TheArbiter;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class ParticlesRegistry {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPE = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, TheArbiter.MODID);

    public static final RegistryObject<SimpleParticleType> SHELTER_PARTICLES = PARTICLE_TYPE.register("shelter_particles", () -> new SimpleParticleType(true));

    public static void register(IEventBus bus) {
        PARTICLE_TYPE.register(bus);
    }
}
