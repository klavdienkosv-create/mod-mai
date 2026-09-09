package net.example.mod.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.example.mod.client.gui.NetworkTestScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ExampleModClient implements ClientModInitializer {
    private static KeyBinding openGuiKey;

    @Override
    public void onInitializeClient() {
        // Карта клавиши GLFW_KEY_SEMICOLON — это физическая кнопка Ж / ;
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.vulnerability_test.open",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_SEMICOLON, 
                "category.vulnerability_test"
        ));

        // Каждый игровой тик проверяем нажатие кнопки
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGuiKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new NetworkTestScreen());
                }
            }
        });
    }
}
