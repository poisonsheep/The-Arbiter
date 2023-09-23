package io.github.poisonsheep.thearbiter.compat.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.poisonsheep.thearbiter.recipe.BlueprintRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Arrays;
import java.util.List;

public class BlueprintRecipeExtension extends GuiComponent implements ICraftingCategoryExtension{
    private final BlueprintRecipe recipe;
    public BlueprintRecipeExtension(BlueprintRecipe recipe) {
        this.recipe = recipe;
    }
    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ICraftingGridHelper craftingGridHelper, IFocusGroup focuses) {
        IIngredientType<ItemStack> item = VanillaTypes.ITEM_STACK;
        craftingGridHelper.createAndSetOutputs(builder, item, List.of(recipe.getResultItem()));
        int width = recipe.getWidth();
        int height = recipe.getHeight();
        craftingGridHelper.createAndSetInputs(builder, item, recipe.getIngredients()
                .stream()
                .map(Ingredient::getItems)
                .map(Arrays::asList)
                .toList(), width, height);
    }
    @Override
    public void drawInfo(int recipeWidth, int recipeHeight, PoseStack matrixStack, double mouseX, double mouseY) {
        Minecraft minecraft = Minecraft.getInstance();
        String blueprint = recipe.getBlueprint();
        blueprint = blueprint.replace(":",".");
        minecraft.font.draw(matrixStack, I18n.get("gui.read")+I18n.get(blueprint)+I18n.get("gui.unlock"), 0, -11, 0x00000000);
    }
}
