package io.github.poisonsheep.thearbiter.recipe;

import io.github.poisonsheep.thearbiter.capability.PlayerBlueprint;
import io.github.poisonsheep.thearbiter.capability.PlayerBlueprintProvider;
import net.minecraft.client.Minecraft;
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
        return craftingRecipe.matches(container, worldIn);
    }

    @Override
    public ItemStack assemble(CraftingContainer container) {
        if(isAbleToCraft()) {
            return craftingRecipe.assemble(container);
        }
        return ItemStack.EMPTY;
    }

    //这俩int不知道是啥
    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return craftingRecipe.canCraftInDimensions(p_43999_, p_44000_);
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
        return RecipeRegistry.BLUEPRINT_SERIALIZER.get();
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
    public boolean isAbleToCraft(){
        System.out.println("crafting");
        Player player = Minecraft.getInstance().player;
        if(player != null) {
            System.out.println("Check that the player is crafting");
            PlayerBlueprint playerBlueprint = player.getCapability(PlayerBlueprintProvider.PLAYER_BLUEPRINT_CAPABILITY).orElseThrow(() -> new RuntimeException("Player does not have PlayerBlueprint capability"));
            List<String> blueprints = playerBlueprint.getBlueprints();
            System.out.println(blueprint);
            System.out.println(blueprints);
            if(blueprints.contains(blueprint)) {
                System.out.println("true");
                return true;
            }
        }
        return false;
    }
}
