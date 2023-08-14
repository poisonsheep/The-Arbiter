package io.github.poisonsheep.thearbiter.recipe;

import com.google.gson.JsonObject;
import io.github.poisonsheep.thearbiter.TheArbiter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public class BlueprintSerializer implements IConditionSerializer<BlueprintCondition> {
    public static final ResourceLocation id = new ResourceLocation(TheArbiter.MODID, "blueprint");
    public static final BlueprintSerializer INSTANCE = new BlueprintSerializer();
    @Override
    public void write(JsonObject json, BlueprintCondition value) {
        json.addProperty("blueprint", value.getBlueprint());
    }
    @Override
    public BlueprintCondition read(JsonObject json) {
        String blueprint = GsonHelper.getAsString(json, "blueprint");
        return new BlueprintCondition(blueprint, id);
    }

    @Override
    public ResourceLocation getID() {
        return id;
    }
}
