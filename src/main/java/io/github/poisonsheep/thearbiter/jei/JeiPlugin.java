package io.github.poisonsheep.thearbiter.jei;

import io.github.poisonsheep.thearbiter.TheArbiter;
import io.github.poisonsheep.thearbiter.recipe.BlueprintRecipe;
import io.github.poisonsheep.thearbiter.recipe.RecipeRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
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
        IModPlugin.super.registerCategories(registration);
    }

    @Override
    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
        IModPlugin.super.registerVanillaCategoryExtensions(registration);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(BLUEPRINT_RECIPE, getRecipe(RecipeRegistry.BLUEPRINT_RECIPE.get()));
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        IModPlugin.super.registerRecipeTransferHandlers(registration);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        IModPlugin.super.registerRecipeCatalysts(registration);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        IModPlugin.super.registerGuiHandlers(registration);
    }

    @Override
    public void registerAdvanced(IAdvancedRegistration registration) {
        IModPlugin.super.registerAdvanced(registration);
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        IModPlugin.super.onRuntimeAvailable(jeiRuntime);
    }
}
