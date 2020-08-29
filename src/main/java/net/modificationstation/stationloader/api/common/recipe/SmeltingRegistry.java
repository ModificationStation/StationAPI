package net.modificationstation.stationloader.api.common.recipe;

import net.minecraft.item.ItemInstance;
import net.modificationstation.stationloader.api.common.util.HasHandler;

public interface SmeltingRegistry extends HasHandler<SmeltingRegistry> {

    SmeltingRegistry INSTANCE = new SmeltingRegistry() {

        private SmeltingRegistry handler;

        @Override
        public void setHandler(SmeltingRegistry handler) {
            this.handler = handler;
        }

        @Override
        public void addSmeltingRecipe(int input, ItemInstance output) {
            if (handler == null)
                throw new RuntimeException("Accessed StationLoader too early!");
            else
                handler.addSmeltingRecipe(input, output);
        }

        @Override
        public void addSmeltingRecipe(ItemInstance input, ItemInstance output) {
            if (handler == null)
                throw new RuntimeException("Accessed StationLoader too early!");
            else
                handler.addSmeltingRecipe(input, output);
        }

        @Override
        public ItemInstance getResultFor(ItemInstance itemInstance) {
            if (handler == null)
                throw new RuntimeException("Accessed StationLoader too early!");
            else
                return handler.getResultFor(itemInstance);
        }
    };

    void addSmeltingRecipe(int input, ItemInstance output);

    void addSmeltingRecipe(ItemInstance input, ItemInstance output);

    ItemInstance getResultFor(ItemInstance itemInstance);
}
