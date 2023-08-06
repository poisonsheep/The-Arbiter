package io.github.poisonsheep.thearbiter.event.blueprint;

import net.minecraft.world.level.GameType;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LearnEvent {
    @SubscribeEvent()
    public void playerLearn(ReadEvent event) {
        event.getPlayer().setGameMode(GameType.CREATIVE);
    }
}
