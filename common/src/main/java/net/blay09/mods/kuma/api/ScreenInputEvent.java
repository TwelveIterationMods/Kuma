package net.blay09.mods.kuma.api;

import net.minecraft.client.gui.screens.Screen;

/**
 * Represents an event for screen input, such as mouse movement or click.
 * The event contains the screen instance, as well as the mouse X and Y coordinates.
 */
public record ScreenInputEvent(Screen screen, double mouseX, double mouseY, ManagedKeyMapping keyMapping) {
}
