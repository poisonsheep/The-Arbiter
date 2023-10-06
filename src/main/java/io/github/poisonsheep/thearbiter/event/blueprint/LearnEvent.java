package io.github.poisonsheep.thearbiter.event.blueprint;

import io.github.poisonsheep.thearbiter.Item.Blueprint;
import io.github.poisonsheep.thearbiter.advancement.AdvancementTriggerRegistry;
import io.github.poisonsheep.thearbiter.capability.PlayerBlueprint;
import io.github.poisonsheep.thearbiter.capability.PlayerBlueprintProvider;
import io.github.poisonsheep.thearbiter.client.gui.RecipeData;
import io.github.poisonsheep.thearbiter.network.ModNetwork;
import io.github.poisonsheep.thearbiter.network.packet.BlueprintUpdatePacket;
import io.github.poisonsheep.thearbiter.recipe.RecipeDataList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;


public class LearnEvent {
    @SubscribeEvent()
    public void playerLearn(ReadEvent event) {
        Player player = event.getPlayer();
        AdvancementTriggerRegistry.FIRST_READ.trigger((ServerPlayer) player);
        ItemStack stack = event.getStack();
        CompoundTag tag = stack.getTag();
        if(tag != null && tag.contains("blueprint")) {
            String blueprint = tag.getString("blueprint");
            if(blueprint.equals(Blueprint.ARBITER_SWORD_BLUEPRINT.toString())) {
                AdvancementTriggerRegistry.ENTHRONED.trigger((ServerPlayer) player);
            }
            if(player.getCapability(PlayerBlueprintProvider.PLAYER_BLUEPRINT_CAPABILITY).isPresent()){
                PlayerBlueprint playerBlueprint = player.getCapability(PlayerBlueprintProvider.PLAYER_BLUEPRINT_CAPABILITY).orElseThrow(() -> new RuntimeException("Player does not have PlayerBlueprint capability"));
                playerBlueprint.addBluePrints(blueprint);
                ModNetwork.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)player), new BlueprintUpdatePacket(playerBlueprint.getBlueprints()));
                Boolean transform = true;
                for (RecipeData data : RecipeDataList.INSTANCE.recipeData) {
                    String blueprint2 = data.getBlueprint();
                    if(!blueprint2.equals(Blueprint.ARBITER_SWORD_BLUEPRINT.toString())) {
                        if(!playerBlueprint.getBlueprints().contains(blueprint2)) {
                            transform = false;
                            break;
                        }
                    }
                }
                if(transform) {
                    AdvancementTriggerRegistry.TRANSFORM.trigger((ServerPlayer) player);
                }
            }
        }
    }
}
