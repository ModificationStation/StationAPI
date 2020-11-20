package net.modificationstation.stationloader.impl.common.util;

import net.minecraft.item.ItemInstance;
import net.minecraft.item.armour.Armour;

public class ArmourUtils {

    // This is not perfect, but it gets very close to exact vanilla. Uses double to allow for extra precision.
    public static double getVanillaArmourReduction(ItemInstance armour) {
        double var1 = 0;
        double var2 = 0;
        double var3 = 0;

        if (armour != null && armour.getType() instanceof Armour) {
            double var5 = armour.method_723();
            double var6 = armour.method_721();
            double var7 = var5 - var6;
            var2 += var7;
            var3 += var5;
            double var8 = ((Armour) armour.getType()).field_2084;
            var1 += var8;
        }

        return (var1 - 1D) * var2 / var3 + 1D;
    }
}
