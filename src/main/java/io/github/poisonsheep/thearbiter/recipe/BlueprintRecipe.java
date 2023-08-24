package io.github.poisonsheep.thearbiter.recipe;

import io.github.poisonsheep.thearbiter.capability.PlayerBlueprint;
import io.github.poisonsheep.thearbiter.capability.PlayerBlueprintProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.List;

public class BlueprintRecipe implements CraftingRecipe {
    private final ResourceLocation id;
    private final String blueprint;
    private final CraftingRecipe craftingRecipe;

    public BlueprintRecipe(ResourceLocation id, String blueprint, CraftingRecipe craftingRecipe){
        this.id = id;
        this.blueprint = blueprint;
        this.craftingRecipe = craftingRecipe;
    }

    @Override
    public boolean matches(CraftingContainer container, Level worldIn) {
        if(worldIn.isClientSide()) {
            return false;
        }
        for(Player p :worldIn.players()) {
            if(p.containerMenu == container.menu) {
                if(isAbleToCraft(p)) {
                    return craftingRecipe.matches(container, worldIn);
                }
            }
        }
        return false;
    }

    @Override
    public ItemStack assemble(CraftingContainer container) {
       return craftingRecipe.assemble(container);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return craftingRecipe.canCraftInDimensions(width, height);
    }

    @Override
    public ItemStack getResultItem() {
        return craftingRecipe.getResultItem();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.BLUEPRINT_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return craftingRecipe.getType();
    }

    public String getBlueprint(){
        return blueprint;
    }

    public CraftingRecipe getRecipe() {
        return craftingRecipe;
    }
    public boolean isAbleToCraft(Player player){
//        Player player = ForgeEvent.player;
        if(player != null) {
            PlayerBlueprint playerBlueprint = player.getCapability(PlayerBlueprintProvider.PLAYER_BLUEPRINT_CAPABILITY).orElseThrow(() -> new RuntimeException("Player does not have PlayerBlueprint capability"));
            List<String> blueprints = playerBlueprint.getBlueprints();
            if(blueprints.contains(blueprint)) {
                return true;
            }
        }
        return false;
    }
}
