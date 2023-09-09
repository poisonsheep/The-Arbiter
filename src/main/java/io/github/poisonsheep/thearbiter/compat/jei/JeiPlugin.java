package io.github.poisonsheep.thearbiter.compat.jei;

import io.github.poisonsheep.thearbiter.Item.ItemRegistry;
import io.github.poisonsheep.thearbiter.Item.blueprint.Blueprint;
import io.github.poisonsheep.thearbiter.Item.blueprint.BlueprintList;
import io.github.poisonsheep.thearbiter.TheArbiter;
import io.github.poisonsheep.thearbiter.recipe.BlueprintRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@mezz.jei.api.JeiPlugin
public class JeiPlugin implements IModPlugin {
    public static final ResourceLocation UID = new ResourceLocation(TheArbiter.MODID, "jei_plugin");
    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        for (String blueprint : BlueprintList.INSTANCE.blueprints) {
            ItemStack stack = new ItemStack(ItemRegistry.BLUEPRINT.get());
            ResourceLocation name = new ResourceLocation(blueprint);
            Blueprint.setBluePrint(stack, name);
            registration.addIngredientInfo(stack, VanillaTypes.ITEM_STACK, new TranslatableComponent(blueprint.replace(":", ".") + ".message"));
        }
    }

    @Override
    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
        registration.getCraftingCategory().addCategoryExtension(BlueprintRecipe.class, BlueprintRecipeExtension::new);
    }
}
