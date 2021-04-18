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

package io.github.minecraftcursedlegacy.api.event;

import javax.annotation.Nullable;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.Player;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.minecraft.tile.Tile;

/**
 * Callback for right clicking a tile. This is run both client and server side.
 *
 * <p>Upon return:
 * <ul>
 * <li> SUCCESS cancels further event processing and vanilla code, and the method this is event is called from returns true (succeeds).
 * <li> PASS falls back to further event processing. If all events PASS, then vanilla behaviour runs.
 * <li> FAIL cancels further event processing and vanilla code, and the method this is event is called from returns false (fails).
 * </ul>
 */
@FunctionalInterface
public interface TileInteractionCallback {
	Event<TileInteractionCallback> EVENT = EventFactory.createArrayBacked(TileInteractionCallback.class,
			(listeners) -> (player, level, item, tile, x, y, z, face) -> {
				for (TileInteractionCallback listener : listeners) {
					ActionResult result = listener.onTileInteract(player, level, item, tile, x, y, z, face);

					if (result != ActionResult.PASS) {
						return result;
					}
				}

				return ActionResult.PASS;
			});

	/**
	 * @param player the player causing the tile interaction.
	 * @param level the level the tile is being interacted with in.
	 * @param item the item instance that the player is using to interact with the tile.
	 * @param tile the tile being interacted with at the time of this event firing. This does not change if an event subscriber alters the tile at that position.
	 * @param face probably the tile face. The last parameter of {@link ItemInstance#useOnTile(Player, Level, int, int, int, int)};
	 * @return the action result, as specified in the javadoc of {@link TileInteractionCallback}.
	 */
	ActionResult onTileInteract(Player player, Level level, @Nullable ItemInstance item, Tile tile, int x, int y, int z, int face);
}
