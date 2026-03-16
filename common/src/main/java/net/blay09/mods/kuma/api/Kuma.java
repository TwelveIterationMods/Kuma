package net.blay09.mods.kuma.api;

import com.mojang.blaze3d.platform.InputConstants;
import net.blay09.mods.kuma.KumaRuntime;
import net.blay09.mods.kuma.KumaRuntimeSpi;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import org.lwjgl.glfw.GLFW;

/**
 * Provides utility methods for managing key bindings and input handling in the Kuma mod.
 * This class serves as the main API entry point for interacting with the Kuma input system.
 */
public class Kuma {
    private static final KumaRuntime runtime = KumaRuntimeSpi.create();

    /**
     * Creates a new {@link ManagedKeyMapping.Builder} instance with a specified id.
     * The returned builder can be used to configure and register a new key mapping.
     *
     * @param id The resource location that uniquely identifies the key mapping.
     * @return A new {@link ManagedKeyMapping.Builder} instance.
     */
    public static ManagedKeyMapping.Builder createKeyMapping(Identifier id) {
        return runtime.createKeyMapping(id);
    }

    /**
     * Checks if the specified key conflict context is currently active.
     *
     * @param context The key conflict context to check.
     * @return True if the specified context is active, false otherwise.
     */
    public static boolean isContextActive(KeyConflictContext context) {
        final var client = Minecraft.getInstance();
        return switch (context) {
            case SCREEN -> client.screen != null;
            case WORLD -> client.screen == null && client.level != null;
            default -> true;
        };
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
        final var window = Minecraft.getInstance().getWindow();
        for (final var key : modifiers.getCustomModifiers()) {
            if (!InputConstants.isKeyDown(window, key.getValue())) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasAltDown() {
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow(), InputConstants.KEY_LALT)
                || InputConstants.isKeyDown(Minecraft.getInstance().getWindow(), InputConstants.KEY_RALT);
    }

    public static boolean hasControlDown() {
        final var window = Minecraft.getInstance().getWindow();
        if (Util.getPlatform() == Util.OS.OSX) {
            return InputConstants.isKeyDown(window, InputConstants.KEY_LSUPER)
                    || InputConstants.isKeyDown(window, InputConstants.KEY_RSUPER);
        }

        return InputConstants.isKeyDown(window, InputConstants.KEY_LCONTROL)
                || InputConstants.isKeyDown(window, InputConstants.KEY_RCONTROL);
    }

    public static boolean hasShiftDown() {
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow(), InputConstants.KEY_LSHIFT)
                || InputConstants.isKeyDown(Minecraft.getInstance().getWindow(), InputConstants.KEY_RSHIFT);
    }

    /**
     * Checks if the runtime supports multiple key mappings to be bound to the same key. On some platforms, only the first key mapping will receive input events if multiple key mappings are bound to the same key.
     *
     * @deprecated Kuma supports all capabilities on all platforms now. This will always be true.
     * @return True if the runtime supports multiple key bindings, false otherwise.
     */
    @Deprecated
    public static boolean areMultiBindingsSupported() {
        return true;
    }

    /**
     * Checks if the runtime supports key modifiers.
     *
     * @deprecated Kuma supports all capabilities on all platforms now. This will always be true.
     * @return True if the runtime supports key modifiers, false otherwise.
     */
    @Deprecated
    public static boolean areModifiersSupported() {
        return true;
    }

    /**
     * Checks if the runtime supports multiple key modifiers for a key mapping at the same time. Most platforms only support one key modifier at a time.
     *
     * @deprecated Kuma supports all capabilities on all platforms now. This will always be true.
     * @return True if the runtime supports multiple key modifiers, false otherwise.
     */
    @Deprecated
    public static boolean areMultiModifiersSupported() {
        return true;
    }

    /**
     * Checks if the runtime supports custom key modifiers. Custom key modifiers are modifiers that are not the standard Alt, Control, or Shift modifiers. Most platforms do not support these kind of modifiers.
     *
     * @deprecated Kuma supports all capabilities on all platforms now. This will always be true.
     * @return True if the runtime supports custom key modifiers, false otherwise.
     */
    @Deprecated
    public static boolean areCustomModifiersSupported() {
        return true;
    }

    /**
     * Checks if the specified input binding is supported by the runtime.
     *
     * @param binding The input binding to check.
     * @param context The key conflict context.
     * @deprecated Kuma supports all capabilities on all platforms now. This will always be true.
     * @return True if the input binding is supported, false otherwise.
     */
    @Deprecated
    public static boolean isBindingSupported(InputBinding binding, KeyConflictContext context) {
        return true;
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
        final var window = Minecraft.getInstance().getWindow();
        if (type.equals(InputConstants.Type.MOUSE) && key.getValue() != InputConstants.UNKNOWN.getValue()) {
            return GLFW.glfwGetMouseButton(window.handle(), key.getValue()) == GLFW.GLFW_PRESS;
        } else if (type.equals(InputConstants.Type.KEYSYM) && key.getValue() != InputConstants.UNKNOWN.getValue()) {
            return InputConstants.isKeyDown(window, key.getValue());
        }
        return false;
    }

    public static KeyMappingStorage getDefaultStorage() {
        return runtime.getDefaultStorage();
    }

    public static @InputWithModifiers.Modifiers int getActiveModifierFlags() {
        int modifiers = 0;
        if (hasShiftDown()) {
            modifiers |= 1;
        }
        if (hasControlDown()) {
            modifiers |= 2;
        }
        if (hasAltDown()) {
            modifiers |= 4;
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
