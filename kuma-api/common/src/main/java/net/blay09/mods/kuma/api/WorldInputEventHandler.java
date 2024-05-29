package net.blay09.mods.kuma.api;

@FunctionalInterface
public interface WorldInputEventHandler {
    boolean handle(WorldInputEvent event);
}
