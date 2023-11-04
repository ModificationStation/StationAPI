package net.modificationstation.sltest.item;

import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.template.item.TemplateItem;

public class ModelItem extends TemplateItem/* implements ItemModelProvider*/ {

    public ModelItem(Identifier identifier) {
        super(identifier);
    }

//    @Override
//    public Model getModel(int damage) {
//        return TextureListener.testItemModel;
//    }
}
