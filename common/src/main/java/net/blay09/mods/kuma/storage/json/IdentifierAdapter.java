package net.blay09.mods.kuma.storage.json;

import com.google.gson.*;
import net.minecraft.resources.Identifier;

import java.lang.reflect.Type;

public class IdentifierAdapter implements JsonSerializer<Identifier>, JsonDeserializer<Identifier> {

    @Override
    public JsonElement serialize(Identifier src, Type typeOfSrc, JsonSerializationContext context) {
        if (src == null) {
            throw new JsonParseException("Identifier cannot be null");
        }
        return new JsonPrimitive(src.toString());
    }

    @Override
    public Identifier deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json == null || json.isJsonNull()) {
            throw new JsonParseException("Identifier cannot be null");
        }

        if (json.isJsonPrimitive()) {
            final var key = json.getAsString();
            if (key == null || key.isEmpty()) {
                throw new JsonParseException("Identifier cannot be empty");
            }

            return Identifier.parse(key);
        } else {
            throw new JsonParseException("Identifier must be a string");
        }
    }
}
