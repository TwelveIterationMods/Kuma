package net.blay09.mods.kuma.storage.json;

import com.google.gson.*;
import net.blay09.mods.kuma.api.KeyModifiers;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Type;

class ExtendedKeyMappingDataAdapter implements JsonSerializer<ExtendedKeyMappingData>, JsonDeserializer<ExtendedKeyMappingData> {

    @Override
    public JsonElement serialize(@Nullable ExtendedKeyMappingData src, Type typeOfSrc, JsonSerializationContext context) {
        final var result = new JsonObject();
        if (src != null) {
            result.add("modifiers", context.serialize(src.keyModifiers(), KeyModifiers.class));
        }
        return result;
    }

    @Override
    public ExtendedKeyMappingData deserialize(@Nullable JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json == null || json.isJsonNull()) {
            return new ExtendedKeyMappingData(KeyModifiers.none());
        }

        if (!json.isJsonObject()) {
            throw new JsonParseException("ExtendedKeyMappingData must be a JSON object");
        }

        final var jsonObject = json.getAsJsonObject();

        final var jsonModifiers = jsonObject.get("modifiers");
        final KeyModifiers modifiers;
        if (jsonModifiers != null && !jsonModifiers.isJsonNull()) {
            modifiers = context.deserialize(jsonModifiers, KeyModifiers.class);
        } else {
            modifiers = KeyModifiers.none();
        }

        return new ExtendedKeyMappingData(modifiers);
    }
}
