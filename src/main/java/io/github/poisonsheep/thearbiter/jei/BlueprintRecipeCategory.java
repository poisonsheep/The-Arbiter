package io.github.poisonsheep.thearbiter.jei;

import io.github.poisonsheep.thearbiter.TheArbiter;
import io.github.poisonsheep.thearbiter.recipe.BlueprintRecipe;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class BlueprintRecipeCategory implements IRecipeCategory<BlueprintRecipe> {
    private final RecipeType<BlueprintRecipe> type = JeiPlugin.BLUEPRINT_RECIPE;
    private final ResourceLocation UID = new ResourceLocation(TheArbiter.MODID, "blueprint_unlock");


    @Override
    public Component getTitle() {
        return null;
    }

    public IDrawable getBackground() {
        return null;
    }

    @Override
    public IDrawable getIcon() {
        return null;
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends BlueprintRecipe> getRecipeClass() {
        return BlueprintRecipe.class;
    }

    @Override
    public RecipeType<BlueprintRecipe> getRecipeType() {
        return type;
    }
}
