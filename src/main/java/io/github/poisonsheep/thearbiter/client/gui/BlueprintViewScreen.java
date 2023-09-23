package io.github.poisonsheep.thearbiter.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.poisonsheep.thearbiter.Item.Blueprint;
import io.github.poisonsheep.thearbiter.Item.ItemRegistry;
import io.github.poisonsheep.thearbiter.client.blueprint.BlueprintList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.function.Consumer;

public class BlueprintViewScreen extends BasicBookScreen{

    int page;
    private final Screen parent;
    int maxPagePairCount;
    BlueprintWidget[][][][] items;
    protected BlueprintViewScreen(Screen parent) {
        super(new TextComponent(""));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        createMenu();
        load(this.page);

        PageButton pageBack = new PageButton(this.leftPos + 5, this.topPos - 15, false, button -> {
            unload(page);
            this.page = page == 0 ? maxPagePairCount - 1 : (page - 1) % maxPagePairCount;
            load(this.page);
        }, true);
        this.addRenderableWidget(pageBack);
        pageBack.x = this.leftPos + 15;
        pageBack.y = this.topPos - pageBack.getHeight() - 13;

        PageButton pageForward = new PageButton(this.rightPos - 5, this.topPos - 15, true, button -> {
            unload(page);
            this.page = (page + 1) % maxPagePairCount;
            load(this.page);
        }, true);
        this.addRenderableWidget(pageForward);
        pageForward.x = this.rightPos - pageBack.getWidth() - 22;
        pageForward.y = this.topPos - pageBack.getHeight() - 13;
    }

    private void createMenu() {
        createMenu(7, 10);
    }

    private void createMenu(int rowLength, int columnLength) {
        List<String> blueprints = BlueprintList.INSTANCE.blueprints;
        this.maxPagePairCount = (blueprints.size()/ (rowLength * columnLength)) / 2 + 1;
        items = new BlueprintWidget[maxPagePairCount][2][columnLength][rowLength];
        int registryIdx = 0;
        int width = 17;
        int buttonSize = width - 2;
        int offsetFromEdge = 15;

        for (BlueprintWidget[][][] pagePair : items) {
            for (int pageSide = 0; pageSide < pagePair.length; pageSide++) {
                BlueprintWidget[][] page = pagePair[pageSide];
                int yOffset = this.bottomPos + offsetFromEdge;
                for (BlueprintWidget[] row : page) {
                    int xOffset = this.leftPos + offsetFromEdge + 4;
                    for (int columnIdx = 0; columnIdx < row.length; columnIdx++) {
                        int startX = ((IMAGE_WIDTH / 2) - 8) * pageSide;
                        if (registryIdx > blueprints.size() - 1) {
                            break;
                        }
                        String blueprint = blueprints.get(columnIdx);
                        ItemStack stack = new ItemStack(ItemRegistry.BLUEPRINT.get());
                        Blueprint.setBluePrint(stack, new ResourceLocation(blueprint));
                        BlueprintWidget blueprintWidget = new BlueprintWidget(stack, this.itemRenderer, xOffset + startX, yOffset, buttonSize, buttonSize, button -> {
                            this.minecraft.setScreen(new BlueprintInformationScreen(blueprint, this));
                        });
                        row[columnIdx] = this.addRenderableWidget(blueprintWidget);
                        registryIdx++;
                        xOffset += width;
                    }
                    yOffset += width;
                }
            }
        }
    }
    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        super.render(poseStack, mouseX, mouseY, partialTick);
        forEach(items, BlueprintWidget -> {
            if (BlueprintWidget.isMouseOver(mouseX, mouseY)) {
                List<Component> tooltipLines = BlueprintWidget.stack.getTooltipLines(Minecraft.getInstance().player, this.minecraft.options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL);
                if (BlueprintWidget.hasAdditonalInfo) {
                    tooltipLines.add(1, new TextComponent("Click for more info"));
                }
                this.renderTooltip(poseStack, tooltipLines, BlueprintWidget.stack.getTooltipImage(), mouseX, mouseY);
            }
        });
    }
    private static void forEach(BlueprintWidget[][][][] widgets, Consumer<BlueprintWidget> widget) {
        for (BlueprintWidget[][][] BlueprintWidgetsPage : widgets) {
            for (BlueprintWidget[][] BlueprintWidgetsLeftRight : BlueprintWidgetsPage) {
                for (BlueprintWidget[] BlueprintWidgets : BlueprintWidgetsLeftRight) {
                    for (BlueprintWidget BlueprintWidget : BlueprintWidgets) {
                        if (BlueprintWidget != null) {
                            widget.accept(BlueprintWidget);
                        }
                    }
                }
            }
        }
    }
    private void unload(int page) {
        BlueprintWidget[][][] renderableItem = this.items[page];
        for (BlueprintWidget[][] blueprintWidgetsByPage : renderableItem) {
            for (BlueprintWidget[] blueprintWidgets : blueprintWidgetsByPage) {
                for (BlueprintWidget BlueprintWidget : blueprintWidgets) {
                    if (BlueprintWidget != null) {
                        BlueprintWidget.visible = false;
                        BlueprintWidget.active = false;
                    }
                }
            }
        }
    }

    private void load(int page) {
        BlueprintWidget[][][] renderableItem = this.items[page];
        for (BlueprintWidget[][] blueprintWidgetsByPage : renderableItem) {
            for (BlueprintWidget[] BlueprintWidgets : blueprintWidgetsByPage) {
                for (BlueprintWidget blueprintWidget : BlueprintWidgets) {
                    if (blueprintWidget != null) {
                        blueprintWidget.visible = true;
                        blueprintWidget.active = true;
                    }
                }
            }
        }
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }
}
