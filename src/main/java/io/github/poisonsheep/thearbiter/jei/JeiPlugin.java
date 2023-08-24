package io.github.poisonsheep.thearbiter.jei;

import io.github.poisonsheep.thearbiter.Item.blueprint.BlueprintRegistry;
import io.github.poisonsheep.thearbiter.TheArbiter;
import io.github.poisonsheep.thearbiter.recipe.BlueprintRecipe;
import io.github.poisonsheep.thearbiter.recipe.RecipeRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;

import java.util.List;

@mezz.jei.api.JeiPlugin
public class JeiPlugin implements IModPlugin {
    public static final ResourceLocation UID = new ResourceLocation(TheArbiter.MODID, "jei_plugin");

    public static final RecipeType<BlueprintRecipe> BLUEPRINT_RECIPE= new RecipeType<>(new ResourceLocation(TheArbiter.MODID, "blueprint"), BlueprintRecipe.class);

    protected <C extends Container, T extends Recipe<C>> List<T> getRecipe(net.minecraft.world.item.crafting.RecipeType<T> recipeType) {
        return Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(recipeType);
    }

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new BlueprintRecipeCategory());
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(BLUEPRINT_RECIPE, getRecipe(RecipeRegistry.BLUEPRINT_RECIPE.get()));
    }
}
