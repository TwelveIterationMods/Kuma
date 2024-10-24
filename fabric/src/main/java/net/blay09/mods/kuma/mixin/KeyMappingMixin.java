package net.blay09.mods.kuma.mixin;

import net.blay09.mods.kuma.fabric.FabricKeyMappingContexts;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KeyMapping.class)
public class KeyMappingMixin {
    @SuppressWarnings("UnreachableCode")
    @Inject(method = "same(Lnet/minecraft/client/KeyMapping;)Z", cancellable = true, at = @At("RETURN"))
    public void same(KeyMapping keyMapping, CallbackInfoReturnable<Boolean> callbackInfo) {
        KeyMapping thisKeyMapping = (KeyMapping) (Object) this;
        final var same = callbackInfo.getReturnValue();
        if (same && !FabricKeyMappingContexts.same(thisKeyMapping, keyMapping)) {
            // TODO Normally we would do this to prevent showing incorrect key conflicts,
            //      but this is currently misleading because even with differing context,
            //      another key mapping may take preference (Vanilla only ever triggers one key mapping).
            // callbackInfo.setReturnValue(false);
        }
    }
}
