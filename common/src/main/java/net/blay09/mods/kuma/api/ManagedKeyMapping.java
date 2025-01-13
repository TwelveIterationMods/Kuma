package net.blay09.mods.kuma.api;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/**
 * Represents a managed key mapping that can be used to handle input events.
 * The mapping can be associated with a specific input binding, context, and event handlers.
 * Implementations of this interface can be created using {@link Kuma#createKeyMapping(ResourceLocation id)}.
 */
public interface ManagedKeyMapping {
    /**
     * Gets the input bound to this managed key mapping.
     *
     * @return The input binding bound to this key mapping.
     */
    InputBinding getBinding();

    /**
     * Sets the input bound to  this managed key mapping.
     *
     * @param binding The new input binding to bind to this key mapping.
     */
    void setBinding(InputBinding binding);

    /**
     * Checks if the {@link KeyConflictContext} associated with this managed key mapping is currently active.
     *
     * @return True if the context is active, false otherwise.
     */
    boolean isContextActive();

    /**
     * Checks if the modifiers (e.g. Shift, Ctrl, Alt) associated with this managed key mapping are currently active.
     *
     * @return True if the modifiers are active, false otherwise.
     */
    boolean areModifiersActive();

    /**
     * Checks if the key mapping's {@link KeyConflictContext} is currently active, its modifiers are active, and the key is currently pressed.
     *
     * @return True if the key conflict context, modifiers, and key are active, false otherwise.
     */
    default boolean isActiveAndDown() {
        return isContextActive() && areModifiersActive() && isDown();
    }

    /**
     * Checks if the {@link KeyConflictContext} associated with this managed key mapping is currently active, its modifiers are active, and the specified mouse button is currently pressed.
     *
     * @param button The mouse button to check (0 = left, 1 = right, 2 = middle).
     * @return True if the key conflict context, modifiers, and mouse button are active, false otherwise.
     */
    default boolean isActiveAndMatchesMouse(int button) {
        return isContextActive() && areModifiersActive() && matchesMouse(button);
    }

    /**
     * Checks if the {@link KeyConflictContext} associated with this managed key mapping is currently active, its modifiers are active, and the specified key, scan code, and modifiers match the current input.
     *
     * @param key       The key to check.
     * @param scanCode  The scan code to check.
     * @param modifiers The modifiers to check.
     * @return True if the key conflict context, modifiers, and input match, false otherwise.
     */
    default boolean isActiveAndMatchesKey(int key, int scanCode, int modifiers) {
        return isContextActive() && areModifiersActive() && matchesKey(key, scanCode, modifiers);
    }

    /**
     * Checks if the key mapping is currently pressed down.
     *
     * @return True if the key mapping is currently pressed, false otherwise.
     */
    boolean isDown();

    /**
     * Checks if the bound input matches the given mouse button.
     *
     * @param button The mouse button to check (0 = left, 1 = right, 2 = middle).
     * @return True if the bound input matches the given mouse button, false otherwise.
     */
    boolean matchesMouse(int button);

    /**
     * Checks if the bound input matches the given key, scan code, and modifiers.
     *
     * @param key       The key to check.
     * @param scanCode  The scan code to check.
     * @param modifiers The modifiers to check.
     * @return True if the bound input matches the given key, scan code, and modifiers, false otherwise.
     */
    boolean matchesKey(int key, int scanCode, int modifiers);

    /**
     * Invokes the screen input event handler for this key mapping.
     *
     * @param event The screen input event to handle.
     * @return True if the event was handled, false otherwise.
     */
    boolean handleScreenInput(ScreenInputEvent event);

    /**
     * Invokes the world input event handler for this key mapping.
     *
     * @param event The world input event to handle.
     * @return True if the event was handled, false otherwise.
     */
    boolean handleWorldInput(WorldInputEvent event);

    /**
     * Gets the display name for the key that is currently bound to this managed key mapping.
     *
     * @return The display name for the bound key.
     */
    Component getBoundKeyDisplayName();

    /**
     * Checks if the key mapping is bound to any input.
     * @return True if the key mapping is bound to any input, false otherwise.
     */
    boolean isBound();

    /**
     * Checks if the key mapping is unbound, i.e. not configured to any input.
     * @return True if the key mapping is unbound, false otherwise.
     */
    boolean isUnbound();

    /**
     * A builder interface for creating instances of {@link ManagedKeyMapping}.
     * This builder allows configuring various properties of the key mapping, such as the input binding, event handlers, and conflict context.
     * You can obtain an instance of this builder by calling {@link Kuma#createKeyMapping(ResourceLocation id)}.
     */
    interface Builder {
        /**
         * Sets the category for this key mapping. The category is used to group related key mappings together in the controls menu.
         * By default, the category is set to <code>key.categories.[namespace]</code> where <code>[namespace]</code> is the namespace of the key mapping's id
         *
         * @param category The category for this key mapping.
         * @return This builder instance, for chaining.
         */
        Builder overrideCategory(String category);

        /**
         * Sets the key conflict context for this key mapping. The conflict context is used to determine what other key mappings can be considered conflicting.
         *
         * @param context The key conflict context for this key mapping.
         * @return This builder instance, for chaining.
         */
        Builder withContext(KeyConflictContext context);

        /**
         * Sets the default input binding for this key mapping.
         *
         * @param binding The default input binding to use for this key mapping.
         * @return This builder instance, for chaining.
         */
        Builder withDefault(InputBinding binding);

        /**
         * Add a fallback default input binding for this key mapping, to be used if the primary default binding or previous default bindings are not supported by the current runtime.
         *
         * @param binding The fallback default input binding to add to this key mapping.
         * @return This builder instance, for chaining.
         */
        Builder withFallbackDefault(InputBinding binding);

        /**
         * Adds a handler for world input events that are associated with this managed key mapping.
         * The handler will be called whenever the bound key is pressed or released while the player is interacting with the world (i.e. not on a screen).
         *
         * @param handler The handler to be called for world input events.
         * @return This builder instance, for chaining.
         */
        Builder handleWorldInput(WorldInputEventHandler handler);

        /**
         * Forces this key mapping to be treated as a virtual key mapping, even if the runtime would support it natively.
         * Virtual key mappings are not registered as regular key mappings and support all advanced features regardless of runtime.
         *
         * @return This builder instance, for chaining.
         */
        Builder forceVirtual();

        /**
         * Adds a handler for screen input events that are associated with this managed key mapping.
         * The handler will be called whenever the bound key is pressed or released while a screen is open.
         *
         * @param handler The handler to be called for screen input events.
         * @return This builder instance, for chaining.
         */
        Builder handleScreenInput(ScreenInputEventHandler handler);
        
        /**
         * Builds and returns the configured ManagedKeyMapping instance.
         *
         * @return The built ManagedKeyMapping instance.
         */
        ManagedKeyMapping build();
    }

}
