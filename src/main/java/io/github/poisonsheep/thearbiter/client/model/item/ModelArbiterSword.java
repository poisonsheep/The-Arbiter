package io.github.poisonsheep.thearbiter.client.model.item;


import io.github.poisonsheep.thearbiter.Item.ArbiterSword;
import io.github.poisonsheep.thearbiter.TheArbiter;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelArbiterSword extends AnimatedGeoModel<ArbiterSword> {
    @Override
    public ResourceLocation getModelLocation(ArbiterSword object) {
        return new ResourceLocation(TheArbiter.MODID,"geo/arbiter_sword.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ArbiterSword object) {
        return new ResourceLocation(TheArbiter.MODID,"textures/item/arbiter_sword.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ArbiterSword animatable) {
        return new ResourceLocation(TheArbiter.MODID,"animations/arbiter_sword.animation.json");
    }
}
