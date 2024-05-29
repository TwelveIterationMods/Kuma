package net.blay09.mods.kuma.api;

@FunctionalInterface
public interface ScreenInputEventHandler {
    boolean handle(ScreenInputEvent event);
}
