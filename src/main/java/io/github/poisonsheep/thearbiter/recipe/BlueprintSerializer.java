package io.github.poisonsheep.thearbiter.recipe;

import com.google.gson.JsonObject;
import io.github.poisonsheep.thearbiter.client.gui.RecipeData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.Nullable;

public class BlueprintSerializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<BlueprintRecipe> {
    @Override
    public BlueprintRecipe fromJson(ResourceLocation id, JsonObject json) {
        String blueprint = GsonHelper.getAsString(json, "blueprint");
        Recipe<?> recipe = RecipeManager.fromJson(id, GsonHelper.getAsJsonObject(json, "recipe"));
        //这部分用于指引书的配方gui渲染
        RecipeData data = new RecipeData(blueprint, recipe);
        RecipeDataList.INSTANCE.recipeData.add(data);
        return new BlueprintRecipe(id, blueprint, (CraftingRecipe) recipe);
    }

    //这部分代码用于网络数据包
    @Nullable
    @Override
    public BlueprintRecipe fromNetwork(ResourceLocation blueprintId, FriendlyByteBuf buffer) {
        //顺序非常重要
        ResourceLocation innerRecipeId = buffer.readResourceLocation();
        ResourceLocation recipeSerializerId = buffer.readResourceLocation();
        RecipeSerializer<?> value = ForgeRegistries.RECIPE_SERIALIZERS.getValue(recipeSerializerId);
        Recipe<?> recipe = value.fromNetwork(innerRecipeId, buffer);
        String blueprint = buffer.readUtf();
        RecipeData data = new RecipeData(blueprint, recipe);
        if(!RecipeDataList.INSTANCE.recipeData.contains(data)) {
            RecipeDataList.INSTANCE.recipeData.add(data);
        }
        return new BlueprintRecipe(blueprintId, blueprint, (CraftingRecipe) recipe);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, BlueprintRecipe blueprintRecipe) {

        Recipe<?> recipe = blueprintRecipe.getRecipe();
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
