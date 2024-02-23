package net.modificationstation.stationapi.api.template.item;

public class MetaBlockItem extends TemplateBlockItem {
    public MetaBlockItem(int i) {
        super(i);
        setHasSubtypes(true);
    }

    @Override
    public int method_470(int i) {
        return i;
    }
}
