package net.modificationstation.stationapi.api.client.model.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public interface ItemModelPredicateProvider {

   float call(ItemStack stack, @Nullable BlockView world, @Nullable LivingEntity entity, int seed);
}
