# Fabric Example Mod with BIN Mappings for beta 1.7.3 server + client

## Setup

For setup instructions please see the [fabric wiki page](https://fabricmc.net/wiki/tutorial:setup) that relates to the IDE that you are using.

NOTE: There is no Fabric-API for beta 1.7.3!

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
7. Profit!

## Using a Mod

You will want to use the [Cursed Fabric MultiMC Instance](https://github.com/calmilamsy/Cursed-Fabric-MultiMC)

## License

This template is available under the CC0 license. Feel free to learn from it and incorporate it in your own projects.
