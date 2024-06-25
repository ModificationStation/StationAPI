package net.modificationstation.stationapi.impl.config;


import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.config.ConfigCategory;
import net.modificationstation.stationapi.api.config.ConfigEntry;
import net.modificationstation.stationapi.api.config.ConfigFactoryProvider;
import net.modificationstation.stationapi.api.config.ConfigRoot;
import net.modificationstation.stationapi.api.config.GeneratedConfig;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.ReflectionHelper;
import net.modificationstation.stationapi.impl.config.object.ConfigHandlerBase;
import net.modificationstation.stationapi.impl.config.object.ConfigCategoryHandler;
import net.modificationstation.stationapi.impl.config.object.ConfigEntryHandler;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.simpleyaml.configuration.file.YamlFileWrapper;
import uk.co.benjiweber.expressions.function.SeptFunction;
import uk.co.benjiweber.expressions.tuple.BiTuple;
import uk.co.benjiweber.expressions.tuple.QuadTuple;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

/**
 * Do not use this class directly in your code.
 * This class changes a lot between updates, and should never ever be used by a mod using GCAPI, as there are update-safe wrappers for most of this class' functionality inside other classes.
 */
@SuppressWarnings("DeprecatedIsStillUsed") // shush, I just don't want others using this class without getting yelled at.
@Deprecated
public class GCCore implements PreLaunchEntrypoint {
    public static final ModContainer NAMESPACE = FabricLoader.getInstance().getModContainer("gcapi").orElseThrow(RuntimeException::new);
    public static final HashMap<Identifier, ConfigRootEntry> MOD_CONFIGS = new HashMap<>();

    public static final HashMap<Identifier, HashMap<String, Object>> DEFAULT_MOD_CONFIGS = new HashMap<>();
    private static boolean loaded = false;
    public static boolean isMultiplayer = false;
    private static final Logger LOGGER = LogManager.getFormatterLogger("GCAPI");

    static {
        Configurator.setLevel("GCAPI", Level.INFO);
    }

    public static void loadServerConfig(String modID, String string) {
        AtomicReference<Identifier> mod = new AtomicReference<>();
        MOD_CONFIGS.keySet().forEach(modContainer -> {
            if (modContainer.toString().equals(modID)) {
                mod.set(modContainer);
            }
        });
        if (mod.get() != null) {
            ConfigRootEntry rootEntry = MOD_CONFIGS.get(mod.get());
            saveConfig(rootEntry.modContainer(), rootEntry.configCategoryHandler(), EventStorage.EventSource.SERVER_JOIN | EventStorage.EventSource.MODDED_SERVER_JOIN);
            try {
                loadModConfig(rootEntry.configRoot(), rootEntry.modContainer(), rootEntry.configCategoryHandler().parentField, mod.get(), new GlassYamlFile(string));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void exportConfigsForServer(NbtCompound nbtCompound) {
        for (Identifier modContainer : MOD_CONFIGS.keySet()) {
            ConfigRootEntry entry = MOD_CONFIGS.get(modContainer);
            nbtCompound.putString(modContainer.toString(), saveConfig(entry.modContainer(), entry.configCategoryHandler(), EventStorage.EventSource.SERVER_EXPORT));
        }
    }

    @Override
    public void onPreLaunch() {
        loadConfigs();
    }

    public static void log(String message) {
        LOGGER.info(message);
    }

    public static void log(Level level, String message) {
        LOGGER.log(level, message);
    }

    private static void loadConfigs() {
        if (loaded) {
            log(Level.WARN, "Tried to load configs a second time! Printing stacktrace and aborting!");
            new Exception("Stacktrace for duplicate loadConfigs call!").printStackTrace();
            return;
        }
        log("Loading config factories.");

        List<EntrypointContainer<ConfigFactoryProvider>> containers = FabricLoader.getInstance().getEntrypointContainers("gcapi:factory_provider", ConfigFactoryProvider.class);

        ImmutableMap.Builder<Type, SeptFunction<String, ConfigEntry, Field, Object, Boolean, Object, Object, ConfigEntryHandler<?>>> loadImmutableBuilder = ImmutableMap.builder();
        containers.forEach((customConfigFactoryProviderEntrypointContainer -> customConfigFactoryProviderEntrypointContainer.getEntrypoint().provideLoadFactories(loadImmutableBuilder)));
        ConfigFactories.loadFactories = loadImmutableBuilder.build();
        log(ConfigFactories.loadFactories.size() + " config load factories loaded.");

        ImmutableMap.Builder<Type, Function<Object, Object>> saveImmutableBuilder = ImmutableMap.builder();
        containers.forEach((customConfigFactoryProviderEntrypointContainer -> customConfigFactoryProviderEntrypointContainer.getEntrypoint().provideSaveFactories(saveImmutableBuilder)));
        ConfigFactories.saveFactories = saveImmutableBuilder.build();
        log(ConfigFactories.saveFactories.size() + " config save factories loaded.");

        //noinspection rawtypes
        ImmutableMap.Builder<Type, Class> loadTypeAdapterImmutableBuilder = ImmutableMap.builder();
        containers.forEach((customConfigFactoryProviderEntrypointContainer -> customConfigFactoryProviderEntrypointContainer.getEntrypoint().provideLoadTypeAdapterFactories(loadTypeAdapterImmutableBuilder)));
        ConfigFactories.loadTypeAdapterFactories = loadTypeAdapterImmutableBuilder.build();
        log(ConfigFactories.loadTypeAdapterFactories.size() + " config load transformer factories loaded.");

        log("Loading config event listeners.");
        EventStorage.loadListeners();
        log("Loaded config event listeners.");

        FabricLoader.getInstance().getEntrypointContainers(NAMESPACE.getMetadata().getId(), Object.class).forEach((entrypointContainer -> {
            try {
                for (Field field : ReflectionHelper.getFieldsWithAnnotation(entrypointContainer.getEntrypoint().getClass(), ConfigRoot.class)) {
                    Identifier configID = Identifier.of(entrypointContainer.getProvider().getMetadata().getId() + ":" + field.getAnnotation(ConfigRoot.class).value());
                    MOD_CONFIGS.put(configID, new ConfigRootEntry(entrypointContainer.getProvider(), field.getAnnotation(ConfigRoot.class), entrypointContainer.getEntrypoint(), null));
                    loadModConfig(entrypointContainer.getEntrypoint(), entrypointContainer.getProvider(), field, configID, null);
                    saveConfig(entrypointContainer.getProvider(), MOD_CONFIGS.get(configID).configCategoryHandler(), EventStorage.EventSource.GAME_LOAD);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if (EventStorage.POST_LOAD_LISTENERS.containsKey(entrypointContainer.getProvider().getMetadata().getId())) {
                EventStorage.POST_LOAD_LISTENERS.get(entrypointContainer.getProvider().getMetadata().getId()).getEntrypoint().PostConfigLoaded(EventStorage.EventSource.GAME_LOAD);
            }
        }));
        loaded = true;
    }

    public static void loadModConfig(Object rootConfigObject, ModContainer modContainer, Field configField, Identifier configID, GlassYamlFile jsonOverride) {
        AtomicInteger totalReadCategories = new AtomicInteger();
        AtomicInteger totalReadFields = new AtomicInteger();
        try {
            configField.setAccessible(true);
            Object objField = configField.get(rootConfigObject);
            if(objField instanceof GeneratedConfig generatedConfig) {
                if(!generatedConfig.shouldLoad()) {
                    return;
                }
            }
            GlassYamlFile modConfigFile = new GlassYamlFile(new File(FabricLoader.getInstance().getConfigDir().toFile(), modContainer.getMetadata().getId() + "/" + configField.getAnnotation(ConfigRoot.class).value() + ".yml"));
            if (jsonOverride == null) {
                modConfigFile.createOrLoad();
            }
            else {
                modConfigFile.merge(jsonOverride);
                isMultiplayer = modConfigFile.getBoolean("multiplayer", false);
                // Try to catch mods reloading configs while on a server.
                if(FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT && !isMultiplayer) {
                    isMultiplayer = ((Minecraft) FabricLoader.getInstance().getGameInstance()).world.isRemote;
                }
                if (isMultiplayer) {
                    log("Loading server config for " + modContainer.getMetadata().getId() + "!");
                }
                else {
                    log("Loading forced mod config for " + modContainer.getMetadata().getId() + "!");
                }
            }
            HashMap<String, Object> defaultEntry;
            if(!loaded) {
                defaultEntry = new HashMap<>();
                DEFAULT_MOD_CONFIGS.put(configID, defaultEntry);
            }
            else {
                defaultEntry = DEFAULT_MOD_CONFIGS.get(configID);
            }
            ConfigRoot rootConfigAnnotation = configField.getAnnotation(ConfigRoot.class);
            ConfigCategoryHandler configCategory = new ConfigCategoryHandler(modContainer.getMetadata().getId(), rootConfigAnnotation.visibleName(), null, configField, objField, rootConfigAnnotation.multiplayerSynced(), HashMultimap.create(), true);
            readDeeper(rootConfigObject, configField, modConfigFile.path(), configCategory, totalReadFields, totalReadCategories, isMultiplayer, defaultEntry);
            if (!loaded) {
                ConfigRootEntry oldEntry = MOD_CONFIGS.remove(configID);
                MOD_CONFIGS.put(configID, new ConfigRootEntry(oldEntry.modContainer(), oldEntry.configRoot(), oldEntry.configObject(), configCategory));
            } else {
                MOD_CONFIGS.get(configID).configCategoryHandler().values = configCategory.values;
            }
            log("Successfully read \"" + configID + "\"'s mod configs, reading " + totalReadCategories.get() + " categories, and " + totalReadFields.get() + " values.");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void readDeeper(Object rootConfigObject, Field configField, GlassYamlWrapper rootJsonObject, ConfigCategoryHandler category, AtomicInteger totalReadFields, AtomicInteger totalReadCategories, boolean isMultiplayer, HashMap<String, Object> defaultConfig) throws IllegalAccessException {
        totalReadCategories.getAndIncrement();
        configField.setAccessible(true);
        Object objField = configField.get(rootConfigObject);

        Field[] fields;
        if(objField instanceof GeneratedConfig config) {
            fields = config.getFields();
        }
        else {
            fields = objField.getClass().getDeclaredFields();
        }

        for (Field field : fields) {
            Object childObjField = field.get(objField);
            if(objField instanceof GeneratedConfig generatedConfig) {
                if(!generatedConfig.shouldLoad()) {
                    continue;
                }
            }
            if (field.isAnnotationPresent(ConfigCategory.class)) {
                ConfigCategory configCategoryAnnotation = field.getAnnotation(ConfigCategory.class);
                GlassYamlWrapper jsonCategory = rootJsonObject.path(field.getName());
                ConfigCategoryHandler childCategory = new ConfigCategoryHandler(
                        field.getName(),
                        configCategoryAnnotation.name(),
                        configCategoryAnnotation.description(),
                        field,
                        objField,
                        category.multiplayerSynced || configCategoryAnnotation.multiplayerSynced(),
                        HashMultimap.create(),
                        false
                );
                category.values.put(ConfigCategory.class, childCategory);
                HashMap<String, Object> childDefaultConfig;
                if(!loaded) {
                    childDefaultConfig = new HashMap<>();
                    defaultConfig.put(childCategory.id, childDefaultConfig);
                }
                else {
                    //noinspection unchecked
                    childDefaultConfig = (HashMap<String, Object>) defaultConfig.get(childCategory.id);
                }
                readDeeper(objField, field, jsonCategory, childCategory, totalReadFields, totalReadCategories, isMultiplayer, childDefaultConfig);
            }
            else {
                if (!field.isAnnotationPresent(ConfigEntry.class)) {
                    throw new RuntimeException("Config value \"" + field.getType().getName() + ";" + field.getName() + "\" has no ConfigName annotation!");
                }
                if (field.getType() == HashMap.class) {
                    throw new RuntimeException("Config value \"" + field.getType().getName() + ";" + field.getName() + "\" is a HashMap! Create a new HashMap subclass, as the basic type is used in GCAPI internals!");
                }
                SeptFunction<String, ConfigEntry, Field, Object, Boolean, Object, Object, ConfigEntryHandler<?>> function = ConfigFactories.loadFactories.get(field.getType());
                if (function == null) {
                    throw new RuntimeException("Config value \"" + field.getType().getName() + ";" + field.getName() + "\" has no config loader for it's type!");
                }
                if (Modifier.isStatic(field.getModifiers())) {
                    throw new RuntimeException("Config value \"" + field.getType().getName() + ";" + field.getName() + "\" is static! Do not use static fields for configs, it can cause undocumented and unpredictable behavior!");
                }
                if (Modifier.isFinal(field.getModifiers())) {
                    throw new RuntimeException("Config value \"" + field.getType().getName() + ";" + field.getName() + "\" is final! How am I meant to load configs into this?");
                }
                field.setAccessible(true);
                if(!loaded) {
                    defaultConfig.put(field.getName(), field.get(objField));
                }
                ConfigEntry configEntryAnnotation = field.getAnnotation(ConfigEntry.class);
                Class<?> fieldType = ConfigFactories.loadTypeAdapterFactories.get(field.getType());
                fieldType = fieldType != null ? fieldType : field.getType();
                ConfigEntryHandler<?> configEntry = function.apply(
                        field.getName(),
                        configEntryAnnotation,
                        field,
                        objField,
                        category.multiplayerSynced || configEntryAnnotation.multiplayerSynced(),
                        rootJsonObject.getChild(field.getName(), fieldType) != null? rootJsonObject.getChild(field.getName(), fieldType) : childObjField,
                        defaultConfig.get(field.getName())
                );
                configEntry.multiplayerLoaded = isMultiplayer && configEntry.multiplayerSynced;
                category.values.put(field.getType(), configEntry);
                configEntry.saveToField();
                totalReadFields.getAndIncrement();
            }
        }
    }

    public static String saveConfig(ModContainer mod, ConfigCategoryHandler category, int source) {
        try {
            AtomicInteger readValues = new AtomicInteger();
            AtomicInteger readCategories = new AtomicInteger();
            GlassYamlFile configFile = new GlassYamlFile(new File(FabricLoader.getInstance().getConfigDir().toFile(), mod.getMetadata().getId() + "/" + category.parentField.getAnnotation(ConfigRoot.class).value() + ".yml"));
            configFile.createNewFile();
            GlassYamlFile serverExported = new GlassYamlFile();
            saveDeeper(configFile.path(), serverExported.path(), category, category.parentField, readValues, readCategories);

            if (EventStorage.PRE_SAVE_LISTENERS.containsKey(mod.getMetadata().getId())) {
                EventStorage.PRE_SAVE_LISTENERS.get(mod.getMetadata().getId()).getEntrypoint().onPreConfigSaved(source, new GlassYamlFile(configFile.getConfigurationFile()), configFile);
            }

            configFile.save();
            log("Successfully saved " + readCategories + " categories, containing " + readValues.get() + " values for " + mod.getMetadata().getName() + "(" + mod.getMetadata().getId() + ").");
            return serverExported.saveToString();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void saveDeeper(YamlFileWrapper newValues, YamlFileWrapper serverExported, ConfigCategoryHandler category, Field childField, AtomicInteger readValues, AtomicInteger readCategories) throws IllegalAccessException {
        for (ConfigHandlerBase entry : category.values.values()) {
            childField.setAccessible(true);
            if (entry instanceof ConfigCategoryHandler) {
                ConfigCategory configEntryAnnotation = entry.parentField.getAnnotation(ConfigCategory.class);
                saveDeeper(newValues.path(entry.id), serverExported.path(entry.id), (ConfigCategoryHandler) entry, entry.parentField, readValues, readCategories);
                readCategories.getAndIncrement();
                if (!configEntryAnnotation.comment().isEmpty()) {
                    newValues.path(entry.id).comment(configEntryAnnotation.comment());
                }
            }
            else if (entry instanceof ConfigEntryHandler) {
                ConfigEntry configCategoryAnnotation = entry.parentField.getAnnotation(ConfigEntry.class);
                Function<Object, Object> configFactory = ConfigFactories.saveFactories.get(entry.parentField.getType());
                if (configFactory == null) {
                    throw new RuntimeException("Config value \"" + entry.parentObject.getClass().getName() + ";" + entry.id + "\" has no config saver for it's type!");
                }
                Object jsonElement = configFactory.apply(((ConfigEntryHandler<?>) entry).value);
                if (!((ConfigEntryHandler<?>) entry).multiplayerLoaded) {
                    newValues.setChild(entry.id, jsonElement);
                    if (entry.description != null && !entry.description.isEmpty()) {
                        newValues.path(entry.id).comment(entry.description);
                    }
                }
                if (entry.multiplayerSynced) {
                    serverExported.setChild(entry.id, jsonElement);
                }
                ((ConfigEntryHandler<?>) entry).saveToField();
                if (!configCategoryAnnotation.comment().isEmpty()) {
                    newValues.path(entry.id).comment(configCategoryAnnotation.comment());
                }
                readValues.getAndIncrement();
            }
            else {
                throw new RuntimeException("What?! Config contains a non-serializable entry!");
            }
        }
    }
}
