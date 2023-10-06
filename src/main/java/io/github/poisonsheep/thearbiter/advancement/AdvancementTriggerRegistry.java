package io.github.poisonsheep.thearbiter.advancement;

import io.github.poisonsheep.thearbiter.TheArbiter;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.resources.ResourceLocation;

public class AdvancementTriggerRegistry {
    public static AdvancementTrigger TRANSFORM = new AdvancementTrigger(new ResourceLocation(TheArbiter.MODID, "transform"));
    public static AdvancementTrigger ENTHRONED = new AdvancementTrigger(new ResourceLocation(TheArbiter.MODID, "enthroned"));
    public static AdvancementTrigger FIRST_READ = new AdvancementTrigger(new ResourceLocation(TheArbiter.MODID, "first_read"));

    public static void register() {
        CriteriaTriggers.register(TRANSFORM);
        CriteriaTriggers.register(ENTHRONED);
        CriteriaTriggers.register(FIRST_READ);
    }
}
