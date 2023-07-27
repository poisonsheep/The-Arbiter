package io.github.poisonsheep.thearbiter.client.render.item;


import io.github.poisonsheep.thearbiter.Item.ArbiterSword;
import io.github.poisonsheep.thearbiter.client.model.item.ModelArbiterSword;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderArbiterSword extends GeoItemRenderer<ArbiterSword> {

    public RenderArbiterSword() {
        super(new ModelArbiterSword());
    }
}
