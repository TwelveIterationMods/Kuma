package net.blay09.mods.kuma.mixin;

import net.blay09.mods.kuma.api.KumaKeyMapping;
import net.blay09.mods.kuma.screen.KeyBindsListHooks;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.options.controls.KeyBindsList;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyBindsList.KeyEntry.class)
public class KeyEntryMixin {

    @Shadow
    @Final
    private KeyMapping key;

    @Shadow
    @Final
    private Button changeButton;

    @Shadow
    @Final
    private Button resetButton;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(KeyBindsList keyBindsList, KeyMapping keyMapping, final Component component, CallbackInfo ci) {
        if (keyMapping instanceof KumaKeyMapping kumaKeyMapping && kumaKeyMapping.kuma$isManaged()) {
            KeyBindsListHooks.updateResetButton(resetButton, keyMapping);
        }
    }

    @ModifyArg(
            method = "refreshEntry",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/components/Button;setMessage(Lnet/minecraft/network/chat/Component;)V",
                    ordinal = 0
            )
    )
    private Component refreshEntryButtonSetMessage(Component originalMessage) {
        if (key instanceof KumaKeyMapping kumaKeyMapping && kumaKeyMapping.kuma$isManaged()) {
            return kumaKeyMapping.kuma$getModifiers().getTranslatedKeyMessage(key);
        }

        return originalMessage;
    }

}
