package net.blay09.mods.kuma.api;

/**
 * Functional interface for handling world input events.
 * Implementations of this interface can be used to handle key mappings while interacting with the world (i.e. no screens are open).
 */
@FunctionalInterface
public interface WorldInputEventHandler {
    /**
     * Handles a key mapping event while interacting with the world (i.e. no screens are open).
     * @param event The world input event to handle.
     * @return {@code true} if the event was handled, {@code false} otherwise.
     */
    boolean handle(WorldInputEvent event);
}
