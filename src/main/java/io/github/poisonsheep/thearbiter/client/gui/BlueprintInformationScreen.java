package io.github.poisonsheep.thearbiter.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.poisonsheep.thearbiter.Item.Blueprint;
import io.github.poisonsheep.thearbiter.Item.ItemRegistry;
import io.github.poisonsheep.thearbiter.recipe.RecipeDataList;
import io.github.poisonsheep.thearbiter.recipe.SpecialRecipeInGuideBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

import java.util.List;
import java.util.Objects;

public class BlueprintInformationScreen extends BasicBookScreen{

    private String blueprint;
    private final Screen parent;
    int toolTipMaxWidth;
    protected final List<RecipeData> recipes = RecipeDataList.INSTANCE.recipeData;

    public BlueprintInformationScreen(String blueprint, Screen parent) {
        super(new TranslatableComponent("the_arbiter.blueprint_anthology.title"));
        this.toolTipMaxWidth = (IMAGE_WIDTH / 2) - 20;
        this.blueprint = blueprint;
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        super.render(poseStack, mouseX, mouseY, partialTick);
        renderGui(poseStack);
        putMap();
        Minecraft.getInstance().font.draw(poseStack, this.getTitle(), this.leftPos + Math.round(this.IMAGE_WIDTH / 4) - 24, this.bottomPos + Math.round(this.IMAGE_HEIGHT / 2) -64, 1);
        renderRecipeWidget();
    }

    private void renderGui(PoseStack poseStack) {
        poseStack.pushPose();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.setShaderTexture(0, BlueprintAnthologyScreen.BOOK_TEXTURES);
        float scale = 1F;
        poseStack.scale(scale, scale, 0);
        float toolTipMaxWidthScaled = (this.toolTipMaxWidth) / scale;
        blit(poseStack, Math.round(this.leftPos + (IMAGE_WIDTH / 2 - toolTipMaxWidthScaled) / 2) + 25 , Math.round(this.bottomPos + 30), 0, 128, 128, 128);
        poseStack.popPose();
    }

    private void putMap() {
        ItemStack stack = new ItemStack(ItemRegistry.BLUEPRINT.get());
        Blueprint.setBluePrint(stack, new ResourceLocation(blueprint));
        this.itemRenderer.renderAndDecorateItem(stack,this.leftPos + Math.round(this.IMAGE_WIDTH / 4) - 8 ,this.bottomPos + Math.round(this.IMAGE_HEIGHT / 2) -24);
    }

    //citadel渲染合成配方的方法
    protected void renderRecipe(PoseStack poseStack, Recipe<?> recipe) {
        int playerTicks = Minecraft.getInstance().player.tickCount;
        float scale = 1F;
        NonNullList<Ingredient> ingredients = recipe instanceof SpecialRecipeInGuideBook ? ((SpecialRecipeInGuideBook)recipe).getDisplayIngredients() : recipe.getIngredients();
        NonNullList<ItemStack> displayedStacks = NonNullList.create();
        for (int i = 0; i < ingredients.size(); i++) {
            Ingredient ing = ingredients.get(i);
            ItemStack stack = ItemStack.EMPTY;
            if (!ing.isEmpty()) {
                if (ing.getItems().length > 1) {
                    int currentIndex = (int) ((playerTicks / 20F) % ing.getItems().length);
                    stack = ing.getItems()[currentIndex];
                } else {
                    stack = ing.getItems()[0];
                }
            }
            if (!stack.isEmpty()) {
                poseStack.pushPose();
                poseStack.translate(this.leftPos, this.bottomPos, 32.0F);
                poseStack.translate((int) (3 + (i % 3) * 20 * scale), (int) (3 + (i / 3) * 20 * scale), 0);
                poseStack.scale(scale, scale, scale);
                this.itemRenderer.blitOffset = 100.0F;
                this.itemRenderer.renderAndDecorateItem(stack, 0, 0);
                this.itemRenderer.blitOffset = 0.0F;
                poseStack.popPose();
            }
            displayedStacks.add(i, stack);
        }
        poseStack.pushPose();
        poseStack.translate(this.leftPos, this.bottomPos, 32.0F);
        float finScale = scale * 1.5F;
        poseStack.translate(3 + 70 * finScale, 3 + 10 * finScale, 0);
        poseStack.scale(finScale, finScale, finScale);
        this.itemRenderer.blitOffset = 100.0F;
        ItemStack result = recipe.getResultItem();
        if(recipe instanceof SpecialRecipeInGuideBook){
            result = ((SpecialRecipeInGuideBook) recipe).getDisplayResultFor(displayedStacks);
        }
        this.itemRenderer.renderAndDecorateItem(result, 0, 0);
        this.itemRenderer.blitOffset = 0.0F;
        poseStack.popPose();
    }

    public void renderRecipeWidget() {
        Recipe<?> recipe = getRecipe(this.blueprint);
        if (recipe != null) {
            PoseStack poseStack = RenderSystem.getModelViewStack();
            renderRecipe(poseStack, recipe);
        }
    }

    private Recipe getRecipe(String blueprint) {
        for(RecipeData recipeData : recipes) {
            if(Objects.equals(recipeData.getBlueprint(), blueprint)) {
                return recipeData.getRecipe();
            }
        }
        return null;
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }
}
