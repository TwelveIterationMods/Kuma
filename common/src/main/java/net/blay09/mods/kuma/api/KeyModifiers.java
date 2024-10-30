package net.blay09.mods.kuma.api;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

/**
 * Represents a set of key modifiers, such as Shift, Ctrl, Alt, etc, as well as custom modifiers.
 */
public class KeyModifiers {
    private final EnumSet<KeyModifier> modifiers = EnumSet.noneOf(KeyModifier.class);
    private final List<InputConstants.Key> customModifiers = new ArrayList<>();

    private KeyModifiers(KeyModifier... modifiers) {
        this.modifiers.addAll(Arrays.asList(modifiers));
    }

    /**
     * Checks if this set of key modifiers contains the specified key modifier.
     *
     * @param keyModifier The key modifier to check for.
     * @return True if this set of key modifiers contains the specified key modifier, false otherwise.
     */
    public boolean contains(KeyModifier keyModifier) {
        return modifiers.contains(keyModifier);
    }

    /**
     * Returns the number of key modifiers in this set.
     *
     * @return The number of key modifiers in this set.
     */
    public int size() {
        return modifiers.size();
    }

    /**
     * Checks if this set of key modifiers is empty.
     *
     * @return True if this set of key modifiers is empty, false otherwise.
     */
    public boolean isEmpty() {
        return modifiers.isEmpty();
    }

    /**
     * Adds a custom key modifier to this set of key modifiers.
     *
     * @param key The custom key modifier to add.
     * @return This {@link KeyModifiers} instance, for method chaining.
     */
    public KeyModifiers addCustomModifier(InputConstants.Key key) {
        customModifiers.add(key);
        return this;
    }

    /**
     * Adds a custom key modifier to this set of key modifiers.
     *
     * @param keyCode The key code of the custom key modifier to add.
     * @return This {@link KeyModifiers} instance, for method chaining.
     */
    public KeyModifiers addCustomModifier(int keyCode) {
        customModifiers.add(InputConstants.Type.KEYSYM.getOrCreate(keyCode));
        return this;
    }

    /**
     * Returns the list of custom key modifiers for this {@link KeyModifiers} instance.
     *
     * @return The list of custom key modifiers.
     */
    public List<InputConstants.Key> getCustomModifiers() {
        return customModifiers;
    }

    /**
     * Returns a new list containing all the key modifiers in this {@link KeyModifiers} instance.
     *
     * @return A new list containing all the key modifiers.
     */
    public List<KeyModifier> asList() {
        return new ArrayList<>(modifiers);
    }

    /**
     * Checks if this set of key modifiers has any custom modifiers.
     *
     * @return True if this set of key modifiers has any custom modifiers, false otherwise.
     */
    public boolean hasCustomModifiers() {
        return !customModifiers.isEmpty();
    }

    /**
     * Returns a new {@link KeyModifiers} instance with no key modifiers.
     *
     * @return A new {@link KeyModifiers} instance with no key modifiers.
     */
    public static KeyModifiers none() {
        return new KeyModifiers();
    }

    /**
     * Creates a new {@link KeyModifiers} instance from the key modifiers associated with the given {@link KeyMapping}.
     *
     * @param keyMapping The {@link KeyMapping} to get the key modifiers from.
     * @return A new {@link KeyModifiers} instance containing the key modifiers from the given {@link KeyMapping}.
     */
    public static KeyModifiers of(KeyMapping keyMapping) {
        return Kuma.getKeyModifiers(keyMapping);
    }

    /**
     * Creates a new {@link KeyModifiers} instance from the given key modifiers.
     *
     * @param modifiers The key modifiers to include in the new {@link KeyModifiers} instance.
     * @return A new {@link KeyModifiers} instance containing the given key modifiers.
     */
    public static KeyModifiers of(KeyModifier... modifiers) {
        return new KeyModifiers(modifiers);
    }

    /**
     * Creates a new {@link KeyModifiers} instance with the given custom key modifiers.
     *
     * @param modifiers The custom key modifiers to include in the new {@link KeyModifiers} instance.
     * @return A new {@link KeyModifiers} instance containing the given custom key modifiers.
     */
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
