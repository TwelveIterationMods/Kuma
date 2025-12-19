package net.blay09.mods.kuma.api;

public interface KumaKeyMapping {
    boolean kuma$isManaged();

    ManagedKeyMapping kuma$getManagedKeyMapping();

    void kuma$setManagedKeyMapping(ManagedKeyMapping managed);

    KeyConflictContext kuma$getConflictContext();

    void kuma$setConflictContext(KeyConflictContext context);

    KeyModifiers kuma$getModifiers();

    void kuma$setModifiers(KeyModifiers modifiers);

    KeyModifiers kuma$getDefaultModifiers();

    void kuma$setDefaultModifiers(KeyModifiers modifiers);
}
