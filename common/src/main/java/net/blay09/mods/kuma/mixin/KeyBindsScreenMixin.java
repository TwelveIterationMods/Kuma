package net.blay09.mods.kuma.mixin;

import net.blay09.mods.kuma.api.KumaKeyMapping;
import net.blay09.mods.kuma.screen.KeyBindsScreenHooks;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KeyBindsScreen.class)
public class KeyBindsScreenMixin {

    @Shadow
    public @Nullable KeyMapping selectedKey;

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    public void mouseClicked(MouseButtonEvent event, boolean b, CallbackInfoReturnable<Boolean> cir) {
        if (selectedKey instanceof KumaKeyMapping kumaKeyMapping && kumaKeyMapping.kuma$isManaged()) {
            KeyBindsScreenHooks.consumeNextMouse();
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    public void keyPressed(KeyEvent event, CallbackInfoReturnable<Boolean> cir) {
        if (selectedKey instanceof KumaKeyMapping kumaKeyMapping && kumaKeyMapping.kuma$isManaged()) {
            KeyBindsScreenHooks.consumeNextKey(event);
            cir.setReturnValue(true);
        }
    }
}
