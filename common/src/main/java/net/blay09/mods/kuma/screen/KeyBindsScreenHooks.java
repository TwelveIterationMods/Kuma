package net.blay09.mods.kuma.screen;

import com.mojang.blaze3d.platform.InputConstants;
import net.blay09.mods.kuma.api.*;
import net.blay09.mods.kuma.mixin.KeyBindsScreenAccessor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.util.Util;

import java.util.ArrayList;
import java.util.List;

public class KeyBindsScreenHooks {
    private static final List<InputConstants.Key> heldKeys = new ArrayList<>();
    private static boolean consumeNext;

    private static boolean isShift(InputWithModifiers event) {
        return event.input() == InputConstants.KEY_LSHIFT || event.input() == InputConstants.KEY_RSHIFT;
    }

    private static boolean isControl(InputWithModifiers event) {
        return event.input() == InputConstants.KEY_LCONTROL || event.input() == InputConstants.KEY_RCONTROL;
    }

    private static boolean isAlt(InputWithModifiers event) {
        return event.input() == InputConstants.KEY_LALT || event.input() == InputConstants.KEY_RALT;
    }

    private static KeyModifier getStandardModifier(InputWithModifiers event) {
        if (isShift(event)) {
            return KeyModifier.SHIFT;
        } else if (isControl(event)) {
            return KeyModifier.CONTROL;
        } else if (isAlt(event)) {
            return KeyModifier.ALT;
        }
        return KeyModifier.NONE;
    }

    private static InputBinding getInputBinding(InputWithModifiers event) {
        final var modifiers = getActiveModifiers(event);

        // In case a standard modifier is let go of first, we swap it with the first custom modifier to avoid cases like `E + Left Shift`
        final var eventStandardModifier = getStandardModifier(event);
        if (modifiers.hasCustomModifiers() && eventStandardModifier != KeyModifier.NONE) {
            final var firstCustomModifier = modifiers.getCustomModifiers().removeFirst();
            modifiers.addModifier(eventStandardModifier);
            return InputBinding.of(firstCustomModifier, modifiers);
        }

        return InputBinding.of(event, modifiers);
    }

    private static KeyModifiers getActiveModifiers(InputWithModifiers event) {
        final var modifiers = KeyModifiers.none();
        if (event.hasShiftDown() && event.input() != InputConstants.KEY_LSHIFT && event.input() != InputConstants.KEY_RSHIFT) {
            modifiers.addModifier(KeyModifier.SHIFT);
        }
        if (event.hasAltDown() && event.input() != InputConstants.KEY_LALT && event.input() != InputConstants.KEY_RALT) {
            modifiers.addModifier(KeyModifier.ALT);
        }
        if (event.hasControlDown() && event.input() != InputConstants.KEY_LCONTROL && event.input() != InputConstants.KEY_RCONTROL) {
            modifiers.addModifier(KeyModifier.CONTROL);
        }
        for (final var heldKey : heldKeys) {
            if (Kuma.isDown(heldKey) && event.input() != heldKey.getValue()) {
                if (heldKey.getValue() != InputConstants.KEY_LSHIFT
                        && heldKey.getValue() != InputConstants.KEY_RSHIFT
                        && heldKey.getValue() != InputConstants.KEY_LALT
                        && heldKey.getValue() != InputConstants.KEY_RALT
                        && heldKey.getValue() != InputConstants.KEY_LCONTROL
                        && heldKey.getValue() != InputConstants.KEY_RCONTROL) {
                    modifiers.addCustomModifier(heldKey);
                }
            }
        }
        return modifiers;
    }

    public static boolean afterMouseRelease(Screen screen, MouseButtonEvent mouseButtonEvent) {
        if (screen instanceof KeyBindsScreen keyBindsScreen) {
            if (keyBindsScreen.selectedKey instanceof KumaKeyMapping kumaKeyMapping && kumaKeyMapping.kuma$isManaged()) {
                if (consumeNext) {
                    final var binding = getInputBinding(mouseButtonEvent);
                    keyBindsScreen.selectedKey.setKey(binding.key());
                    kumaKeyMapping.kuma$setModifiers(binding.modifiers());
                    final var managedKeyMapping = kumaKeyMapping.kuma$getManagedKeyMapping();
                    if (managedKeyMapping != null) {
                        managedKeyMapping.getStorage().saveKeyMapping(managedKeyMapping);
                    }
                    keyBindsScreen.selectedKey = null;
                    ((KeyBindsScreenAccessor) keyBindsScreen).getKeyBindsList().resetMappingAndUpdateButtons();
                    consumeNext = false;
                    heldKeys.clear();
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean afterKeyRelease(Screen screen, KeyEvent event) {
        if (screen instanceof KeyBindsScreen keyBindsScreen) {
            if (keyBindsScreen.selectedKey instanceof KumaKeyMapping kumaKeyMapping && kumaKeyMapping.kuma$isManaged()) {
                if (consumeNext) {
                    if (event.isEscape()) {
                        keyBindsScreen.selectedKey.setKey(InputConstants.UNKNOWN);
                        kumaKeyMapping.kuma$setModifiers(KeyModifiers.none());
                    } else {
                        final var binding = getInputBinding(event);
                        keyBindsScreen.selectedKey.setKey(binding.key());
                        kumaKeyMapping.kuma$setModifiers(binding.modifiers());
                    }
                    final var managedKeyMapping = kumaKeyMapping.kuma$getManagedKeyMapping();
                    if (managedKeyMapping != null) {
                        managedKeyMapping.getStorage().saveKeyMapping(managedKeyMapping);
                    }
                    keyBindsScreen.selectedKey = null;
                    keyBindsScreen.lastKeySelection = Util.getMillis();
                    ((KeyBindsScreenAccessor) keyBindsScreen).getKeyBindsList().resetMappingAndUpdateButtons();
                    consumeNext = false;
                    heldKeys.clear();
                    return true;
                }
            }
        }

        return false;
    }

    public static void consumeNextKey(KeyEvent event) {
        final var key = InputConstants.getKey(event);
        if (!heldKeys.contains(key)) {
            heldKeys.add(key);
        }
        consumeNext = true;
    }

    public static void consumeNextMouse() {
        consumeNext = true;
    }
}
