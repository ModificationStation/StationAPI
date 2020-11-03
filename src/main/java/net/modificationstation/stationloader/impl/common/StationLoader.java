package net.modificationstation.stationloader.impl.common;

import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.metadata.EntrypointMetadata;
import net.fabricmc.loader.metadata.LoaderModMetadata;
import net.fabricmc.loader.metadata.NestedJarEntry;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationloader.api.common.event.block.BlockNameSet;
import net.modificationstation.stationloader.api.common.event.block.BlockRegister;
import net.modificationstation.stationloader.api.common.event.item.ItemNameSet;
import net.modificationstation.stationloader.api.common.event.item.ItemRegister;
import net.modificationstation.stationloader.api.common.event.mod.Init;
import net.modificationstation.stationloader.api.common.event.mod.PostInit;
import net.modificationstation.stationloader.api.common.event.mod.PreInit;
import net.modificationstation.stationloader.api.common.event.packet.PacketRegister;
import net.modificationstation.stationloader.api.common.event.recipe.RecipeRegister;
import net.modificationstation.stationloader.api.common.mod.StationMod;
import net.modificationstation.stationloader.impl.common.achievement.AchievementPage;
import net.modificationstation.stationloader.impl.common.achievement.AchievementPageManager;
import net.modificationstation.stationloader.impl.common.block.BlockManager;
import net.modificationstation.stationloader.impl.common.config.Category;
import net.modificationstation.stationloader.impl.common.config.Configuration;
import net.modificationstation.stationloader.impl.common.config.Property;
import net.modificationstation.stationloader.impl.common.event.Event;
import net.modificationstation.stationloader.impl.common.event.ModIDEvent;
import net.modificationstation.stationloader.impl.common.factory.EnumFactory;
import net.modificationstation.stationloader.impl.common.factory.EventFactory;
import net.modificationstation.stationloader.impl.common.factory.GeneralFactory;
import net.modificationstation.stationloader.impl.common.item.CustomReach;
import net.modificationstation.stationloader.impl.common.lang.I18n;
import net.modificationstation.stationloader.impl.common.packet.CustomData;
import net.modificationstation.stationloader.impl.common.preset.item.PlaceableTileEntityWithMeta;
import net.modificationstation.stationloader.impl.common.preset.item.PlaceableTileEntityWithMetaAndName;
import net.modificationstation.stationloader.impl.common.recipe.CraftingRegistry;
import net.modificationstation.stationloader.impl.common.recipe.RecipeManager;
import net.modificationstation.stationloader.impl.common.recipe.SmeltingRegistry;
import net.modificationstation.stationloader.impl.common.util.RecursiveReader;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;

public class StationLoader implements net.modificationstation.stationloader.api.common.StationLoader {

    protected static final Logger LOGGER = LogManager.getFormatterLogger("StationLoader|API");

    protected StationLoader(ModMetadata data) {
        this.data = data;
    }

    @Override
    public void setup() throws IllegalAccessException, ClassNotFoundException, InstantiationException, IOException, URISyntaxException {
        getLogger().info("Initializing StationLoader...");
        Configurator.setLevel("mixin", Level.TRACE);
        Configurator.setLevel("Fabric|Loader", Level.INFO);
        Configurator.setLevel("StationLoader|API", Level.INFO);
        getLogger().info("Setting up API...");
        setupAPI();
        getLogger().info("Setting up lang folder...");
        net.modificationstation.stationloader.api.common.lang.I18n.INSTANCE.addLangFolder("/assets/stationloader/lang");
        getLogger().info("Loading mods...");
        loadMods();
        getLogger().info("Finished StationLoader setup");
    }

    public void setupAPI() {
        getLogger().info("Setting up GeneralFactory...");
        net.modificationstation.stationloader.api.common.factory.GeneralFactory generalFactory = net.modificationstation.stationloader.api.common.factory.GeneralFactory.INSTANCE;
        generalFactory.setHandler(new GeneralFactory());
        generalFactory.addFactory(net.modificationstation.stationloader.api.common.config.Configuration.class, args -> new Configuration((File) args[0]));
        generalFactory.addFactory(net.modificationstation.stationloader.api.common.config.Category.class, args -> new Category((String) args[0]));
        generalFactory.addFactory(net.modificationstation.stationloader.api.common.config.Property.class, args -> new Property((String) args[0]));
        generalFactory.addFactory(net.modificationstation.stationloader.api.common.achievement.AchievementPage.class, args -> new AchievementPage((String) args[0]));
        generalFactory.addFactory(net.modificationstation.stationloader.api.common.packet.CustomData.class, args -> new CustomData((String) args[0]));
        generalFactory.addFactory(net.modificationstation.stationloader.api.common.preset.item.PlaceableTileEntityWithMeta.class, args -> new PlaceableTileEntityWithMeta((int) args[0]));
        generalFactory.addFactory(net.modificationstation.stationloader.api.common.preset.item.PlaceableTileEntityWithMetaAndName.class, args -> new PlaceableTileEntityWithMetaAndName((int) args[0]));
        net.modificationstation.stationloader.api.common.factory.EnumFactory enumFactory = net.modificationstation.stationloader.api.common.factory.EnumFactory.INSTANCE;
        generalFactory.addFactory(ToolMaterial.class, args -> enumFactory.addEnum(ToolMaterial.class, (String) args[0], new Class[] {int.class, int.class, float.class, int.class}, new Object[] {args[1], args[2], args[3], args[4]}));
        generalFactory.addFactory(EntityType.class, args -> enumFactory.addEnum(EntityType.class, (String) args[0], new Class[] {Class.class, int.class, Material.class, boolean.class}, new Object[] {args[1], args[2], args[3], args[4]}));
        getLogger().info("Setting up EnumFactory...");
        enumFactory.setHandler(new EnumFactory());
        getLogger().info("Setting up EventFactory...");
        net.modificationstation.stationloader.api.common.factory.EventFactory.INSTANCE.setHandler(new EventFactory());
        net.modificationstation.stationloader.api.common.factory.EventFactory.INSTANCE.addEvent(net.modificationstation.stationloader.api.common.event.Event.class, Event::new);
        net.modificationstation.stationloader.api.common.factory.EventFactory.INSTANCE.addEvent(net.modificationstation.stationloader.api.common.event.ModIDEvent.class, ModIDEvent::new);
        getLogger().info("Setting up config...");
        net.modificationstation.stationloader.api.common.config.Configuration config = generalFactory.newInst(net.modificationstation.stationloader.api.common.config.Configuration.class, new File(FabricLoader.getInstance().getConfigDirectory() + File.separator + "stationloader" + File.separator + "stationloader.cfg"));
        config.load();
        getLogger().info("Setting up I18n...");
        net.modificationstation.stationloader.api.common.lang.I18n.INSTANCE.setHandler(new I18n());
        getLogger().info("Setting up BlockManager...");
        net.modificationstation.stationloader.api.common.block.BlockManager.INSTANCE.setHandler(new BlockManager());
        getLogger().info("Setting up RecipeManager...");
        RecipeManager recipeManager = new RecipeManager();
        net.modificationstation.stationloader.api.common.recipe.RecipeManager.INSTANCE.setHandler(recipeManager);
        RecipeRegister.EVENT.register(recipeManager);
        getLogger().info("Setting up CraftingRegistry...");
        net.modificationstation.stationloader.api.common.recipe.CraftingRegistry.INSTANCE.setHandler(new CraftingRegistry());
        getLogger().info("Setting up SmeltingRegistry...");
        net.modificationstation.stationloader.api.common.recipe.SmeltingRegistry.INSTANCE.setHandler(new SmeltingRegistry());
        getLogger().info("Setting up CustomReach...");
        net.modificationstation.stationloader.api.common.item.CustomReach.CONSUMERS.put("setDefaultBlockReach", CustomReach::setDefaultBlockReach);
        net.modificationstation.stationloader.api.common.item.CustomReach.CONSUMERS.put("setHandBlockReach", CustomReach::setHandBlockReach);
        net.modificationstation.stationloader.api.common.item.CustomReach.CONSUMERS.put("setDefaultEntityReach", CustomReach::setDefaultEntityReach);
        net.modificationstation.stationloader.api.common.item.CustomReach.CONSUMERS.put("setHandEntityReach", CustomReach::setHandEntityReach);
        net.modificationstation.stationloader.api.common.item.CustomReach.SUPPLIERS.put("getDefaultBlockReach", CustomReach::getDefaultBlockReach);
        net.modificationstation.stationloader.api.common.item.CustomReach.SUPPLIERS.put("getHandBlockReach", CustomReach::getHandBlockReach);
        net.modificationstation.stationloader.api.common.item.CustomReach.SUPPLIERS.put("getDefaultEntityReach", CustomReach::getDefaultEntityReach);
        net.modificationstation.stationloader.api.common.item.CustomReach.SUPPLIERS.put("getHandEntityReach", CustomReach::getHandEntityReach);
        getLogger().info("Setting up AchievementPageManager...");
        net.modificationstation.stationloader.api.common.achievement.AchievementPageManager.INSTANCE.setHandler(new AchievementPageManager());
        getLogger().info("Setting up CustomData packet...");
        net.modificationstation.stationloader.api.common.config.Category networkConfig = config.getCategory("Network");
        PacketRegister.EVENT.register((register, customDataPackets) -> register.accept(networkConfig.getProperty("PacketCustomDataID", 254).getIntValue(), true, true, CustomData.class));
        getLogger().info("Setting up BlockNameSet...");
        BlockNameSet.EVENT.register((block, name) -> {
            net.modificationstation.stationloader.api.common.event.ModIDEvent<BlockRegister> event = BlockRegister.EVENT;
            BlockRegister listener = event.getCurrentListener();
            if (listener != null) {
                String modid = event.getListenerModID(listener);
                if (modid != null) {
                    modid += ":";
                    if (!name.startsWith(modid) && !name.contains(":"))
                        return modid + name;
                }
            }
            return name;
        });
        ItemNameSet.EVENT.register((item, name) -> {
            net.modificationstation.stationloader.api.common.event.ModIDEvent<ItemRegister> event = ItemRegister.EVENT;
            ItemRegister listener = event.getCurrentListener();
            if (listener != null) {
                String modid = event.getListenerModID(listener);
                if (modid != null) {
                    modid += ":";
                    if (!name.startsWith(modid) && !name.contains(":"))
                        return modid + name;
                }
            }
            return name;
        });
        config.save();
    }

    public void loadMods() throws IllegalAccessException, InstantiationException, ClassNotFoundException, IOException, URISyntaxException {
        for (ModContainer mod : FabricLoader.getInstance().getAllMods())
            if (mod.getMetadata() instanceof LoaderModMetadata) {
                LoaderModMetadata loaderData = ((LoaderModMetadata) mod.getMetadata());
                EnvType envType = FabricLoader.getInstance().getEnvironmentType();
                List<EntrypointMetadata> entries = loaderData.getEntrypoints("stationmod_" + envType.name().toLowerCase());
                if (entries.isEmpty()) {
                    envType = null;
                    entries = loaderData.getEntrypoints("stationmod");
                }
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
                        addMod(mod.getMetadata(), envType, entry.getValue());
                }
            }
        getLogger().info("Invoking preInit event");
        PreInit.EVENT.getInvoker().preInit();
        getLogger().info("Invoking init event");
        Init.EVENT.getInvoker().init();
        getLogger().info("Invoking postInit event");
        PostInit.EVENT.getInvoker().postInit();
    }

    @Override
    public void addMod(ModMetadata data, EnvType envType, String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException, IOException, URISyntaxException {
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
        mod.setSide(envType);
        getLogger().info("Set mod's side");
        mod.setData(data);
        getLogger().info("Set mod's metadata");
        String name = data.getName() + "|StationMod";
        mod.setLogger(LogManager.getFormatterLogger(name));
        Configurator.setLevel(name, Level.INFO);
        getLogger().info("Registered logger \"" + name + "\"");
        mod.setConfigPath(Paths.get(FabricLoader.getInstance().getConfigDirectory() + File.separator + data.getId()));
        mod.setDefaultConfig(net.modificationstation.stationloader.api.common.factory.GeneralFactory.INSTANCE.newInst(net.modificationstation.stationloader.api.common.config.Configuration.class, new File(mod.getConfigPath() + File.separator + data.getId() + ".cfg")));
        getLogger().info("Initialized default config");
        String pathName = "/assets/" + data.getId() + "/lang";
        URL path = getClass().getResource(pathName);
        if (path != null) {
            net.modificationstation.stationloader.api.common.lang.I18n.INSTANCE.addLangFolder(pathName);
            getLogger().info("Registered lang path");
        }
        pathName = "/assets/" +data.getId() + "/recipes";
        path = getClass().getResource(pathName);
        if (path != null) {
            new RecursiveReader(pathName, (file) -> file.endsWith(".json")).read().forEach(net.modificationstation.stationloader.api.common.recipe.RecipeManager.INSTANCE::addJsonRecipe);
            getLogger().info("Listed recipes");
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

    @Getter
    private final ModMetadata data;
    private final Map<Class<? extends StationMod>, StationMod> mods = new HashMap<>();
}
