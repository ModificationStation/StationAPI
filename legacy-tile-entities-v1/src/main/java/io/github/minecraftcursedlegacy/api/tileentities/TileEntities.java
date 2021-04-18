/*
 * Copyright (c) 2020 The Cursed Legacy Team.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package io.github.minecraftcursedlegacy.api.tileentities;

import io.github.minecraftcursedlegacy.accessor.tileentities.AccessorTileEntity;
import io.github.minecraftcursedlegacy.api.registry.Id;
import net.minecraft.tile.entity.TileEntity;

import java.util.Locale;

/**
 * Utilities for registering and retrieving tile entity classes.
 * @since 1.0.0
 */
public final class TileEntities {
	/**
	 * Adds a registered tile entity.
	 * @param clazz the class of the tile entity to register.
	 * @param id the ID to register the tile entity as.
	 */
	public static void registerTileEntity(Class<? extends TileEntity> clazz, Id id) {
		AccessorTileEntity.register(clazz, id.toString());
	}

	/**
	 * Retrieves the tile entity class registered with the given ID.
	 * @param id the ID of the tile entity class.
	 * @return the tile entity class with that ID.
	 */
	public static Class<? extends TileEntity> get(Id id) {
		return AccessorTileEntity.getIdToClassMap().get(id.toString());
	}

	/**
	 * Retrieves the id for a given tile entity class.
	 * @param clazz the tile entity class.
	 * @return the ID of that tile entity class.
	 */
	public static Id getId(Class<? extends TileEntity> clazz) {
		return new Id(getStringId(clazz).toLowerCase(Locale.ROOT));
	}

	/**
	 * Retrieves the string id for a given tile entity class.
	 * @param clazz the tile entity class.
	 * @return the string ID of that tile entity class.
	 */
	public static String getStringId(Class<? extends TileEntity> clazz) {
		return AccessorTileEntity.getClassToIdMap().get(clazz);
	}
}
