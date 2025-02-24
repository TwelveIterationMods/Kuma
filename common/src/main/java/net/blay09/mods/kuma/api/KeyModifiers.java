package net.blay09.mods.kuma.api;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public class KeyModifiers {
    private final EnumSet<KeyModifier> modifiers = EnumSet.noneOf(KeyModifier.class);
    private final List<InputConstants.Key> customModifiers = new ArrayList<>();

    private KeyModifiers(KeyModifier... modifiers) {
        this.modifiers.addAll(Arrays.asList(modifiers));
    }

    public boolean contains(KeyModifier keyModifier) {
        return modifiers.contains(keyModifier);
    }

    public int size() {
        return modifiers.size();
    }

    public boolean isEmpty() {
        return modifiers.isEmpty();
    }

    public KeyModifiers addCustomModifier(InputConstants.Key key) {
        customModifiers.add(key);
        return this;
    }

    public KeyModifiers addCustomModifier(int keyCode) {
        customModifiers.add(InputConstants.Type.KEYSYM.getOrCreate(keyCode));
        return this;
    }

    public List<InputConstants.Key> getCustomModifiers() {
        return customModifiers;
    }

    public List<KeyModifier> asList() {
        return new ArrayList<>(modifiers);
    }

    public boolean hasCustomModifiers() {
        return !customModifiers.isEmpty();
    }

    public static KeyModifiers none() {
        return new KeyModifiers();
    }

    public static KeyModifiers of(KeyMapping keyMapping) {
        return Kuma.getKeyModifiers(keyMapping);
    }

    public static KeyModifiers of(KeyModifier... modifiers) {
        return new KeyModifiers(modifiers);
    }

    public static KeyModifiers ofCustom(InputConstants.Key... modifiers) {
        KeyModifiers keyModifiers = new KeyModifiers();
        for (InputConstants.Key modifier : modifiers) {
            keyModifiers.addCustomModifier(modifier);
        }
        return keyModifiers;
    }

    @Override
    public String toString() {
        return "KeyModifiers{" +
                "modifiers=" + modifiers +
                ", customModifiers=" + customModifiers +
                '}';
    }
}
