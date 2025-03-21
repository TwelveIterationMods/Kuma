package net.blay09.mods.kuma.api;

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

    boolean isKeyRepeatEnabled();

    interface Builder {
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
