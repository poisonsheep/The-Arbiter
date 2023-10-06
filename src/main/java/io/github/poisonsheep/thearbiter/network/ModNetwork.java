package io.github.poisonsheep.thearbiter.network;

import io.github.poisonsheep.thearbiter.TheArbiter;
import io.github.poisonsheep.thearbiter.network.packet.BlueprintUpdatePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public class ModNetwork {
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(TheArbiter.MODID, "main"),
            () -> TheArbiter.VERSION,
            TheArbiter.VERSION::equals,
            TheArbiter.VERSION::equals
    );
    private static  int PROTOCOL_VERSION = 0;

    private static int nextId() {
        return PROTOCOL_VERSION++;
    }

    public static void register() {
        INSTANCE.registerMessage(nextId(), BlueprintUpdatePacket.class, BlueprintUpdatePacket::write, BlueprintUpdatePacket::new, BlueprintUpdatePacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }
}
