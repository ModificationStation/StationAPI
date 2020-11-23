package net.modificationstation.stationloader.impl.common;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationloader.api.common.block.EffectiveForTool;
import net.modificationstation.stationloader.api.common.event.block.BlockNameSet;
import net.modificationstation.stationloader.api.common.event.block.BlockRegister;
import net.modificationstation.stationloader.api.common.event.block.TileEntityRegister;
import net.modificationstation.stationloader.api.common.event.item.ItemNameSet;
import net.modificationstation.stationloader.api.common.event.item.ItemRegister;
import net.modificationstation.stationloader.api.common.event.item.tool.IsEffectiveOn;
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
import net.modificationstation.stationloader.impl.common.util.ReflectionHelper;
import net.modificationstation.stationloader.impl.common.util.UnsafeProvider;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;

public class StationLoader implements net.modificationstation.stationloader.api.common.StationLoader {

    @Override
    public void setup() {
        entrypoints.add("stationmod");
        entrypoints.add(getContainer().getMetadata().getId() + ":mod");
        String sideName = FabricLoader.getInstance().getEnvironmentType().name().toLowerCase();
        entrypoints.add("stationmod_" + sideName);
        entrypoints.add(getContainer().getMetadata().getId() + ":mod_" + sideName);
        String name = getContainer().getMetadata().getName();
        setLogger(LogManager.getFormatterLogger(name + "|API"));
        Configurator.setLevel("mixin", Level.TRACE);
        Configurator.setLevel("Fabric|Loader", Level.INFO);
        Configurator.setLevel(name + "|API", Level.INFO);
        getLogger().info("Initializing StationLoader...");
        String modid = getContainer().getMetadata().getId();
        setConfigPath(Paths.get(FabricLoader.getInstance().getConfigDirectory() + File.separator + modid));
        setDefaultConfig(new Configuration(new File(getConfigPath() + File.separator + modid + ".cfg")));
        getLogger().info("Setting up API...");
        setupAPI();
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
        getLogger().info("Loading config...");
        net.modificationstation.stationloader.api.common.config.Configuration config = getDefaultConfig();
        config.load();
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
        SmeltingRegistry smeltingRegistry = new SmeltingRegistry();
        net.modificationstation.stationloader.api.common.recipe.SmeltingRegistry.INSTANCE.setHandler(smeltingRegistry);
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
        PacketRegister.EVENT.register((register, customDataPackets) -> {
            register.accept(networkConfig.getProperty("PacketCustomDataID", 254).getIntValue(), true, true, CustomData.class);
            config.save();
        }, getContainer().getMetadata());
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
        getLogger().info("Setting up TileEntityRegister...");
        TileEntityRegister.EVENT.register(smeltingRegistry);
    }

    @Override
    public void loadMods() {
        getLogger().info("Loading entrypoints...");
        entrypoints.forEach(entrypoint -> FabricLoader.getInstance().getEntrypointContainers(entrypoint, StationMod.class).forEach(this::addMod));
        getLogger().info("Loading assets...");
        FabricLoader.getInstance().getAllMods().forEach(this::addModAssets);
        getLogger().info("Gathering mods that require client verification...");
        String value = getContainer().getMetadata().getId() + ":verify_client";
        getAllMods().forEach(modContainer -> {
            ModMetadata modMetadata = modContainer.getMetadata();
            if (!modMetadata.containsCustomValue(value) || modMetadata.getCustomValue(value).getAsBoolean())
                modsToVerifyOnClient.add(modContainer);
        });
        getLogger().info("Invoking preInit event...");
        PreInit.EVENT.getInvoker().preInit();
    }

    @Override
    public void addMod(EntrypointContainer<StationMod> stationModEntrypointContainer) {
        ModContainer modContainer = stationModEntrypointContainer.getProvider();
        StationMod stationMod = stationModEntrypointContainer.getEntrypoint();
        ModMetadata modMetadata = modContainer.getMetadata();
        for (Field field : ReflectionHelper.getFieldsWithAnnotation(stationMod.getClass(), StationMod.Instance.class)) {
            try {
                ReflectionHelper.setFinalField(field, stationMod, stationMod);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(String.format("Failed to set \"%s\" field of %s (%s)'s \"%s\" class!", field.getName(), modMetadata.getName(), modMetadata.getId(), stationMod.getClass().getName()), e);
            }
            getLogger().info("Set \"" + field.getName() + "\" field to mod's instance");
        }
        stationMod.setContainer(modContainer);
        getLogger().info("Set mod's container");
        String name = modMetadata.getName() + "|StationMod";
        stationMod.setLogger(LogManager.getFormatterLogger(name));
        Configurator.setLevel(name, Level.INFO);
        getLogger().info("Registered logger \"" + name + "\"");
        stationMod.setConfigPath(Paths.get(FabricLoader.getInstance().getConfigDirectory() + File.separator + modMetadata.getId()));
        stationMod.setDefaultConfig(net.modificationstation.stationloader.api.common.factory.GeneralFactory.INSTANCE.newInst(net.modificationstation.stationloader.api.common.config.Configuration.class, new File(stationMod.getConfigPath() + File.separator + modMetadata.getId() + ".cfg")));
        getLogger().info("Initialized default config");
        PreInit.EVENT.register(stationMod);
        getLogger().info("Registered events");
        mods.compute(modContainer, (modContainer1, stationMods) -> {
            stationMods = stationMods == null ? new HashSet<>() : stationMods;
            stationMods.add(stationMod);
            return stationMods;
        });
        getLogger().info(String.format("Done loading %s (%s)'s \"%s\" StationMod", modMetadata.getName(), modMetadata.getId(), stationMod.getClass().getName()));
    }

    @Override
    public void addModAssets(ModContainer modContainer) {
        ModMetadata modMetadata = modContainer.getMetadata();
        String modid = modMetadata.getId();
        String slSubFolder = "/assets/" + modid + "/" + getContainer().getMetadata().getId();
        URL path = getClass().getResource(slSubFolder);
        if (path != null)
            mods.putIfAbsent(modContainer, new HashSet<>());
        String pathName = slSubFolder + "/lang";
        path = getClass().getResource(pathName);
        if (path != null) {
            net.modificationstation.stationloader.api.common.lang.I18n.INSTANCE.addLangFolder(pathName, modid);
            getLogger().info("Registered lang path");
        }
        pathName = slSubFolder + "/recipes";
        path = getClass().getResource(pathName);
        if (path != null) {
            try {
                for (URL url : new RecursiveReader(pathName, (file) -> file.endsWith(".json")).read())
                    net.modificationstation.stationloader.api.common.recipe.RecipeManager.INSTANCE.addJsonRecipe(url);
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
            getLogger().info("Listed recipes");
        }
    }

    @Override
    public Collection<ModContainer> getAllMods() {
        return Collections.unmodifiableSet(mods.keySet());
    }

    @Override
    public Set<StationMod> getAllModInstances() {
        return Collections.unmodifiableSet(Sets.newHashSet(Iterables.concat(mods.values())));
    }

    @Override
    public Set<StationMod> getModInstances(ModContainer modContainer) {
        return Collections.unmodifiableSet(mods.get(modContainer));
    }

    @Override
    public Set<ModContainer> getModsToVerifyOnClient() {
        return Collections.unmodifiableSet(modsToVerifyOnClient);
    }

    protected final Set<String> entrypoints = new HashSet<>();

    private final Map<ModContainer, Set<StationMod>> mods = new HashMap<>();
    private final Set<ModContainer> modsToVerifyOnClient = new HashSet<>();

    //private final Map<ModMetadata, Set<Class<? extends StationMod>>> stationMods = new HashMap<>();
    //private final Map<Class<? extends StationMod>, StationMod> stationModInstances = new HashMap<>();
    //private final Map<ModMetadata, EnvType> modSides = new HashMap<>();
}
