package net.blay09.mods.kuma.api;

import net.minecraft.client.input.InputWithModifiers;

/**
 * Represents the different key modifiers that can be used in conjunction with a key press.
 * <ul>
 *     <li>{@code NONE} - No modifier keys are pressed</li>
 *     <li>{@code SHIFT} - The Shift key is pressed</li>
 *     <li>{@code CONTROL} - The Control key is pressed</li>
 *     <li>{@code ALT} - The Alt key is pressed</li>
 * </ul>
 */
public enum KeyModifier {
    NONE,
    SHIFT,
    CONTROL,
    ALT;

    public boolean isActiveOn(InputWithModifiers event) {
        return switch (this) {
            case NONE -> !event.hasShiftDown() && !event.hasControlDown() && !event.hasAltDown();
            case SHIFT -> event.hasShiftDown();
            case CONTROL -> event.hasControlDown();
            case ALT -> event.hasAltDown();
        };
    }
}
