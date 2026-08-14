package com.example.zombieevolution;

import com.example.zombieevolution.entity.client.ModEntityRenderers;
import com.example.zombieevolution.network.ModPackets;
import com.example.zombieevolution.screen.DaySetterScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;

public class ZombieEvolutionClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModEntityRenderers.register();

        ClientPlayNetworking.registerGlobalReceiver(ModPackets.OPEN_DAY_SETTER_S2C_ID, (client, handler, buf, responseSender) -> {
            int day = buf.readInt();
            client.execute(() -> {
                Minecraft.getInstance().setScreen(new DaySetterScreen(day));
            });
        });
    }
}
