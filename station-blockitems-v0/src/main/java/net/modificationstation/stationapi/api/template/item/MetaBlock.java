package net.modificationstation.stationapi.api.template.item;

public class MetaBlock extends TemplateBlock {

    public MetaBlock(int i) {
        super(i);
        setHasSubItems(true);
    }

    @Override
    public int getMetaData(int i) {
        return i;
    }
}
