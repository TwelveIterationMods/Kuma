package net.blay09.mods.kuma;

import net.blay09.mods.kuma.api.InputBinding;
import net.blay09.mods.kuma.api.KeyModifiers;
import net.blay09.mods.kuma.api.KumaKeyMapping;
import net.blay09.mods.kuma.api.ManagedKeyMapping;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

import java.util.Locale;

public class WrappedManagedKeyMappingBuilder extends AbstractManagedKeyMappingBuilder<ManagedKeyMapping.WrapperBuilder> implements ManagedKeyMapping.WrapperBuilder {

    private final KeyMapping keyMapping;

    public WrappedManagedKeyMappingBuilder(KeyMapping keyMapping) {
        super(deriveId(keyMapping));
        if (keyMapping instanceof KumaKeyMapping kumaKeyMapping && kumaKeyMapping.kuma$isManaged()) {
            throw new IllegalStateException("KeyMapping is already managed by Kuma");
        }

        this.keyMapping = keyMapping;
        this.defaultBinding = InputBinding.of(keyMapping);
    }

    @Override
    protected ManagedKeyMapping.WrapperBuilder self() {
        return this;
    }

    private static Identifier deriveId(KeyMapping keyMapping) {
        final var name = keyMapping.getName();
        if (name.startsWith("key.")) {
            final var suffix = name.substring("key.".length());
            final var separatorIndex = suffix.indexOf('.');
            if (separatorIndex >= 0) {
                final var namespace = suffix.substring(0, separatorIndex);
                final var path = suffix.substring(separatorIndex + 1);
                if (Identifier.isValidNamespace(namespace) && Identifier.isValidPath(path)) {
                    return Identifier.fromNamespaceAndPath(namespace, path);
                }
            } else if (Identifier.isValidPath(suffix)) {
                return Identifier.fromNamespaceAndPath("minecraft", suffix);
            }
        }

        return Identifier.fromNamespaceAndPath("kuma", sanitizeFallbackPath(name));
    }

    private static String sanitizeFallbackPath(String name) {
        return name.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9_./-]", "_");
    }

    @Override
    public ManagedKeyMapping.WrapperBuilder withDefaultModifiers(KeyModifiers modifiers) {
        defaultBinding = new InputBinding(defaultBinding.key(), modifiers);
        return this;
    }

    @Override
    public ManagedKeyMapping build() {
        final var managedKeyMapping = new ManagedWrappedKeyMapping(id, keyMapping, determineContext(), screenInputHandler, worldInputHandler, keyRepeat, ignoresScreenFocus, defaultBinding, storage);
        managedKeyMapping.wrap();
        managedKeyMapping.getStorage().loadKeyMapping(managedKeyMapping);
        NativeKeyModifierReconciler.reconcile(keyMapping);
        ManagedKeyMappingRegistry.register(managedKeyMapping);
        return managedKeyMapping;
    }
}
