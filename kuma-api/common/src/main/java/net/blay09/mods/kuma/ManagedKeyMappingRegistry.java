package net.blay09.mods.kuma;

import net.blay09.mods.kuma.api.ManagedKeyMapping;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ManagedKeyMappingRegistry {
    public static final Comparator<ManagedKeyMapping> COMPARATOR = Comparator.comparingInt((ManagedKeyMapping it) -> it.getBinding().modifiers().size())
            .reversed();

    private static final List<ManagedKeyMapping> keyMappings = new ArrayList<>();

    public static void register(ManagedKeyMapping keyMapping) {
        keyMappings.add(keyMapping);
        keyMappings.sort(COMPARATOR);
    }

    public static List<ManagedKeyMapping> getKeyMappings() {
        return keyMappings.stream().sorted(COMPARATOR).toList();
    }
}
