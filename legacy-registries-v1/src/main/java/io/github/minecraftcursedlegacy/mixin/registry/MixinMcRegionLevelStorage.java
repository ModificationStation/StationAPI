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

package io.github.minecraftcursedlegacy.mixin.registry;

import java.io.File;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.minecraftcursedlegacy.accessor.registry.AccessorDimensionFile;
import io.github.minecraftcursedlegacy.impl.registry.sync.RegistryRemapper;
import net.minecraft.level.dimension.DimensionData;
import net.minecraft.level.dimension.DimensionFile;
import net.minecraft.level.storage.McRegionLevelStorage;
import net.minecraft.level.storage.OldLevelStorage;

@Mixin(McRegionLevelStorage.class)
public class MixinMcRegionLevelStorage extends OldLevelStorage {
	public MixinMcRegionLevelStorage(File file) {
		super(file);
	}

	@Inject(at = @At("RETURN"), method = "createDimensionFile")
	private void addRemapping(String string, boolean flag, CallbackInfoReturnable<DimensionData> info) {
		DimensionFile data = (DimensionFile) info.getReturnValue();
		RegistryRemapper.readAndWrite(new File(((AccessorDimensionFile) data).getParentFolder(), "pomf_registry.dat"), null);
	}
}
