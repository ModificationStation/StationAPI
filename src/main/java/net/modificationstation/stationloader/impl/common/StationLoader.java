package net.modificationstation.stationloader.impl.common;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.metadata.EntrypointMetadata;
import net.fabricmc.loader.metadata.LoaderModMetadata;
import net.fabricmc.loader.metadata.NestedJarEntry;
import net.minecraft.client.MinecraftApplet;
import net.modificationstation.stationloader.api.common.event.mod.PreInit;
import net.modificationstation.stationloader.api.common.mod.StationMod;
import net.modificationstation.stationloader.impl.common.lang.I18n;
import net.modificationstation.stationloader.impl.common.block.BlockManager;
import net.modificationstation.stationloader.impl.common.config.Category;
import net.modificationstation.stationloader.impl.common.config.Configuration;
import net.modificationstation.stationloader.impl.common.config.Property;
import net.modificationstation.stationloader.impl.common.event.EventFactory;
import net.modificationstation.stationloader.impl.common.factory.GeneralFactory;
import net.modificationstation.stationloader.impl.common.recipe.CraftingRegistry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;

public class StationLoader implements net.modificationstation.stationloader.api.common.StationLoader {

    protected static final Logger LOGGER = LogManager.getFormatterLogger("StationLoader|API");

    @Override
    public void setup() throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        if (new Exception().getStackTrace()[1].getClassName().equals(MinecraftApplet.class.getName())) {
            getLogger().info("Initializing StationLoader...");
            Configurator.setLevel("mixin", Level.TRACE);
            Configurator.setLevel("Fabric|Loader", Level.INFO);
            Configurator.setLevel("StationLoader|API", Level.INFO);
            getLogger().info("Setting up API...");
            setupAPI();
            getLogger().info("Loading mods...");
            loadMods();
            getLogger().info("Finished StationLoader setup");
        } else
            throw new IllegalAccessException("Tried running StationLoader.setup() from an unknown source!");
    }

    public void setupAPI() {
        getLogger().info("Setting up GeneralFactory...");
        net.modificationstation.stationloader.api.common.factory.GeneralFactory.INSTANCE.setHandler(new GeneralFactory());
        net.modificationstation.stationloader.api.common.factory.GeneralFactory.INSTANCE.addFactory(net.modificationstation.stationloader.api.common.config.Configuration.class, (args) -> new Configuration((File) args[0]));
        net.modificationstation.stationloader.api.common.factory.GeneralFactory.INSTANCE.addFactory(net.modificationstation.stationloader.api.common.config.Category.class, (args) -> new Category((String) args[0]));
        net.modificationstation.stationloader.api.common.factory.GeneralFactory.INSTANCE.addFactory(net.modificationstation.stationloader.api.common.config.Property.class, (args) -> new Property((String) args[0]));
        getLogger().info("Setting up EventFactory...");
        EventFactory.INSTANCE.setHandler(new EventFactory());
        getLogger().info("Setting up I18n...");
        net.modificationstation.stationloader.api.common.lang.I18n.INSTANCE.setHandler(new I18n());
        getLogger().info("Setting up BlockManager...");
        BlockManager.INSTANCE.setHandler(new BlockManager());
        getLogger().info("Setting up CraftingRegistry...");
        CraftingRegistry.INSTANCE.setHandler(new CraftingRegistry());
    }

    public void loadMods() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
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
    }

    @Override
    public void addMod(ModMetadata data, String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        getLogger().info("Adding \"" + className + "\" mod");
        Class<?> clazz = Class.forName(className);
        getLogger().info("Found the class");
        Class<? extends StationMod> modClass;
        StationMod mod;
        if (StationMod.class.isAssignableFrom(clazz)) {
            modClass = clazz.asSubclass(StationMod.class);
            mod = modClass.newInstance();
        } else
            throw new RuntimeException("Corrupted mod " + data.getId() + " at " + className);
        getLogger().info("Created an instance");
        String name = data.getName() + "|StationMod";
        mod.setLogger(LogManager.getFormatterLogger(name));
        Configurator.setLevel(name, Level.INFO);
        getLogger().info("Registered logger \"" + name + "\"");
        mod.setConfigPath(Paths.get(FabricLoader.getInstance().getConfigDirectory() + File.separator + data.getId()));
        mod.setDefaultConfig(net.modificationstation.stationloader.api.common.factory.GeneralFactory.INSTANCE.newInst(net.modificationstation.stationloader.api.common.config.Configuration.class, new File(mod.getConfigPath() + File.separator + data.getId() + ".cfg")));
        getLogger().info("Initialized default config");
        String langPathName = "/assets/" + data.getId() + "/lang";
        URL langPath = getClass().getResource(langPathName);
        if (langPath != null) {
            net.modificationstation.stationloader.api.common.lang.I18n.INSTANCE.addLangFolder(langPathName);
            getLogger().info("Registered lang path");
        }
        PreInit.EVENT.register(mod);
        getLogger().info("Registered events");
        mods.put(modClass, mod);
        getLogger().info("Success");
    }

    @Override
    public Collection<StationMod> getAllMods() {
        return Collections.unmodifiableCollection(mods.values());
    }

    @Override
    public Collection<Class<? extends StationMod>> getAllModsClasses() {
        return Collections.unmodifiableCollection(mods.keySet());
    }

    @Override
    public StationMod getModInstance(Class<? extends StationMod> modClass) {
        return mods.get(modClass);
    }

    @Override
    public Logger getLogger() {
        return LOGGER;
    }

    private final Map<Class<? extends StationMod>, StationMod> mods = new HashMap<>();
}
