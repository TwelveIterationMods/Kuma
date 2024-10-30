package net.blay09.mods.kuma.api;

/**
 * Represents the context in which a key conflict can occur.
 * <ul>
 * <li><code>UNIVERSAL</code>: Key conflicts apply globally, across both screens and world interaction.</li>
 * <li><code>SCREEN</code>: Key conflicts are limited to key mappings within screens.</li>
 * <li><code>WORLD</code>: Key conflicts are limited to key mappings within world interaction.</li>
 * </ul>
 */
public enum KeyConflictContext {
    UNIVERSAL,
    SCREEN,
    WORLD
}
