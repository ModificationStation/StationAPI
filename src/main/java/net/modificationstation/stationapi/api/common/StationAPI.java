package net.modificationstation.stationapi.api.common;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.level.ClientLevel;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.item.tool.ToolMaterial;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.client.event.gui.GuiHandlerRegister;
import net.modificationstation.stationapi.api.client.event.gui.RenderItemOverlay;
import net.modificationstation.stationapi.api.client.item.CustomItemOverlay;
import net.modificationstation.stationapi.api.client.texture.TextureRegistry;
import net.modificationstation.stationapi.api.common.config.Category;
import net.modificationstation.stationapi.api.common.config.Configuration;
import net.modificationstation.stationapi.api.common.config.Property;
import net.modificationstation.stationapi.api.common.entity.EntityHandlerRegistry;
import net.modificationstation.stationapi.api.common.entity.HasOwner;
import net.modificationstation.stationapi.api.common.event.EventBus;
import net.modificationstation.stationapi.api.common.event.ListenerPriority;
import net.modificationstation.stationapi.api.common.event.level.LoadLevelPropertiesOnLevelInit;
import net.modificationstation.stationapi.api.common.event.level.SaveLevelProperties;
import net.modificationstation.stationapi.api.common.event.mod.Init;
import net.modificationstation.stationapi.api.common.event.mod.PostInit;
import net.modificationstation.stationapi.api.common.event.mod.PreInit;
import net.modificationstation.stationapi.api.common.event.packet.MessageListenerRegister;
import net.modificationstation.stationapi.api.common.event.packet.PacketRegister;
import net.modificationstation.stationapi.api.common.event.recipe.BeforeRecipeStats;
import net.modificationstation.stationapi.api.common.event.recipe.RecipeRegister;
import net.modificationstation.stationapi.api.common.gui.GuiHandlerRegistry;
import net.modificationstation.stationapi.api.common.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.common.packet.Message;
import net.modificationstation.stationapi.api.common.packet.MessageListenerRegistry;
import net.modificationstation.stationapi.api.common.packet.StationHandshake;
import net.modificationstation.stationapi.api.common.recipe.JsonRecipeParserRegistry;
import net.modificationstation.stationapi.api.common.recipe.JsonRecipesRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;
import net.modificationstation.stationapi.api.common.registry.LevelRegistry;
import net.modificationstation.stationapi.api.common.registry.ModID;
import net.modificationstation.stationapi.api.common.registry.Registry;
import net.modificationstation.stationapi.api.common.resource.ResourceManager;
import net.modificationstation.stationapi.api.common.util.SideUtils;
import net.modificationstation.stationapi.api.common.util.exception.BadGradleIdentifierException;
import net.modificationstation.stationapi.api.common.util.exception.DuplicateIDException;
import net.modificationstation.stationapi.api.server.entity.IStationSpawnData;
import net.modificationstation.stationapi.api.server.event.network.HandleLogin;
import net.modificationstation.stationapi.api.server.event.network.TrackEntity;
import net.modificationstation.stationapi.impl.client.entity.player.PlayerHelper;
import net.modificationstation.stationapi.impl.client.model.CustomModelRenderer;
import net.modificationstation.stationapi.impl.client.packet.PacketHelper;
import net.modificationstation.stationapi.impl.client.texture.TextureFactory;
import net.modificationstation.stationapi.impl.common.achievement.AchievementPage;
import net.modificationstation.stationapi.impl.common.achievement.AchievementPageManager;
import net.modificationstation.stationapi.impl.common.config.CategoryImpl;
import net.modificationstation.stationapi.impl.common.config.PropertyImpl;
import net.modificationstation.stationapi.impl.common.factory.EnumFactory;
import net.modificationstation.stationapi.impl.common.factory.GeneralFactory;
import net.modificationstation.stationapi.impl.common.item.CustomReach;
import net.modificationstation.stationapi.impl.common.item.JsonItemKey;
import net.modificationstation.stationapi.impl.common.lang.I18n;
import net.modificationstation.stationapi.impl.common.recipe.*;
import net.modificationstation.stationapi.impl.common.util.UnsafeProvider;
import net.modificationstation.stationapi.impl.server.entity.CustomTrackingImpl;
import net.modificationstation.stationapi.impl.server.entity.TrackingImpl;
import net.modificationstation.stationapi.mixin.client.accessor.ClientPlayNetworkHandlerAccessor;
import net.modificationstation.stationapi.mixin.common.accessor.RecipeRegistryAccessor;
import net.modificationstation.stationapi.mixin.common.accessor.SmeltingRecipeRegistryAccessor;
import net.modificationstation.stationapi.mixin.common.accessor.StatsAccessor;
import net.modificationstation.stationapi.template.common.item.MetaBlock;
import net.modificationstation.stationapi.template.common.item.MetaNamedBlock;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
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
    public static final StationAPI INSTANCE = Entrypoint.getNull();

    /**
     * StationAPI's ModID.
     */
    @Entrypoint.ModID
    public static final ModID MODID = Entrypoint.getNull();

    @Entrypoint.Logger("Station|API")
    public static final Logger LOGGER = Entrypoint.getNull();

    @Entrypoint.Config
    public static final Configuration CONFIG = Entrypoint.getNull();

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
        FabricLoader.getInstance().getModContainer("stationapi").ifPresent(modContainer -> Entrypoint.setup(this, modContainer));
        String name = MODID.getName();
        LOGGER.info("Initializing " + name + "...");
        Configurator.setLevel("mixin", Level.TRACE);
        Configurator.setLevel("Fabric|Loader", Level.INFO);
        LOGGER.info("Setting up API...");
        setupAPI();
        LOGGER.info("Setting up lang folder...");
        net.modificationstation.stationapi.api.common.lang.I18n.INSTANCE.addLangFolder("/assets/" + MODID + "/lang", MODID);
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
        generalFactory.addFactory(net.modificationstation.stationapi.api.common.achievement.AchievementPage.class, args -> new AchievementPage((String) args[0]));
        generalFactory.addFactory(MetaBlock.class, args -> new MetaBlock((int) args[0]));
        generalFactory.addFactory(MetaNamedBlock.class, args -> new MetaNamedBlock((int) args[0]));
        net.modificationstation.stationapi.api.common.factory.EnumFactory enumFactory = net.modificationstation.stationapi.api.common.factory.EnumFactory.INSTANCE;
        generalFactory.addFactory(ToolMaterial.class, args -> enumFactory.addEnum(ToolMaterial.class, (String) args[0], new Class[]{int.class, int.class, float.class, int.class}, new Object[]{args[1], args[2], args[3], args[4]}));
        generalFactory.addFactory(EntityType.class, args -> enumFactory.addEnum(EntityType.class, (String) args[0], new Class[]{Class.class, int.class, Material.class, boolean.class}, new Object[]{args[1], args[2], args[3], args[4]}));
        LOGGER.info("Setting up EnumFactory...");
        enumFactory.setHandler(new EnumFactory());
        LOGGER.info("Setting up I18n...");
        net.modificationstation.stationapi.api.common.lang.I18n.INSTANCE.setHandler(new I18n());
        LOGGER.info("Setting up CraftingRegistry...");
        net.modificationstation.stationapi.api.common.recipe.CraftingRegistry.INSTANCE.setHandler(new CraftingRegistry());
        LOGGER.info("Setting up UnsafeProvider...");
        net.modificationstation.stationapi.api.common.util.UnsafeProvider.INSTANCE.setHandler(new UnsafeProvider());
        LOGGER.info("Setting up SmeltingRegistry...");
        SmeltingRegistry smeltingRegistry = new SmeltingRegistry();
        net.modificationstation.stationapi.api.common.recipe.SmeltingRegistry.INSTANCE.setHandler(smeltingRegistry);
        LOGGER.info("Setting up CustomReach...");
        net.modificationstation.stationapi.api.common.item.CustomReach.CONSUMERS.put("setDefaultBlockReach", CustomReach::setDefaultBlockReach);
        net.modificationstation.stationapi.api.common.item.CustomReach.CONSUMERS.put("setHandBlockReach", CustomReach::setHandBlockReach);
        net.modificationstation.stationapi.api.common.item.CustomReach.CONSUMERS.put("setDefaultEntityReach", CustomReach::setDefaultEntityReach);
        net.modificationstation.stationapi.api.common.item.CustomReach.CONSUMERS.put("setHandEntityReach", CustomReach::setHandEntityReach);
        net.modificationstation.stationapi.api.common.item.CustomReach.SUPPLIERS.put("getDefaultBlockReach", CustomReach::getDefaultBlockReach);
        net.modificationstation.stationapi.api.common.item.CustomReach.SUPPLIERS.put("getHandBlockReach", CustomReach::getHandBlockReach);
        net.modificationstation.stationapi.api.common.item.CustomReach.SUPPLIERS.put("getDefaultEntityReach", CustomReach::getDefaultEntityReach);
        net.modificationstation.stationapi.api.common.item.CustomReach.SUPPLIERS.put("getHandEntityReach", CustomReach::getHandEntityReach);
        LOGGER.info("Setting up AchievementPageManager...");
        net.modificationstation.stationapi.api.common.achievement.AchievementPageManager acpMngr = new AchievementPageManager();
        net.modificationstation.stationapi.api.common.achievement.AchievementPageManager.INSTANCE.setHandler(acpMngr);
        EVENT_BUS.register(acpMngr);
        LOGGER.info("Setting up CustomData packet...");
        Category networkConfig = CONFIG.getCategory("Network");
        EVENT_BUS.register(PacketRegister.class, event -> {
            event.register(networkConfig.getProperty("PacketCustomDataID", 254).getIntValue(), true, true, Message.class);
            CONFIG.save();
            EVENT_BUS.post(new MessageListenerRegister(MessageListenerRegistry.INSTANCE));
        }, ListenerPriority.HIGH.numPriority);
        LOGGER.info("Setting up TileEntityRegister...");
        EVENT_BUS.register(smeltingRegistry);
        LOGGER.info("Setting up LoadLevelPropertiesOnLevelInit...");
        EVENT_BUS.register(LoadLevelPropertiesOnLevelInit.class, event -> {
            LevelRegistry.remapping = true;
            StatsAccessor.setBlocksInit(false);
            StatsAccessor.setItemsInit(false);
            Registry<Registry<?>> registriesRegistry = Registry.REGISTRIES;
            CompoundTag registriesTag = event.tag.getCompoundTag(registriesRegistry.getRegistryId().toString());
            registriesRegistry.forEach((identifier, registry) -> {
                if (registry instanceof LevelRegistry)
                    ((LevelRegistry<?>) registry).load(registriesTag.getCompoundTag(registry.getRegistryId().toString()));
            });
            LevelRegistry.remapping = false;
        }, ListenerPriority.HIGH.numPriority);
        LOGGER.info("Setting up SaveLevelProperties...");
        EVENT_BUS.register(SaveLevelProperties.class, event -> {
            Registry<Registry<?>> registriesRegistry = Registry.REGISTRIES;
            CompoundTag registriesTag = new CompoundTag();
            registriesRegistry.forEach((identifier, registry) -> {
                if (registry instanceof LevelRegistry) {
                    CompoundTag registryTag = new CompoundTag();
                    ((LevelRegistry<?>) registry).save(registryTag);
                    registriesTag.put(identifier.toString(), registryTag);
                }
            });
            event.tag.put(registriesRegistry.getRegistryId().toString(), registriesTag);
        }, ListenerPriority.HIGH.numPriority);
        LOGGER.info("Setting up RecipeRegister...");
        EVENT_BUS.register(RecipeRegister.class, event -> JsonRecipeParserRegistry.INSTANCE.getByIdentifier(event.recipeId).ifPresent(recipeParser -> JsonRecipesRegistry.INSTANCE.getByIdentifier(event.recipeId).ifPresent(recipes -> recipes.forEach(recipeParser))), ListenerPriority.HIGH.numPriority);
        LOGGER.info("Setting up BeforeRecipesStats...");
        EVENT_BUS.register(BeforeRecipeStats.class, event -> {
            RecipeRegistryAccessor.invokeCor();
            SmeltingRecipeRegistryAccessor.invokeCor();
        }, ListenerPriority.HIGH.numPriority);
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
                    LOGGER.info("Setting up PlayerHelper...");
                    net.modificationstation.stationapi.api.common.entity.player.PlayerHelper.INSTANCE.setHandler(new PlayerHelper());
                    LOGGER.info("Setting up PacketHelper...");
                    net.modificationstation.stationapi.api.common.packet.PacketHelper.INSTANCE.setHandler(new PacketHelper());
                    LOGGER.info("Setting up RenderItemOverlay...");
                    EVENT_BUS.register(RenderItemOverlay.class, (event) -> {
                        if (event.itemInstance != null && event.itemInstance.getType() instanceof CustomItemOverlay) {
                            ((CustomItemOverlay) event.itemInstance.getType()).renderItemOverlay(event.itemRenderer, event.itemX, event.itemY, event.itemInstance, event.textRenderer, event.textureManager);
                        }
                    }, ListenerPriority.HIGH.numPriority);
                    EVENT_BUS.register(MessageListenerRegister.class, event -> {
                        event.registry.registerValue(Identifier.of(MODID, "open_gui"), (playerBase, message) -> {
                            boolean isClient = playerBase.level.isClient;
                            //noinspection deprecation
                            GuiHandlerRegistry.INSTANCE.getByIdentifier(Identifier.of(message.strings()[0])).ifPresent(guiHandler -> ((Minecraft) FabricLoader.getInstance().getGameInstance()).openScreen(guiHandler.one().apply(playerBase, isClient ? guiHandler.two().get() : (InventoryBase) message.objects()[0], message)));
                            if (isClient)
                                playerBase.container.currentContainerId = message.ints()[0];
                        });
                        EVENT_BUS.post(new GuiHandlerRegister(GuiHandlerRegistry.INSTANCE));
                        event.registry.registerValue(Identifier.of(MODID, "spawn_entity"), (playerBase, message) -> EntityHandlerRegistry.INSTANCE.getByIdentifier(Identifier.of(message.strings()[0])).ifPresent(entityProvider -> {
                            double x = message.ints()[1] / 32D, y = message.ints()[2] / 32D, z = message.ints()[3] / 32D;
                            //noinspection deprecation
                            ClientPlayNetworkHandlerAccessor networkHandler = (ClientPlayNetworkHandlerAccessor) ((Minecraft) FabricLoader.getInstance().getGameInstance()).getNetworkHandler();
                            ClientLevel level = networkHandler.getLevel();
                            EntityBase entity = entityProvider.apply(level, x, y, z);
                            if (entity != null) {
                                entity.clientX = message.ints()[1];
                                entity.clientY = message.ints()[2];
                                entity.clientZ = message.ints()[3];
                                entity.yaw = 0.0F;
                                entity.pitch = 0.0F;
                                entity.entityId = message.ints()[0];
                                level.method_1495(message.ints()[0], entity);
                                if (message.ints()[4] > 0) {
                                    if (entity instanceof HasOwner)
                                        ((HasOwner) entity).setOwner(networkHandler.invokeMethod_1645(message.ints()[4]));
                                    entity.setVelocity((double)message.shorts()[0] / 8000.0D, (double)message.shorts()[1] / 8000.0D, (double)message.shorts()[2] / 8000.0D);
                                }
                                if (entity instanceof IStationSpawnData)
                                    ((IStationSpawnData) entity).readFromMessage(message);
                            }
                        }));
                    }, ListenerPriority.HIGH.numPriority);
                },

                // SERVER

                () -> {
                    EVENT_BUS.register(TrackEntity.class, CustomTrackingImpl::trackEntity, ListenerPriority.HIGH.numPriority);
                    EVENT_BUS.register(TrackEntity.class, TrackingImpl::trackEntity, ListenerPriority.HIGH.numPriority);
                    LOGGER.info("Setting up PlayerHelper...");
                    net.modificationstation.stationapi.api.common.entity.player.PlayerHelper.INSTANCE.setHandler(new net.modificationstation.stationapi.impl.server.entity.player.PlayerHelper());
                    LOGGER.info("Setting up PacketHelper...");
                    net.modificationstation.stationapi.api.common.packet.PacketHelper.INSTANCE.setHandler(new net.modificationstation.stationapi.impl.server.packet.PacketHelper());
                    LOGGER.info("Setting up HandleLogin...");
                    EVENT_BUS.register(HandleLogin.class, (event) -> {
                        if (!getModsToVerifyOnClient().isEmpty()) {
                            StationHandshake handshake = (StationHandshake) event.handshakePacket;
                            String stationAPI = handshake.getStationAPI();
                            String version = handshake.getVersion();
                            String serverStationAPI = MODID.toString();
                            String serverStationVersion = MODID.getVersion().getFriendlyString();
                            TranslationStorage translationStorage = TranslationStorage.getInstance();
                            if (stationAPI == null || version == null || !stationAPI.equals(serverStationAPI)) {
                                event.pendingConnection.drop(translationStorage.translate("disconnect.stationapi:missing_station"));
                                return;
                            } else if (!version.equals(serverStationVersion)) {
                                event.pendingConnection.drop(translationStorage.translate("disconnect.stationapi:station_version_mismatch", serverStationVersion, version));
                                return;
                            }
                            Map<String, String> clientMods = handshake.getMods();
                            String modid;
                            String clientVersion;
                            String serverVersion;
                            for (ModContainer serverMod : getModsToVerifyOnClient()) {
                                ModMetadata modMetadata = serverMod.getMetadata();
                                modid = modMetadata.getId();
                                serverVersion = modMetadata.getVersion().getFriendlyString();
                                if (clientMods.containsKey(modid)) {
                                    clientVersion = clientMods.get(modid);
                                    if (clientVersion == null || !clientVersion.equals(serverVersion)) {
                                        event.pendingConnection.drop(translationStorage.translate("disconnect.stationapi:mod_version_mismatch", modMetadata.getName(), modid, serverVersion, clientVersion == null ? "null" : clientVersion));
                                        return;
                                    }
                                } else {
                                    event.pendingConnection.drop(translationStorage.translate("disconnect.stationapi:missing_mod", modMetadata.getName(), modid, serverVersion));
                                    return;
                                }
                            }
                        }
                    }, ListenerPriority.HIGH.numPriority);
                }
        );
    }

    /**
     * Loads main entrypoints and scans mods assets, also invokes preInit, init and postInit events. No Minecraft classes must be referenced here.
     */
    public void setupMods() {
        FabricLoader fabricLoader = FabricLoader.getInstance();
        fabricLoader.getEntrypointContainers(Identifier.of(MODID, "event_bus").toString(), Object.class).forEach(Entrypoint::setup);
        fabricLoader.getEntrypointContainers(Identifier.of(MODID, "event_bus_" + fabricLoader.getEnvironmentType().name().toLowerCase()).toString(), Object.class).forEach(Entrypoint::setup);
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
                net.modificationstation.stationapi.api.common.lang.I18n.INSTANCE.addLangFolder(pathName, modID);
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
        EVENT_BUS.post(new PreInit());
        LOGGER.info("Invoking Init event...");
        EVENT_BUS.post(new Init());
        LOGGER.info("Invoking PostInit event...");
        EVENT_BUS.post(new PostInit());
    }

    //@Override
    public void preInit(JsonRecipeParserRegistry jsonRecipeParserRegistry, net.modificationstation.stationapi.api.common.registry.ModID modID) {
        jsonRecipeParserRegistry.registerValue(Identifier.of("crafting_shaped"), recipe -> {
            JsonElement rawJson;
            try {
                rawJson = JsonParser.parseReader(new BufferedReader(new InputStreamReader(recipe.openStream())));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            JsonCraftingShaped json = new Gson().fromJson(rawJson, JsonCraftingShaped.class);
            Set<Map.Entry<String, JsonElement>> rawKeys = rawJson.getAsJsonObject().getAsJsonObject("key").entrySet();
            String[] pattern = json.getPattern();
            Object[] keys = new Object[rawKeys.size() * 2 + pattern.length];
            int i = 0;
            for (; i < pattern.length; i++)
                keys[i] = pattern[i];
            for (Map.Entry<String, JsonElement> key : rawKeys) {
                keys[i] = key.getKey().charAt(0);
                keys[i + 1] = new Gson().fromJson(key.getValue(), JsonItemKey.class).getItemInstance();
                i += 2;
            }
            net.modificationstation.stationapi.api.common.recipe.CraftingRegistry.INSTANCE.addShapedRecipe(json.getResult().getItemInstance(), keys);
        });
        jsonRecipeParserRegistry.registerValue(Identifier.of("crafting_shapeless"), recipe -> {
            JsonCraftingShapeless json;
            try {
                json = new Gson().fromJson(new BufferedReader(new InputStreamReader(recipe.openStream())), JsonCraftingShapeless.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            JsonItemKey[] ingredients = json.getIngredients();
            Object[] iteminstances = new Object[json.getIngredients().length];
            for (int i = 0; i < ingredients.length; i++)
                iteminstances[i] = ingredients[i].getItemInstance();
            net.modificationstation.stationapi.api.common.recipe.CraftingRegistry.INSTANCE.addShapelessRecipe(json.getResult().getItemInstance(), iteminstances);
        });
        jsonRecipeParserRegistry.registerValue(Identifier.of("smelting"), recipe -> {
            JsonSmelting json;
            try {
                json = new Gson().fromJson(new BufferedReader(new InputStreamReader(recipe.openStream())), JsonSmelting.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            net.modificationstation.stationapi.api.common.recipe.SmeltingRegistry.INSTANCE.addSmeltingRecipe(json.getIngredient().getItemInstance(), json.getResult().getItemInstance());
        });
    }

    /**
     * Returns the set of mods that need client-side verification when the client joins server.
     * @return the set of mods that need client-side verification when the client joins server.
     */
    public Set<ModContainer> getModsToVerifyOnClient() {
        return Collections.unmodifiableSet(modsToVerifyOnClient);
    }
}
