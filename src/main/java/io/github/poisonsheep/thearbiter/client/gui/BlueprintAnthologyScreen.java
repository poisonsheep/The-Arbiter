package io.github.poisonsheep.thearbiter.client.gui;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.poisonsheep.thearbiter.TheArbiter;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Predicate;

public class BlueprintAnthologyScreen extends BasicBookScreen {
    public static final ResourceLocation BLUEPRINT_ANTHOLOGY_TEXTURE = new ResourceLocation(TheArbiter.MODID,"textures/gui/blueprint_anthology_gui.png");
    public static final ResourceLocation BOOK_TEXTURES = new ResourceLocation(TheArbiter.MODID, "textures/gui/ui.png");
    int toolTipMaxWidth;
    private WidgetList widgets;
    public BlueprintAnthologyScreen(Component component) {
        super(component);
    }

    @Override
    protected void init() {
        super.init();
        this.toolTipMaxWidth = (IMAGE_WIDTH / 2) - 20;
        int buttonWidth = (IMAGE_WIDTH - 10) / 3;
        int buttonHeight = 20;
        Button blueprint = new Button(0, this.topPos, buttonWidth, buttonHeight, new TranslatableComponent(TheArbiter.MODID),
                button -> this.minecraft.setScreen(new BlueprintViewScreen(this)), makeButtonToolTip(new TranslatableComponent(""), this));
        int listRenderedHeight = IMAGE_HEIGHT + this.bottomPos;
        List<AbstractWidget> buttons = ImmutableList.of(blueprint);
        this.widgets = new WidgetList(buttons, buttonWidth + 9, listRenderedHeight + 20, this.bottomPos + 15, listRenderedHeight - 15, buttonHeight + 4);
        this.widgets.setLeftPos(this.leftPos + (IMAGE_WIDTH / 4) + buttonWidth);
        this.addWidget(this.widgets);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        super.render(poseStack, mouseX, mouseY, partialTick);
        renderLogo(poseStack);
        this.widgets.render(poseStack, mouseX, mouseY, partialTick);
    }

    private void renderLogo(PoseStack poseStack) {
        poseStack.pushPose();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.setShaderTexture(0, BOOK_TEXTURES);
        float scale = 1F;
        poseStack.scale(scale, scale, 0);
        float toolTipMaxWidthScaled = (this.toolTipMaxWidth) / scale;
        blit(poseStack, Math.round(this.leftPos + (IMAGE_WIDTH / 2 - toolTipMaxWidthScaled) / 2) + 30 , Math.round(this.bottomPos + 25), 0, 64, 64, 64);
        poseStack.popPose();
    }


    @NotNull
    public static Button.OnTooltip makeButtonToolTip(Component component, Screen screen) {
        return makeButtonToolTip(component, screen, button -> button.active);
    }
    @NotNull
    public static Button.OnTooltip makeButtonToolTip(Component component, Screen screen, Predicate<Button> buttonPredicate) {
        return (button, poseStack, mouseX, mouseZ) -> {
            if (buttonPredicate.test(button)) {
                screen.renderTooltip(poseStack, component, mouseX, mouseZ);
            }
        };
    }
}
