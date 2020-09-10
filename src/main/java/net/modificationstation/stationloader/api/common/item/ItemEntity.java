package net.modificationstation.stationloader.api.common.item;

public interface ItemEntity {

    ItemEntity copy();

    ItemEntity split(int countToTake);
}
