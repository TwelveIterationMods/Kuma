package net.blay09.mods.kuma.api;

/**
 * Represents a handler for screen input events. Implementations of this interface can be used to handle key mappings while screens are open.
 */
@FunctionalInterface
public interface ScreenInputEventHandler {
    /**
     * Handles a key mapping event while a screen is open.
     * @param event The screen input event to handle.
     * @return True if the event was handled, false otherwise.
     */
    boolean handle(ScreenInputEvent event);
}
