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

package io.github.minecraftcursedlegacy.api.attacheddata.v1;

import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Supplier;

import io.github.minecraftcursedlegacy.api.registry.Id;
import net.minecraft.util.io.CompoundTag;

/**
 * Interface implemented by mixins onto game objects that specifies useful methods for the storage of persistent attached data.
 * @since 1.0.0
 * @apiNote this class has existed ever since the attached data code was added to cursed legacy api. However, it was an implementation-only class
 * until it was officially made API in 1.0.0.
 */
public interface DataStorage {
	/**
	 * @return an NBT tag which contains the modded data of the object.
	 */
	CompoundTag getModdedTag();
	/**
	 * Puts the attached data on the object.
	 * @param id the id of the attached data
	 * @param data the data to attach.
	 */
	void putAttachedData(Id id, AttachedData data);
	/**
	 * Retrieves the attached data for the id or attaches the default via a given supplier if it does not exist.
	 * @param id the id of the attached data.
	 * @param supplier the supplier which constructs a new attached data object if it does not exist already.
	 * This supplier should be similar or equal to the one used in the built in {@linkplain DataManager}.
	 * @return the data attached to the object.
	 */
	AttachedData getAttachedData(Id id, Supplier<AttachedData> supplier);
	/**
	 * Retrieves the entry set of all attached data on the object.
	 */
	Set<Entry<Id,AttachedData>> getAllAttachedData();
}
