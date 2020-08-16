package net.modificationstation.stationloader.common;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.metadata.EntrypointMetadata;
import net.fabricmc.loader.metadata.LoaderModMetadata;
import net.fabricmc.loader.metadata.NestedJarEntry;
import net.minecraft.client.MinecraftApplet;
import net.modificationstation.stationloader.client.lang.I18n;
import net.modificationstation.stationloader.common.api.StationMod;
import net.modificationstation.stationloader.common.config.Configuration;
import net.modificationstation.stationloader.common.event.mod.PreInit;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;

public class StationLoader implements net.modificationstation.stationloader.common.api.StationLoader {
    /** @deprecated */
    @Deprecated
    public static final StationLoader INSTANCE;
    static {
        INSTANCE = new StationLoader();
    }
    protected static final Logger LOGGER = LogManager.getFormatterLogger("StationLoader|API");

    private StationLoader() {}

    @Override
    public void setup() throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        if (new Exception().getStackTrace()[1].getClassName().equals(MinecraftApplet.class.getName())) {
            getLogger().info("Initializing StationLoader...");
            Configurator.setLevel("mixin", Level.TRACE);
            Configurator.setLevel("Fabric|Loader", Level.INFO);
            Configurator.setLevel("StationLoader|API", Level.INFO);
            for (ModContainer mod : FabricLoader.getInstance().getAllMods())
                if (mod.getMetadata() instanceof LoaderModMetadata) {
                    LoaderModMetadata loaderData = ((LoaderModMetadata) mod.getMetadata());
                    List<EntrypointMetadata> entries = loaderData.getEntrypoints("stationmod");
                    if (!entries.isEmpty()) {
                        Collection<NestedJarEntry> jars = loaderData.getJars();
                        String[] files = new String[jars.size()];
                        int i = 0;
                        for (NestedJarEntry jar : jars) {
                            files[i] = jar.getFile();
                            i++;
                        }
                        StringBuilder out = new StringBuilder("classpath");
                        if (files.length > 1) {
                            out = new StringBuilder("{ ");
                        }
                        for (int j = 0; j < files.length; j++) {
                            out.append(files[j]);
                            if (j < files.length + 1)
                                out.append(", ");
                        }
                        if (files.length > 1)
                            out.append(" }");
                        getLogger().info("Detected a StationMod in " + out);
                        for (EntrypointMetadata entry : entries)
                            addMod(mod.getMetadata(), entry.getValue());
                    }
                }
            getLogger().info("Invoking preInit event");
            PreInit.EVENT.getInvoker().preInit();
            getLogger().info("Finished StationLoader setup");
        } else
            throw new IllegalAccessException("Tried running StationLoader.setup() from an unknown source!");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addMod(ModMetadata data, String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        getLogger().info("Adding \"" + className + "\" mod");
        Class<StationMod> modClass = (Class<StationMod>) Class.forName(className);
        getLogger().info("Found the class");
        StationMod mod = modClass.newInstance();
        getLogger().info("Created an instance");
        String name = data.getName() + "|StationMod";
        mod.setLogger(LogManager.getFormatterLogger(name));
        Configurator.setLevel(name, Level.INFO);
        getLogger().info("Registered logger \"" + name + "\"");
        mod.setConfigPath(Paths.get(FabricLoader.getInstance().getConfigDirectory() + File.separator + data.getId()));
        mod.setDefaultConfig(new Configuration(new File(mod.getConfigPath() + File.separator + data.getId() + ".cfg")));
        getLogger().info("Initialized default config");
        String langPathName = "/assets/" + data.getId() + "/lang";
        URL langPath = getClass().getResource(langPathName);
        if (langPath != null) {
            I18n.addLangFolder(langPathName);
            getLogger().info("Registered lang path");
        }
        PreInit.EVENT.register(mod);
        getLogger().info("Registered events");
        mods.put(modClass, mod);
        getLogger().info("Success");
        FabricLoader.getInstance().getGameDirectory();
    }

    @Override
    public Collection<StationMod> getAllMods() {
        return Collections.unmodifiableCollection(mods.values());
    }

    @Override
    public StationMod getModInstance(Class<? extends StationMod> modClass) {
        return mods.get(modClass);
    }

    @Override
    public Logger getLogger() {
        return LOGGER;
    }

    private final Map<Class<StationMod>, StationMod> mods = new HashMap<>();
}
