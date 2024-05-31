# Kuma

Minecraft Mod. Universal Key Modifier API for Fabric and Forge.

`kuma-api` is a library mod intended to be included in existing mods, providing an easy API layer for compatible
key mappings with multi-loader, context and (multi-) modifier support.

`kuma` is a companion mod built upon `kuma-api` that extends the Controls menu with the ability to manage key mappings
that otherwise would not be supported within the given loader, such as key modifiers on Fabric,
multi-modifiers on (Neo)Forge, and custom modifiers (like `Space + Click`). Unlike the API, it is not yet available.

- [Modpack Permissions](https://mods.twelveiterations.com/permissions)

#### Downloads

[![Versions](http://cf.way2muchnoise.eu/versions/1027078_latest.svg)](https://www.curseforge.com/minecraft/mc-mods/kuma)
[![Downloads](http://cf.way2muchnoise.eu/full_1027078_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/kuma)

## Who needs this?

This library is useful for mod developers targeting both Fabric and (Neo)Forge at once, or for those who wish to use the
same API for their key mappings even when depending on more advanced features like multiple modifiers or
custom modifier keys. Kuma API is designed to progressively upgrade or fallback to match the capabilities of its
environment.

I created it because both Crafting Tweaks and Inventory Essentials have plenty of modifier-based key mappings that were
difficult to properly support across the different mod loaders and repeatedly ran into limitations with the Vanilla
KeyMapping system.

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

For Common / Mojmap:

```groovy
dependencies {
    compileOnly "net.blay09.mods:kuma-api-common:[20.6.0,20.7.0)"
}
```

For Fabric:

```groovy
dependencies {
    include modApi("net.blay09.mods:kuma-api-fabric:20.6.1")
}
```

For Forge:

```groovy
jarJar.enable() // Enable the Jar-in-Jar system. Make sure to put this line *before* the minecraft block!

dependencies {
    jarJar(group: "net.blay09.mods", name: "kuma-api-forge", version: "[20.6.0,20.7.0)")
}
```

2\. In your mod constructor or initializer, start creating key mappings using `Kuma`.

Kuma API takes care of registering the vanilla `KeyMapping`s at the correct time.
The method returns a `ManagedKeyMapping` instance that you can use to operate on the key mapping later, be it a
real `KeyMapping` or a virtual one.

Here's some examples for creating key mappings:

```java
class ExampleMod {
    public ExampleMod() {
        // Just a regular key mapping with a single modifier.
        // Will register as a regular KeyMapping on Forge, and as a virtual key mapping on Fabric.
        Kuma.createKeyMapping(new ResourceLocation("example", "example_key_1"))
                .withDefault(InputBinding.key(InputConstants.KEY_G, KeyModifiers.of(KeyModifier.CONTROL)))
                .handleScreenInput((event) -> {
                    // TODO Add your press logic here
                    return true;
                })
                .build(); // Don't forget to call build() at the end!

        // A key mapping with a fallback binding. 
        // If the environment does not support the binding, it will attempt to use the fallback instead of creating a virtual key mapping,
        // which means this key would not have a default on Fabric environments.
        Kuma.createKeyMapping(new ResourceLocation("example", "example_key_2"))
                .withDefault(InputBinding.key(InputConstants.KEY_G, KeyModifiers.of(KeyModifier.CONTROL)))
                .withFallbackDefault(InputBinding.none())
                .handleScreenInput((event) -> {
                    // TODO Add your press logic here
                    return true;
                })
                .build(); // Don't forget to call build() at the end!

        // A key mapping with a custom modifier. These will always result in a virtual key mapping if no fallback binding is provided, since 
        // no mod loader supports them, unless the user also installs the Kuma companion mod.
        Kuma.createKeyMapping(new ResourceLocation("example", "example_key_3"))
                // We want to use SPACE-CLICK by default. This will not be remappable unless the user installs also installs Kuma (not just Kuma API).
                .withDefault(InputBinding.mouse(InputConstants.MOUSE_BUTTON_LEFT,
                        KeyModifiers.ofCustom(InputConstants.getKey(InputConstants.KEY_SPACE, -1))))
                .handleScreenInput((event) -> {
                    // TODO Add your press logic here
                    return true;
                })
                .build(); // Don't forget to call build() at the end!

        // A nonsense key mapping just to show off the rest of the methods.
        Kuma.createKeyMapping(new ResourceLocation("example", "example_key_4"))
                // By default, the category is created based on the resource location above. You can override it.
                .overrideCategory("key.categories.movement")
                .withDefault(InputBinding.key(InputConstants.KEY_G,
                        KeyModifiers.of(KeyModifier.CONTROL, KeyModifier.SHIFT)))
                .withFallbackDefault(InputBinding.key(InputConstants.KEY_G, KeyModifiers.of(KeyModifier.CONTROL)))
                .withContext(KeyConflictContext.UNIVERSAL) // This is normally just inferred from the supplied input handlers.
                .handleScreenInput((event) -> {
                    // TODO Add your press logic here
                    return true;
                })
                .handleWorldInput((event) -> {
                    // TODO Add your press logic here
                    return true;
                })
                .build(); // Don't forget to call build() at the end!
    }
}
```

## Contributing

If you're interested in contributing to the mod, you can check
out [issues labelled as "help wanted"](https://github.com/TwelveIterationMods/Kuma/issues?q=is%3Aopen+is%3Aissue+label%3A%22help+wanted%22).

When it comes to new features, it's best to confer with me first to ensure we share the same vision. You can join us
on [Discord](https://discord.gg/VAfZ2Nau6j) if you'd like to talk.

Contributions must be done through pull requests. I will not be able to accept translations, code or other assets
through any other channels.
