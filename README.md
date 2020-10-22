# StationLoader API for Minecraft Beta 1.7.3 BIN Fabric

StationLoader is a new mod loader for Legacy Minecraft that aims to make mods compatible on both client and server without needing
seperate verisons for each, along with tools so mod developers no longer need to edit Minecraft's base classes.

## Setup

For general project setup instructions please see the [Fabric Wiki page](https://fabricmc.net/wiki/tutorial:setup) that relates to the IDE that you are using.

Extra steps for better Mixin making and Fabric configuring in IntelliJ IDEA:

1. Go to `File > Settings...`
2. Go to `Plugins` and install the `Minecraft Development` plugin.
3. Restart IntelliJ IDEA.
4. Follow the instructions in [Using This to Make Mods](#using-this-to-make-mods).
5. Profit!

## Using This to Run Mods

You will want to install the [Cursed Fabric MultiMC Instance](https://github.com/calmilamsy/Cursed-Fabric-MultiMC) and [download the latest release of StationLoader](https://github.com/modificationstation/StationLoader/releases/latest).  
Put the downloaded StationLoader jar into your mods folder, do NOT add as a jar mod.

## Using This to Make Mods

Add this to your build.gradle in the SECOND repositories entry (~line 33):  
`maven { url "https://maven.glass-launcher.net/repo"}`

Then add this to the end of your dependencies (~line 68):  
`modImplementation "net.modificationstation:StationLoader:1.2:dev"`

You will likely also want to install HMI-Fabric (unofficial port to SL by calmilamsy) for debugging your items and recipes.  
If so, download the latest **-dev** jar for HMI from [here](https://maven.glass-launcher.net/repo/net/glasslauncher/HMI-Fabric/) and put it in your `run/mods` folder.

Then reimport your Gradle project.

Proper documentation coming soon.

## License

StationLoader is available under the [CC0 1.0 Universal License](LICENSE). Feel free to learn from it and incorporate it in your own projects.
