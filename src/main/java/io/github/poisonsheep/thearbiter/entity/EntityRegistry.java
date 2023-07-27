package io.github.poisonsheep.thearbiter.entity;

import io.github.poisonsheep.thearbiter.TheArbiter;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityRegistry {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPE = DeferredRegister.create(ForgeRegistries.ENTITIES, TheArbiter.MODID);

    public static final RegistryObject<EntityType<Zooey>> ZOOEY = ENTITY_TYPE.register("zooey",()->EntityType.Builder.of(Zooey::new, MobCategory.CREATURE)
            .sized(1.0f, 2.0f)
            .clientTrackingRange(8)
            .setShouldReceiveVelocityUpdates(false)
            .build("zooey"));
}
