package net.modificationstation.stationapi.impl.common;

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
import net.modificationstation.stationapi.api.client.event.keyboard.KeyPressed;
import net.modificationstation.stationapi.api.client.event.model.ModelRegister;
import net.modificationstation.stationapi.api.client.event.option.KeyBindingRegister;
import net.modificationstation.stationapi.api.client.event.render.entity.EntityRendererRegister;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegister;
import net.modificationstation.stationapi.api.client.event.texture.TexturesPerFileListener;
import net.modificationstation.stationapi.api.client.item.CustomItemOverlay;
import net.modificationstation.stationapi.api.client.texture.TextureRegistry;
import net.modificationstation.stationapi.api.common.block.EffectiveForTool;
import net.modificationstation.stationapi.api.common.entity.EntityHandlerRegistry;
import net.modificationstation.stationapi.api.common.entity.HasOwner;
import net.modificationstation.stationapi.api.common.event.EventRegistry;
import net.modificationstation.stationapi.api.common.event.GameEvent;
import net.modificationstation.stationapi.api.common.event.ModEvent;
import net.modificationstation.stationapi.api.common.event.achievement.AchievementRegister;
import net.modificationstation.stationapi.api.common.event.block.BlockNameSet;
import net.modificationstation.stationapi.api.common.event.block.BlockRegister;
import net.modificationstation.stationapi.api.common.event.block.TileEntityRegister;
import net.modificationstation.stationapi.api.common.event.container.slot.ItemUsedInCrafting;
import net.modificationstation.stationapi.api.common.event.entity.EntityRegister;
import net.modificationstation.stationapi.api.common.event.entity.player.PlayerHandlerRegister;
import net.modificationstation.stationapi.api.common.event.item.ItemCreation;
import net.modificationstation.stationapi.api.common.event.item.ItemNameSet;
import net.modificationstation.stationapi.api.common.event.item.ItemRegister;
import net.modificationstation.stationapi.api.common.event.item.tool.EffectiveBlocksProvider;
import net.modificationstation.stationapi.api.common.event.item.tool.OverrideIsEffectiveOn;
import net.modificationstation.stationapi.api.common.event.level.LevelInit;
import net.modificationstation.stationapi.api.common.event.level.LoadLevelProperties;
import net.modificationstation.stationapi.api.common.event.level.LoadLevelPropertiesOnLevelInit;
import net.modificationstation.stationapi.api.common.event.level.SaveLevelProperties;
import net.modificationstation.stationapi.api.common.event.level.biome.BiomeByClimateProvider;
import net.modificationstation.stationapi.api.common.event.level.biome.BiomeRegister;
import net.modificationstation.stationapi.api.common.event.level.gen.ChunkPopulator;
import net.modificationstation.stationapi.api.common.event.mod.Init;
import net.modificationstation.stationapi.api.common.event.mod.PostInit;
import net.modificationstation.stationapi.api.common.event.mod.PreInit;
import net.modificationstation.stationapi.api.common.event.packet.MessageListenerRegister;
import net.modificationstation.stationapi.api.common.event.packet.PacketRegister;
import net.modificationstation.stationapi.api.common.event.recipe.BeforeRecipeStats;
import net.modificationstation.stationapi.api.common.event.recipe.RecipeRegister;
import net.modificationstation.stationapi.api.common.gui.GuiHandlerRegistry;
import net.modificationstation.stationapi.api.common.mod.StationMod;
import net.modificationstation.stationapi.api.common.mod.entrypoint.Instance;
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
import net.modificationstation.stationapi.api.common.util.ModCore;
import net.modificationstation.stationapi.api.common.util.SideUtils;
import net.modificationstation.stationapi.api.server.entity.StationSpawnData;
import net.modificationstation.stationapi.api.server.event.network.HandleLogin;
import net.modificationstation.stationapi.api.server.event.network.PlayerLogin;
import net.modificationstation.stationapi.api.server.event.network.TrackEntity;
import net.modificationstation.stationapi.impl.client.entity.player.PlayerHelper;
import net.modificationstation.stationapi.impl.client.model.CustomModelRenderer;
import net.modificationstation.stationapi.impl.client.packet.PacketHelper;
import net.modificationstation.stationapi.impl.client.texture.TextureFactory;
import net.modificationstation.stationapi.impl.common.achievement.AchievementPage;
import net.modificationstation.stationapi.impl.common.achievement.AchievementPageManager;
import net.modificationstation.stationapi.impl.common.block.BlockManager;
import net.modificationstation.stationapi.impl.common.config.Category;
import net.modificationstation.stationapi.impl.common.config.Configuration;
import net.modificationstation.stationapi.impl.common.config.Property;
import net.modificationstation.stationapi.impl.common.factory.EnumFactory;
import net.modificationstation.stationapi.impl.common.factory.GeneralFactory;
import net.modificationstation.stationapi.impl.common.item.CustomReach;
import net.modificationstation.stationapi.impl.common.item.JsonItemKey;
import net.modificationstation.stationapi.impl.common.lang.I18n;
import net.modificationstation.stationapi.impl.common.preset.item.PlaceableTileEntityWithMeta;
import net.modificationstation.stationapi.impl.common.preset.item.PlaceableTileEntityWithMetaAndName;
import net.modificationstation.stationapi.impl.common.recipe.*;
import net.modificationstation.stationapi.impl.common.util.ReflectionHelper;
import net.modificationstation.stationapi.impl.common.util.UnsafeProvider;
import net.modificationstation.stationapi.mixin.client.accessor.ClientPlayNetworkHandlerAccessor;
import net.modificationstation.stationapi.mixin.common.accessor.RecipeRegistryAccessor;
import net.modificationstation.stationapi.mixin.common.accessor.SmeltingRecipeRegistryAccessor;
import net.modificationstation.stationapi.mixin.common.accessor.StatsAccessor;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

/**
 * StationAPI main class. Used for some initialization.
 * @author mine_diver
 */
public class StationAPI implements ModCore, PreInit, Init, PreLaunchEntrypoint {

    /**
     * StationAPI's instance.
     */
    @Instance
    public static StationAPI INSTANCE;

    /**
     * StationAPI's ModID.
     */
    @ModID.At
    public static ModID MODID;

    /**
     * StationMod side-dependent entrypoints.
     */
    protected final Set<String> entrypoints = new HashSet<>();

    /**
     * A set of mods that need client-side verification when the client joins server.
     */
    private final Set<ModContainer> modsToVerifyOnClient = new HashSet<>();

    /**
     * Initial setup. Configures logger, entrypoints, and calls the rest of initialization sequence.
     */
    @Override
    public void onPreLaunch() {
        setModID(ModID.of("stationapi"));
        ModID modID = getModID();
        setupAnnotations(modID.getContainer(), this);
        entrypoints.add(modID + ":mod");
        String sideName = FabricLoader.getInstance().getEnvironmentType().name().toLowerCase();
        entrypoints.add(modID + ":mod_" + sideName);
        String name = modID.getName();
        setLogger(LogManager.getFormatterLogger(name + "|API"));
        Configurator.setLevel("mixin", Level.TRACE);
        Configurator.setLevel("Fabric|Loader", Level.INFO);
        Configurator.setLevel(name + "|API", Level.INFO);
        getLogger().info("Initializing " + name + "...");
        PreInit.EVENT.register(this, modID);
        Init.EVENT.register(this, modID);
        getLogger().info("Setting up API...");
        setupAPI();
        getLogger().info("Setting up lang folder...");
        net.modificationstation.stationapi.api.common.lang.I18n.INSTANCE.addLangFolder("/assets/" + modID + "/lang", modID);
        getLogger().info("Loading mods...");
        setupMods();
        getLogger().info("Finished " + name + " setup");
    }

    /**
     * Performs some API setup. Most likely will be removed due to API becoming less abstract.
     */
    public void setupAPI() {
        getLogger().info("Setting up GeneralFactory...");
        net.modificationstation.stationapi.api.common.factory.GeneralFactory generalFactory = net.modificationstation.stationapi.api.common.factory.GeneralFactory.INSTANCE;
        generalFactory.setHandler(new GeneralFactory());
        generalFactory.addFactory(net.modificationstation.stationapi.api.common.config.Configuration.class, args -> new Configuration((File) args[0]));
        generalFactory.addFactory(net.modificationstation.stationapi.api.common.config.Category.class, args -> new Category((String) args[0]));
        generalFactory.addFactory(net.modificationstation.stationapi.api.common.config.Property.class, args -> new Property((String) args[0]));
        generalFactory.addFactory(net.modificationstation.stationapi.api.common.achievement.AchievementPage.class, args -> new AchievementPage((String) args[0]));
        generalFactory.addFactory(net.modificationstation.stationapi.api.common.preset.item.PlaceableTileEntityWithMeta.class, args -> new PlaceableTileEntityWithMeta((int) args[0]));
        generalFactory.addFactory(net.modificationstation.stationapi.api.common.preset.item.PlaceableTileEntityWithMetaAndName.class, args -> new PlaceableTileEntityWithMetaAndName((int) args[0]));
        net.modificationstation.stationapi.api.common.factory.EnumFactory enumFactory = net.modificationstation.stationapi.api.common.factory.EnumFactory.INSTANCE;
        generalFactory.addFactory(ToolMaterial.class, args -> enumFactory.addEnum(ToolMaterial.class, (String) args[0], new Class[]{int.class, int.class, float.class, int.class}, new Object[]{args[1], args[2], args[3], args[4]}));
        generalFactory.addFactory(EntityType.class, args -> enumFactory.addEnum(EntityType.class, (String) args[0], new Class[]{Class.class, int.class, Material.class, boolean.class}, new Object[]{args[1], args[2], args[3], args[4]}));
        getLogger().info("Loading config...");
        net.modificationstation.stationapi.api.common.config.Configuration config = getDefaultConfig();
        config.load();
        getLogger().info("Setting up EnumFactory...");
        enumFactory.setHandler(new EnumFactory());
        getLogger().info("Setting up I18n...");
        net.modificationstation.stationapi.api.common.lang.I18n.INSTANCE.setHandler(new I18n());
        getLogger().info("Setting up BlockManager...");
        net.modificationstation.stationapi.api.common.block.BlockManager.INSTANCE.setHandler(new BlockManager());
        getLogger().info("Setting up CraftingRegistry...");
        net.modificationstation.stationapi.api.common.recipe.CraftingRegistry.INSTANCE.setHandler(new CraftingRegistry());
        getLogger().info("Setting up UnsafeProvider...");
        net.modificationstation.stationapi.api.common.util.UnsafeProvider.INSTANCE.setHandler(new UnsafeProvider());
        getLogger().info("Setting up SmeltingRegistry...");
        SmeltingRegistry smeltingRegistry = new SmeltingRegistry();
        net.modificationstation.stationapi.api.common.recipe.SmeltingRegistry.INSTANCE.setHandler(smeltingRegistry);
        getLogger().info("Setting up CustomReach...");
        net.modificationstation.stationapi.api.common.item.CustomReach.CONSUMERS.put("setDefaultBlockReach", CustomReach::setDefaultBlockReach);
        net.modificationstation.stationapi.api.common.item.CustomReach.CONSUMERS.put("setHandBlockReach", CustomReach::setHandBlockReach);
        net.modificationstation.stationapi.api.common.item.CustomReach.CONSUMERS.put("setDefaultEntityReach", CustomReach::setDefaultEntityReach);
        net.modificationstation.stationapi.api.common.item.CustomReach.CONSUMERS.put("setHandEntityReach", CustomReach::setHandEntityReach);
        net.modificationstation.stationapi.api.common.item.CustomReach.SUPPLIERS.put("getDefaultBlockReach", CustomReach::getDefaultBlockReach);
        net.modificationstation.stationapi.api.common.item.CustomReach.SUPPLIERS.put("getHandBlockReach", CustomReach::getHandBlockReach);
        net.modificationstation.stationapi.api.common.item.CustomReach.SUPPLIERS.put("getDefaultEntityReach", CustomReach::getDefaultEntityReach);
        net.modificationstation.stationapi.api.common.item.CustomReach.SUPPLIERS.put("getHandEntityReach", CustomReach::getHandEntityReach);
        getLogger().info("Setting up AchievementPageManager...");
        net.modificationstation.stationapi.api.common.achievement.AchievementPageManager.INSTANCE.setHandler(new AchievementPageManager());
        getLogger().info("Setting up CustomData packet...");
        net.modificationstation.stationapi.api.common.config.Category networkConfig = config.getCategory("Network");
        PacketRegister.EVENT.register(register -> {
            register.accept(networkConfig.getProperty("PacketCustomDataID", 254).getIntValue(), true, true, Message.class);
            config.save();
            MessageListenerRegister.EVENT.getInvoker().registerMessageListeners(MessageListenerRegistry.INSTANCE, MessageListenerRegister.EVENT.getListenerModID(MessageListenerRegister.EVENT.getInvoker()));
        });
        getLogger().info("Setting up BlockNameSet...");
        BlockNameSet.EVENT.register((block, name) -> {
            ModEvent<BlockRegister> event = BlockRegister.EVENT;
            if (event.getCurrentListener() != null) {
                ModID modID = event.getCurrentListenerModID();
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
            if (event.getCurrentListener() != null) {
                ModID modID = event.getCurrentListenerModID();
                if (modID != null) {
                    String modid = modID + ":";
                    if (!name.startsWith(modid) && !name.contains(":"))
                        return modid + name;
                }
            }
            return name;
        });
        getLogger().info("Setting up IsEffectiveOn...");
        OverrideIsEffectiveOn.EVENT.register((toolLevel, arg, meta, effective) -> {
            if (arg instanceof EffectiveForTool)
                effective = ((EffectiveForTool) arg).isEffectiveFor(toolLevel, meta);
            return effective;
        });
        getLogger().info("Setting up TileEntityRegister...");
        TileEntityRegister.EVENT.register(smeltingRegistry);
        getLogger().info("Setting up LoadLevelPropertiesOnLevelInit...");
        LoadLevelPropertiesOnLevelInit.EVENT.register((levelProperties, tag) -> {
            LevelRegistry.remapping = true;
            StatsAccessor.setField_812(false);
            StatsAccessor.setField_813(false);
            Registry<Registry<?>> registriesRegistry = Registry.REGISTRIES;
            CompoundTag registriesTag = tag.getCompoundTag(registriesRegistry.getRegistryId().toString());
            registriesRegistry.forEach((identifier, registry) -> {
                if (registry instanceof LevelRegistry)
                    ((LevelRegistry<?>) registry).load(registriesTag.getCompoundTag(registry.getRegistryId().toString()));
            });
            LevelRegistry.remapping = false;
        });
        getLogger().info("Setting up SaveLevelProperties...");
        SaveLevelProperties.EVENT.register((levelProperties, tag, spPlayerData) -> {
            Registry<Registry<?>> registriesRegistry = Registry.REGISTRIES;
            CompoundTag registriesTag = new CompoundTag();
            registriesRegistry.forEach((identifier, registry) -> {
                if (registry instanceof LevelRegistry) {
                    CompoundTag registryTag = new CompoundTag();
                    ((LevelRegistry<?>) registry).save(registryTag);
                    registriesTag.put(identifier.toString(), registryTag);
                }
            });
            tag.put(registriesRegistry.getRegistryId().toString(), registriesTag);
        });
        getLogger().info("Setting up RecipeRegister...");
        RecipeRegister.EVENT.register(recipeId -> JsonRecipeParserRegistry.INSTANCE.getByIdentifier(recipeId).ifPresent(recipeParser -> JsonRecipesRegistry.INSTANCE.getByIdentifier(recipeId).ifPresent(recipes -> recipes.forEach(recipeParser))));
        getLogger().info("Setting up BeforeRecipesStats...");
        BeforeRecipeStats.EVENT.register(() -> {
            RecipeRegistryAccessor.invokeCor();
            SmeltingRecipeRegistryAccessor.invokeCor();
        });
        SideUtils.run(

                // CLIENT

                () -> {
                    getLogger().info("Setting up client GeneralFactory...");
                    net.modificationstation.stationapi.api.common.factory.GeneralFactory.INSTANCE.addFactory(net.modificationstation.stationapi.api.client.model.CustomModelRenderer.class, (args) -> new CustomModelRenderer((String) args[0], (String) args[1]));
                    getLogger().info("Setting up TextureFactory...");
                    net.modificationstation.stationapi.api.client.texture.TextureFactory.INSTANCE.setHandler(new TextureFactory());
                    getLogger().info("Setting up TextureRegistry...");
                    TextureRegistry.RUNNABLES.put("unbind", net.modificationstation.stationapi.impl.client.texture.TextureRegistry::unbind);
                    TextureRegistry.FUNCTIONS.put("getRegistry", net.modificationstation.stationapi.impl.client.texture.TextureRegistry::getRegistry);
                    TextureRegistry.SUPPLIERS.put("currentRegistry", net.modificationstation.stationapi.impl.client.texture.TextureRegistry::currentRegistry);
                    TextureRegistry.SUPPLIERS.put("registries", net.modificationstation.stationapi.impl.client.texture.TextureRegistry::registries);
                    getLogger().info("Setting up PlayerHelper...");
                    net.modificationstation.stationapi.api.common.entity.player.PlayerHelper.INSTANCE.setHandler(new PlayerHelper());
                    getLogger().info("Setting up PacketHelper...");
                    net.modificationstation.stationapi.api.common.packet.PacketHelper.INSTANCE.setHandler(new PacketHelper());
                    getLogger().info("Setting up RenderItemOverlay...");
                    RenderItemOverlay.EVENT.register((itemRenderer, itemX, itemY, itemInstance, textRenderer, textureManager) -> {
                        if (itemInstance != null && itemInstance.getType() instanceof CustomItemOverlay) {
                            ((CustomItemOverlay) itemInstance.getType()).renderItemOverlay(itemRenderer, itemX, itemY, itemInstance, textRenderer, textureManager);
                        }
                    });
                    MessageListenerRegister.EVENT.register((messageListeners, modID) -> {
                        messageListeners.registerValue(Identifier.of(modID, "open_gui"), (playerBase, message) -> {
                            boolean isClient = playerBase.level.isClient;
                            GuiHandlerRegistry.INSTANCE.getByIdentifier(Identifier.of(message.strings()[0])).ifPresent(guiHandler -> ((Minecraft) FabricLoader.getInstance().getGameInstance()).openScreen(guiHandler.one().apply(playerBase, isClient ? guiHandler.two().get() : (InventoryBase) message.objects()[0], message)));
                            if (isClient)
                                playerBase.container.currentContainerId = message.ints()[0];
                        });
                        GuiHandlerRegister.EVENT.getInvoker().registerGuiHandlers(GuiHandlerRegistry.INSTANCE, GuiHandlerRegister.EVENT.getListenerModID(GuiHandlerRegister.EVENT.getInvoker()));
                        messageListeners.registerValue(Identifier.of(modID, "spawn_entity"), (playerBase, message) -> EntityHandlerRegistry.INSTANCE.getByIdentifier(Identifier.of(message.strings()[0])).ifPresent(entityProvider -> {
                            double x = message.ints()[1] / 32D, y = message.ints()[2] / 32D, z = message.ints()[3] / 32D;
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
                                if (entity instanceof StationSpawnData)
                                    ((StationSpawnData) entity).readFromMessage(message);
                            }
                        }));
                    }, getModID());
                },

                // SERVER

                () -> {
                    getLogger().info("Setting up PlayerHelper...");
                    net.modificationstation.stationapi.api.common.entity.player.PlayerHelper.INSTANCE.setHandler(new net.modificationstation.stationapi.impl.server.entity.player.PlayerHelper());
                    getLogger().info("Setting up PacketHelper...");
                    net.modificationstation.stationapi.api.common.packet.PacketHelper.INSTANCE.setHandler(new net.modificationstation.stationapi.impl.server.packet.PacketHelper());
                    getLogger().info("Setting up HandleLogin...");
                    HandleLogin.EVENT.register((pendingConnection, arg) -> {
                        if (!getModsToVerifyOnClient().isEmpty()) {
                            StationHandshake handshake = (StationHandshake) arg;
                            String stationAPI = handshake.getStationAPI();
                            String version = handshake.getVersion();
                            ModID modID = getModID();
                            String serverStationAPI = modID.toString();
                            String serverStationVersion = modID.getVersion().getFriendlyString();
                            TranslationStorage translationStorage = TranslationStorage.getInstance();
                            if (stationAPI == null || version == null || !stationAPI.equals(serverStationAPI)) {
                                pendingConnection.drop(translationStorage.translate("disconnect.stationapi:missing_station"));
                                return;
                            } else if (!version.equals(serverStationVersion)) {
                                pendingConnection.drop(translationStorage.translate("disconnect.stationapi:station_version_mismatch", serverStationVersion, version));
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
                                        pendingConnection.drop(translationStorage.translate("disconnect.stationapi:mod_version_mismatch", modMetadata.getName(), modid, serverVersion, clientVersion == null ? "null" : clientVersion));
                                        return;
                                    }
                                } else {
                                    pendingConnection.drop(translationStorage.translate("disconnect.stationapi:missing_mod", modMetadata.getName(), modid, serverVersion));
                                    return;
                                }
                            }
                        }
                    });
                }
        );
    }

    /**
     * Loads main entrypoints and scans mods assets, also invokes preInit, init and postInit events.
     */
    public void setupMods() {
        FabricLoader fabricLoader = FabricLoader.getInstance();
        Collection<ModContainer> mods = fabricLoader.getAllMods();
        getLogger().info("Loading entrypoints...");
        entrypoints.forEach(entrypoint -> fabricLoader.getEntrypointContainers(entrypoint, StationMod.class).forEach(stationModEntrypointContainer -> {
            ModContainer modContainer = stationModEntrypointContainer.getProvider();
            StationMod stationMod = stationModEntrypointContainer.getEntrypoint();
            ModID modID = ModID.of(modContainer);
            stationMod.setModID(modID);
            getLogger().info("Set mod's container");
            if (stationMod instanceof PreInit)
                PreInit.EVENT.register((PreInit) stationMod, modID);
            Init.EVENT.register(stationMod, modID);
            if (stationMod instanceof PostInit)
                PostInit.EVENT.register((PostInit) stationMod, modID);
            getLogger().info("Registered events");
            setupAnnotations(modContainer, stationMod);
            getLogger().info(String.format("Done loading %s (%s)'s \"%s\" StationMod", modID.getName(), modID, stationMod.getClass().getName()));
        }));
        fabricLoader.getEntrypointContainers(Identifier.of(getModID(), "game_event_bus").toString(), Object.class).forEach(entrypointContainer -> {
            ModContainer modContainer = entrypointContainer.getProvider();
            Object o = entrypointContainer.getEntrypoint();
            GameEvent.EVENT_BUS.register(o);
            setupAnnotations(modContainer, o);
        });
        fabricLoader.getEntrypointContainers(Identifier.of(getModID(), "mod_event_bus").toString(), Object.class).forEach(entrypointContainer -> {
            ModContainer modContainer = entrypointContainer.getProvider();
            Object o = entrypointContainer.getEntrypoint();
            ModEvent.getEventBus(ModID.of(modContainer)).register(o);
            setupAnnotations(modContainer, o);
        });
        getLogger().info("Loading assets...");
        ResourceManager.findResources(getModID() + "/recipes", file -> file.endsWith(".json")).forEach(recipe -> {
            try {
                String rawId = new Gson().fromJson(new InputStreamReader(recipe.openStream()), JsonRecipeType.class).getType();
                try {
                    Identifier recipeId = Identifier.of(rawId);
                    JsonRecipesRegistry.INSTANCE.computeIfAbsent(recipeId, identifier -> new HashSet<>()).add(recipe);
                } catch (NullPointerException e) {
                    getLogger().warn("Found an unknown recipe type " + rawId + ". Ignoring.");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        mods.forEach(modContainer -> {
            ModID modID = ModID.of(modContainer);
            String pathName = "/assets/" + modID + "/" + getModID() + "/lang";
            URL path = getClass().getResource(pathName);
            if (path != null) {
                net.modificationstation.stationapi.api.common.lang.I18n.INSTANCE.addLangFolder(pathName, modID);
                getLogger().info("Registered lang path");
            }
        });
        getLogger().info("Gathering mods that require client verification...");
        String value = getModID() + ":verify_client";
        mods.forEach(modContainer -> {
            ModMetadata modMetadata = modContainer.getMetadata();
            if (modMetadata.containsCustomValue(value) && modMetadata.getCustomValue(value).getAsBoolean())
                modsToVerifyOnClient.add(modContainer);
        });
        getLogger().info("Invoking preInit event...");
        PreInit.EVENT.getInvoker().preInit(EventRegistry.INSTANCE, JsonRecipeParserRegistry.INSTANCE, PreInit.EVENT.getInvokerModID());
        getLogger().info("Invoking init event...");
        Init.EVENT.getInvoker().init(Init.EVENT.getInvokerModID());
        getLogger().info("Invoking postInit event...");
        PostInit.EVENT.getInvoker().postInit(PostInit.EVENT.getInvokerModID());
    }

    /**
     * Registers StationAPI's events in the {@link EventRegistry} and vanilla JSON recipe parsers in the {@link JsonRecipeParserRegistry}
     * @param eventRegistry the event registry used to initialize event listeners through fabric.mod.json entrypoints.
     * @param jsonRecipeParserRegistry the JSON recipe parser registry that holds all JSON recipe parsers to automatically run when {@link RecipeRegister} event is called with a proper identifier.
     * @param modID current mod's ModID.
     * @see EventRegistry
     * @see JsonRecipeParserRegistry
     */
    @Override
    public void preInit(EventRegistry eventRegistry, JsonRecipeParserRegistry jsonRecipeParserRegistry, ModID modID) {
        eventRegistry.registerValue(Identifier.of(modID, "achievement_register"), AchievementRegister.EVENT);
        eventRegistry.registerValue(Identifier.of(modID, "block_name_set"), BlockNameSet.EVENT);
        eventRegistry.registerValue(Identifier.of(modID, "block_register"), BlockRegister.EVENT);
        eventRegistry.registerValue(Identifier.of(modID, "tile_entity_register"), TileEntityRegister.EVENT);
        eventRegistry.registerValue(Identifier.of(modID, "item_used_in_crafting"), ItemUsedInCrafting.EVENT);
        eventRegistry.registerValue(Identifier.of(modID, "player_handler_register"), PlayerHandlerRegister.EVENT);
        eventRegistry.registerValue(Identifier.of(modID, "entity_register"), EntityRegister.EVENT);
        eventRegistry.registerValue(Identifier.of(modID, "effective_blocks_provider"), EffectiveBlocksProvider.EVENT);
        eventRegistry.registerValue(Identifier.of(modID, "is_effective_on"), OverrideIsEffectiveOn.EVENT);
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
        eventRegistry.registerValue(Identifier.of(modID, "load_level_properties"), LoadLevelProperties.EVENT);
        eventRegistry.registerValue(Identifier.of(modID, "save_level_properties"), SaveLevelProperties.EVENT);
        eventRegistry.registerValue(Identifier.of(modID, "load_level_properties_on_level_init"), LoadLevelPropertiesOnLevelInit.EVENT);
        eventRegistry.registerValue(Identifier.of(modID, "before_recipes_stats"), BeforeRecipeStats.EVENT);
        SideUtils.run(

                // CLIENT

                () -> {
                    eventRegistry.registerValue(Identifier.of(modID, "gui_register"), GuiHandlerRegister.EVENT);
                    eventRegistry.registerValue(Identifier.of(modID, "render_item_overlay"), RenderItemOverlay.EVENT);
                    eventRegistry.registerValue(Identifier.of(modID, "key_pressed"), KeyPressed.EVENT);
                    eventRegistry.registerValue(Identifier.of(modID, "model_register"), ModelRegister.EVENT);
                    eventRegistry.registerValue(Identifier.of(modID, "key_binding_register"), KeyBindingRegister.EVENT);
                    eventRegistry.registerValue(Identifier.of(modID, "entity_renderer_register"), EntityRendererRegister.EVENT);
                    eventRegistry.registerValue(Identifier.of(modID, "texture_register"), TextureRegister.EVENT);
                    eventRegistry.registerValue(Identifier.of(modID, "textures_per_file_listener"), TexturesPerFileListener.EVENT);
                },

                // SERVER

                () -> {
                    eventRegistry.registerValue(Identifier.of(modID, "handle_login"), HandleLogin.EVENT);
                    eventRegistry.registerValue(Identifier.of(modID, "player_login"), PlayerLogin.EVENT);
                    eventRegistry.registerValue(Identifier.of(modID, "track_entity"), TrackEntity.EVENT);
                }
        );
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
     * Registers the events entrypoints.
     * @param modID current listener's ModID.
     * @see EventRegistry
     */
    @Override
    public void init(ModID modID) {
        EventRegistry.INSTANCE.forEach((identifier, event) -> event.register(identifier));
    }

    /**
     * Performs the setup of entrypoint's annotated fields.
     * @param modContainer entrypoint's mod.
     * @param o entrypoint's instance.
     */
    public void setupAnnotations(ModContainer modContainer, Object o) {
        try {
            ReflectionHelper.setFinalFieldsWithAnnotation(o, Instance.class, o);
            ReflectionHelper.setFinalFieldsWithAnnotation(o, ModID.At.class, modIDField -> modIDField.value().isEmpty() ? ModID.of(modContainer) : ModID.of(modIDField.value()));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the set of mods that need client-side verification when the client joins server.
     * @return the set of mods that need client-side verification when the client joins server.
     */
    public Set<ModContainer> getModsToVerifyOnClient() {
        return Collections.unmodifiableSet(modsToVerifyOnClient);
    }
}
