package io.github.poisonsheep.thearbiter.network.packet;

import io.github.poisonsheep.thearbiter.capability.PlayerBlueprintProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class BlueprintUpdatePacket {
    private List<String> blueprints; // 玩家的蓝图数据

    public BlueprintUpdatePacket(List<String> blueprints) {
        this.blueprints = blueprints;
    }

    public BlueprintUpdatePacket(FriendlyByteBuf buf) {
        this.blueprints = new ArrayList<>();
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            this.blueprints.add(buf.readUtf());
        }
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.blueprints.size());
        for (String blueprint : this.blueprints) {
            buf.writeUtf(blueprint);
        }
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        supplier.get().setPacketHandled(true);
        var context = supplier.get();
        if(context.getDirection().getReceptionSide().isClient()) {
            context.enqueueWork(() -> {
                // 获取目标玩家
                Player player = Minecraft.getInstance().player;
                Objects.requireNonNull(player).getCapability(PlayerBlueprintProvider.PLAYER_BLUEPRINT_CAPABILITY).ifPresent(c -> c.setBlueprints(blueprints));
            });
        }
    }
}

