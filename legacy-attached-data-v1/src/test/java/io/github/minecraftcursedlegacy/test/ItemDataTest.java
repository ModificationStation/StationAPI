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

package io.github.minecraftcursedlegacy.test;

import javax.annotation.Nullable;

import io.github.minecraftcursedlegacy.api.attacheddata.v1.AttachedData;
import io.github.minecraftcursedlegacy.api.attacheddata.v1.DataManager;
import io.github.minecraftcursedlegacy.api.attacheddata.v1.DataManager.DataKey;
import io.github.minecraftcursedlegacy.api.event.ActionResult;
import io.github.minecraftcursedlegacy.api.event.TileInteractionCallback;
import io.github.minecraftcursedlegacy.api.registry.Id;
import io.github.minecraftcursedlegacy.api.registry.Registries;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.ItemType;
import net.minecraft.util.io.CompoundTag;

// Adds data attached to wooden axes. When a wooden axe is first right clicked on a tile, it picks up that tile's essence.
// Subsequent right clicks change the tiles interacted with to the stored essence.
public class ItemDataTest implements ModInitializer {
	@Override
	public void onInitialize() {
		test_axe = DataManager.ITEM_INSTANCE.addAttachedData(TestAxeData.ID, item -> new TestAxeData(null));

		TileInteractionCallback.EVENT.register((player, level, item, tile, x, y, z, i1) -> {
			if (tile != null && item != null && item.getType() == ItemType.hatchetWood) {
				TestAxeData data = DataManager.ITEM_INSTANCE.getAttachedData(item, test_axe);

				if (data.tile == null) {
					data.tile = Registries.TILE.getId(tile);
				} else {
					level.setTile(x, y, z, Registries.TILE.getById(data.tile).id);
				}
			}

			return ActionResult.PASS;
		});
	}

	public static DataKey<TestAxeData> test_axe;

	public static class TestAxeData implements AttachedData {
		public TestAxeData(Id tileId) {
			this.tile = tileId;
		}

		@Nullable
		public Id tile;

		@Override
		public Id getId() {
			return ID;
		}

		@Override
		public CompoundTag toTag(CompoundTag tag) {
			tag.put("tile", this.tile == null ? "NULL" : this.tile.toString());
			return tag;
		}

		@Override
		public void fromTag(CompoundTag tag) {
			String tile = tag.getString("tile");

			if (tile.equals("NULL")) {
				this.tile = null;
			} else {
				this.tile = new Id(tile);
			}
		}

		@Override
		public AttachedData copy() {
			return new TestAxeData(this.tile);
		}

		public static final Id ID = new Id("modid", "test_axe");
	}
}
