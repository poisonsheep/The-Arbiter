package io.github.poisonsheep.thearbiter.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.poisonsheep.thearbiter.Item.Blueprint;
import io.github.poisonsheep.thearbiter.Item.ItemRegistry;
import io.github.poisonsheep.thearbiter.recipe.RecipeDataList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.PageButton;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class BlueprintInformationScreen extends BasicBookScreen{

    private final String blueprint;
    private final Screen parent;
    int toolTipMaxWidth;
    protected final List<RecipeData> recipeData = RecipeDataList.INSTANCE.recipeData;
    private final List<Recipe<?>> recipes;
    TranslatableComponent message;
    private ScrollableText scrollableText;
    ItemWidget[][] recipeItems;
    int page;
    private boolean renderRecipe;
    public BlueprintInformationScreen(String blueprint, Screen parent) {
        super(new TranslatableComponent("the_arbiter.blueprint_anthology.title"));
        this.toolTipMaxWidth = (IMAGE_WIDTH / 2) - 20;
        this.blueprint = blueprint;
        this.parent = parent;
        recipes = getRecipe(blueprint);
        renderRecipe = !recipes.isEmpty();
        message = new TranslatableComponent(blueprint.replace(":", ".") + ".message");
    }
    private static void forEach(ItemWidget[][] widgets, Consumer<ItemWidget> widget) {
        for (ItemWidget[] widgetsPage : widgets) {
            for(ItemWidget itemWidget : widgetsPage) {
                if(itemWidget != null) {
                    widget.accept(itemWidget);
                }
            }
        }
    }

    @Override
    protected void init() {
        super.init();
        int textStartHeight = this.bottomPos + this.IMAGE_HEIGHT / 2 + 6;
        int y1 = this.topPos - 36;
        this.scrollableText = new ScrollableText(message, (this.IMAGE_WIDTH / 2) - 56, y1 - textStartHeight -24, textStartHeight, y1);
        this.scrollableText.setLeftPos(this.leftPos + 31);
        this.addRenderableWidget(scrollableText);
        if(renderRecipe) {
            createRecipeMenu();
            load(this.page);
            PageButton pageForward = new PageButton(this.leftPos + IMAGE_WIDTH / 2 + 93, this.bottomPos + 130, true, button -> {
                unload(page);
                this.page = (page + 1) % recipes.size();
                load(this.page);
            }, true);
            this.addRenderableWidget(pageForward);
        }
    }

    protected void putMap() {
        ItemStack stack = new ItemStack(ItemRegistry.BLUEPRINT.get());
        Blueprint.setBluePrint(stack, new ResourceLocation(blueprint));
        //按理说ItemWidget带有渲染功能，但是不知道为什么不显示
        this.itemRenderer.renderAndDecorateItem(stack,this.leftPos + this.IMAGE_WIDTH / 4 - 8 ,this.bottomPos + Math.round(this.IMAGE_HEIGHT / 2) - 48);
    }

    protected void putTexture(PoseStack poseStack, ResourceLocation location, int x, int y, int x0, int y0, int length, int height) {
        poseStack.pushPose();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.setShaderTexture(0, location);
        float scale = 1F;
        poseStack.scale(scale, scale, 0);
        blit(poseStack, x, y, x0, y0, length, height);
        poseStack.popPose();
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        super.render(poseStack, mouseX, mouseY, partialTick);
        putTexture(poseStack, BlueprintAnthologyScreen.BOOK_TEXTURES, this.leftPos + IMAGE_WIDTH / 2 + 10, this.bottomPos + 42, 0, 128, 110, 127);
        putTexture(poseStack, BlueprintAnthologyScreen.BOOK_TEXTURES, this.leftPos + 40, this.bottomPos + 36, 64, 0, 64, 64);
        putMap();
        Minecraft.getInstance().font.draw(poseStack, this.getTitle(), this.leftPos + Math.round(this.IMAGE_WIDTH / 4) - 24, this.bottomPos + 24, 1);
        if(renderRecipe) {
            renderRecipe(poseStack, mouseX, mouseY);
        }
    }

    //citadel渲染合成配方的方法
    protected void renderRecipe(PoseStack poseStack, int mouseX, int mouseY) {
        forEach(recipeItems, ItemWidget -> {
            if (ItemWidget.isMouseOver(mouseX, mouseY)) {
                List<Component> tooltipLines = ItemWidget.stack.getTooltipLines(Minecraft.getInstance().player, this.minecraft.options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL);
                if (ItemWidget.hasAdditonalInfo) {
                    tooltipLines.add(1, new TextComponent("Click for more info"));
                }
                this.renderTooltip(poseStack, tooltipLines, ItemWidget.stack.getTooltipImage(), mouseX, mouseY);
            }
        });
    }

    protected void createRecipeMenu() {
        List<Recipe<?>> recipeList = getRecipe(this.blueprint);
        int maxPage = recipeList.size();
        recipeItems = new ItemWidget[maxPage][10];
        int page = 0;
        for(ItemWidget[] pagePair : recipeItems){
            Recipe<?> recipe = recipeList.get(page);
            int width = 3;
            int hight = 3;
            int initialX = this.leftPos + Math.round(this.IMAGE_WIDTH * 1 / 2) + 14;
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
            NonNullList<Ingredient> ingredients = recipe.getIngredients();
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
                    int Xmove = 3 + (i % width) * 20;
                    int Ymove = 3 + (i / width) * 20;
                    if(move) {
                        Xmove += 20;
                    }
                    ItemWidget itemWidget = new ItemWidget(stack, this.itemRenderer, initialX + Xmove, initialY + Ymove, 16, 16, button -> {});
                    pagePair[i] = this.addRenderableWidget(itemWidget);
                }
            }
            float Scale = 1.5F;
            int resultItemMoveX = 3 + Math.round(70 * Scale - 28);
            int resultItemMoveY = Math.round(3 + 10 * Scale + 7);
            ItemStack result = recipe.getResultItem();
            ItemWidget itemWidget = new ItemWidget(result, this.itemRenderer, initialX + resultItemMoveX, initialY + resultItemMoveY, 16, 16, button -> {});
            pagePair[ingredients.size()] = this.addRenderableWidget(itemWidget);
            page++;
        }
    }

    private List<Recipe<?>> getRecipe(String blueprint) {
        List<Recipe<?>> recipeList = new ArrayList<>();
        for(RecipeData recipeData : recipeData) {
            if(Objects.equals(recipeData.getBlueprint(), blueprint)) {
                recipeList.add(recipeData.getRecipe());
            }
        }
        return recipeList;
    }

    private void unload(int page) {
        ItemWidget[] renderableItem = this.recipeItems[page];
        for (ItemWidget itemWidgets : renderableItem) {
            if (itemWidgets != null) {
                itemWidgets.visible = false;
                itemWidgets.active = false;
            }
        }
    }

    private void load(int page) {
        ItemWidget[] renderableItem = this.recipeItems[page];
        for (ItemWidget itemWidgets : renderableItem) {
            if (itemWidgets != null) {
                itemWidgets.visible = true;
                itemWidgets.active = true;
            }
        }
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }
}
