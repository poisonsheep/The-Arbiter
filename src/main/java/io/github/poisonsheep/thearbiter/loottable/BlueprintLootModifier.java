package io.github.poisonsheep.thearbiter.loottable;

import com.google.gson.JsonObject;
import io.github.poisonsheep.thearbiter.Item.ItemRegistry;
import io.github.poisonsheep.thearbiter.Item.Blueprint;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BlueprintLootModifier extends LootModifier {
    private final ResourceLocation id;
    public BlueprintLootModifier(LootItemCondition[] conditionsIn, ResourceLocation id) {
        super(conditionsIn);
        this.id = id;
    }

    @NotNull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> list, LootContext lootContext) {
        ItemStack stack = new ItemStack(ItemRegistry.BLUEPRINT.get());
        Blueprint.setBluePrint(stack, id);
        list.add(stack);
        return list;
    }
    public static class Serializer extends GlobalLootModifierSerializer<BlueprintLootModifier> {

        @Override
        public BlueprintLootModifier read(ResourceLocation name, JsonObject object, LootItemCondition[] conditions) {
            ResourceLocation id = new ResourceLocation(GsonHelper.getAsString(object,"blueprint"));
            return new BlueprintLootModifier(conditions, id);
        }

        @Override
        public JsonObject write(BlueprintLootModifier instance) {
            JsonObject json = makeConditions(instance.conditions);
            return json;
        }
    }
}
