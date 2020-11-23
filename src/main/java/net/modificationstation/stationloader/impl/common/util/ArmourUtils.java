package net.modificationstation.stationloader.impl.common.util;

import net.minecraft.item.ItemInstance;
import net.minecraft.item.armour.Armour;

public class ArmourUtils {

    // This is not perfect, but it gets very close to exact vanilla. Uses double to allow for extra precision.
    public static double getVanillaArmourReduction(ItemInstance armour) {
        return ((((double)armour.method_723() - (double)armour.getDamage()) / (double)armour.method_723()) * (double)((Armour) armour.getType()).field_2084) / 25D;
    }
}
