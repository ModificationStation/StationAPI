package net.modificationstation.stationapi.api.client.item;

import net.minecraft.item.ArmorItem;
import net.modificationstation.stationapi.api.registry.Identifier;

public interface ArmourTextureProvider {
    Identifier getTexture(ArmorItem armour);
}
