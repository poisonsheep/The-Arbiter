package io.github.poisonsheep.thearbiter.event.blueprint;

import io.github.poisonsheep.thearbiter.capability.PlayerBlueprint;
import io.github.poisonsheep.thearbiter.capability.PlayerBlueprintProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;


public class LearnEvent {
    @SubscribeEvent()
    public void playerLearn(ReadEvent event) {
        Player player = event.getPlayer();
        ItemStack stack = event.getStack();
        CompoundTag tag = stack.getTag();
        if(tag != null && tag.contains("blueprint")) {
            String blueprint = tag.getString("blueprint");
            if(player.getCapability(PlayerBlueprintProvider.PLAYER_BLUEPRINT_CAPABILITY).isPresent()){
                PlayerBlueprint playerBlueprint = player.getCapability(PlayerBlueprintProvider.PLAYER_BLUEPRINT_CAPABILITY).orElseThrow(() -> new RuntimeException("Player does not have PlayerBlueprint capability"));
                playerBlueprint.addBluePrints(blueprint);
            }
        }
    }
}
