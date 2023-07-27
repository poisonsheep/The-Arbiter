package io.github.poisonsheep.thearbiter.client.model.entity;

import io.github.poisonsheep.thearbiter.TheArbiter;
import io.github.poisonsheep.thearbiter.entity.Zooey;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelZooey extends AnimatedGeoModel<Zooey> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(TheArbiter.MODID, "zooey"), "main");

    @Override
    public ResourceLocation getModelLocation(Zooey object) {
        return new ResourceLocation(TheArbiter.MODID, "geo/zooey.geo.json");
    }

    //我们生物皮肤材质的路径
    @Override
    public ResourceLocation getTextureLocation(Zooey object) {
        //return RenderDund.LOCATION_BY_VARIANT.get(object.getVariant());
        return new ResourceLocation(TheArbiter.MODID, "textures/entity/zooey.png");
    }

    //我们生物皮肤动画的路径
    @Override
    public ResourceLocation getAnimationFileLocation(Zooey animatable) {
        return new ResourceLocation(TheArbiter.MODID, "animations/zooey.animation.json");
    }

}
