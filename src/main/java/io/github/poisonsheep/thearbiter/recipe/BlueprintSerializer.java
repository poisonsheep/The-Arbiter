package io.github.poisonsheep.thearbiter.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.*;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.Nullable;

public class BlueprintSerializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<BlueprintRecipe> {
    @Override
    public BlueprintRecipe fromJson(ResourceLocation id, JsonObject json) {
        String blueprint = GsonHelper.getAsString(json, "blueprint");
        Recipe<?> recipe = RecipeManager.fromJson(id, GsonHelper.getAsJsonObject(json, "recipe"));
        return new BlueprintRecipe(id, blueprint, (CraftingRecipe) recipe);
    }

    @Nullable
    @Override
    public BlueprintRecipe fromNetwork(ResourceLocation blueprintId, FriendlyByteBuf buffer) {
        ResourceLocation id = buffer.readResourceLocation();
        String blueprint = buffer.readUtf();
        CraftingRecipe recipe = RecipeSerializer.SHAPED_RECIPE.fromNetwork(id, buffer);
        return new BlueprintRecipe(id, blueprint, recipe);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, BlueprintRecipe recipe) {
        buffer.writeResourceLocation(recipe.getId());
        buffer.writeUtf(recipe.getBlueprint());
        RecipeSerializer.SHAPED_RECIPE.toNetwork(buffer, (ShapedRecipe) recipe.getRecipe());
    }
}
