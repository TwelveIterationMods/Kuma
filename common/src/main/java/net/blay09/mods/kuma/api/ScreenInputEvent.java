package net.blay09.mods.kuma.api;

import net.minecraft.client.gui.screens.Screen;

public record ScreenInputEvent(Screen screen, double mouseX, double mouseY) {
}
