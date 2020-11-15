package net.modificationstation.stationloader.impl.common;

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
import net.modificationstation.stationloader.api.common.block.EffectiveForTool;
import net.modificationstation.stationloader.api.common.event.block.BlockNameSet;
import net.modificationstation.stationloader.api.common.event.block.BlockRegister;
import net.modificationstation.stationloader.api.common.event.item.ItemNameSet;
import net.modificationstation.stationloader.api.common.event.item.ItemRegister;
import net.modificationstation.stationloader.api.common.event.item.tool.IsEffectiveOn;
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
import net.modificationstation.stationloader.impl.common.util.UnsafeProvider;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;

public class StationLoader implements net.modificationstation.stationloader.api.common.StationLoader {

    @Override
    public void setup() throws IllegalAccessException, ClassNotFoundException, InstantiationException, IOException, URISyntaxException {
        String name = getData().getName();
        setLogger(LogManager.getFormatterLogger(name + "|API"));
        Configurator.setLevel("mixin", Level.TRACE);
        Configurator.setLevel("Fabric|Loader", Level.INFO);
        Configurator.setLevel(name + "|API", Level.INFO);
        getLogger().info("Initializing StationLoader...");
        setSide(null);
        String modid = getData().getId();
        setConfigPath(Paths.get(FabricLoader.getInstance().getConfigDirectory() + File.separator + modid));
        setDefaultConfig(new Configuration(new File(getConfigPath() + File.separator + modid + ".cfg")));
        getLogger().info("Setting up API...");
        setupAPI();
        getDefaultConfig().save();
        getLogger().info("Setting up lang folder...");
        net.modificationstation.stationloader.api.common.lang.I18n.INSTANCE.addLangFolder("/assets/" + modid + "/lang", modid);
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
        getLogger().info("Setting up UnsafeProvider...");
        net.modificationstation.stationloader.api.common.util.UnsafeProvider.INSTANCE.setHandler(new UnsafeProvider());
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
        net.modificationstation.stationloader.api.common.config.Configuration config = getDefaultConfig();
        config.load();
        net.modificationstation.stationloader.api.common.config.Category networkConfig = config.getCategory("Network");
        PacketRegister.EVENT.register((register, customDataPackets) -> register.accept(networkConfig.getProperty("PacketCustomDataID", 254).getIntValue(), true, true, CustomData.class), getData());
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
        getLogger().info("Setting up ItemNameSet...");
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
        getLogger().info("Setting up IsEffectiveOn...");
        IsEffectiveOn.EVENT.register((toolLevel, arg, meta, effective) -> {
            if (arg instanceof EffectiveForTool)
                effective.set(((EffectiveForTool) arg).isEffectiveFor(toolLevel, meta));
        });
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
                    stationMods.put(loaderData, new HashSet<>());
                    modSides.put(loaderData, envType);
                    for (EntrypointMetadata entry : entries)
                        addMod(mod.getMetadata(), envType, entry.getValue());
                }
                getLogger().info("Searching for StationLoader assets in " + loaderData.getName() + " (" + loaderData.getId() + ") mod");
                addModAssets(loaderData);
            }
        getLogger().info("Invoking preInit event");
        PreInit.EVENT.getInvoker().preInit();
        getLogger().info("Invoking init event");
        Init.EVENT.getInvoker().init();
        getLogger().info("Invoking postInit event");
        PostInit.EVENT.getInvoker().postInit();
    }

    @Override
    public void addModAssets(ModMetadata data) throws IOException, URISyntaxException {
        boolean hasAssets = false;
        String modid = data.getId();
        String pathName = "/assets/" + modid + "/" + getData().getId() + "/lang";
        URL path = getClass().getResource(pathName);
        if (path != null) {
            hasAssets = true;
            net.modificationstation.stationloader.api.common.lang.I18n.INSTANCE.addLangFolder(pathName, modid);
            getLogger().info("Registered lang path");
        }
        pathName = "/assets/" + modid + "/" + getData().getId() + "/recipes";
        path = getClass().getResource(pathName);
        if (path != null) {
            hasAssets = true;
            new RecursiveReader(pathName, (file) -> file.endsWith(".json")).read().forEach(net.modificationstation.stationloader.api.common.recipe.RecipeManager.INSTANCE::addJsonRecipe);
            getLogger().info("Listed recipes");
        }
        if (!stationMods.containsKey(data) && !modSides.containsKey(data) && hasAssets) {
            getLogger().info("Registering the mod as assets-only common mod.");
            stationMods.put(data, new HashSet<>());
            modSides.put(data, null);
        }
    }

    @Override
    public void addMod(ModMetadata data, EnvType envType, String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException, IOException, URISyntaxException {
        String modid = data.getId();
        getLogger().info("Adding \"" + className + "\" mod");
        Class<?> clazz = Class.forName(className);
        getLogger().info("Found the class");
        Class<? extends StationMod> modClass;
        StationMod mod;
        if (StationMod.class.isAssignableFrom(clazz)) {
            modClass = clazz.asSubclass(StationMod.class);
            stationMods.get(data).add(modClass);
            mod = modClass.newInstance();
        } else
            throw new RuntimeException("Corrupted mod " + modid + " at " + className);
        getLogger().info("Created an instance");
        mod.setSide(envType);
        getLogger().info("Set mod's side");
        mod.setData(data);
        getLogger().info("Set mod's metadata");
        String name = data.getName() + "|StationMod";
        mod.setLogger(LogManager.getFormatterLogger(name));
        Configurator.setLevel(name, Level.INFO);
        getLogger().info("Registered logger \"" + name + "\"");
        mod.setConfigPath(Paths.get(FabricLoader.getInstance().getConfigDirectory() + File.separator + modid));
        mod.setDefaultConfig(net.modificationstation.stationloader.api.common.factory.GeneralFactory.INSTANCE.newInst(net.modificationstation.stationloader.api.common.config.Configuration.class, new File(mod.getConfigPath() + File.separator + modid + ".cfg")));
        getLogger().info("Initialized default config");
        PreInit.EVENT.register(mod);
        getLogger().info("Registered events");
        stationModInstances.put(modClass, mod);
        getLogger().info("Success");
    }

    @Override
    public Collection<ModMetadata> getAllStationMods() {
        return Collections.unmodifiableCollection(stationMods.keySet());
    }

    @Override
    public Collection<Class<? extends StationMod>> getAllStationModsClasses() {
        return Collections.unmodifiableCollection(stationModInstances.keySet());
    }

    @Override
    public Collection<StationMod> getAllStationModInstances() {
        return Collections.unmodifiableCollection(stationModInstances.values());
    }

    @Override
    public Collection<Class<? extends StationMod>> getStationModClasses(ModMetadata data) {
        return Collections.unmodifiableSet(stationMods.get(data));
    }

    @Override
    public StationMod getModInstance(Class<? extends StationMod> modClass) {
        return stationModInstances.get(modClass);
    }

    @Override
    public EnvType getModSide(ModMetadata data) {
        return modSides.get(data);
    }

    private final Map<ModMetadata, Set<Class<? extends StationMod>>> stationMods = new HashMap<>();
    private final Map<Class<? extends StationMod>, StationMod> stationModInstances = new HashMap<>();
    private final Map<ModMetadata, EnvType> modSides = new HashMap<>();
}
