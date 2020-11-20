package net.modificationstation.stationloader.impl.common.util;

/**
 * If you use this, you will have to manage damaging armour yourself!
 */
public class ArmourDamageAdjust {
    public int damageNegated;
    public boolean doVanillaDamage;

    public ArmourDamageAdjust(int damageToArmor, boolean doVanillaDamage) {
        this.damageNegated = damageToArmor;
        this.doVanillaDamage = doVanillaDamage;
    }
}
