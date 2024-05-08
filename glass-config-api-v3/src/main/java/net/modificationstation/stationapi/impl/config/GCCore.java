package net.modificationstation.stationapi.impl.config;


import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.modificationstation.stationapi.api.config.Comment;
import net.modificationstation.stationapi.api.config.ConfigCategory;
import net.modificationstation.stationapi.api.config.ConfigFactoryProvider;
import net.modificationstation.stationapi.api.config.ConfigName;
import net.modificationstation.stationapi.api.config.GConfig;
import net.modificationstation.stationapi.api.config.GeneratedConfig;
import net.modificationstation.stationapi.api.config.MaxLength;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.config.MultiplayerSynced;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.ReflectionHelper;
import net.modificationstation.stationapi.impl.config.object.ConfigBase;
import net.modificationstation.stationapi.impl.config.object.ConfigEntry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.simpleyaml.configuration.file.YamlFileWrapper;
import org.simpleyaml.configuration.implementation.api.QuoteStyle;
import uk.co.benjiweber.expressions.tuple.BiTuple;

import java.io.*;
import java.lang.annotation.*;
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
    public static final HashMap<Identifier, BiTuple<EntrypointContainer<Object>, net.modificationstation.stationapi.impl.config.object.ConfigCategory>> MOD_CONFIGS = new HashMap<>();

    public static final HashMap<Identifier, HashMap<String, Object>> DEFAULT_MOD_CONFIGS = new HashMap<>();
    private static boolean loaded = false;
    public static boolean isMultiplayer = false;
    private static final Logger LOGGER = LogManager.getFormatterLogger("GCAPI");

    private static final Supplier<MaxLength> MAX_LENGTH_SUPPLIER = () -> new MaxLength() {
        @Override
        public Class<? extends Annotation> annotationType() {
            return MaxLength.class;
        }

        @Override
        public int value() {
            return 32;
        }

        @Override
        public int arrayValue() {
            return -1;
        }

        @Override
        public boolean fixedArray() {
            return false;
        }
    };

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
            BiTuple<EntrypointContainer<Object>, net.modificationstation.stationapi.impl.config.object.ConfigCategory> category = MOD_CONFIGS.get(mod.get());
            saveConfig(category.one(), category.two(), EventStorage.EventSource.SERVER_JOIN | EventStorage.EventSource.MODDED_SERVER_JOIN);
            try {
                loadModConfig(category.one().getEntrypoint(), category.one().getProvider(), category.two().parentField, mod.get(), new GlassYamlFile(string));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void exportConfigsForServer(NbtCompound nbtCompound) {
        for (Identifier modContainer : MOD_CONFIGS.keySet()) {
            BiTuple<EntrypointContainer<Object>, net.modificationstation.stationapi.impl.config.object.ConfigCategory> entry = MOD_CONFIGS.get(modContainer);
            nbtCompound.putString(modContainer.toString(), saveConfig(entry.one(), entry.two(), EventStorage.EventSource.SERVER_EXPORT));
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

        ImmutableMap.Builder<Type, NonFunction<String, String, String, Field, Object, Boolean, Object, Object, MaxLength, ConfigEntry<?>>> loadImmutableBuilder = ImmutableMap.builder();
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
                for (Field field : ReflectionHelper.getFieldsWithAnnotation(entrypointContainer.getEntrypoint().getClass(), GConfig.class)) {
                    Identifier configID = Identifier.of(entrypointContainer.getProvider().getMetadata().getId() + ":" + field.getAnnotation(GConfig.class).value());
                    MOD_CONFIGS.put(configID, BiTuple.of(entrypointContainer, null));
                    loadModConfig(entrypointContainer.getEntrypoint(), entrypointContainer.getProvider(), field, configID, null);
                    saveConfig(entrypointContainer, MOD_CONFIGS.get(configID).two(), EventStorage.EventSource.GAME_LOAD);
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
            GlassYamlFile modConfigFile = new GlassYamlFile(new File(FabricLoader.getInstance().getConfigDir().toFile(), modContainer.getMetadata().getId() + "/" + configField.getAnnotation(GConfig.class).value() + ".yml"));
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
            net.modificationstation.stationapi.impl.config.object.ConfigCategory configCategory = new net.modificationstation.stationapi.impl.config.object.ConfigCategory(modContainer.getMetadata().getId(), configField.getAnnotation(GConfig.class).visibleName(), null, configField, objField, configField.isAnnotationPresent(MultiplayerSynced.class), HashMultimap.create(), true);
            readDeeper(rootConfigObject, configField, modConfigFile.path(""), configCategory, totalReadFields, totalReadCategories, isMultiplayer, defaultEntry);
            if (!loaded) {
                MOD_CONFIGS.put(configID, BiTuple.of(MOD_CONFIGS.remove(configID).one(), configCategory));
            } else {
                MOD_CONFIGS.get(configID).two().values = configCategory.values;
            }
            log("Successfully read \"" + configID + "\"'s mod configs, reading " + totalReadCategories.get() + " categories, and " + totalReadFields.get() + " values.");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void readDeeper(Object rootConfigObject, Field configField, GlassYamlWrapper rootJsonObject, net.modificationstation.stationapi.impl.config.object.ConfigCategory category, AtomicInteger totalReadFields, AtomicInteger totalReadCategories, boolean isMultiplayer, HashMap<String, Object> defaultConfig) throws IllegalAccessException {
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
                GlassYamlWrapper jsonCategory = rootJsonObject.path(field.getName());
                net.modificationstation.stationapi.impl.config.object.ConfigCategory childCategory = new net.modificationstation.stationapi.impl.config.object.ConfigCategory(
                        field.getName(),
                        field.getAnnotation(ConfigCategory.class).value(),
                        field.isAnnotationPresent(Comment.class)? field.getAnnotation(Comment.class).value() : null,
                        field,
                        objField,
                        category.multiplayerSynced || field.isAnnotationPresent(MultiplayerSynced.class),
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
                if (!field.isAnnotationPresent(ConfigName.class)) {
                    throw new RuntimeException("Config value \"" + field.getType().getName() + ";" + field.getName() + "\" has no ConfigName annotation!");
                }
                if (field.getType() == HashMap.class) {
                    throw new RuntimeException("Config value \"" + field.getType().getName() + ";" + field.getName() + "\" is a HashMap! Create a new HashMap subclass, as the basic type is used in GCAPI internals!");
                }
                NonFunction<String, String, String, Field, Object, Boolean, Object, Object, MaxLength, ConfigEntry<?>> function = ConfigFactories.loadFactories.get(field.getType());
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
                Class<?> fieldType = ConfigFactories.loadTypeAdapterFactories.get(field.getType());
                fieldType = fieldType != null ? fieldType : field.getType();
                ConfigEntry<?> configEntry = function.apply(
                        field.getName(),
                        field.getAnnotation(ConfigName.class).value(),
                        field.isAnnotationPresent(Comment.class)? field.getAnnotation(Comment.class).value() : null,
                        field,
                        objField,
                        category.multiplayerSynced || field.isAnnotationPresent(MultiplayerSynced.class),
                        rootJsonObject.getChild(field.getName(), fieldType) != null? rootJsonObject.getChild(field.getName(), fieldType) : childObjField,
                        defaultConfig.get(field.getName()),
                        field.isAnnotationPresent(MaxLength.class)? field.getAnnotation(MaxLength.class) : MAX_LENGTH_SUPPLIER.get()
                );
                configEntry.multiplayerLoaded = isMultiplayer && configEntry.multiplayerSynced;
                category.values.put(field.getType(), configEntry);
                configEntry.saveToField();
                totalReadFields.getAndIncrement();
            }
        }
    }

    public static String saveConfig(EntrypointContainer<Object> container, net.modificationstation.stationapi.impl.config.object.ConfigCategory category, int source) {
        try {
            AtomicInteger readValues = new AtomicInteger();
            AtomicInteger readCategories = new AtomicInteger();
            GlassYamlFile configFile = new GlassYamlFile(new File(FabricLoader.getInstance().getConfigDir().toFile(), container.getProvider().getMetadata().getId() + "/" + category.parentField.getAnnotation(GConfig.class).value() + ".yml"));
            configFile.createNewFile();
            GlassYamlFile serverExported = new GlassYamlFile();
            // The path("") is critical for saving a shitton of copy-paste snowflake code. Thank you library maker, for not being controlling.
            saveDeeper(configFile.path(""), serverExported.path(""), category, category.parentField, readValues, readCategories);

            if (EventStorage.PRE_SAVE_LISTENERS.containsKey(container.getProvider().getMetadata().getId())) {
                EventStorage.PRE_SAVE_LISTENERS.get(container.getProvider().getMetadata().getId()).getEntrypoint().onPreConfigSaved(source, new GlassYamlFile(configFile.getConfigurationFile()), configFile);
            }

            configFile.save();
            log("Successfully saved " + readCategories + " categories, containing " + readValues.get() + " values for " + container.getProvider().getMetadata().getName() + "(" + container.getProvider().getMetadata().getId() + ").");
            return serverExported.saveToString();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void saveDeeper(YamlFileWrapper newValues, YamlFileWrapper serverExported, net.modificationstation.stationapi.impl.config.object.ConfigCategory category, Field childField, AtomicInteger readValues, AtomicInteger readCategories) throws IllegalAccessException {
        for (ConfigBase entry : category.values.values()) {
            childField.setAccessible(true);
            if (entry instanceof net.modificationstation.stationapi.impl.config.object.ConfigCategory) {
                saveDeeper(newValues.path(entry.id), serverExported.path(entry.id), (net.modificationstation.stationapi.impl.config.object.ConfigCategory) entry, entry.parentField, readValues, readCategories);
                readCategories.getAndIncrement();
                if (entry.parentField.getAnnotation(Comment.class) != null) {
                    newValues.path(entry.id).comment(entry.parentField.getAnnotation(Comment.class).value());
                }
            }
            else if (entry instanceof ConfigEntry) {
                Function<Object, Object> configFactory = ConfigFactories.saveFactories.get(entry.parentField.getType());
                if (configFactory == null) {
                    throw new RuntimeException("Config value \"" + entry.parentObject.getClass().getName() + ";" + entry.id + "\" has no config saver for it's type!");
                }
                Object jsonElement = configFactory.apply(((ConfigEntry<?>) entry).value);
                if (!((ConfigEntry<?>) entry).multiplayerLoaded) {
                    YamlFileWrapper child = newValues.setChild(entry.id, jsonElement);
                    if (entry.description != null && !entry.description.isEmpty()) {
                        child.comment(entry.description);
                    }
                }
                if (entry.multiplayerSynced) {
                    serverExported.setChild(entry.id, jsonElement);
                }
                ((ConfigEntry<?>) entry).saveToField();
                if (entry.parentField.getAnnotation(Comment.class) != null) {
                    newValues.path(entry.id).comment(entry.parentField.getAnnotation(Comment.class).value());
                }
                readValues.getAndIncrement();
            }
            else {
                throw new RuntimeException("What?! Config contains a non-serializable entry!");
            }
        }
    }
}
