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
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class BlueprintInformationScreen extends BasicBookScreen{

    private final String blueprint;
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
        Minecraft.getInstance().font.draw(poseStack, this.getTitle(), this.leftPos + Math.round(this.IMAGE_WIDTH / 4) - 24, this.bottomPos + Math.round(this.IMAGE_HEIGHT / 2) -64, 1);
        putMap(poseStack, mouseX, mouseY);
        renderRecipeWidget(poseStack, mouseX, mouseY);
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

    private void putMap(PoseStack poseStack, int mouseX, int mouseY) {
        ItemStack stack = new ItemStack(ItemRegistry.BLUEPRINT.get());
        Blueprint.setBluePrint(stack, new ResourceLocation(blueprint));
        ItemWidget itemWidget = new ItemWidget(stack, this.itemRenderer, this.leftPos + Math.round(this.IMAGE_WIDTH / 4) - 8 , this.bottomPos + Math.round(this.IMAGE_HEIGHT / 2) -24, 16, 16, button -> {});
        renderTooltip(poseStack ,itemWidget, mouseX, mouseY);
        //按理说ItemWidget带有渲染功能，但是不知道为什么不显示
        this.itemRenderer.renderAndDecorateItem(stack,this.leftPos + Math.round(this.IMAGE_WIDTH / 4) - 8 ,this.bottomPos + Math.round(this.IMAGE_HEIGHT / 2) -24);
    }
    private static void forEach(ItemWidget widgets, Consumer<ItemWidget> widget) {
        widgets.visible = true;
        widgets.active = true;
        widget.accept(widgets);
    }

    public void renderTooltip(PoseStack stack, ItemWidget itemWidget,int mouseX, int mouseY) {
        forEach(itemWidget, ItemWidget -> {
            if (ItemWidget.isMouseOver(mouseX, mouseY)) {
                List<Component> tooltipLines = ItemWidget.stack.getTooltipLines(Minecraft.getInstance().player, this.minecraft.options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL);
                if (ItemWidget.hasAdditonalInfo) {
                    tooltipLines.add(1, new TextComponent("Click for more info"));
                }
                this.renderTooltip(stack, tooltipLines, ItemWidget.stack.getTooltipImage(), mouseX, mouseY);
            }
        });
    }
    //citadel渲染合成配方的方法
    protected void renderRecipe(PoseStack poseStack, Recipe<?> recipe, int mouseX, int mouseY) {
        int width = 3;
        int hight = 3;
        int initialX = this.leftPos + Math.round(this.IMAGE_WIDTH * 1 / 2) + 8;
        int initialY = this.bottomPos + Math.round(this.IMAGE_HEIGHT / 3);
        boolean move = false;
        if(recipe instanceof ShapedRecipe) {
            width = ((ShapedRecipe) recipe).getWidth();
            hight = ((ShapedRecipe) recipe).getHeight();
            if(width == 1 && hight == 3) {
                move = true;
            }
        }
        int playerTicks = Minecraft.getInstance().player.tickCount;
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
                int Xmove = 3 + (i % width) * 20;
                int Ymove = 3 + (i / width) * 20;
                if(move) {
                    Xmove += 23;
                }
                this.itemRenderer.blitOffset = 100.0F;
                this.itemRenderer.renderAndDecorateItem(stack, initialX + Xmove, initialY + Ymove);
                this.itemRenderer.blitOffset = 0.0F;
                ItemWidget itemWidget = new ItemWidget(stack, this.itemRenderer, initialX + Xmove, initialY + Ymove, 16, 16, button -> {});
                renderTooltip(poseStack ,itemWidget, mouseX, mouseY);
            }
            displayedStacks.add(i, stack);
        }
        float Scale = 1.5F;
        int resultItemMoveX = 3 + Math.round(70 * Scale - 28);
        int resultItemMoveY = Math.round(3 + 10 * Scale);
        this.itemRenderer.blitOffset = 100.0F;
        ItemStack result = recipe.getResultItem();
        if(recipe instanceof SpecialRecipeInGuideBook){
            result = ((SpecialRecipeInGuideBook) recipe).getDisplayResultFor(displayedStacks);
        }
        this.itemRenderer.renderAndDecorateItem(result, initialX + resultItemMoveX, initialY + resultItemMoveY);
        this.itemRenderer.blitOffset = 0.0F;
        ItemWidget itemWidget = new ItemWidget(result, this.itemRenderer, initialX + resultItemMoveX, initialY + resultItemMoveY, 16, 16, button -> {});
        renderTooltip(poseStack ,itemWidget, mouseX, mouseY);
    }

    public void renderRecipeWidget(PoseStack poseStack, int mouseX, int mouseY) {
        Recipe<?> recipe = getRecipe(this.blueprint);
        if (recipe != null) {
            renderRecipe(poseStack, recipe, mouseX, mouseY);
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
