package io.github.poisonsheep.thearbiter.event.blueprint;

import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LearnEvent {
    @SubscribeEvent()
    public void playerDeath(ReadEvent event) {
        event.getPlayer().die(null);
    }
}
