package net.modificationstation.stationapi.api.client.model.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.level.ClientLevel;
import net.minecraft.entity.Living;
import net.minecraft.item.ItemInstance;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public interface ModelPredicateProvider {

   float call(ItemInstance stack, @Nullable ClientLevel world, @Nullable Living entity);
}
