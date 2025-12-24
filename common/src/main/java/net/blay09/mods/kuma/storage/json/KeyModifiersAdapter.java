package net.blay09.mods.kuma.storage.json;

import com.google.gson.*;
import com.mojang.blaze3d.platform.InputConstants;
import net.blay09.mods.kuma.api.KeyModifier;
import net.blay09.mods.kuma.api.KeyModifiers;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Type;

class KeyModifiersAdapter implements JsonSerializer<KeyModifiers>, JsonDeserializer<KeyModifiers> {

    @Override
    public JsonElement serialize(@Nullable KeyModifiers src, Type typeOfSrc, JsonSerializationContext context) {
        final var result = new JsonArray();
        if (src != null) {
            for (final var modifier : src.asList()) {
                if (modifier != KeyModifier.NONE) {
                    result.add(new JsonPrimitive(modifier.name()));
                }
            }
            for (final var key : src.getCustomModifiers()) {
                result.add(new JsonPrimitive(key.getName()));
            }
        }

        return result;
    }

    @Override
    public KeyModifiers deserialize(@Nullable JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json == null || json.isJsonNull()) {
            return KeyModifiers.none();
        }

        if (!json.isJsonArray()) {
            throw new JsonParseException("KeyModifiers must be a JSON array");
        }

        final var result = KeyModifiers.none();
        for (final var jsonModifier : json.getAsJsonArray()) {
            if (jsonModifier == null || !jsonModifier.isJsonPrimitive()) {
                continue;
            }

            final var modifierName = jsonModifier.getAsString();
            if (modifierName == null || modifierName.isEmpty()) {
                continue;
            }

            try {
                final var modifier = KeyModifier.valueOf(modifierName.toUpperCase());
                if (modifier != KeyModifier.NONE) {
                    result.addModifier(modifier);
                }
            } catch (IllegalArgumentException ignored) {
                result.addCustomModifier(InputConstants.getKey(modifierName));
            }
        }

        return result;
    }
}
