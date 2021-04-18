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

package io.github.minecraftcursedlegacy.api.client;

import java.util.OptionalInt;

import io.github.minecraftcursedlegacy.impl.registry.client.AtlasMapper;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.ItemType;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * Utility methods for {@link ItemType}s which use textures from other than the main item atlas.
 *
 * @deprecated use paulevs' CoreLib for textures instead
 * @since 0.5.0
 *
 * @author Chocohead
 */
@Deprecated
public final class AtlasMap {
	private AtlasMap() {
	}

	/**
	 * Register the given atlas for use with the {@link ItemType} corresponding to the given 
	 * {@link ItemInstance#itemId} when it has the given {@link ItemInstance#getDamage()}.
	 *
	 * @param stack The item type and damage which uses the given atlas.
	 * @param atlas The location of the custom atlas.
	 *
	 * @throws NullPointerException If atlas is <tt>null</tt>.
	 */
	public static void registerAtlas(ItemInstance stack, String atlas) {
		registerAtlas(stack.itemId, stack.getDamage(), atlas);
	}

	/**
	 * Register the given atlas for use with the given {@link ItemType},
	 * regardless of what {@link ItemInstance#getDamage()} on an instance of it returns.
	 *
	 * @param item The item which uses the given atlas.
	 * @param atlas The location of the custom atlas.
	 *
	 * @throws NullPointerException If atlas is <tt>null</tt>.
	 */
	public static void registerAtlas(ItemType item, String atlas) {
		registerAtlas(item.id, atlas);
	}

	/**
	 * Register the given atlas for use with the {@link ItemType} corresponding to the given ID
	 * when {@link ItemInstance#getDamage()} on an instance of it is the same as the given meta.
	 *
	 * @param itemID The ID of the item which uses the given atlas.
	 * @param meta The damage value an instance must have to use this atlas.
	 * @param atlas The location of the custom atlas.
	 *
	 * @throws NullPointerException If atlas is <tt>null</tt>.
	 */
	public static void registerAtlas(int itemID, int meta, String atlas) {
		AtlasMapper.registerAtlas(itemID, meta, atlas);
	}

	/**
	 * Register the given atlas for use with the {@link ItemType} corresponding to the given ID,
	 * regardless of what {@link ItemInstance#getDamage()} on an instance of it returns.
	 *
	 * @param itemID The ID of the item which uses the given atlas.
	 * @param atlas The location of the custom atlas.
	 *
	 * @throws NullPointerException If atlas is <tt>null</tt>.
	 */
	public static void registerAtlas(int itemID, String atlas) {
		AtlasMapper.registerDefaultAtlas(itemID, atlas);
	}

	/**
	 * Register the given sprite for use with the {@link ItemType} corresponding to the given 
	 * {@link ItemInstance#itemId} when it has the given {@link ItemInstance#getDamage()}.
	 *
	 * @param stack The item type and damage which uses the given sprite.
	 * @param sprite The location of the custom sprite.
	 *
	 * @return The atlas positioning for use with {@link ItemType#method_458(int)}.
	 *
	 * @throws NullPointerException If sprite is <tt>null</tt>.
	 * @throws IllegalArgumentException If there is already an atlas or sprite registered for the given ID and meta.
	 */
	public static int registerSprite(ItemInstance stack, String sprite) {
		return registerSprite(stack.itemId, stack.getDamage(), sprite);
	}

	/**
	 * Register the given sprite for use with the given {@link ItemType},
	 * regardless of what {@link ItemInstance#getDamage()} on an instance of it returns.
	 *
	 * @param item The item which uses the given sprite.
	 * @param sprite The location of the custom sprite.
	 *
	 * @return The atlas positioning for use with {@link ItemType#method_458(int)}.
	 *
	 * @throws NullPointerException If sprite is <tt>null</tt>.
	 * @throws IllegalArgumentException If there is already an atlas or sprite registered for the given ID.
	 */
	public static int registerSprite(ItemType item, String sprite) {
		return registerSprite(item.id, sprite);
	}

	/**
	 * Register the given sprite for use with the {@link ItemType} corresponding to the given ID
	 * when {@link ItemInstance#getDamage()} on an instance of it is the same as the given meta.
	 *
	 * @param itemID The ID of the item which uses the given sprite.
	 * @param meta The damage value an instance must have to use this sprite.
	 * @param sprite The location of the custom sprite.
	 *
	 * @return The atlas positioning for use with {@link ItemType#method_458(int)}.
	 *
	 * @throws NullPointerException If sprite is <tt>null</tt>.
	 * @throws IllegalArgumentException If there is already an atlas or sprite registered for the given ID and meta.
	 */
	public static int registerSprite(int itemID, int meta, String sprite) {
		return AtlasMapper.registerSprite(itemID, meta, sprite);
	}

	/**
	 * Register the given sprite for use with the {@link ItemType} corresponding to the given ID,
	 * regardless of what {@link ItemInstance#getDamage()} on an instance of it returns.
	 *
	 * @param itemID The ID of the item which uses the given sprite.
	 * @param sprite The location of the custom sprite.
	 *
	 * @return The atlas positioning for use with {@link ItemType#method_458(int)}.
	 *
	 * @throws NullPointerException If sprite is <tt>null</tt>.
	 * @throws IllegalArgumentException If there is already an atlas or sprite registered for the given ID.
	 */
	public static int registerSprite(int itemID, String sprite) {
		return AtlasMapper.registerDefaultSprite(itemID, sprite);
	}

	/**
	 * Get the atlas the given {@link ItemType} corresponding to the given ID has.
	 *
	 * @param manager The texture manager rendering the atlas.
	 * @param itemID The ID of the item.
	 *
	 * @return The texture ID of the custom atlas or {@link OptionalInt#empty()} if it uses the default one.
	 */
	@Environment(EnvType.CLIENT)
	public static OptionalInt getAtlas(TextureManager manager, int itemID) {
		return getAtlas(manager, itemID, 0);
	}

	/**
	 * Get the atlas the given {@link ItemType} corresponding to the given ID has with the given damage.
	 *
	 * @param manager The texture manager rendering the atlas.
	 * @param itemID The ID of the item.
	 * @param meta The damage of an {@link ItemInstance}.
	 *
	 * @return The texture ID of the custom atlas or {@link OptionalInt#empty()} if it uses the default one.
	 *
	 * @throws NullPointerException If manager is <tt>null</tt>.
	 */
	@Environment(EnvType.CLIENT)
	public static OptionalInt getAtlas(TextureManager manager, int itemID, int meta) {
		return AtlasMapper.getAtlas(manager, itemID, meta);
	}
}
