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

package io.github.minecraftcursedlegacy.mixin.worldtype;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.minecraftcursedlegacy.api.attacheddata.v1.DataManager;
import io.github.minecraftcursedlegacy.api.worldtype.WorldType;
import io.github.minecraftcursedlegacy.impl.worldtype.WorldTypeData;
import io.github.minecraftcursedlegacy.impl.worldtype.WorldTypeImpl;
import net.minecraft.level.LevelProperties;
import net.minecraft.level.dimension.Dimension;
import net.minecraft.level.source.LevelSource;

@Mixin(Dimension.class)
public class MixinDimension {
	@Inject(at = @At("HEAD"), method = "createBiomeSource", cancellable = true)
	private void api_createBiomeSource(CallbackInfo info) {
		Dimension self = (Dimension) (Object) this;					// convert to dimension
		LevelProperties properties = self.level.getProperties();	// retrieve the level properties
		WorldTypeData data = DataManager.LEVEL_PROPERTIES.getAttachedData(properties, WorldTypeImpl.worldTypeData); // get world type data
		WorldType type = WorldType.getById(data.getTypeId());		// retrieve the world type 

		if (type != WorldType.DEFAULT) {							// only mess with non default in case another mod wants to mixin here for some reason
			self.biomeSource = type.createBiomeSource(self.level, data.getOrCreateLoadedData(type.storesAdditionalData())); // set the biome source
			info.cancel(); // prevent default
		}
	}

	@Inject(at = @At("HEAD"), method = "createLevelSource", cancellable = true)
	private void api_createLevelSource(CallbackInfoReturnable<LevelSource> info) {
		Dimension self = (Dimension) (Object) this;					// convert to dimension
		LevelProperties properties = self.level.getProperties();	// retrieve the level properties
		WorldTypeData data = DataManager.LEVEL_PROPERTIES.getAttachedData(properties, WorldTypeImpl.worldTypeData); // get world type data
		WorldType type = WorldType.getById(data.getTypeId());		// retrieve the world type 

		if (type != WorldType.DEFAULT) {							// only mess with non default in case another mod wants to mixin here for some reason
			info.setReturnValue(type.createChunkGenerator(self.level, data.getOrCreateLoadedData(type.storesAdditionalData()))); // set the custom chunk generator
		}
	}
}
