package net.blay09.mods.kuma.api;

import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.InputWithModifiers;
import org.jspecify.annotations.Nullable;

/**
 * Represents an event for screen input, such as mouse movement or click.
 * The event contains the screen instance, as well as the mouse X and Y coordinates.
 */
public record ScreenInputEvent(Screen screen, InputWithModifiers input, double mouseX, double mouseY, ManagedKeyMapping keyMapping) {

    /**
     * Returns true if an element on the screen holds the focus.
     * @return true if any element on the screen is focused
     */
    public boolean hasFocusedElement() {
        return getFocusedElement() != null;
    }

    /**
     * Finds the focused child element on the screen, if any.
     *
     * @return The deepest focused child element, or {@code null} if no child is focused.
     */
    public @Nullable GuiEventListener getFocusedElement() {
        return getFocusedElement(screen);
    }

    private static @Nullable GuiEventListener getFocusedElement(ContainerEventHandler parent) {
        final var focused = parent.getFocused();
        if (focused instanceof ContainerEventHandler focusedParent) {
            final var focusedChild = getFocusedElement(focusedParent);
            return focusedChild != null ? focusedChild : focused;
        } else if (focused != null) {
            return focused;
        }

        for (final var child : parent.children()) {
            if (child instanceof ContainerEventHandler childParent) {
                final var focusedChild = getFocusedElement(childParent);
                if (focusedChild != null) {
                    return focusedChild;
                }
            }

            if (child.isFocused()) {
                return child;
            }
        }

        return null;
    }
}
