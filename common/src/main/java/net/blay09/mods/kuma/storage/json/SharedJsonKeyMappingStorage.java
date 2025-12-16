package net.blay09.mods.kuma.storage.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import net.blay09.mods.kuma.api.InputBinding;
import net.blay09.mods.kuma.api.KeyMappingStorage;
import net.blay09.mods.kuma.api.ManagedKeyMapping;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;

public class SharedJsonKeyMappingStorage implements KeyMappingStorage {
    private static final Logger logger = LoggerFactory.getLogger(SharedJsonKeyMappingStorage.class);

    private final File file;
    private boolean wasLoaded;

    private static final Type JSON_MAP_TYPE = new TypeToken<Map<Identifier, ExtendedKeyMappingData>>() {
    }.getType();

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Identifier.class, new IdentifierAdapter())
            .registerTypeAdapter(net.blay09.mods.kuma.api.KeyModifiers.class, new KeyModifiersAdapter())
            .registerTypeAdapter(ExtendedKeyMappingData.class, new ExtendedKeyMappingDataAdapter())
            .create();

    private final Map<Identifier, ExtendedKeyMappingData> storedData = new LinkedHashMap<>();

    public SharedJsonKeyMappingStorage(File file) {
        this.file = file;
    }

    private void ensureLoaded() {
        if (wasLoaded) {
            return;
        }

        synchronized (this) {
            if (wasLoaded) {
                return;
            }

            storedData.clear();
            final var path = file.toPath();
            if (Files.exists(path)) {
                try {
                    final var content = Files.readString(path, StandardCharsets.UTF_8);
                    final var rootElement = JsonParser.parseString(content);
                    if (rootElement != null) {
                        final Map<Identifier, ExtendedKeyMappingData> loaded = gson.fromJson(rootElement, JSON_MAP_TYPE);
                        if (loaded != null) {
                            storedData.putAll(loaded);
                        }
                    }
                } catch (Exception e) {
                    logger.error("Failed to load Kuma key data", e);
                }
            }

            wasLoaded = true;
        }
    }

    private void save() {
        synchronized (this) {
            final var path = file.toPath();
            try {
                final var parent = path.getParent();
                if (parent != null) {
                    Files.createDirectories(parent);
                }

                Files.writeString(path, gson.toJson(storedData, JSON_MAP_TYPE), StandardCharsets.UTF_8);
            } catch (IOException e) {
                logger.error("Failed to save Kuma key data", e);
            }
        }
    }

    @Override
    public void loadKeyMapping(ManagedKeyMapping keyMapping) {
        ensureLoaded();

        synchronized (this) {
            final var components = storedData.get(keyMapping.getId());
            if (components != null) {
                final var current = keyMapping.getBinding();
                keyMapping.setBinding(new InputBinding(current.key(), components.keyModifiers()));
            }
        }
    }

    @Override
    public void saveKeyMapping(ManagedKeyMapping keyMapping) {
        ensureLoaded();
        synchronized (this) {
            storedData.put(keyMapping.getId(), new ExtendedKeyMappingData(keyMapping.getBinding().modifiers()));
        }
        save();
    }
}
