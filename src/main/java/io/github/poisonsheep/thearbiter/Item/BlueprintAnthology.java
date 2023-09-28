package io.github.poisonsheep.thearbiter.Item;

import io.github.poisonsheep.thearbiter.client.gui.BlueprintAnthologyScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BlueprintAnthology extends Item {
    public BlueprintAnthology() {
        super(new Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if(level.isClientSide) {
            Client.openScreen();
        }
        return super.use(level, player, hand);
    }
    private static class Client {
        public static void openScreen() {
            Minecraft.getInstance().setScreen(new BlueprintAnthologyScreen(new TextComponent("book")));
        }
    }
}
