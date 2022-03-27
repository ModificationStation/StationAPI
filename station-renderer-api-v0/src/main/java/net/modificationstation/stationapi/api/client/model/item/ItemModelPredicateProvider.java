package net.modificationstation.stationapi.api.client.model.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Living;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.BlockView;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public interface ItemModelPredicateProvider {

   float call(ItemInstance stack, @Nullable BlockView world, @Nullable Living entity, int seed);
}
