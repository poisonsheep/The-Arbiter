package io.github.poisonsheep.thearbiter.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.poisonsheep.thearbiter.capability.PlayerBlueprint;
import io.github.poisonsheep.thearbiter.capability.PlayerBlueprintProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class BlueprintWidget extends ItemWidget{
    private final String blueprint;
    private Player player = Minecraft.getInstance().player;

    public BlueprintWidget(ItemStack stack, ItemRenderer itemRenderer, String blueprint,int pX, int pY, int pWidth, int pHeight, OnClick onClick) {
        super(stack, itemRenderer, pX, pY, pWidth, pHeight, onClick);
        this.blueprint = blueprint;
    }

    @Override
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        super.renderButton(poseStack, mouseX, mouseY, partialTick);
        if(Screen.hasShiftDown()) {
            if(player.getCapability(PlayerBlueprintProvider.PLAYER_BLUEPRINT_CAPABILITY).isPresent()) {
                PlayerBlueprint playerBlueprint = player.getCapability(PlayerBlueprintProvider.PLAYER_BLUEPRINT_CAPABILITY).orElseThrow(() -> new RuntimeException("Player does not have PlayerBlueprint capability"));
                List<String> blueprints = playerBlueprint.getBlueprints();
                if(blueprints.contains(blueprint)) {
                    poseStack.pushPose();
                    poseStack.scale(1, 1, 0);
                    RenderSystem.setShader(GameRenderer::getPositionTexShader);
                    RenderSystem.setShaderColor(1, 1, 1, 1);
                    RenderSystem.setShaderTexture(0, BlueprintAnthologyScreen.BOOK_TEXTURES);
                    blit(poseStack, this.x -1 , this.y -3, 0,  236, 18, 20);
                    poseStack.popPose();
                }
            }
        }
    }
}
