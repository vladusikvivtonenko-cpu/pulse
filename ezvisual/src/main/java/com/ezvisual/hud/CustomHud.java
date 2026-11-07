package com.ezvisual.hud;

import com.ezvisual.modules.VisualModules;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class CustomHud implements HudRenderCallback {
    private static final Identifier GIRL_1 = Identifier.of("ezvisual", "textures/gui/anime_girl1.png");
    private static final Identifier GIRL_2 = Identifier.of("ezvisual", "textures/gui/anime_girl2.png");

    @Override
    public void onHudRender(DrawContext context, RenderTickCounter tickCounter) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.options.hudHidden) return;

        int fps = mc.getCurrentFps();
        long ping = 0;
        if (mc.getNetworkHandler() != null) {
            PlayerListEntry e = mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid());
            if (e != null) ping = e.getLatency();
        }

        int themeColor = VisualModules.getThemeColor();

        String wm = "ezvisals.pro  |  " + ping + " ms  |  " + fps + " FPS";
        context.fill(4, 4, mc.textRenderer.getWidth(wm) + 18, 18, 0xD00A0A0F);
        context.fill(4, 4, 6, 18, themeColor | 0xFF000000);
        context.drawText(mc.textRenderer, wm, 12, 7, 0xFFEFEFEF, false);

        if (VisualModules.ftHelper) {
            context.drawText(mc.textRenderer, "[FT Helper: ACTIVE]", 6, 24, themeColor | 0xFF000000, true);
        }

        int effectY = 36;
        for (StatusEffectInstance effect : mc.player.getStatusEffects()) {
            int sec = effect.getDuration() / 20;
            String name = effect.getEffectType().value().getName().getString();
            String text = name + " -> " + String.format("%02d:%02d", sec / 60, sec % 60);
            context.drawText(mc.textRenderer, text, 6, effectY, 0xFFFFAA00, true);
            effectY += 12;
        }

        if (VisualModules.itemHighlight) {
            int w = 80, h = 110;
            int x = context.getScaledWindowWidth() - w - 10;
            Identifier tex = VisualModules.selectedAnimeGirl == 2 ? GIRL_2 : GIRL_1;
            context.drawTexture(RenderLayer::getGuiTextured, tex, x, 40, 0f, 0f, w, h, w, h);
        }

        if (VisualModules.armorPercent) {
            EquipmentSlot[] slots = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
            int x = context.getScaledWindowWidth() / 2 + 110;
            int y = context.getScaledWindowHeight() - 22 - 18 * slots.length;
            for (EquipmentSlot slot : slots) {
                ItemStack stack = mc.player.getEquippedStack(slot);
                if (!stack.isEmpty()) {
                    int max = stack.getMaxDamage();
                    int percent = max > 0 ? ((max - stack.getDamage()) * 100) / max : 100;
                    int color = percent > 60 ? 0xFF55FF55 : percent > 25 ? 0xFFFFFF55 : 0xFFFF5555;
                    context.drawItem(stack, x, y);
                    context.drawText(mc.textRenderer, percent + "%", x + 20, y + 4, color, true);
                }
                y += 18;
            }
        }
    }
}
