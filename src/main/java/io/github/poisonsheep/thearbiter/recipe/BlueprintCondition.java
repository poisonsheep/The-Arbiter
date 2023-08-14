package io.github.poisonsheep.thearbiter.recipe;

import io.github.poisonsheep.thearbiter.capability.PlayerBlueprint;
import io.github.poisonsheep.thearbiter.capability.PlayerBlueprintProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.crafting.conditions.ICondition;

import java.util.List;

public class BlueprintCondition implements ICondition {
    private final String blueprint;
    private final ResourceLocation id;
    public BlueprintCondition(String blueprint, ResourceLocation id){
        this.blueprint = blueprint;
        this.id = id;
    }
    @Override
    public ResourceLocation getID() {
        return id;
    }
    @Override
    public boolean test() {
        Player player = Minecraft.getInstance().player;
        PlayerBlueprint playerblueprint = player.getCapability(PlayerBlueprintProvider.PLAYER_BLUEPRINT_CAPABILITY).orElseThrow(() -> new RuntimeException("Player does not have PlayerBlueprint capability"));
        List<String> blueprints = playerblueprint.getBlueprints();
        if(blueprints.contains(blueprint)){
            return true;
        }else {
            return false;
        }
    }
    public String getBlueprint() {
        return blueprint;
    }
}
