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

import io.github.minecraftcursedlegacy.api.attacheddata.v1.AttachedData;
import io.github.minecraftcursedlegacy.api.attacheddata.v1.DataManager;
import io.github.minecraftcursedlegacy.api.attacheddata.v1.DataManager.DataKey;
import io.github.minecraftcursedlegacy.api.event.ActionResult;
import io.github.minecraftcursedlegacy.api.event.TileInteractionCallback;
import io.github.minecraftcursedlegacy.api.registry.Id;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.ItemType;
import net.minecraft.util.io.CompoundTag;

public class LevelPropertiesDataTest implements ModInitializer {
	@Override
	public void onInitialize() {
		test_level = DataManager.LEVEL_PROPERTIES.addAttachedData(TestLevelData.ID, properties -> new TestLevelData(false));

		TileInteractionCallback.EVENT.register((player, level, item, tile, x, y, z, face) -> {
			if (!level.isClient) {
				if (item != null && item.getType() == ItemType.stick) {
					TestLevelData data = DataManager.LEVEL_PROPERTIES.getAttachedData(level.getProperties(), test_level);
					data.active = !data.active;
				} else if (DataManager.LEVEL_PROPERTIES.getAttachedData(level.getProperties(), test_level).active) {
					level.createExplosion(player, x, y, z, 3.0f);
					return ActionResult.SUCCESS;
				}
			}

			return ActionResult.PASS;
		});
	}

	public static DataKey<TestLevelData> test_level;

	private static class TestLevelData implements AttachedData {
		private TestLevelData(boolean active) {
			this.active = active;
		}

		private boolean active;

		@Override
		public Id getId() {
			return ID;
		}

		@Override
		public CompoundTag toTag(CompoundTag tag) {
			tag.put("active", this.active);
			return tag;
		}

		@Override
		public void fromTag(CompoundTag tag) {
			this.active = tag.getBoolean("active");
		}

		@Override
		public AttachedData copy() {
			return new TestLevelData(this.active);
		}

		static final Id ID = new Id("modid", "test_level");
	}
}
