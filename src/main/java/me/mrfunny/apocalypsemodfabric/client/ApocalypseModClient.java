package me.mrfunny.apocalypsemodfabric.client;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class ApocalypseModClient implements ClientModInitializer {

    public static int scopeMode = 0;
    private double lastFov = 0;

    @Override
    public void onInitializeClient() {
        ClientPlayConnectionEvents.JOIN.register((event, packetSender, client) -> {
            lastFov = client.options.fov;
            PacketByteBuf packetByteBuf = PacketByteBufs.create();
            StringBuilder mods = new StringBuilder();
            for(ModContainer mod : FabricLoader.getInstance().getAllMods()) {
                mods.append(mod.getMetadata().getId()).append(",");
            }
            mods.append("NULL");
            packetByteBuf.writeString(mods.toString());
            ClientPlayNetworking.send(new Identifier("apocalypse", "auth"), packetByteBuf);
        });
        ClientPlayConnectionEvents.DISCONNECT.register((event, client) -> {
            scopeMode = 0;
            client.options.fov = lastFov;
        });
        ClientPlayNetworking.registerGlobalReceiver(new Identifier("apocalypse", "play"), (client, handler, buf, responseSender) -> {
            byte[] data = new byte[buf.readableBytes()];
            buf.duplicate().readBytes(data);
            ByteArrayDataInput in = ByteStreams.newDataInput(data);
            int packet = in.readInt();
            if (packet == 0){
                int mode = in.readInt();
                System.out.println(mode);
                if(mode == 0 && scopeMode == 1){
                    client.options.fov = lastFov;
                    ApocalypseModClient.scopeMode = 0;
                } else if(mode == 1 && scopeMode == 0){
                    lastFov = client.options.fov;
                    client.options.fov = 25d;
                    ApocalypseModClient.scopeMode = 1;
                }
            }
        });
    }
}
