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
    ItemWidget[][][][] items;
    protected BlueprintViewScreen(Screen parent) {
        super(new TextComponent(""));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        createMenu();
        load(this.page);
        PageButton pageBack = new PageButton(this.leftPos + 18, this.topPos - 36, false, button -> {
            unload(page);
            this.page = page == 0 ? maxPagePairCount - 1 : (page - 1) % maxPagePairCount;
            load(this.page);
        }, true);
        this.addRenderableWidget(pageBack);
        PageButton pageForward = new PageButton(this.rightPos - 42, this.topPos - 36, true, button -> {
            unload(page);
            this.page = (page + 1) % maxPagePairCount;
            load(this.page);
        }, true);
        this.addRenderableWidget(pageForward);
    }

    private void createMenu() {
        createMenu(6, 10);
    }

    private void createMenu(int rowLength, int columnLength) {
        List<String> blueprints = BlueprintList.INSTANCE.blueprints;
        this.maxPagePairCount = (blueprints.size()/ (rowLength * columnLength)) / 2 + 1;
        items = new ItemWidget[maxPagePairCount][2][columnLength][rowLength];
        int registryIdx = 0;
        int width = 17;
        int buttonSize = width - 2;
        int offsetFromEdge = 15;

        for (ItemWidget[][][] pagePair : items) {
            for (int pageSide = 0; pageSide < pagePair.length; pageSide++) {
                ItemWidget[][] page = pagePair[pageSide];
                int yOffset = this.bottomPos + offsetFromEdge;
                for (ItemWidget[] row : page) {
                    int xOffset = this.leftPos + offsetFromEdge + 4;
                    for (int columnIdx = 0; columnIdx < row.length; columnIdx++) {
                        int startX = ((IMAGE_WIDTH / 2) - 8) * pageSide;
                        if (registryIdx > blueprints.size() - 1) {
                            break;
                        }
                        String blueprint = blueprints.get(registryIdx);
                        ItemStack stack = new ItemStack(ItemRegistry.BLUEPRINT.get());
                        Blueprint.setBluePrint(stack, new ResourceLocation(blueprint));
                        ItemWidget itemWidget = new ItemWidget(stack, this.itemRenderer, xOffset + startX, yOffset, buttonSize, buttonSize, button -> {
                            this.minecraft.setScreen(new BlueprintInformationScreen(blueprint, this));
                        });
                        row[columnIdx] = this.addRenderableWidget(itemWidget);
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
        forEach(items, ItemWidget -> {
            if (ItemWidget.isMouseOver(mouseX, mouseY)) {
                List<Component> tooltipLines = ItemWidget.stack.getTooltipLines(Minecraft.getInstance().player, this.minecraft.options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL);
                if (ItemWidget.hasAdditonalInfo) {
                    tooltipLines.add(1, new TextComponent("Click for more info"));
                }
                this.renderTooltip(poseStack, tooltipLines, ItemWidget.stack.getTooltipImage(), mouseX, mouseY);
            }
        });
    }
    private static void forEach(ItemWidget[][][][] widgets, Consumer<ItemWidget> widget) {
        for (ItemWidget[][][] itemWidgetsPage : widgets) {
            for (ItemWidget[][] itemWidgetsLeftRight : itemWidgetsPage) {
                for (ItemWidget[] itemWidgets : itemWidgetsLeftRight) {
                    for (ItemWidget ItemWidget : itemWidgets) {
                        if (ItemWidget != null) {
                            widget.accept(ItemWidget);
                        }
                    }
                }
            }
        }
    }
    private void unload(int page) {
        ItemWidget[][][] renderableItem = this.items[page];
        for (ItemWidget[][] itemWidgetsByPage : renderableItem) {
            for (ItemWidget[] itemWidgets : itemWidgetsByPage) {
                for (ItemWidget ItemWidget : itemWidgets) {
                    if (ItemWidget != null) {
                        ItemWidget.visible = false;
                        ItemWidget.active = false;
                    }
                }
            }
        }
    }

    private void load(int page) {
        ItemWidget[][][] renderableItem = this.items[page];
        for (ItemWidget[][] itemWidgetsByPage : renderableItem) {
            for (ItemWidget[] itemWidgets : itemWidgetsByPage) {
                for (ItemWidget itemWidget : itemWidgets) {
                    if (itemWidget != null) {
                        itemWidget.visible = true;
                        itemWidget.active = true;
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
