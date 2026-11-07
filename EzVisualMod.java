package com.ezvisual;

import com.ezvisual.gui.ClickGuiScreen;
import com.ezvisual.hud.CustomHud;
import com.ezvisual.modules.VisualModules;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class EzVisualMod implements ClientModInitializer {
    public static final String MOD_ID = "ezvisual";
    public static KeyBinding openGuiKey;

    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register(new CustomHud());

        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.ezvisual.opengui", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, "category.ezvisual"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            VisualModules.onClientTick(client);
            while (openGuiKey.wasPressed()) {
                if (client.player != null) client.setScreen(new ClickGuiScreen());
            }
        });
    }
}
