package net.blay09.mods.kuma.mixin.controlling;

import com.blamejared.controlling.client.NewKeyBindsList;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.blay09.mods.kuma.api.KumaKeyMapping;
import net.blay09.mods.kuma.screen.KeyBindsListHooks;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NewKeyBindsList.KeyEntry.class)
public class ControllingKeyEntryMixin {

    @Shadow
    @Final
    private Button btnResetKeyBinding;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(NewKeyBindsList this$0, KeyMapping key, Component keyDesc, CallbackInfo ci) {
        if (key instanceof KumaKeyMapping kumaKeyMapping && kumaKeyMapping.kuma$isManaged()) {
            KeyBindsListHooks.updateResetButton(btnResetKeyBinding, key);
        }
    }

    @WrapOperation(
            method = "refreshEntry",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/KeyMapping;getTranslatedKeyMessage()Lnet/minecraft/network/chat/Component;"
            )
    )
    private Component refreshEntryGetTranslatedKeyMessage(KeyMapping instance, Operation<Component> original) {
        final var originalMessage = original.call(instance);
        if (instance instanceof KumaKeyMapping kumaKeyMapping && kumaKeyMapping.kuma$isManaged()) {
            return kumaKeyMapping.kuma$getModifiers().getTranslatedKeyMessage(instance, originalMessage);
        }

        return originalMessage;
    }

}
