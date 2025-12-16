package net.blay09.mods.kuma.storage.json;

import com.google.gson.*;
import com.mojang.blaze3d.platform.InputConstants;
import net.blay09.mods.kuma.api.InputBinding;
import net.blay09.mods.kuma.api.KeyModifiers;

import java.lang.reflect.Type;

public class InputBindingAdapter implements JsonSerializer<InputBinding>, JsonDeserializer<InputBinding> {

    @Override
    public JsonElement serialize(InputBinding src, Type typeOfSrc, JsonSerializationContext context) {
        if (src == null) {
            return new JsonObject();
        }

        final var result = new JsonObject();
        result.addProperty("key", src.key().getName());
        result.add("modifiers", context.serialize(src.modifiers(), KeyModifiers.class));
        return result;
    }

    @Override
    public InputBinding deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json == null || json.isJsonNull()) {
            return InputBinding.none();
        }

        // Allow simple bindings with just a key and no modifiers
        if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isString()) {
            final String keyName = json.getAsString();
            return new InputBinding(InputConstants.getKey(keyName), KeyModifiers.none());
        }

        if (!json.isJsonObject()) {
            throw new JsonParseException("InputBinding must be a string or an object");
        }

        final var jsonObject = json.getAsJsonObject();
        final var jsonKey = jsonObject.get("key");
        if (jsonKey == null || !jsonKey.isJsonPrimitive()) {
            return InputBinding.none();
        }

        final var key = InputConstants.getKey(jsonKey.getAsString());

        final KeyModifiers modifiers;
        final var jsonModifiers = jsonObject.get("modifiers");
        if (jsonModifiers != null) {
            modifiers = context.deserialize(jsonModifiers, KeyModifiers.class);
        } else {
            modifiers = KeyModifiers.none();
        }

        return new InputBinding(key, modifiers);
    }
}
