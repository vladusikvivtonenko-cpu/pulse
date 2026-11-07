package com.ezvisual.modules;

import net.minecraft.client.MinecraftClient;

public class VisualModules {
    public static boolean noFire = true;
    public static boolean noShake = true;
    public static boolean ftHelper = true;
    public static boolean armorPercent = true;
    public static boolean itemHighlight = true; // показ картинки в HUD
    public static boolean tapeMouse = false;

    // 0 - Pulse Violet, 1 - Emerald Cyber, 2 - Crimson Blood
    public static int currentTheme = 0;
    public static int selectedAnimeGirl = 1;

    public static int getThemeColor() {
        switch (currentTheme) {
            case 1: return 0x00FF66;
            case 2: return 0xFF3333;
            default: return 0x7A5CFF;
        }
    }

    public static void onClientTick(MinecraftClient client) {
        if (client.player == null || client.currentScreen != null) return;
        if (tapeMouse) {
            client.options.attackKey.setPressed(true);
        }
    }
}
