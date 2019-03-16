package net.modificationstation.stationloader.util;

import java.lang.reflect.Method;

import net.minecraft.src.ItemStack;
import net.modificationstation.classloader.ReflectionHelper;

public class PackageAccess {
	public static class CraftingManager {
		public static final CraftingManager getInstance() {
			return instance;
		}
		public void addRecipe(ItemStack itemstack, Object aobj[]) {
			try {
				net.minecraft.src.CraftingManager cf = net.minecraft.src.CraftingManager.getInstance();
				Method addRecipe = ReflectionHelper.findMethod(net.minecraft.src.CraftingManager.class, cf, new String[] {"a", "addRecipe"}, ItemStack.class, Object[].class);
				addRecipe.setAccessible(true);
				addRecipe.invoke(cf, itemstack, aobj);
			} catch (Exception e) {e.printStackTrace();}
		}
		private CraftingManager() {}
		private static final CraftingManager instance = new CraftingManager();
	}
}
