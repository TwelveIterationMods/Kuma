# Kuma

Minecraft Mod. Universal Key Modifier API for Fabric, NeoForge and Forge.

`kuma-api` is a library mod intended to be included in existing mods, providing an easy API layer for compatible
key mappings with multi-loader, context and (multi-) modifier support.

`kuma` is a companion mod built upon `kuma-api` that extends the Controls menu with the ability to manage key mappings 
that otherwise would not be supported within the given loader, such as key modifiers on Fabric, 
multi-modifiers on (Neo)Forge, and custom modifiers (like `Space + Click`).

- [Modpack Permissions](https://mods.twelveiterations.com/permissions)

#### Downloads

[![Versions](http://cf.way2muchnoise.eu/versions/531761_latest.svg)](https://www.curseforge.com/minecraft/mc-mods/kuma)
[![Downloads](http://cf.way2muchnoise.eu/full_531761_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/kuma)

## Adding Kuma to a development environment

### Using Twelve Iterations Maven (includes snapshot versions)

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

dependencies {
    // Replace ${kuma_version} with the version you want to depend on. 
    // You may also have to change the Minecraft version in the artifact name.
    // You can find the latest version for a given Minecraft version at https://maven.twelveiterations.com/service/rest/repository/browse/maven-public/net/blay09/mods/kuma-common/
    // Common (mojmap): compileOnly "net.blay09.mods:kuma-api-common:${kuma_version}"
    // NeoForge: implementation "net.blay09.mods:kuma-api-neoforge:${kuma_version}"
    // Fabric: include modApi("net.blay09.mods:kuma-api-fabric:${kuma_version}")
    // Forge: implementation "net.blay09.mods:kuma-api-forge:${kuma_version}"
}
```

## Contributing

If you're interested in contributing to the mod, you can check
out [issues labelled as "help wanted"](https://github.com/TwelveIterationMods/Kuma/issues?q=is%3Aopen+is%3Aissue+label%3A%22help+wanted%22).

When it comes to new features, it's best to confer with me first to ensure we share the same vision. You can join us
on [Discord](https://discord.gg/VAfZ2Nau6j) if you'd like to talk.

Contributions must be done through pull requests. I will not be able to accept translations, code or other assets
through any other channels.
