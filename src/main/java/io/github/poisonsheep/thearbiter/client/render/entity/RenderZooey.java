package io.github.poisonsheep.thearbiter.client.render.entity;

import io.github.poisonsheep.thearbiter.TheArbiter;
import io.github.poisonsheep.thearbiter.client.model.entity.ModelZooey;
import io.github.poisonsheep.thearbiter.entity.Zooey;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RenderZooey extends GeoEntityRenderer<Zooey> {

    public RenderZooey(EntityRendererProvider.Context context) {
        super(context, new ModelZooey());
        this.shadowRadius = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(Zooey object) {
        //return LOCATION_BY_VARIANT.get(instance.getVariant());
        return new ResourceLocation(TheArbiter.MODID, "textures/entity/zooey.png");
    }

}