package io.github.poisonsheep.thearbiter.potion;


import io.github.poisonsheep.thearbiter.TheArbiter;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MobEffectRegistry {
    public static final DeferredRegister<MobEffect> EFFECT = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, TheArbiter.MODID);

    public static final RegistryObject<MobEffect> UNDYING = EFFECT.register("undying", () -> new MobEffectUndying());
}
