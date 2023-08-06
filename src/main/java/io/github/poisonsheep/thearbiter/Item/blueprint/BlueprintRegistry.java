package io.github.poisonsheep.thearbiter.Item.blueprint;

import io.github.poisonsheep.thearbiter.util.JsonUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ForgeModelBakery;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//这部分也看不太懂，如果看懂的话可以帮我打一些注释
public class BlueprintRegistry {
    private static final Map<ResourceLocation, BakedModel> MODEL_MAP = new HashMap<>();

    public static BakedModel get(ResourceLocation resourceLocation) {
        return MODEL_MAP.get(resourceLocation);
    }

    static ResourceLocation from(ModelResourceLocation modelResourceLocation) {
        return new ResourceLocation(modelResourceLocation.getNamespace(), modelResourceLocation.getPath().split("blueprint/")[1]);
    }
    public static void register(ModelRegistryEvent e) {
        ResourceManager manager = Minecraft.getInstance().getResourceManager();
        for (String namespace : manager.getNamespaces()) {
            try {
                ResourceLocation resourceName = new ResourceLocation(namespace, "blueprint/list.json");
                if (manager.hasResource(resourceName)) {
                    List<Resource> resources = manager.getResources(resourceName);
                    for (Resource resource : resources) {
                        InputStreamReader reader = new InputStreamReader(resource.getInputStream());
                        BlueprintList list = JsonUtil.INSTANCE.noExpose.fromJson(reader, BlueprintList.class);
                        BlueprintList.INSTANCE.blueprints.addAll(list.blueprints);
                    }
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        for (var info : BlueprintList.INSTANCE.blueprints) {
            ForgeModelBakery.addSpecialModel(to(new ResourceLocation(info)));
        }
    }
    public static ResourceLocation to(ResourceLocation resourceLocation) {
        return new ResourceLocation(resourceLocation.getNamespace(), "blueprint/" + resourceLocation.getPath());
    }
}