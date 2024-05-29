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

    boolean isDown();

    boolean matchesMouse(int button);

    boolean matchesKey(int key, int scanCode, int modifiers);

    boolean handleScreenInput(ScreenInputEvent event);

    boolean handleWorldInput(WorldInputEvent event);

    interface Builder {
        Builder overrideCategory(String category);

        Builder withContext(KeyConflictContext context);

        Builder withDefault(InputBinding binding);

        Builder withFallbackDefault(InputBinding binding);

        Builder handleWorldInput(WorldInputEventHandler handler);

        Builder handleScreenInput(ScreenInputEventHandler handler);

        ManagedKeyMapping build();
    }

}
