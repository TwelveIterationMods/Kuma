package net.blay09.mods.kuma.mixin;

import net.blay09.mods.kuma.fabric.FabricKeyMappingContexts;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Options.class)
public class OptionsMixin {
    @Inject(method = "load", at = @At("RETURN"))
    public void load(CallbackInfo callbackInfo) {
        FabricKeyMappingContexts.initVanilla((Options) (Object) this);
    }
}
