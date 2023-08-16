package io.github.poisonsheep.thearbiter.event.blueprint;

import io.github.poisonsheep.thearbiter.TheArbiter;
import io.github.poisonsheep.thearbiter.capability.PlayerBlueprint;
import io.github.poisonsheep.thearbiter.capability.PlayerBlueprintProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TheArbiter.MODID)
public class BlueprintEvent {
    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event){
        event.register(PlayerBlueprint.class);
    }
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent event){
        if (event.getObject() instanceof Player) {
            if (!((Player) event.getObject()).getCapability(PlayerBlueprintProvider.PLAYER_BLUEPRINT_CAPABILITY).isPresent()) {
                // The player does not already have this capability so we need to add the capability provider here
                event.addCapability(new ResourceLocation(TheArbiter.MODID, "player_blueprints"), new PlayerBlueprintProvider());
            }
        }
    }
    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        event.getOriginal().reviveCaps();
        if (event.isWasDeath()) {
            // We need to copyFrom the capabilities
            event.getOriginal().getCapability(PlayerBlueprintProvider.PLAYER_BLUEPRINT_CAPABILITY).ifPresent(oldStore -> {
                event.getPlayer().getCapability(PlayerBlueprintProvider.PLAYER_BLUEPRINT_CAPABILITY).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
        }
    }
}
