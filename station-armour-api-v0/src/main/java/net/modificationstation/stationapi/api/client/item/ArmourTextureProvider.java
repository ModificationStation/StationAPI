package net.modificationstation.stationapi.api.client.item;

import net.minecraft.item.armour.Armour;
import net.modificationstation.stationapi.api.registry.Identifier;

public interface ArmourTextureProvider {

    Identifier getTexture(Armour armour);
}
