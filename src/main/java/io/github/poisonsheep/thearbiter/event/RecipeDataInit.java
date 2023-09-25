package io.github.poisonsheep.thearbiter.event;

import com.google.gson.*;
import io.github.poisonsheep.thearbiter.client.gui.RecipeData;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeDataInit {
    private List<RecipeData> recipes = new ArrayList<>();

    public RecipeDataInit(List<RecipeData> recipes) {
        this.recipes = recipes;
    }
    public List<RecipeData> getRecipes() {
        return recipes;
    }
    public static class Deserializer implements JsonDeserializer<RecipeDataInit> {

        @Override
        public RecipeDataInit deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonobject = GsonHelper.convertToJsonObject(json, "book page");
            RecipeData[] recipesRead = GsonHelper.getAsObject(jsonobject, "recipes", new RecipeData[0], context, RecipeData[].class);
            RecipeDataInit recipeDataInit = new RecipeDataInit(Arrays.asList(recipesRead));
            return recipeDataInit;
        }
    }
}
