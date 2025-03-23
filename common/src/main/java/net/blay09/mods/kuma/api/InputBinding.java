package net.blay09.mods.kuma.api;

import com.mojang.blaze3d.platform.InputConstants;
import net.blay09.mods.kuma.mixin.KeyMappingAccessor;
import net.minecraft.client.KeyMapping;

public record InputBinding(InputConstants.Key key, KeyModifiers modifiers) {

    public static InputBinding mouse(int button) {
        return mouse(button, KeyModifiers.none());
    }

    public static InputBinding mouse(int button, KeyModifiers modifiers) {
        return new InputBinding(InputConstants.Type.MOUSE.getOrCreate(button), modifiers);
    }

    public static InputBinding key(int keyCode) {
        return key(keyCode, KeyModifiers.none());
    }

    public static InputBinding key(int keyCode, KeyModifiers modifiers) {
        return new InputBinding(InputConstants.getKey(keyCode, -1), modifiers);
    }

    public static InputBinding none() {
        return new InputBinding(InputConstants.UNKNOWN, KeyModifiers.none());
    }

    public static InputBinding of(KeyMapping mapping) {
        return new InputBinding(((KeyMappingAccessor) mapping).getKey(), KeyModifiers.of(mapping));
    }
}