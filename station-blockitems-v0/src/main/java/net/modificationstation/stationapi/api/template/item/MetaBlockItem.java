package net.modificationstation.stationapi.api.template.item;

public class MetaBlockItem extends TemplateBlockItem {
    public MetaBlockItem(int i) {
        super(i);
        setHasSubtypes(true);
    }

    @Override
    public int getPlacementMetadata(int i) {
        return i;
    }
}
