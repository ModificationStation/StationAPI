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

package io.github.minecraftcursedlegacy.api.data;

import io.github.minecraftcursedlegacy.api.registry.Id;
import net.minecraft.util.io.CompoundTag;

/**
 * Data which can be attached to various vanilla objects, such as items and blocks.
 * @see {@link DataManager}.
 * @deprecated since 1.0.0. Use {@linkplain io.github.minecraftcursedlegacy.api.attacheddata.v1.AttachedData this equivalent} from api v1 instead.
 */
@Deprecated
public interface AttachedData extends io.github.minecraftcursedlegacy.api.attacheddata.v1.AttachedData {
	/**
	 * @return the id of this modded data.
	 */
	Id getId();
	/**
	 * @return a tag representation of this data.
	 */
	CompoundTag toTag(CompoundTag tag);
	/**
	 * @param tag the tag from which to load data.
	 */
	void fromTag(CompoundTag tag);
	/**
	 * Creates a deep copy of this {@link AttachedData}, similar to the recommendations for {@link Object#clone}.
	 */
	AttachedData copy();
}