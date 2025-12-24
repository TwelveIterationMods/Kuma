package net.blay09.mods.kuma.api;

import org.jspecify.annotations.Nullable;

public interface KumaKeyMapping {
    boolean kuma$isManaged();

    @Nullable
    ManagedKeyMapping kuma$getManagedKeyMapping();

    void kuma$setManagedKeyMapping(ManagedKeyMapping managed);

    KeyConflictContext kuma$getConflictContext();

    void kuma$setConflictContext(KeyConflictContext context);

    KeyModifiers kuma$getModifiers();

    void kuma$setModifiers(@Nullable KeyModifiers modifiers);

    KeyModifiers kuma$getDefaultModifiers();

    void kuma$setDefaultModifiers(KeyModifiers modifiers);
}
