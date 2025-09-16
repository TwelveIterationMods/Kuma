package net.blay09.mods.kuma.mixin;

import net.blay09.mods.kuma.ManagedKeyMappingRegistry;
import net.blay09.mods.kuma.api.WorldInputEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.input.MouseButtonInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {

    @Final
    @Shadow
    private Minecraft minecraft;

    @Inject(method = "onButton(JLnet/minecraft/client/input/MouseButtonInfo;I)V", at = @At("HEAD"), cancellable = true)
    public void keyPress(long window, MouseButtonInfo button, int press, CallbackInfo callbackInfo) {
        if (window == minecraft.getWindow().handle() && minecraft.screen == null && press == 1) {
            for (final var keyMapping : ManagedKeyMappingRegistry.getKeyMappings()) {
                if (keyMapping.isActiveAndMatchesMouse(button)) {
                    keyMapping.handleWorldInput(new WorldInputEvent(keyMapping));
                    callbackInfo.cancel();
                    return;
                }
            }
        }
    }

}
