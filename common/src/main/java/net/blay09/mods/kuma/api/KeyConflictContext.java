package net.blay09.mods.kuma.api;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;

import java.util.Objects;

/**
 * Represents the context in which a key conflict can occur.
 * <ul>
 * <li><code>UNIVERSAL</code>: Key conflicts apply globally, across both screens and world interaction.</li>
 * <li><code>SCREEN</code>: Key conflicts are limited to key mappings within screens.</li>
 * <li><code>WORLD</code>: Key conflicts are limited to key mappings within world interaction.</li>
 * </ul>
 */
public class KeyConflictContext {

    public static KeyConflictContext UNIVERSAL = new KeyConflictContext(Identifier.fromNamespaceAndPath("kuma", "universal")) {
        @Override
        public boolean conflictsWith(KeyConflictContext other) {
            return true;
        }
    };

    public static KeyConflictContext SCREEN = new KeyConflictContext(Identifier.fromNamespaceAndPath("kuma", "screen")) {
        @Override
        public boolean isActive() {
            return Minecraft.getInstance().screen != null;
        }
    };
    public static KeyConflictContext WORLD = new KeyConflictContext(Identifier.fromNamespaceAndPath("kuma", "world")) {
        @Override
        public boolean isActive() {
            final var client = Minecraft.getInstance();
            return client.screen == null && client.level != null;
        }
    };

    private final Identifier identifier;

    public KeyConflictContext(Identifier identifier) {
        this.identifier = identifier;
    }

    public boolean conflictsWith(KeyConflictContext other) {
        return this == other;
    }

    public boolean isActive() {
        return true;
    }

    public Identifier identifier() {
        return identifier;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (KeyConflictContext) obj;
        return Objects.equals(this.identifier, that.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier);
    }

    @Override
    public String toString() {
        return "KeyConflictContext[" +
                "identifier=" + identifier + ']';
    }

}
