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

package io.github.minecraftcursedlegacy.impl.worldtype;

import javax.annotation.Nullable;

import io.github.minecraftcursedlegacy.api.attacheddata.v1.AttachedData;
import io.github.minecraftcursedlegacy.api.registry.Id;
import net.minecraft.util.io.CompoundTag;

/**
 * Attached data for world types.
 * @since 1.0.0
 */
public class WorldTypeData implements AttachedData {
	WorldTypeData(Id typeId) {
		this.typeId = typeId;
	}

	private Id typeId;
	@Nullable
	private CompoundTag additionalData;

	/**
	 * Set the world type id in this data.
	 */
	public void setTypeId(Id id) {
		this.typeId = id;
	}

	@Nullable
	public CompoundTag getOrCreateLoadedData(boolean storesAdditionalData) {
		// If null and should not be null, set it to a new blank compound tag. Otherwise, just return the value (which will be null if it doesn't store data, or the existing tag)
		return this.additionalData == null && storesAdditionalData ? (this.additionalData = new CompoundTag()) : this.additionalData;
	}

	/**
	 * @return the world type id in this data.
	 */
	public Id getTypeId() {
		return this.typeId;
	}

	@Override
	public Id getId() {
		return ID;
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.put("id", this.typeId.toString());

		if (this.additionalData != null) {
			tag.put("data", this.typeId.toString());
		}

		return tag;
	}

	@Override
	public void fromTag(CompoundTag tag) {
		this.typeId = new Id(tag.getString("id"));

		if (tag.containsKey("data")) {
			this.additionalData = tag.getCompoundTag("data");
		}
	}

	@Override
	public AttachedData copy() {
		return new WorldTypeData(this.typeId);
	}

	public static final Id ID = new Id("api", "world_type");
}
