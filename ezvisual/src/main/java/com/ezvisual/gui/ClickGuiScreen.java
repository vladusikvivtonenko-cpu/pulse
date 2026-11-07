package com.ezvisual.gui;

import com.ezvisual.modules.VisualModules;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class ClickGuiScreen extends Screen {
    private static final Identifier GIRL_1 = Identifier.of("ezvisual", "textures/gui/anime_girl1.png");
    private static final Identifier GIRL_2 = Identifier.of("ezvisual", "textures/gui/anime_girl2.png");

    private static final int WIN_W = 400, WIN_H = 230;
    private int activeTab = 0; // 0 Visuals, 1 HUD, 2 Utilities

    public ClickGuiScreen() { super(Text.of("EzVisual")); }

    private static String onOff(boolean b) { return b ? "ON" : "OFF"; }

    private void toggle(String label, int x, int y, BooleanSupplier get, Consumer<Boolean> set) {
        addDrawableChild(ButtonWidget.builder(Text.of(label + ": " + onOff(get.getAsBoolean())), b -> {
            set.accept(!get.getAsBoolean());
            b.setMessage(Text.of(label + ": " + onOff(get.getAsBoolean())));
        }).dimensions(x, y, 160, 22).build());
    }

    @Override
    protected void init() {
        this.clearChildren();
        int winX = (this.width - WIN_W) / 2;
        int winY = (this.height - WIN_H) / 2;
        int tabX = winX + 120, tabY = winY + 20;

        addDrawableChild(ButtonWidget.builder(Text.of("Visuals"), b -> { activeTab = 0; init(); }).dimensions(tabX, tabY, 60, 18).build());
        addDrawableChild(ButtonWidget.builder(Text.of("HUD"), b -> { activeTab = 1; init(); }).dimensions(tabX + 64, tabY, 45, 18).build());
        addDrawableChild(ButtonWidget.builder(Text.of("Utilities"), b -> { activeTab = 2; init(); }).dimensions(tabX + 113, tabY, 60, 18).build());

        int x1 = winX + 120, y = winY + 60;
        if (activeTab == 0) {
            toggle("NoFire", x1, y, () -> VisualModules.noFire, v -> VisualModules.noFire = v);
            toggle("NoShake", x1, y + 30, () -> VisualModules.noShake, v -> VisualModules.noShake = v);
        } else if (activeTab == 1) {
            toggle("FT Helper", x1, y, () -> VisualModules.ftHelper, v -> VisualModules.ftHelper = v);
            toggle("Armor %", x1, y + 30, () -> VisualModules.armorPercent, v -> VisualModules.armorPercent = v);
            toggle("Picture", x1, y + 60, () -> VisualModules.itemHighlight, v -> VisualModules.itemHighlight = v);
            addDrawableChild(ButtonWidget.builder(Text.of("Picture №" + VisualModules.selectedAnimeGirl), b -> {
                VisualModules.selectedAnimeGirl = VisualModules.selectedAnimeGirl == 1 ? 2 : 1;
                b.setMessage(Text.of("Picture №" + VisualModules.selectedAnimeGirl));
            }).dimensions(x1, y + 90, 160, 22).build());
        } else {
            toggle("Tape Mouse", x1, y, () -> VisualModules.tapeMouse, v -> VisualModules.tapeMouse = v);
            String[] names = {"Pulse Violet", "Emerald Cyber", "Crimson Blood"};
            addDrawableChild(ButtonWidget.builder(Text.of("Theme: " + names[VisualModules.currentTheme]), b -> {
                VisualModules.currentTheme = (VisualModules.currentTheme + 1) % 3;
                b.setMessage(Text.of("Theme: " + names[VisualModules.currentTheme]));
            }).dimensions(x1, y + 30, 160, 22).build());
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int theme = VisualModules.getThemeColor();
        context.fillGradient(0, 0, this.width, this.height, 0xC00A0A0F, 0xD012121A);

        int winX = (this.width - WIN_W) / 2;
        int winY = (this.height - WIN_H) / 2;
        context.fill(winX, winY, winX + WIN_W, winY + WIN_H, 0xFA14141E);
        context.fill(winX, winY, winX + 110, winY + WIN_H, 0xFA0D0D14);
        context.fill(winX + 115, winY + 45, winX + WIN_W - 10, winY + 46, theme | 0x80000000);

        context.drawText(textRenderer, "Main", winX + 15, winY + 25, theme | 0xFF000000, false);
        context.drawText(textRenderer, "Combat", winX + 15, winY + 50, 0xBBFFFFFF, false);
        context.drawText(textRenderer, "Defense", winX + 15, winY + 75, 0xBBFFFFFF, false);

        Identifier tex = VisualModules.selectedAnimeGirl == 2 ? GIRL_2 : GIRL_1;
        context.drawTexture(RenderLayer::getGuiTextured, tex, winX + 15, winY + WIN_H - 95, 0f, 0f, 75, 90, 75, 90);

        super.render(context, mouseX, mouseY, delta);
    }
}
