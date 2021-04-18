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

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Supplier;

import io.github.minecraftcursedlegacy.api.registry.Id;
import net.minecraft.util.io.CompoundTag;

/**
 * Simple {@linkplain DataStorage} which only requires one method to be implemented for the others to be automatically done.
 * @since 1.0.0
 */
public interface SimpleDataStorage extends DataStorage {
	@Override
	default CompoundTag getModdedTag() {
		CompoundTag tag = new CompoundTag();

		this.getRawAttachedDataMap().forEach((id, data) -> {
			tag.put(id.toString(), data.toTag(new CompoundTag()));
		});

		return tag;
	}

	@Override
	default Set<Entry<Id,AttachedData>> getAllAttachedData() {
		return this.getRawAttachedDataMap().entrySet();
	}

	@Override
	default void putAttachedData(Id id, AttachedData data) {
		this.getRawAttachedDataMap().put(id, data);
	}

	@Override
	default AttachedData getAttachedData(Id id, Supplier<AttachedData> supplier) {
		return this.getRawAttachedDataMap().computeIfAbsent(id, i -> supplier.get());
	}

	/**
	 * @return the raw map of ids to attached data that underlies this {@link SimpleDataStorage}.
	 */
	Map<Id, AttachedData> getRawAttachedDataMap();
}
