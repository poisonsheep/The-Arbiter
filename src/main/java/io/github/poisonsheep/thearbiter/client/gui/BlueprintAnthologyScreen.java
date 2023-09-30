package io.github.poisonsheep.thearbiter.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.poisonsheep.thearbiter.TheArbiter;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class BlueprintAnthologyScreen extends BasicBookScreen {
    public static final ResourceLocation BLUEPRINT_ANTHOLOGY_TEXTURE = new ResourceLocation(TheArbiter.MODID,"textures/gui/blueprint_anthology_gui.png");
    public static final ResourceLocation BOOK_TEXTURES = new ResourceLocation(TheArbiter.MODID, "textures/gui/ui.png");
    TranslatableComponent title  = new TranslatableComponent("the_arbiter.title");
    TranslatableComponent author  = new TranslatableComponent("the_arbiter.author");
    TranslatableComponent textComponent = new TranslatableComponent("the_arbiter.intro");
    TranslatableComponent preface  = new TranslatableComponent("the_arbiter.title.preface");

    TranslatableComponent viewBlueprint  = new TranslatableComponent("the_arbiter.tip.viewBlueprint");
    private ScrollableText scrollableText;
    int toolTipMaxWidth;
    public BlueprintAnthologyScreen(Component component) {
        super(component);
    }

    @Override
    protected void init() {
        super.init();
        this.toolTipMaxWidth = (IMAGE_WIDTH / 2) - 20;
        int textStartHeight = this.bottomPos + 56;
        int y1 = this.topPos - 36;
        this.scrollableText = new ScrollableText(textComponent, (this.IMAGE_WIDTH / 2) - 46, y1 - textStartHeight -24, textStartHeight, y1);
        this.scrollableText.setLeftPos(this.leftPos + this.IMAGE_WIDTH / 2 + 18);
        this.addRenderableWidget(scrollableText);
        PageButton pageForward = new PageButton(this.rightPos - 42, this.topPos - 36, true, button -> {
            this.minecraft.setScreen(new BlueprintViewScreen(this));
        }, true);
        this.addRenderableWidget(pageForward);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        super.render(poseStack, mouseX, mouseY, partialTick);
        renderLogo(poseStack);
        writeTitle(poseStack, preface, this.leftPos + Math.round(this.IMAGE_WIDTH * 3 / 4) - 14, this.bottomPos + 34, 2F);
        writeTitle(poseStack, title,this.leftPos + 30, this.bottomPos + this.IMAGE_HEIGHT / 2 - 4, 0.9F);
        writeTitle(poseStack, author,this.leftPos + 56, this.bottomPos + this.IMAGE_HEIGHT / 2 + 4, 0.6F);
    }

    protected void renderLogo(PoseStack poseStack) {
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
    protected void writeTitle(PoseStack matrixStack, TranslatableComponent component, int x, int y, float scale) {
        Font font = this.font;
        matrixStack.pushPose();
        matrixStack.translate(x, y, 0);
        matrixStack.scale(scale, scale, scale);
        font.draw(matrixStack, component, 0, 0, 000000);
        matrixStack.popPose();
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
