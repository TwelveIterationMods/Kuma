package net.blay09.mods.kuma;

import net.blay09.mods.kuma.api.ManagedKeyMapping;

import java.util.ArrayList;
import java.util.List;

public class ManagedKeyMappingRegistry {
    private static final List<ManagedKeyMapping> keyMappings = new ArrayList<>();

    public static void register(ManagedKeyMapping keyMapping) {
        keyMappings.add(keyMapping);
    }

    public static List<ManagedKeyMapping> getKeyMappings() {
        return keyMappings;
    }
}
