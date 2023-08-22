package io.github.poisonsheep.thearbiter.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.poisonsheep.thearbiter.recipe.BlueprintRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Arrays;
import java.util.List;

public class BlueprintRecipeExtension implements ICraftingCategoryExtension {
    
    private final BlueprintRecipe recipe;
    
    public BlueprintRecipeExtension(BlueprintRecipe recipe) {
        this.recipe = recipe;
    }
    
    
    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ICraftingGridHelper craftingGridHelper, IFocusGroup focuses) {
        
        IIngredientType<ItemStack> item = VanillaTypes.ITEM_STACK;
        
        craftingGridHelper.setOutputs(builder, item, List.of(recipe.getResultItem()));
        craftingGridHelper.setInputs(builder, item, recipe.getIngredients()
                .stream()
                .map(Ingredient::getItems)
                .map(Arrays::asList)
                .toList(), 3, 3);
    }
    
    
    @Override
    public void drawInfo(int recipeWidth, int recipeHeight, PoseStack matrixStack, double mouseX, double mouseY) {
        Minecraft.getInstance().font.draw(matrixStack, I18n.get("gui.rs.tip.blueprint"), 0, -11, 0x00000000);
    }
}
