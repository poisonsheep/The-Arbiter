package io.github.poisonsheep.thearbiter.Item.blueprint;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.github.poisonsheep.thearbiter.util.JsonUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ForgeModelBakery;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;

//这部分也看不太懂，如果看懂的话可以帮我打一些注释
public class BlueprintRegistry {
    public static void register(ModelRegistryEvent e) {
        System.out.println("bbbbbbbbbb");
        ResourceManager manager = Minecraft.getInstance().getResourceManager();
        Gson gson = new Gson();
        Collection<ResourceLocation> list = manager.listResources("recipes", s -> s.endsWith(".json"));
        System.out.println(list);
        for (ResourceLocation recipeName : manager.listResources("recipes", s -> s.endsWith(".json"))) {
            try {
                System.out.println("aaaaaaaaa");
                // 获取当前合成配方文件
                Resource resource = manager.getResource(recipeName);
                // 解析JSON内容为JsonObject
                JsonObject json = gson.fromJson(new InputStreamReader(resource.getInputStream()), JsonObject.class);
                if(json.has("blueprint")) {
                    System.out.println("find");
                    String blueprint = json.get("blueprint").getAsString();
                    System.out.println(blueprint);
                    BlueprintList.INSTANCE.blueprints.add(blueprint);
                }
            }catch (IOException exception) {
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