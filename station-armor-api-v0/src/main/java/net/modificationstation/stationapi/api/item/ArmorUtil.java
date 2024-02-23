package net.modificationstation.stationapi.api.item;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;

public class ArmorUtil {
    /**
     * This is not perfect, but it gets very close to exact vanilla. Uses double to allow for extra precision.
     * Gets vanilla armor reduction for the given armor piece.
      */
    public static double getVanillaArmorReduction(ItemStack armor) {
        double var1 = 0;
        double var2 = 0;
        double var3 = 0;

        if (armor != null && armor.getItem() instanceof ArmorItem actualArmor) {
            double var5 = armor.getMaxDamage();
            double var6 = armor.getDamage2();
            double var7 = var5 - var6;
            var2 += var7;
            var3 += var5;
            double var8 = actualArmor.maxProtection;
            var1 += var8;
        }

        return ((var1 - 1D) * var2 / var3 + 1D) / 5;
    }
}
