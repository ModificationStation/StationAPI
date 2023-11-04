package net.modificationstation.stationapi.api.template.item;

public class MetaBlockItem extends TemplateBlock {
    public MetaBlockItem(int i) {
        super(i);
        setFuel(true);
    }

    @Override
    public int method_470(int i) {
        return i;
    }
}
