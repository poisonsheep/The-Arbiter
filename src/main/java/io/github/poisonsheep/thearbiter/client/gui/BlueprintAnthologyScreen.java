package io.github.poisonsheep.thearbiter.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.poisonsheep.thearbiter.TheArbiter;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class BlueprintAnthologyScreen extends BasicBookScreen {

    public static final ResourceLocation BLUEPRINT_ANTHOLOGY_TEXTURE = new ResourceLocation(TheArbiter.MODID,"textures/gui/blueprint_anthology_gui.png");
    public static final ResourceLocation BOOK_TEXTURES = new ResourceLocation(TheArbiter.MODID, "textures/gui/biomepedia.png");
    int toolTipMaxWidth;
    public BlueprintAnthologyScreen(Component component) {
        super(component);
    }

    protected void init() {
        super.init();
        this.toolTipMaxWidth = (IMAGE_WIDTH / 2) - 20;
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        super.render(poseStack, mouseX, mouseY, partialTick);
        renderLogo(poseStack);
    }

    private void renderLogo(PoseStack poseStack) {
        poseStack.pushPose();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.setShaderTexture(0, BOOK_TEXTURES);
        float scale = 3.35F;
        poseStack.scale(scale, scale, 0);

        // We need to scale the position by the scale of the matrix to match the coordinates.
        float toolTipMaxWidthScaled = (this.toolTipMaxWidth) / scale;
        blit(poseStack, Math.round((this.leftPos + (toolTipMaxWidthScaled / 2)) / scale) + 1, Math.round((this.bottomPos / scale) + (4 / scale)), 20, 224, 32, 32);
        poseStack.popPose();
    }
}
