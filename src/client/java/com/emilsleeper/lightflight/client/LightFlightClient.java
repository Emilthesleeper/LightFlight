package com.emilsleeper.lightflight.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.packet.c2s.play.UpdatePlayerAbilitiesC2SPacket;
import org.lwjgl.glfw.GLFW;

public class LightFlightClient implements ClientModInitializer {
    private static KeyBinding flightKey;
    private void updateAbilitiesWithDelay(ClientPlayerEntity player) {
        new Thread(() -> {
            try {
                Thread.sleep(41);
                player.sendAbilitiesUpdate();
                player.networkHandler.sendPacket(new UpdatePlayerAbilitiesC2SPacket(player.getAbilities()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void onInitializeClient() {
        flightKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "lightflight.keybind.flightKey",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_CAPS_LOCK,
                "lightflight.category"
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (flightKey.wasPressed()) {
                if (client.player != null ) {
                    if (client.isInSingleplayer()) {
                        if (client.player.getAbilities().flying) {
                            client.player.getAbilities().flying = false;
                            updateAbilitiesWithDelay(client.player);
                        } else {
                            if (client.player.isCreative() || client.player.hasPermissionLevel(4)) {
                                client.player.getAbilities().flying = true;
                                client.player.fallDistance = 0.0F;
                                updateAbilitiesWithDelay(client.player);
                            }
                        }
                    }
                }
            }
        });
    }
}
