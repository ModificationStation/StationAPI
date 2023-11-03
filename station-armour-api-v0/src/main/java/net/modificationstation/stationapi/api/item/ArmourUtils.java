package net.modificationstation.stationapi.api.item;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;

public class ArmourUtils {
    /**
     * This is not perfect, but it gets very close to exact vanilla. Uses double to allow for extra precision.
     * Gets vanilla armour reduction for the given armour piece.
      */
    public static double getVanillaArmourReduction(ItemStack armour) {
        double var1 = 0;
        double var2 = 0;
        double var3 = 0;

        if (armour != null && armour.getItem() instanceof ArmorItem actualArmour) {
            double var5 = armour.getMaxDamage();
            double var6 = armour.method_721();
            double var7 = var5 - var6;
            var2 += var7;
            var3 += var5;
            double var8 = actualArmour.field_2084;
            var1 += var8;
        }

        return ((var1 - 1D) * var2 / var3 + 1D) / 5;
    }
}
