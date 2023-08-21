package io.github.poisonsheep.thearbiter.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.crafting.*;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.Nullable;

public class BlueprintSerializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<BlueprintRecipe> {
    @Override
    public BlueprintRecipe fromJson(ResourceLocation id, JsonObject json) {
        String blueprint = GsonHelper.getAsString(json, "blueprint");
        Recipe<?> recipe = RecipeManager.fromJson(id, GsonHelper.getAsJsonObject(json, "recipe"));
        return new BlueprintRecipe(id, blueprint, (CraftingRecipe) recipe);
    }

    //这部分代码用于网络数据包
    @Nullable
    @Override
    public BlueprintRecipe fromNetwork(ResourceLocation blueprintId, FriendlyByteBuf buffer) {
        ResourceLocation id = buffer.readResourceLocation();
        String blueprint = buffer.readUtf();
        RecipeSerializer<?> value = ForgeRegistries.RECIPE_SERIALIZERS.getValue(id);
        Recipe<?> recipe = value.fromNetwork(id, buffer);
        return new BlueprintRecipe(blueprintId, blueprint, (CraftingRecipe) recipe);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, BlueprintRecipe blueprintRecipe) {
        Recipe<CraftingContainer> recipe = blueprintRecipe.getRecipe();
        if(recipe.getSerializer().getRegistryName() == null) {
            throw new IllegalArgumentException("Unable to serialize a recipe serializer without an id: " + recipe.getSerializer());
        }
        buffer.writeResourceLocation(recipe.getId());
        buffer.writeResourceLocation(recipe.getSerializer().getRegistryName());
        recipe.getSerializer().toNetwork(buffer, cast(recipe));
        buffer.writeUtf(blueprintRecipe.getBlueprint());
    }
    public static <T> T cast(Object o) {
        return (T) o;
    }
}
