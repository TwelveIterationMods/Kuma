package net.blay09.mods.kuma.mixin;

import net.blay09.mods.kuma.ManagedKeyMappingRegistry;
import net.blay09.mods.kuma.api.ScreenInputEvent;
import net.blay09.mods.kuma.api.WorldInputEvent;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class KeyboardHandlerMixin {

    @Final
    @Shadow
    private Minecraft minecraft;

    @Inject(method = "keyPress(JIIII)V", at = @At("HEAD"), cancellable = true)
    public void keyPress(long window, int key, int scanCode, int action, int modifiers, CallbackInfo callbackInfo) {
        if (window == minecraft.getWindow().getWindow() && minecraft.screen == null) {
            for (final var keyMapping : ManagedKeyMappingRegistry.getKeyMappings()) {
                if (keyMapping.isActiveAndMatchesKey(key, scanCode, modifiers)) {
                    keyMapping.handleWorldInput(new WorldInputEvent());
                    callbackInfo.cancel();
                    return;
                }
            }
        }
    }

}
