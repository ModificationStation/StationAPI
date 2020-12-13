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
import net.modificationstation.stationloader.api.common.event.EventRegistry;
import net.modificationstation.stationloader.api.common.event.ModEvent;
import net.modificationstation.stationloader.api.common.event.achievement.AchievementRegister;
import net.modificationstation.stationloader.api.common.event.block.BlockNameSet;
import net.modificationstation.stationloader.api.common.event.block.BlockRegister;
import net.modificationstation.stationloader.api.common.event.block.TileEntityRegister;
import net.modificationstation.stationloader.api.common.event.container.slot.ItemUsedInCrafting;
import net.modificationstation.stationloader.api.common.event.entity.EntityRegister;
import net.modificationstation.stationloader.api.common.event.entity.player.PlayerHandlerRegister;
import net.modificationstation.stationloader.api.common.event.item.ItemCreation;
import net.modificationstation.stationloader.api.common.event.item.ItemNameSet;
import net.modificationstation.stationloader.api.common.event.item.ItemRegister;
import net.modificationstation.stationloader.api.common.event.item.tool.EffectiveBlocksProvider;
import net.modificationstation.stationloader.api.common.event.item.tool.IsEffectiveOn;
import net.modificationstation.stationloader.api.common.event.level.LevelInit;
import net.modificationstation.stationloader.api.common.event.level.biome.BiomeByClimateProvider;
import net.modificationstation.stationloader.api.common.event.level.biome.BiomeRegister;
import net.modificationstation.stationloader.api.common.event.level.gen.ChunkPopulator;
import net.modificationstation.stationloader.api.common.event.mod.Init;
import net.modificationstation.stationloader.api.common.event.mod.PostInit;
import net.modificationstation.stationloader.api.common.event.mod.PreInit;
import net.modificationstation.stationloader.api.common.event.packet.MessageListenerRegister;
import net.modificationstation.stationloader.api.common.event.packet.PacketRegister;
import net.modificationstation.stationloader.api.common.event.recipe.RecipeRegister;
import net.modificationstation.stationloader.api.common.mod.StationMod;
import net.modificationstation.stationloader.api.common.packet.Message;
import net.modificationstation.stationloader.api.common.packet.MessageListenerRegistry;
import net.modificationstation.stationloader.api.common.registry.Identifier;
import net.modificationstation.stationloader.api.common.registry.ModID;
import net.modificationstation.stationloader.api.common.resource.RecursiveReader;
import net.modificationstation.stationloader.impl.common.achievement.AchievementPage;
import net.modificationstation.stationloader.impl.common.achievement.AchievementPageManager;
import net.modificationstation.stationloader.impl.common.block.BlockManager;
import net.modificationstation.stationloader.impl.common.config.Category;
import net.modificationstation.stationloader.impl.common.config.Configuration;
import net.modificationstation.stationloader.impl.common.config.Property;
import net.modificationstation.stationloader.impl.common.factory.EnumFactory;
import net.modificationstation.stationloader.impl.common.factory.GeneralFactory;
import net.modificationstation.stationloader.impl.common.item.CustomReach;
import net.modificationstation.stationloader.impl.common.lang.I18n;
import net.modificationstation.stationloader.impl.common.preset.item.PlaceableTileEntityWithMeta;
import net.modificationstation.stationloader.impl.common.preset.item.PlaceableTileEntityWithMetaAndName;
import net.modificationstation.stationloader.impl.common.recipe.CraftingRegistry;
import net.modificationstation.stationloader.impl.common.recipe.RecipeManager;
import net.modificationstation.stationloader.impl.common.recipe.SmeltingRegistry;
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

public class StationLoader implements net.modificationstation.stationloader.api.common.StationLoader, PreInit, Init {

    @Override
    public void setup() {
        ModID modID = getModID();
        entrypoints.add("stationmod");
        entrypoints.add(modID + ":mod");
        String sideName = FabricLoader.getInstance().getEnvironmentType().name().toLowerCase();
        entrypoints.add("stationmod_" + sideName);
        entrypoints.add(modID + ":mod_" + sideName);
        String name = modID.getContainer().getMetadata().getName();
        setLogger(LogManager.getFormatterLogger(name + "|API"));
        Configurator.setLevel("mixin", Level.TRACE);
        Configurator.setLevel("Fabric|Loader", Level.INFO);
        Configurator.setLevel(name + "|API", Level.INFO);
        getLogger().info("Initializing StationLoader...");
        PreInit.EVENT.register(this, getModID());
        Init.EVENT.register(this);
        setConfigPath(Paths.get(FabricLoader.getInstance().getConfigDirectory() + File.separator + modID));
        setDefaultConfig(new Configuration(new File(getConfigPath() + File.separator + modID + ".cfg")));
        getLogger().info("Setting up API...");
        setupAPI();
        getLogger().info("Setting up lang folder...");
        net.modificationstation.stationloader.api.common.lang.I18n.INSTANCE.addLangFolder("/assets/" + modID + "/lang", modID);
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
        PacketRegister.EVENT.register(register -> {
            register.accept(networkConfig.getProperty("PacketCustomDataID", 254).getIntValue(), true, true, Message.class);
            config.save();
            MessageListenerRegister.EVENT.getInvoker().registerMessageListeners(MessageListenerRegistry.INSTANCE, MessageListenerRegister.EVENT.getListenerModID(MessageListenerRegister.EVENT.getInvoker()));
        });
        getLogger().info("Setting up BlockNameSet...");
        BlockNameSet.EVENT.register((block, name) -> {
            ModEvent<BlockRegister> event = BlockRegister.EVENT;
            BlockRegister listener = event.getCurrentListener();
            if (listener != null) {
                ModID modID = event.getListenerModID(listener);
                if (modID != null) {
                    String modid = modID + ":";
                    if (!name.startsWith(modid) && !name.contains(":"))
                        return modid + name;
                }
            }
            return name;
        });
        getLogger().info("Setting up ItemNameSet...");
        ItemNameSet.EVENT.register((item, name) -> {
            ModEvent<ItemRegister> event = ItemRegister.EVENT;
            ItemRegister listener = event.getCurrentListener();
            if (listener != null) {
                ModID modID = event.getListenerModID(listener);
                if (modID != null) {
                    String modid = modID + ":";
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
        String value = getModID() + ":verify_client";
        getAllMods().forEach(modContainer -> {
            ModMetadata modMetadata = modContainer.getMetadata();
            if (!modMetadata.containsCustomValue(value) || modMetadata.getCustomValue(value).getAsBoolean())
                modsToVerifyOnClient.add(modContainer);
        });
        getLogger().info("Invoking preInit event...");
        PreInit.EVENT.getInvoker().preInit(EventRegistry.INSTANCE, PreInit.EVENT.getListenerModID(PreInit.EVENT.getInvoker()));
        getLogger().info("Invoking init event...");
        Init.EVENT.getInvoker().init();
        getLogger().info("Invoking postInit event...");
        PostInit.EVENT.getInvoker().postInit();
    }

    @Override
    public void preInit(EventRegistry eventRegistry, ModID modID) {
        eventRegistry.registerValue(Identifier.of(modID, "achievement_register"), AchievementRegister.EVENT);
        eventRegistry.registerValue(Identifier.of(modID, "block_name_set"), BlockNameSet.EVENT);
        eventRegistry.registerValue(Identifier.of(modID, "block_register"), BlockRegister.EVENT);
        eventRegistry.registerValue(Identifier.of(modID, "tile_entity_register"), TileEntityRegister.EVENT);
        eventRegistry.registerValue(Identifier.of(modID, "item_used_in_crafting"), ItemUsedInCrafting.EVENT);
        eventRegistry.registerValue(Identifier.of(modID, "player_handler_register"), PlayerHandlerRegister.EVENT);
        eventRegistry.registerValue(Identifier.of(modID, "entity_register"), EntityRegister.EVENT);
        eventRegistry.registerValue(Identifier.of(modID, "effective_blocks_provider"), EffectiveBlocksProvider.EVENT);
        eventRegistry.registerValue(Identifier.of(modID, "is_effective_on"), IsEffectiveOn.EVENT);
        eventRegistry.registerValue(Identifier.of(modID, "item_creation"), ItemCreation.EVENT);
        eventRegistry.registerValue(Identifier.of(modID, "item_name_set"), ItemNameSet.EVENT);
        eventRegistry.registerValue(Identifier.of(modID, "item_register"), ItemRegister.EVENT);
        eventRegistry.registerValue(Identifier.of(modID, "biome_by_climate_provider"), BiomeByClimateProvider.EVENT);
        eventRegistry.registerValue(Identifier.of(modID, "biome_register"), BiomeRegister.EVENT);
        eventRegistry.registerValue(Identifier.of(modID, "chunk_populator"), ChunkPopulator.EVENT);
        eventRegistry.registerValue(Identifier.of(modID, "packet_register"), PacketRegister.EVENT);
        eventRegistry.registerValue(Identifier.of(modID, "recipe_register"), RecipeRegister.EVENT);
        eventRegistry.registerValue(Identifier.of(modID, "level_init"), LevelInit.EVENT);
        eventRegistry.registerValue(Identifier.of(modID, "message_listener_register"), MessageListenerRegister.EVENT);
    }

    @Override
    public void init() {
        EventRegistry.INSTANCE.forEach((identifier, event) -> event.register(identifier));
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
        ModID modID = ModID.of(modContainer);
        stationMod.setModID(modID);
        getLogger().info("Set mod's container");
        String name = modMetadata.getName() + "|StationMod";
        stationMod.setLogger(LogManager.getFormatterLogger(name));
        Configurator.setLevel(name, Level.INFO);
        getLogger().info("Registered logger \"" + name + "\"");
        stationMod.setConfigPath(Paths.get(FabricLoader.getInstance().getConfigDirectory() + File.separator + modMetadata.getId()));
        stationMod.setDefaultConfig(net.modificationstation.stationloader.api.common.factory.GeneralFactory.INSTANCE.newInst(net.modificationstation.stationloader.api.common.config.Configuration.class, new File(stationMod.getConfigPath() + File.separator + modMetadata.getId() + ".cfg")));
        getLogger().info("Initialized default config");
        if (stationMod instanceof PreInit)
            PreInit.EVENT.register((PreInit) stationMod, modID);
        Init.EVENT.register(stationMod);
        if (stationMod instanceof PostInit)
            PostInit.EVENT.register((PostInit) stationMod);
        getLogger().info("Registered events");
        mods.computeIfAbsent(modContainer, modContainer1 -> new HashSet<>()).add(stationMod);
        getLogger().info(String.format("Done loading %s (%s)'s \"%s\" StationMod", modMetadata.getName(), modMetadata.getId(), stationMod.getClass().getName()));
    }

    @Override
    public void addModAssets(ModContainer modContainer) {
        ModID modID = ModID.of(modContainer);
        String slSubFolder = "/assets/" + modID + "/" + getModID();
        URL path = getClass().getResource(slSubFolder);
        if (path != null)
            mods.putIfAbsent(modContainer, new HashSet<>());
        String pathName = slSubFolder + "/lang";
        path = getClass().getResource(pathName);
        if (path != null) {
            net.modificationstation.stationloader.api.common.lang.I18n.INSTANCE.addLangFolder(pathName, modID);
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
