package net.blay09.mods.kuma.fabric.mixin;

import net.blay09.mods.kuma.ManagedKeyMappingRegistry;
import net.blay09.mods.kuma.api.WorldInputEvent;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.KeyEvent;
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

    @Inject(method = "keyPress(JILnet/minecraft/client/input/KeyEvent;)V", at = @At("HEAD"), cancellable = true)
    public void keyPress(long window, int action, KeyEvent event, CallbackInfo callbackInfo) {
        if (window == minecraft.getWindow().handle() && minecraft.screen == null) {
            for (final var keyMapping : ManagedKeyMappingRegistry.getKeyMappings()) {
                if (keyMapping.wasDown() && !keyMapping.isKeyRepeatEnabled()) {
                    continue;
                }

                if (keyMapping.isActiveAndMatchesKey(event)) {
                    keyMapping.handleWorldInput(new WorldInputEvent(event, keyMapping));
                    callbackInfo.cancel();
                    return;
                }
            }
        }
    }

}
