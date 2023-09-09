package io.github.poisonsheep.thearbiter.Item;

import io.github.poisonsheep.thearbiter.TheArbiter;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TheArbiter.MODID);

    public static final RegistryObject<Item> ARBITER_SWORD = ITEMS.register("arbiter_sword",()->new ArbiterSword());

    public static final RegistryObject<Item> BLUEPRINT_ANTHOLOGY = ITEMS.register("blueprint_anthology",()->new BlueprintAnthology());
    public static final RegistryObject<Item> BLUEPRINT = ITEMS.register("blueprint",()->new Blueprint());

    public static final CreativeModeTab BLUEPRINT_TAB = new CreativeModeTab(TheArbiter.MODID + ".blueprint_tab") {
        @NotNull
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(BLUEPRINT_ANTHOLOGY.get());
        }
    };
}
