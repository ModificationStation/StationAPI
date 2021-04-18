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

import java.util.Random;

import net.minecraft.level.Level;
import net.minecraft.level.structure.Feature;
import net.minecraft.tile.Tile;

public class OreRock extends Feature {

	@Override
	public boolean generate(Level level, Random rand, int x, int y, int z) {
		if (level.getTileId(x, y - 1, z) == Tile.GRASS.id) {
			if (rand.nextInt(4) == 0) { // big boi
				for (int xo = -1; xo <= 1; ++xo) {
					for (int zo = -1; zo <= 1; ++zo) {
						for (int yo = 0; yo < 3; ++yo) {
							if (yo == 2 && Math.abs(xo) == 1 && 1 == Math.abs(zo)) {
								continue;
							}

							level.setTile(x + xo, y + yo, z + zo, rand.nextInt(3) == 0 ? Tile.IRON_ORE.id : Tile.STONE.id);
						}
					}
				}
			} else { // smol boi
				for (int xo = 0; xo < 2; ++xo) {
					for (int zo = 0; zo < 2; ++zo) {
						for (int yo = 0; yo < 2; ++yo) {
							level.setTile(x + xo, y + yo, z + zo, rand.nextInt(3) == 0 ? Tile.IRON_ORE.id : Tile.STONE.id);
						}
					}
				}
			}

			return true;
		}

		return false;
	}

}
