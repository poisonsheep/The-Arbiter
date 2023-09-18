package io.github.poisonsheep.thearbiter.event;

import io.github.poisonsheep.thearbiter.client.gui.BlueprintAnthologyScreen;
import io.github.poisonsheep.thearbiter.client.model.item.blueprint.BlueprintBakedModel;
import io.github.poisonsheep.thearbiter.client.blueprint.BlueprintRegistry;
import io.github.poisonsheep.thearbiter.TheArbiter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = TheArbiter.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModClientEvent {
    @SubscribeEvent
    public static void registerModelUnBake(ModelRegistryEvent event) {
        BlueprintRegistry.register(event);
    }
    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent event) {
        event.getModelRegistry().put(new ModelResourceLocation(
                TheArbiter.MODID,
                "blueprint",
                "inventory"
        ), new BlueprintBakedModel());
    }
}
