package net.blay09.mods.kuma.api;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public interface ManagedKeyMapping {
    InputBinding getBinding();

    void setBinding(InputBinding binding);

    boolean isContextActive();

    boolean areModifiersActive();

    default boolean isActiveAndDown() {
        return isContextActive() && areModifiersActive() && isDown();
    }

    default boolean isActiveAndMatchesMouse(int button) {
        return isContextActive() && areModifiersActive() && matchesMouse(button);
    }

    default boolean isActiveAndMatchesKey(int key, int scanCode, int modifiers) {
        return isContextActive() && areModifiersActive() && matchesKey(key, scanCode, modifiers);
    }

    boolean wasDown();

    boolean isDown();

    boolean matchesMouse(int button);

    boolean matchesKey(int key, int scanCode, int modifiers);

    boolean handleScreenInput(ScreenInputEvent event);

    boolean handleWorldInput(WorldInputEvent event);

    /**
     * Gets the display name for the key that is currently bound to this managed key mapping.
     *
     * @return The display name for the bound key.
     */
    Component getBoundKeyDisplayName();

    /**
     * Checks if the key mapping is bound to any input.
     *
     * @return True if the key mapping is bound to any input, false otherwise.
     */
    boolean isBound();

    /**
     * Checks if the key mapping is unbound, i.e. not configured to any input.
     *
     * @return True if the key mapping is unbound, false otherwise.
     */
    boolean isUnbound();

    boolean isKeyRepeatEnabled();

    interface Builder {
        Builder overrideName(String name);

        Builder overrideName(Function<ResourceLocation, String> nameFunction);

        Builder overrideCategory(String category);

        Builder withContext(KeyConflictContext context);

        Builder withDefault(InputBinding binding);

        Builder withFallbackDefault(InputBinding binding);

        Builder handleWorldInput(WorldInputEventHandler handler);

        Builder forceVirtual();

        Builder handleScreenInput(ScreenInputEventHandler handler);

        /**
         * Already on by default until 1.21.5. Enabling key repeat will cause handle*Input() handlers to be called repeatedly if the key is held down.
         */
        Builder enableKeyRepeat();

        /**
         * Disabling key repeat prevents handle*Input() handlers from calling repeatedly if the key is held down.
         * @deprecated Starting in 1.21.5, key repeat will be disabled by default.
         */
        @Deprecated
        Builder disableKeyRepeat();

        ManagedKeyMapping build();
    }

}
