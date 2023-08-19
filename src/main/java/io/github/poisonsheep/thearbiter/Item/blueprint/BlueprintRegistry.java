package io.github.poisonsheep.thearbiter.Item.blueprint;

import io.github.poisonsheep.thearbiter.util.JsonUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ForgeModelBakery;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

//这部分也看不太懂，如果看懂的话可以帮我打一些注释
public class BlueprintRegistry {
    public static void register(ModelRegistryEvent event) {
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