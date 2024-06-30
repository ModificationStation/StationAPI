# StationAPI for Minecraft Beta 1.7.3 Babric

<!---
Buildbot moment.
-->

A general use API for Fabric Loader mods on legacy Minecraft versions.

## Plugin Setup

Extra steps for better Mixin making and Fabric configuring in IntelliJ IDEA:

1. Go to `File > Settings...`
2. Go to `Plugins` and install the `Minecraft Development` plugin.
3. Restart IntelliJ IDEA.
4. Follow the instructions in [Using This to Make Mods](#using-this-to-make-mods).
5. Profit!

## Using This to Run Mods

You will want to install the [Babric Prism instance](https://github.com/babric/prism-instance) and [download the latest release of StationAPI](https://modrinth.com/mod/stationapi/version/latest).  
Put the downloaded StationAPI jar into your mods folder, do NOT add as a jar mod.
Make sure to use Java 17 (disable Java compatibility checks in Prism if it complains).

## Using This to Make Mods

[See the wiki.](https://github.com/ModificationStation/StationAPI/wiki)  
[Using GCAPI](glass-config-api-v3/docs/Home.md)

Proper code documentation coming eventually. There are some JavaDoc comments for some commonly used classes.

## Common Issues

If you are having any issues with setting up StationAPI or the example mod, have a look at the [BIN Fabric Example Mod's readme entry for common issues](https://github.com/calmilamsy/BIN-fabric-example-mod/#common-issues) (outdated, but some solutions still apply).  

## License

StationAPI is available under the [MIT License](LICENSE).
