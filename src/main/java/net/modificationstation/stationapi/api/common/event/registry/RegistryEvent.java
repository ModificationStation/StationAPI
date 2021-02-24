package net.modificationstation.stationapi.api.common.event.registry;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.entity.EntityHandlerRegistry;
import net.modificationstation.stationapi.api.common.event.Event;
import net.modificationstation.stationapi.api.common.gui.GuiHandlerRegistry;
import net.modificationstation.stationapi.api.common.item.ItemRegistry;
import net.modificationstation.stationapi.api.common.packet.MessageListenerRegistry;
import net.modificationstation.stationapi.api.common.recipe.JsonRecipeParserRegistry;
import net.modificationstation.stationapi.api.common.registry.Registry;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class RegistryEvent<T extends Registry<?>> extends Event {

    public final T registry;

    public static class GuiHandlers extends RegistryEvent<GuiHandlerRegistry> {

        public GuiHandlers() {
            super(GuiHandlerRegistry.INSTANCE);
        }
    }

    public static class Blocks extends RegistryEvent<BlockRegistry> {

        public Blocks() {
            super(BlockRegistry.INSTANCE);
        }
    }

    public static class EntityHandlers extends RegistryEvent<EntityHandlerRegistry> {

        public EntityHandlers() {
            super(EntityHandlerRegistry.INSTANCE);
        }
    }

    public static class Items extends RegistryEvent<ItemRegistry> {

        public Items() {
            super(ItemRegistry.INSTANCE);
        }
    }

    public static class MessageListeners extends RegistryEvent<MessageListenerRegistry> {

        public MessageListeners() {
            super(MessageListenerRegistry.INSTANCE);
        }
    }

    public static class JsonRecipeParsers extends RegistryEvent<JsonRecipeParserRegistry> {

        public JsonRecipeParsers() {
            super(JsonRecipeParserRegistry.INSTANCE);
        }
    }
}
