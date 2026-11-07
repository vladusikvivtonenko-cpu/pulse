package com.ezvisual.mixin;

import com.ezvisual.modules.VisualModules;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(method = "bobView", at = @At("HEAD"), cancellable = true)
    private void ezvisual$noShake(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (VisualModules.noShake) ci.cancel();
    }
}
