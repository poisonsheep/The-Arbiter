package io.github.poisonsheep.thearbiter.jei;

import io.github.poisonsheep.thearbiter.TheArbiter;
import io.github.poisonsheep.thearbiter.recipe.BlueprintRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import net.minecraft.resources.ResourceLocation;

@mezz.jei.api.JeiPlugin
public class JeiPlugin implements IModPlugin {

    public static final ResourceLocation UID = new ResourceLocation(TheArbiter.MODID, "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
        registration.getCraftingCategory().addCategoryExtension(BlueprintRecipe.class, BlueprintRecipeExtension::new);
    }
}
