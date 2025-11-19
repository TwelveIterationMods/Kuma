package net.blay09.mods.kuma;

import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KumaKeyCategories {

    private static final Map<String, KeyMapping.Category> defaultCategories = new ConcurrentHashMap<>();

    public static KeyMapping.Category getDefaultCategory(String namespace) {
        return defaultCategories.computeIfAbsent(namespace,
                key -> KeyMapping.Category.register(Identifier.fromNamespaceAndPath(namespace, "default")));
    }
}
