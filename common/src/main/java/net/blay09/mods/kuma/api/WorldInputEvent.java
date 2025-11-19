package net.blay09.mods.kuma.api;

import net.minecraft.client.input.InputWithModifiers;

/**
 * Represents an event related to user input in the game world.
 */
public record WorldInputEvent(InputWithModifiers input, ManagedKeyMapping keyMapping) {
}
