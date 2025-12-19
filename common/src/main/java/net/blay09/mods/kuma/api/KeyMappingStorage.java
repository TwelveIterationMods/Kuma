package net.blay09.mods.kuma.api;

public interface KeyMappingStorage {
    void loadKeyMapping(ManagedKeyMapping keyMapping);

    void saveKeyMapping(ManagedKeyMapping keyMapping);
}
