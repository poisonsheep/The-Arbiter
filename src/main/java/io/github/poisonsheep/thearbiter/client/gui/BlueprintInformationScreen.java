package io.github.poisonsheep.thearbiter.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.poisonsheep.thearbiter.Item.Blueprint;
import io.github.poisonsheep.thearbiter.Item.ItemRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class BlueprintInformationScreen extends BasicBookScreen{

    private String blueprint;
    private final Screen parent;
    int toolTipMaxWidth;

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

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }
}
