package net.modificationstation.sltest.item;

import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.item.TemplateItemBase;

public class ModelItem extends TemplateItemBase/* implements ItemModelProvider*/ {

    public ModelItem(Identifier identifier) {
        super(identifier);
    }

//    @Override
//    public Model getModel(int damage) {
//        return TextureListener.testItemModel;
//    }
}
