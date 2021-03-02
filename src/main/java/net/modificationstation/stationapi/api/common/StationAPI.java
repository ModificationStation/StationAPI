package net.modificationstation.stationapi.api.common;

import com.google.gson.Gson;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.client.texture.TextureRegistry;
import net.modificationstation.stationapi.api.common.config.Category;
import net.modificationstation.stationapi.api.common.config.Configuration;
import net.modificationstation.stationapi.api.common.config.Property;
import net.modificationstation.stationapi.api.common.event.EventBus;
import net.modificationstation.stationapi.api.common.event.mod.InitEvent;
import net.modificationstation.stationapi.api.common.event.mod.PostInitEvent;
import net.modificationstation.stationapi.api.common.event.mod.PreInitEvent;
import net.modificationstation.stationapi.api.common.lang.I18n;
import net.modificationstation.stationapi.api.common.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.common.mod.entrypoint.EntrypointManager;
import net.modificationstation.stationapi.api.common.recipe.JsonRecipesRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;
import net.modificationstation.stationapi.api.common.registry.ModID;
import net.modificationstation.stationapi.api.common.resource.ResourceManager;
import net.modificationstation.stationapi.api.common.util.Null;
import net.modificationstation.stationapi.api.common.util.SideUtils;
import net.modificationstation.stationapi.impl.client.model.CustomModelRenderer;
import net.modificationstation.stationapi.impl.client.texture.TextureFactory;
import net.modificationstation.stationapi.impl.common.config.CategoryImpl;
import net.modificationstation.stationapi.impl.common.config.PropertyImpl;
import net.modificationstation.stationapi.impl.common.factory.EnumFactory;
import net.modificationstation.stationapi.impl.common.factory.GeneralFactory;
import net.modificationstation.stationapi.impl.common.recipe.JsonRecipeType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * StationAPI main class. Used for some initialization.
 * @author mine_diver
 */
public class StationAPI implements PreLaunchEntrypoint {

    /**
     * StationAPI's instance.
     */
    @Entrypoint.Instance
    public static final StationAPI INSTANCE = Null.get();

    /**
     * StationAPI's ModID.
     */
    @Entrypoint.ModID
    public static final ModID MODID = Null.get();

    @Entrypoint.Logger("Station|API")
    public static final Logger LOGGER = Null.get();

    @Entrypoint.Config
    public static final Configuration CONFIG = Null.get();

    /**
     * A set of mods that need client-side verification when the client joins server.
     */
    private final Set<ModContainer> modsToVerifyOnClient = new HashSet<>();

    public static final EventBus EVENT_BUS = new EventBus();

    /**
     * Initial setup. Configures logger, entrypoints, and calls the rest of initialization sequence. No Minecraft classes must be referenced here.
     */
    @Override
    public void onPreLaunch() {
        FabricLoader.getInstance().getModContainer("stationapi").ifPresent(modContainer -> EntrypointManager.setup(this, modContainer));
        String name = MODID.getName();
        LOGGER.info("Initializing " + name + "...");
        Configurator.setLevel("mixin", Level.TRACE);
        Configurator.setLevel("Fabric|Loader", Level.INFO);
        LOGGER.info("Setting up API...");
        setupAPI();
        LOGGER.info("Setting up lang folder...");
        I18n.addLangFolder(MODID, "/assets/" + MODID + "/lang");
        LOGGER.info("Loading mods...");
        setupMods();
        LOGGER.info("Finished " + name + " setup");
    }

    /**
     * Performs some API setup. Most likely will be removed due to API becoming less abstract. No Minecraft classes must be referenced here.
     */
    public void setupAPI() {
        LOGGER.info("Setting up GeneralFactory...");
        net.modificationstation.stationapi.api.common.factory.GeneralFactory generalFactory = net.modificationstation.stationapi.api.common.factory.GeneralFactory.INSTANCE;
        generalFactory.setHandler(new GeneralFactory());
        generalFactory.addFactory(Category.class, args -> new CategoryImpl((String) args[0]));
        generalFactory.addFactory(Property.class, args -> new PropertyImpl((String) args[0]));
        net.modificationstation.stationapi.api.common.factory.EnumFactory enumFactory = net.modificationstation.stationapi.api.common.factory.EnumFactory.INSTANCE;
        generalFactory.addFactory(ToolMaterial.class, args -> enumFactory.addEnum(ToolMaterial.class, (String) args[0], new Class[]{int.class, int.class, float.class, int.class}, new Object[]{args[1], args[2], args[3], args[4]}));
        generalFactory.addFactory(EntityType.class, args -> enumFactory.addEnum(EntityType.class, (String) args[0], new Class[]{Class.class, int.class, Material.class, boolean.class}, new Object[]{args[1], args[2], args[3], args[4]}));
        LOGGER.info("Setting up EnumFactory...");
        enumFactory.setHandler(new EnumFactory());
        SideUtils.run(

                // CLIENT

                () -> {
                    LOGGER.info("Setting up client GeneralFactory...");
                    net.modificationstation.stationapi.api.common.factory.GeneralFactory.INSTANCE.addFactory(net.modificationstation.stationapi.api.client.model.CustomModelRenderer.class, (args) -> new CustomModelRenderer((String) args[0], (String) args[1]));
                    LOGGER.info("Setting up TextureFactory...");
                    net.modificationstation.stationapi.api.client.texture.TextureFactory.INSTANCE.setHandler(new TextureFactory());
                    LOGGER.info("Setting up TextureRegistry...");
                    TextureRegistry.RUNNABLES.put("unbind", net.modificationstation.stationapi.impl.client.texture.TextureRegistry::unbind);
                    TextureRegistry.FUNCTIONS.put("getRegistry", net.modificationstation.stationapi.impl.client.texture.TextureRegistry::getRegistry);
                    TextureRegistry.SUPPLIERS.put("currentRegistry", net.modificationstation.stationapi.impl.client.texture.TextureRegistry::currentRegistry);
                    TextureRegistry.SUPPLIERS.put("registries", net.modificationstation.stationapi.impl.client.texture.TextureRegistry::registries);
                },

                // SERVER

                () -> { }
        );
    }

    /**
     * Loads main entrypoints and scans mods assets, also invokes preInit, init and postInit events. No Minecraft classes must be referenced here.
     */
    public void setupMods() {
        FabricLoader fabricLoader = FabricLoader.getInstance();
        fabricLoader.getEntrypointContainers(Identifier.of(MODID, "event_bus").toString(), Object.class).forEach(EntrypointManager::setup);
        fabricLoader.getEntrypointContainers(Identifier.of(MODID, "event_bus_" + fabricLoader.getEnvironmentType().name().toLowerCase()).toString(), Object.class).forEach(EntrypointManager::setup);
        Collection<ModContainer> mods = fabricLoader.getAllMods();
        LOGGER.info("Loading assets...");
        ResourceManager.findResources(MODID + "/recipes", file -> file.endsWith(".json")).forEach(recipe -> {
            try {
                String rawId = new Gson().fromJson(new InputStreamReader(recipe.openStream()), JsonRecipeType.class).getType();
                try {
                    Identifier recipeId = Identifier.of(rawId);
                    JsonRecipesRegistry.INSTANCE.computeIfAbsent(recipeId, identifier -> new HashSet<>()).add(recipe);
                } catch (NullPointerException e) {
                    LOGGER.warn("Found an unknown recipe type " + rawId + ". Ignoring.");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        mods.forEach(modContainer -> {
            net.modificationstation.stationapi.api.common.registry.ModID modID = net.modificationstation.stationapi.api.common.registry.ModID.of(modContainer);
            String pathName = "/assets/" + modID + "/" + MODID + "/lang";
            URL path = getClass().getResource(pathName);
            if (path != null) {
                I18n.addLangFolder(modID, pathName);
                LOGGER.info("Registered lang path");
            }
        });
        LOGGER.info("Gathering mods that require client verification...");
        String value = MODID + ":verify_client";
        mods.forEach(modContainer -> {
            ModMetadata modMetadata = modContainer.getMetadata();
            if (modMetadata.containsCustomValue(value) && modMetadata.getCustomValue(value).getAsBoolean())
                modsToVerifyOnClient.add(modContainer);
        });
        LOGGER.info("Invoking PreInit event...");
        EVENT_BUS.post(new PreInitEvent());
        LOGGER.info("Invoking Init event...");
        EVENT_BUS.post(new InitEvent());
        LOGGER.info("Invoking PostInit event...");
        EVENT_BUS.post(new PostInitEvent());
    }

    /**
     * Returns the set of mods that need client-side verification when the client joins server.
     * @return the set of mods that need client-side verification when the client joins server.
     */
    public Set<ModContainer> getModsToVerifyOnClient() {
        return Collections.unmodifiableSet(modsToVerifyOnClient);
    }
}
