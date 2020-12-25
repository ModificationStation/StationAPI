package net.modificationstation.stationapi.impl.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.level.ClientLevel;
import net.minecraft.entity.EntityBase;
import net.minecraft.inventory.InventoryBase;
import net.modificationstation.stationapi.api.client.event.gui.GuiHandlerRegister;
import net.modificationstation.stationapi.api.client.event.keyboard.KeyPressed;
import net.modificationstation.stationapi.api.client.event.model.ModelRegister;
import net.modificationstation.stationapi.api.client.event.option.KeyBindingRegister;
import net.modificationstation.stationapi.api.client.event.render.entity.EntityRendererRegister;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegister;
import net.modificationstation.stationapi.api.client.event.texture.TexturesPerFileListener;
import net.modificationstation.stationapi.api.common.entity.EntityHandlerRegistry;
import net.modificationstation.stationapi.api.common.entity.HasOwner;
import net.modificationstation.stationapi.api.common.event.EventRegistry;
import net.modificationstation.stationapi.api.common.event.packet.MessageListenerRegister;
import net.modificationstation.stationapi.api.common.factory.GeneralFactory;
import net.modificationstation.stationapi.api.common.gui.GuiHandlerRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;
import net.modificationstation.stationapi.api.common.registry.ModID;
import net.modificationstation.stationapi.api.server.entity.StationSpawnData;
import net.modificationstation.stationapi.impl.client.entity.player.PlayerHelper;
import net.modificationstation.stationapi.impl.client.gui.GuiHelper;
import net.modificationstation.stationapi.impl.client.model.CustomModelRenderer;
import net.modificationstation.stationapi.impl.client.packet.PacketHelper;
import net.modificationstation.stationapi.impl.client.texture.TextureFactory;
import net.modificationstation.stationapi.impl.client.texture.TextureRegistry;
import net.modificationstation.stationapi.mixin.client.accessor.ClientPlayNetworkHandlerAccessor;

@Environment(EnvType.CLIENT)
public class StationAPI extends net.modificationstation.stationapi.impl.common.StationAPI {

    @Override
    public void setupAPI() {
        super.setupAPI();
        getLogger().info("Setting up client GeneralFactory...");
        GeneralFactory.INSTANCE.addFactory(net.modificationstation.stationapi.api.client.model.CustomModelRenderer.class, (args) -> new CustomModelRenderer((String) args[0], (String) args[1]));
        getLogger().info("Setting up TextureFactory...");
        net.modificationstation.stationapi.api.client.texture.TextureFactory.INSTANCE.setHandler(new TextureFactory());
        getLogger().info("Setting up TextureRegistry...");
        net.modificationstation.stationapi.api.client.texture.TextureRegistry.RUNNABLES.put("unbind", TextureRegistry::unbind);
        net.modificationstation.stationapi.api.client.texture.TextureRegistry.FUNCTIONS.put("getRegistry", TextureRegistry::getRegistry);
        net.modificationstation.stationapi.api.client.texture.TextureRegistry.SUPPLIERS.put("currentRegistry", TextureRegistry::currentRegistry);
        net.modificationstation.stationapi.api.client.texture.TextureRegistry.SUPPLIERS.put("registries", TextureRegistry::registries);
        getLogger().info("Setting up PlayerHelper...");
        net.modificationstation.stationapi.api.common.entity.player.PlayerHelper.INSTANCE.setHandler(new PlayerHelper());
        getLogger().info("Setting up PacketHelper...");
        net.modificationstation.stationapi.api.common.packet.PacketHelper.INSTANCE.setHandler(new PacketHelper());
        getLogger().info("Setting up GuiHelper...");
        net.modificationstation.stationapi.api.common.gui.GuiHelper.INSTANCE.setHandler(new GuiHelper());
        MessageListenerRegister.EVENT.register((messageListeners, modID) -> {
            messageListeners.registerValue(Identifier.of(modID, "open_gui"), (playerBase, message) -> {
                boolean isClient = playerBase.level.isClient;
                GuiHandlerRegistry.INSTANCE.getByIdentifier(Identifier.of(message.strings()[0])).ifPresent(guiHandler -> ((Minecraft) FabricLoader.getInstance().getGameInstance()).openScreen(guiHandler.one().apply(playerBase, isClient ? guiHandler.two().get() : (InventoryBase) message.objects()[0], message)));
                if (isClient)
                    playerBase.container.currentContainerId = message.ints()[0];
            });
            GuiHandlerRegister.EVENT.getInvoker().registerGuiHandlers(GuiHandlerRegistry.INSTANCE, GuiHandlerRegister.EVENT.getListenerModID(GuiHandlerRegister.EVENT.getInvoker()));
            messageListeners.registerValue(Identifier.of(modID, "spawn_entity"), (playerBase, message) -> {
                EntityHandlerRegistry.INSTANCE.getByIdentifier(Identifier.of(message.strings()[0])).ifPresent(entityProvider -> {
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
                });
            });
        }, getModID());
    }

    @Override
    public void preInit(EventRegistry eventRegistry, ModID modID) {
        super.preInit(eventRegistry, modID);
        eventRegistry.registerValue(Identifier.of(modID, "gui_register"), GuiHandlerRegister.EVENT);
        eventRegistry.registerValue(Identifier.of(modID, "key_pressed"), KeyPressed.EVENT);
        eventRegistry.registerValue(Identifier.of(modID, "model_register"), ModelRegister.EVENT);
        eventRegistry.registerValue(Identifier.of(modID, "key_binding_register"), KeyBindingRegister.EVENT);
        eventRegistry.registerValue(Identifier.of(modID, "entity_renderer_register"), EntityRendererRegister.EVENT);
        eventRegistry.registerValue(Identifier.of(modID, "texture_register"), TextureRegister.EVENT);
        eventRegistry.registerValue(Identifier.of(modID, "textures_per_file_listener"), TexturesPerFileListener.EVENT);
    }
}
