package net.blay09.mods.kuma;

import net.blay09.mods.kuma.api.Kuma;
import net.blay09.mods.kuma.api.KumaKeyMapping;
import net.blay09.mods.kuma.api.KeyModifiers;
import net.minecraft.client.KeyMapping;

public final class NativeKeyModifierReconciler {

    private NativeKeyModifierReconciler() {
    }

    public static void reconcile(KeyMapping keyMapping) {
        if (!(keyMapping instanceof KumaKeyMapping kumaKeyMapping) || !kumaKeyMapping.kuma$isManaged()) {
            return;
        }

        final var nativeModifiers = Kuma.__getRuntime().getNativeKeyModifiers(keyMapping);
        if (nativeModifiers.isEmpty()) {
            return;
        }

        // If native modifiers are set, and we do not have any yet in Kuma, we adopt them.
        if (kumaKeyMapping.kuma$getModifiers().isEmpty()) {
            kumaKeyMapping.kuma$setModifiers(nativeModifiers);
            final var managedKeyMapping = kumaKeyMapping.kuma$getManagedKeyMapping();
            if (managedKeyMapping != null) {
                managedKeyMapping.getStorage().saveKeyMapping(managedKeyMapping);
            }
        }

        // Always reset native key modifiers so that Kuma becomes to sole owner of modifiers.
        Kuma.__getRuntime().setNativeKeyModifiers(keyMapping, KeyModifiers.none());
    }

}
