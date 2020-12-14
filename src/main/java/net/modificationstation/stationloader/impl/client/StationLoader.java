package net.modificationstation.stationloader.impl.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.InventoryBase;
import net.modificationstation.stationloader.api.client.event.gui.GuiHandlerRegister;
import net.modificationstation.stationloader.api.client.event.keyboard.KeyPressed;
import net.modificationstation.stationloader.api.client.event.model.ModelRegister;
import net.modificationstation.stationloader.api.client.event.option.KeyBindingRegister;
import net.modificationstation.stationloader.api.client.event.render.entity.EntityRendererRegister;
import net.modificationstation.stationloader.api.client.event.texture.TextureRegister;
import net.modificationstation.stationloader.api.client.event.texture.TexturesPerFileListener;
import net.modificationstation.stationloader.api.common.event.EventRegistry;
import net.modificationstation.stationloader.api.common.event.packet.MessageListenerRegister;
import net.modificationstation.stationloader.api.common.factory.GeneralFactory;
import net.modificationstation.stationloader.api.common.gui.GuiHandlerRegistry;
import net.modificationstation.stationloader.api.common.registry.Identifier;
import net.modificationstation.stationloader.api.common.registry.ModID;
import net.modificationstation.stationloader.impl.client.entity.player.PlayerHelper;
import net.modificationstation.stationloader.impl.client.gui.GuiHelper;
import net.modificationstation.stationloader.impl.client.model.CustomModelRenderer;
import net.modificationstation.stationloader.impl.client.packet.PacketHelper;
import net.modificationstation.stationloader.impl.client.texture.TextureFactory;
import net.modificationstation.stationloader.impl.client.texture.TextureRegistry;

@Environment(EnvType.CLIENT)
public class StationLoader extends net.modificationstation.stationloader.impl.common.StationLoader {

    @Override
    public void setupAPI() {
        super.setupAPI();
        getLogger().info("Setting up client GeneralFactory...");
        GeneralFactory.INSTANCE.addFactory(net.modificationstation.stationloader.api.client.model.CustomModelRenderer.class, (args) -> new CustomModelRenderer((String) args[0], (String) args[1]));
        getLogger().info("Setting up TextureFactory...");
        net.modificationstation.stationloader.api.client.texture.TextureFactory.INSTANCE.setHandler(new TextureFactory());
        getLogger().info("Setting up TextureRegistry...");
        net.modificationstation.stationloader.api.client.texture.TextureRegistry.RUNNABLES.put("unbind", TextureRegistry::unbind);
        net.modificationstation.stationloader.api.client.texture.TextureRegistry.FUNCTIONS.put("getRegistry", TextureRegistry::getRegistry);
        net.modificationstation.stationloader.api.client.texture.TextureRegistry.SUPPLIERS.put("currentRegistry", TextureRegistry::currentRegistry);
        net.modificationstation.stationloader.api.client.texture.TextureRegistry.SUPPLIERS.put("registries", TextureRegistry::registries);
        getLogger().info("Setting up PlayerHelper...");
        net.modificationstation.stationloader.api.common.entity.player.PlayerHelper.INSTANCE.setHandler(new PlayerHelper());
        getLogger().info("Setting up PacketHelper...");
        net.modificationstation.stationloader.api.common.packet.PacketHelper.INSTANCE.setHandler(new PacketHelper());
        getLogger().info("Setting up GuiHelper...");
        net.modificationstation.stationloader.api.common.gui.GuiHelper.INSTANCE.setHandler(new GuiHelper());
        MessageListenerRegister.EVENT.register((messageListeners, modID) -> {
            messageListeners.registerValue(Identifier.of(modID, "open_gui"), ((playerBase, message) -> {
                boolean isClient = playerBase.level.isClient;
                GuiHandlerRegistry.INSTANCE.getByIdentifier(Identifier.of(message.strings()[0])).ifPresent(guiHandler -> ((Minecraft) FabricLoader.getInstance().getGameInstance()).openScreen(guiHandler.one().apply(playerBase, isClient ? guiHandler.two().get() : (InventoryBase) message.objects()[0], message)));
                if (isClient)
                    playerBase.container.currentContainerId = message.ints()[0];
            }));
            GuiHandlerRegister.EVENT.getInvoker().registerGuiHandlers(GuiHandlerRegistry.INSTANCE, GuiHandlerRegister.EVENT.getListenerModID(GuiHandlerRegister.EVENT.getInvoker()));
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
