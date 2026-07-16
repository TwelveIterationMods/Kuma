package net.blay09.mods.kuma.api;

import com.mojang.blaze3d.platform.InputConstants;
import net.blay09.mods.kuma.KumaRuntime;
import net.blay09.mods.kuma.KumaRuntimeSpi;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.resources.Identifier;
import org.lwjgl.sdl.SDLMouse;

/**
 * Provides utility methods for managing key bindings and input handling in the Kuma mod.
 * This class serves as the main API entry point for interacting with the Kuma input system.
 */
public class Kuma {
    private static final KumaRuntime runtime = KumaRuntimeSpi.create();

    /**
     * Creates a new {@link ManagedKeyMapping.RegistrationBuilder} instance with a specified id.
     * The returned builder can be used to configure and register a new key mapping.
     *
     * @param id The resource location that uniquely identifies the key mapping.
     * @return A new {@link ManagedKeyMapping.RegistrationBuilder} instance.
     */
    public static ManagedKeyMapping.RegistrationBuilder createKeyMapping(Identifier id) {
        return runtime.createKeyMapping(id);
    }

    /**
     * Creates a {@link ManagedKeyMapping.WrapperBuilder} for an existing {@link KeyMapping}.
     * The returned builder uses the existing mapping's current key or mouse button as its default and
     * allows customizing only wrapper-specific behavior such as handlers, storage, repeat, focus, and default modifiers.
     *
     * @param keyMapping the existing key mapping to wrap
     * @return a builder that will turn the supplied key mapping into a Kuma-managed mapping on {@code build()}
     */
    public static ManagedKeyMapping.WrapperBuilder wrap(KeyMapping keyMapping) {
        return runtime.wrapKeyMapping(keyMapping);
    }

    /**
     * Checks if the specified key conflict context is currently active.
     *
     * @param context The key conflict context to check.
     * @return True if the specified context is active, false otherwise.
     */
    public static boolean isContextActive(KeyConflictContext context) {
        return context.isActive();
    }

    /**
     * Checks if the specified key modifiers are currently active.
     *
     * @param modifiers The key modifiers to check.
     * @return True if all the specified modifiers are active, false otherwise.
     */
    public static boolean areModifiersActive(KeyModifiers modifiers) {
        if (modifiers.contains(KeyModifier.ALT) && !hasAltDown()) {
            return false;
        }
        if (modifiers.contains(KeyModifier.CONTROL) && !hasControlDown()) {
            return false;
        }
        if (modifiers.contains(KeyModifier.SHIFT) && !hasShiftDown()) {
            return false;
        }
        for (final var key : modifiers.getCustomModifiers()) {
            if (!InputConstants.isKeyDown(key.getValue())) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasAltDown() {
        return InputConstants.isKeyDown(InputConstants.KEY_LALT)
                || InputConstants.isKeyDown(InputConstants.KEY_RALT);
    }

    public static boolean hasControlDown() {
        return InputConstants.isKeyDown(InputConstants.KEY_LCONTROL)
                || InputConstants.isKeyDown(InputConstants.KEY_RCONTROL);
    }

    public static boolean hasShiftDown() {
        return InputConstants.isKeyDown(InputConstants.KEY_LSHIFT)
                || InputConstants.isKeyDown(InputConstants.KEY_RSHIFT);
    }

    /**
     * Gets the key modifiers for the specified key mapping.
     *
     * @param keyMapping The key mapping to get the modifiers for.
     * @return The key modifiers for the specified key mapping.
     */
    public static KeyModifiers getKeyModifiers(KeyMapping keyMapping) {
        return keyMapping instanceof KumaKeyMapping kumaKeyMapping ? kumaKeyMapping.kuma$getModifiers() : KeyModifiers.none();
    }

    /**
     * Checks if the specified input binding is currently active (i.e. the key and all modifiers are pressed).
     *
     * @param binding The input binding to check.
     * @return True if the input binding is currently active, false otherwise.
     */
    public static boolean isDown(InputBinding binding) {
        return areModifiersActive(binding.modifiers()) && isDown(binding.key());
    }

    /**
     * Checks if the specified key is currently down (pressed).
     *
     * @param key The key to check.
     * @return True if the key is currently down, false otherwise.
     */
    public static boolean isDown(InputConstants.Key key) {
        final var type = key.getType();
        if (type.equals(InputConstants.Type.MOUSE) && key.getValue() != InputConstants.UNKNOWN.getValue()) {
            return isMouseButtonDown(key.getValue());
        } else if (type.equals(InputConstants.Type.KEYBOARD) && key.getValue() != InputConstants.UNKNOWN.getValue()) {
            return InputConstants.isKeyDown(key.getValue());
        }
        return false;
    }

    private static boolean isMouseButtonDown(int button) {
        if (button <= 0 || button > InputConstants.MOUSE_BUTTON_8) {
            return false;
        }

        final int state = SDLMouse.SDL_GetMouseState(null, null);
        return (state & (1 << (button - 1))) != 0;
    }

    public static KeyMappingStorage getDefaultStorage() {
        return runtime.getDefaultStorage();
    }

    public static @InputWithModifiers.Modifiers int getActiveModifierFlags() {
        int modifiers = 0;
        if (hasShiftDown()) {
            modifiers |= InputConstants.MOD_SHIFT;
        }
        if (hasControlDown()) {
            modifiers |= InputConstants.MOD_CONTROL;
        }
        if (hasAltDown()) {
            modifiers |= InputConstants.MOD_ALT;
        }
        return modifiers;
    }

    /**
     * For internal use.
     */
    public static KumaRuntime __getRuntime() {
        return runtime;
    }
}
