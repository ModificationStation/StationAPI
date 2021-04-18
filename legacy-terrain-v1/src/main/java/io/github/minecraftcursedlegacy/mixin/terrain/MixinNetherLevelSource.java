package io.github.minecraftcursedlegacy.mixin.terrain;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.minecraftcursedlegacy.api.terrain.ChunkGenEvents;
import net.minecraft.level.Level;
import net.minecraft.level.source.LevelSource;
import net.minecraft.level.source.NetherLevelSource;
import net.minecraft.tile.SandTile;

@Mixin(NetherLevelSource.class)
public class MixinNetherLevelSource {
	@Shadow
	private Random rand;
	@Shadow
	private Level level;

	@Inject(at = @At("RETURN"), method = "decorate")
	private void onDecorate(LevelSource levelSource, int x, int z, CallbackInfo info) {
		SandTile.fallInstantly = true;
		x *= 16;
		z *= 16;

		ChunkGenEvents.Decorate.NETHER.invoker().onDecorate(
				this.level,
				this.level.getBiomeSource().getBiome(x + 16, z + 16),
				this.rand,
				x,
				z);

		SandTile.fallInstantly = false;
	}
}
