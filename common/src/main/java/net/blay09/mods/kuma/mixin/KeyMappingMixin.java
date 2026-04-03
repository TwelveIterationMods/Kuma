package net.blay09.mods.kuma.mixin;

import net.blay09.mods.kuma.NativeKeyModifierReconciler;
import net.blay09.mods.kuma.api.*;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KeyMapping.class)
public class KeyMappingMixin implements KumaKeyMapping {

    @Shadow
    private int clickCount;

    @Unique
    @Nullable
    private ManagedKeyMapping kuma$managedKeyMapping;

    @Unique
    private KeyModifiers kuma$defaultModifiers = KeyModifiers.none();

    @Unique
    private KeyModifiers kuma$modifiers = KeyModifiers.none();

    @Unique
    private KeyConflictContext kuma$conflictContext = KeyConflictContext.UNIVERSAL;

    @Override
    public boolean kuma$isManaged() {
        return kuma$managedKeyMapping != null;
    }

    @Override
    @Nullable
    public ManagedKeyMapping kuma$getManagedKeyMapping() {
        return kuma$managedKeyMapping;
    }

    @Override
    public void kuma$setManagedKeyMapping(ManagedKeyMapping managed) {
        kuma$managedKeyMapping = managed;
    }

    @Override
    public KeyConflictContext kuma$getConflictContext() {
        return kuma$conflictContext;
    }

    @Override
    public void kuma$setConflictContext(KeyConflictContext context) {
        kuma$conflictContext = context;
    }

    @Override
    public KeyModifiers kuma$getDefaultModifiers() {
        return kuma$defaultModifiers;
    }

    @Override
    public void kuma$setDefaultModifiers(KeyModifiers modifiers) {
        kuma$defaultModifiers = modifiers;
    }

    @Override
    public KeyModifiers kuma$getModifiers() {
        NativeKeyModifierReconciler.reconcile((KeyMapping) (Object) this);
        return kuma$modifiers;
    }

    @Override
    public void kuma$setModifiers(@Nullable KeyModifiers modifiers) {
        kuma$modifiers = modifiers != null ? modifiers : KeyModifiers.none();
    }

    @Inject(method = "same", at = @At("HEAD"), cancellable = true)
    public void same(KeyMapping binding, CallbackInfoReturnable<Boolean> cir) {
        if (binding instanceof KumaKeyMapping other) {
            if (!kuma$getConflictContext().conflictsWith(other.kuma$getConflictContext())) {
                cir.setReturnValue(false);
            } else if (!kuma$getModifiers().equals(other.kuma$getModifiers())) {
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(method = "matches", at = @At("HEAD"), cancellable = true)
    public void matches(KeyEvent event, CallbackInfoReturnable<Boolean> cir) {
        if (kuma$isManaged() && !kuma$getModifiers().test(event)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "matchesMouse", at = @At("HEAD"), cancellable = true)
    public void matchesMouse(MouseButtonEvent event, CallbackInfoReturnable<Boolean> cir) {
        if (kuma$isManaged() && !kuma$getModifiers().test(event)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "isDown", at = @At("HEAD"), cancellable = true)
    public void isDown(CallbackInfoReturnable<Boolean> cir) {
        if (kuma$isManaged() && !Kuma.areModifiersActive(kuma$getModifiers())) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "isDefault", at = @At("HEAD"), cancellable = true)
    public void isDefault(CallbackInfoReturnable<Boolean> cir) {
        if (kuma$isManaged() && !kuma$getModifiers().equals(kuma$getDefaultModifiers())) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "consumeClick", at = @At("HEAD"))
    public void consumeClick(CallbackInfoReturnable<Boolean> cir) {
        if (kuma$isManaged() && !Kuma.areModifiersActive(kuma$getModifiers())) {
            clickCount = 0;
        }
    }

}
