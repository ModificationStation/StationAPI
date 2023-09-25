package net.modificationstation.stationapi.impl.worldgen;

import net.minecraft.util.maths.MathHelper;

public class IDVoronoiNoise {
	private final int seed;
	
	public IDVoronoiNoise(int seed) {
		this.seed = seed;
	}
	
	public int getID(double x, double z, int maxID) {
		int ix = MathHelper.floor(x);
		int iz = MathHelper.floor(z);
		
		float sdx = (float) (x - ix);
		float sdz = (float) (z - iz);
		
		float distance = 1000;
		
		int di = 0;
		int dj = 0;
		
		for (byte i = -1; i < 2; i++) {
			for (byte j = -1; j < 2; j++) {
				float dx = wrap(hash(ix + i, iz + j, seed), 3607) / 3607.0F * 0.8F + i - sdx;
				float dy = wrap(hash(ix + i, iz + j, seed + 13), 3607) / 3607.0F * 0.8F + j - sdz;
				float d = dx * dx + dy * dy;
				if (d < distance) {
					distance = d;
					di = i;
					dj = j;
				}
			}
		}
		
		return wrap(hash(ix + di, iz + dj, seed + 27), maxID);
	}
	
	private int hash(int x, int y, int seed) {
		int h = seed + x * 374761393 + y * 668265263;
		h = (h ^ (h >> 13)) * 1274126177;
		return h ^ (h >> 16);
	}
	
	private int wrap(long value, int side) {
		int result = (int) (value - value / side * side);
		return result < 0 ? result + side : result;
	}
}
