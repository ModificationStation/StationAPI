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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import io.github.minecraftcursedlegacy.api.attacheddata.v1.DataStorage;
import io.github.minecraftcursedlegacy.api.registry.Id;
import net.minecraft.item.ItemInstance;
import net.minecraft.util.io.CompoundTag;

/**
 * Manager for data which can be attached to various vanilla objects, such as items and blocks.
 * @deprecated since 1.0.0. Use {@linkplain io.github.minecraftcursedlegacy.api.attacheddata.v1.DataManager this equivalent} from api v1 instead.
 */
@Deprecated
public class DataManager<T> {
	private final Map<Id, Function<T, ? extends io.github.minecraftcursedlegacy.api.attacheddata.v1.AttachedData>> attachedDataFactories = new HashMap<>();

	/**
	 * Adds the specified attached data to the {@link DataManager} instance. This data can later be accessed on an instance of the object via {@link #getAttachedData}.
	 * @return a key to use to retrieve the attached data from an object.
	 */
	public <E extends io.github.minecraftcursedlegacy.api.attacheddata.v1.AttachedData> DataKey<E> addAttachedData(Id id, Function<T, E> dataProvider) {
		this.attachedDataFactories.put(id, dataProvider);
		return new DataKey<>(id);
	}

	/**
	 * Retrieves the specified attached data from the object.
	 */
	public <E extends io.github.minecraftcursedlegacy.api.attacheddata.v1.AttachedData> E getAttachedData(T object, DataKey<E> key) throws ClassCastException {
		return key.apply(((DataStorage) object).getAttachedData(key.id, () -> this.attachedDataFactories.get(key.id).apply(object)));
	}

	/**
	 * Used by the implementation.
	 * @return a {@linkplain Set set} of all {@linkplain Id ids} of {@link AttachedData} instances registered to this manager.
	 */
	public Set<Id> getDataKeys() {
		return this.attachedDataFactories.keySet();
	}

	/**
	 * Used by the implementation.
	 * @return an attached data instance of the given type constructed by the given tag.
	 */
	public io.github.minecraftcursedlegacy.api.attacheddata.v1.AttachedData deserialize(T object, Id id, CompoundTag data) {
		io.github.minecraftcursedlegacy.api.attacheddata.v1.AttachedData result = this.attachedDataFactories.get(id).apply(object);
		result.fromTag(data);
		return result;
	}

	/**
	 * Used by the implementation.
	 * @param from the object to use the data of.
	 * @param to the object to receive the data.
	 */
	public void copyData(T from, T to) {
		DataStorage to_ = (DataStorage) (Object) to;

		((DataStorage) (Object) from).getAllAttachedData().forEach(entry -> {
			Id dataId = entry.getKey();
			io.github.minecraftcursedlegacy.api.attacheddata.v1.AttachedData data = entry.getValue();
			to_.putAttachedData(dataId, data.copy());
		});
	}

	public static DataManager<ItemInstance> ITEM_INSTANCE;

	/**
	 * @deprecated since 1.0.0. Use {@linkplain io.github.minecraftcursedlegacy.api.attacheddata.v1.DataManager.DataKey this equivalent} from api v1 instead.
	 */
	@Deprecated
	public static class DataKey<T extends io.github.minecraftcursedlegacy.api.attacheddata.v1.AttachedData> {
		protected DataKey(Id id) throws NullPointerException {
			if (id == null) {
				throw new NullPointerException("DataKey cannot store a null ID!");
			}

			this.id = id;
		}

		private final Id id;

		@SuppressWarnings("unchecked")
		private T apply(io.github.minecraftcursedlegacy.api.attacheddata.v1.AttachedData data) throws ClassCastException {
			return (T) data;
		}
	}
}