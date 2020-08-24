# StationLoader API for Minecraft beta 1.7.3 BIN Fabric

## Setup

For general project setup instructions please see the [fabric wiki page](https://fabricmc.net/wiki/tutorial:setup) that relates to the IDE that you are using.

Extra steps for better mixin making in IntelliJ:

1. Go to `File > Settings...`
2. Go to `Plugins > The gear in the top middle > Manage Plugin Repositories...`
3. Press `+` and add one of these: (You can check your version in `Help > About`)
    - If using 2019.2: https://raw.githubusercontent.com/Earthcomputer/MinecraftDev/dev_new/updates/updatePlugins-192.xml
    - If using 2019.3: https://raw.githubusercontent.com/Earthcomputer/MinecraftDev/dev_new/updates/updatePlugins-193.xml
    - If using 2020.1: https://raw.githubusercontent.com/Earthcomputer/MinecraftDev/dev_new/updates/updatePlugins-201.xml
4. Refresh the plugin repo (restarting will do the trick).
5. Go back to plugins and install the `Minecraft Development` plugin.
6. Restart again.
7. Follow the instructions in [Using This to Make Mods](#using-this-to-make-mods).
7. Profit!

## Using This to Run Mods

You will want to install the [Cursed Fabric MultiMC Instance](https://github.com/calmilamsy/Cursed-Fabric-MultiMC) and [download the latest release of StationLoader](https://github.com/modificationstation/StationLoader/releases/latest).  
Put the downloaded StationLoader jar into your mods folder, do NOT add as a jar mod.

## Using This to Make Mods

Add this to your build.gradle in the SECOND repositories entry (~line 33):  
`maven { url "https://maven.glass-launcher.net/repo"}`

Then add this to the end of your dependencies (~line 68):  
`modImplementation "net.modificationstation:StationLoader:1.+:dev"`

Then reimport your gradle project.

Proper documentation coming soon.

## License

This API is available under the CC0 license. Feel free to learn from it and incorporate it in your own projects.
