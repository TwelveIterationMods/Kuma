# Kuma

Minecraft Mod. Universal Key Modifier API for Fabric, NeoForge and Forge.

`kuma-api` is a library mod intended to be included in existing mods, providing an easy API layer and implementation for compatible, remappable
key mappings with multi-loader, context and (multi-) modifier support.

Kuma is designed to only change the behavior of keys that are explicitly registered to it. 
This means it is safe to include without worrying about it affecting other mods or modpacks as a whole.

Balm comes with Kuma already included, but you can use Kuma without Balm too! 

#### Downloads

Kuma API is meant to be included as an embedded library. There is no file to download or install as a user.

## Who needs this?

This library is useful for mod developers targeting both Fabric and Neo/Forge at once, or for those who wish to use native Vanilla key mappings even when depending on more advanced features like multiple modifiers or
custom modifier keys.

I created it because both Crafting Tweaks and Inventory Essentials have plenty of modifier-based key mappings that were
difficult to properly support across the different mod loaders and repeatedly ran into limitations.

## How to use as a Mod Developer

1\. Start by changing your gradle files to have `kuma-api` be embedded in your mod's jar.

Add the following to your `build.gradle`:

```groovy
repositories {
    maven {
        url "https://maven.twelveiterations.com/repository/maven-public/"

        content {
            includeGroup "net.blay09.mods"
        }
    }
}
```

When defining the dependency below, replace the version with the version you want to depend on.
Kuma API follows a versioning scheme where the major and minor version always match the minor and patch version of
Minecraft.
So for Minecraft 1.20.6, you would depend on 20.6.x where x is the patch version of Kuma API itself.

Specifically on jarJar dependencies, you should also use a version range to ensure your mod will continue to function
even if another mod ships a later patch version of Kuma API.

You can find the latest version for a given Minecraft version
at https://maven.twelveiterations.com/service/rest/repository/browse/maven-public/net/blay09/mods/kuma-common/

In your `gradle.properties`:

```ini
kuma_version = 21.11.12
kuma_version_range = [21.11,22)
```

For Common / Mojmap:

```groovy
dependencies {
    compileOnly "net.blay09.mods:kuma-api-common:$kuma_version"
}
```

For NeoForge:

```groovy
jarJar.enable() // Enable the Jar-in-Jar system

dependencies {
    jarJar("net.blay09.mods:kuma-api-neoforge") {
        version {
            strictly kuma_version_range
            prefer kuma_version
        }
    }
}
```

For Fabric:

```groovy
dependencies {
    include modApi("net.blay09.mods:kuma-api-fabric:$kuma_version")
}
```

For Forge (ForgeGradle 6):

```groovy
jarJar.enable() // Enable the Jar-in-Jar system. Make sure to put this line *before* the minecraft block!

dependencies {
    jarJar(group: "net.blay09.mods", name: "kuma-api-forge", version: kuma_version_range) {
        jarJar.pin(it, kuma_version)
    }
}
```

For Forge (ForgeGradle 7):

```groovy
plugins {
    id "net.minecraftforge.jarjar" version "0.2.3"
}

jarJar.register()

dependencies {
    jarJar("net.blay09.mods:kuma-api-forge:${kuma_version}")
}
```

If you are using Balm, Kuma is already available to you! Balm comes with it included.

2\. In your mod constructor or initializer, start creating key mappings using `Kuma`.

Kuma API takes care of registering the vanilla `KeyMapping`s at the correct time.
The method returns a `ManagedKeyMapping` instance that you can use to operate on the key mapping later, be it a
real `KeyMapping` or a virtual one.

Here's some examples for creating key mappings:

```java
class ExampleMod {
    public ExampleMod() {
        // Just a regular key mapping with a single modifier (CONTROL + G).
        Kuma.createKeyMapping(new Identifier("example", "example_key_1"))
                .withDefault(InputBinding.key(InputConstants.KEY_G, KeyModifiers.of(KeyModifier.CONTROL)))
                .handleScreenInput((event) -> {
                    // Add your press logic here
                    return true;
                })
                .build(); // Don't forget to call build() at the end!

        // A key mapping with a custom modifier (SPACE + CLICK).
        Kuma.createKeyMapping(new Identifier("example", "example_key_3"))
                .withDefault(InputBinding.mouse(InputConstants.MOUSE_BUTTON_LEFT,
                        KeyModifiers.ofCustom(InputConstants.getKey(InputConstants.KEY_SPACE, -1))))
                .handleScreenInput((event) -> {
                    // Add your press logic here
                    return true;
                })
                .build(); // Don't forget to call build() at the end!

        // A nonsense key mapping just to show off the rest of the methods.
        Kuma.createKeyMapping(new Identifier("example", "example_key_4"))
                // By default, the category is created based on the resource location above. You can override it.
                .overrideCategory("key.categories.movement")
                .withDefault(InputBinding.key(InputConstants.KEY_G, KeyModifiers.of(KeyModifier.CONTROL, KeyModifier.SHIFT)))
                .withContext(KeyConflictContext.UNIVERSAL) // This is normally just inferred from the supplied input handlers.
                .handleScreenInput((event) -> {
                    // Add your press logic here
                    return true;
                })
                .handleWorldInput((event) -> {
                    // Add your press logic here
                    return true;
                })
                .build(); // Don't forget to call build() at the end!
    }
}
```

## Contributing

If you're interested in contributing to the mod, you can check
out [issues labelled as "help wanted"](https://github.com/TwelveIterations/Kuma/issues?q=is%3Aopen+is%3Aissue+label%3A%22help+wanted%22).

When it comes to new features, it's best to confer with me first to ensure we share the same vision. You can join us
on [Discord](https://discord.gg/36qHFMNgAh) if you'd like to talk.

Contributions must be done through pull requests. I will not be able to accept translations, code or other assets
through any other channels.
