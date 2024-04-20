package net.modificationstation.stationapi.api.item;

/**
 * Intended to be applied to an Item, which provides a custom dispenser behavior
 * @author matthewperiut
 * @see DispenseUtil
 */
public interface CustomDispenseBehavior {
    void dispense(DispenseUtil util);
}
