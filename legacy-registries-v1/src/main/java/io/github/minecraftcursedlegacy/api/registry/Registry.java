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

package io.github.minecraftcursedlegacy.api.registry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import io.github.minecraftcursedlegacy.impl.registry.RegistryImpl;
import io.github.minecraftcursedlegacy.impl.registry.sync.RegistryRemapper;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.util.io.CompoundTag;

/**
 * Registry for game content.
 */
public class Registry<T> implements Iterable<T> {
	/**
	 * Creates a new registry object.
	 * @param clazz the class of the values in this registry.
	 * @param registryName the identifier for this registry.
	 * @param defaultValue the default value for ids in registry.
	 */
	public Registry(Class<T> clazz, Id registryName, @Nullable T defaultValue) {
		this.registryName = registryName;
		this.defaultValue = defaultValue;
		RegistryRemapper.addRegistry(this);

		this.event = RegistryImpl.createEvent(clazz);
	}

	protected final BiMap<Id, T> byRegistryId = HashBiMap.create();
	protected final BiMap<Integer, T> bySerialisedId = HashBiMap.create();
	private final Id registryName;
	@Nullable
	private final T defaultValue;
	private final Event<RegistryEntryAddedCallback<T>> event;
	private int nextId = this.getStartSerialisedId();
	/**
	 * Whether the registry is locked, and values can no longer be registered to it.
	 */
	protected boolean locked = false;

	/**
	 * Registers a value to the registry.
	 * @param id the id of the value to register to the registry.
	 * @param value the value to register to the registry.
	 * @return the value registered to the registry.
	 */
	public <E extends T> E registerValue(Id id, E value) {
		if (locked) {
			throw new RuntimeException("Registry is locked!");
		}

		int serialisedId = this.getNextSerialisedId();
		this.byRegistryId.put(id, value);
		this.bySerialisedId.put(serialisedId, value);
		this.onRegister(serialisedId, id, value);
		return value;
	}

	/**
	 * Registers a value to the registry, with the serialised id supplied.
	 * @param id the id of the value to register to the registry.
	 * @param valueProvider the provider of the value to register to the registry, which takes the int serialised id.
	 * @return the value registered to the registry.
	 */
	public <E extends T> E register(Id id, IntFunction<E> valueProvider) {
		if (locked) {
			throw new RuntimeException("Registry is locked!");
		}

		int serialisedId = this.getNextSerialisedId();
		E value = valueProvider.apply(serialisedId);
		this.byRegistryId.put(id, value);
		this.bySerialisedId.put(serialisedId, value);
		this.onRegister(serialisedId, id, value);
		return value;
	}

	/**
	 * Called when a value is registered.
	 * @param serialisedId the serialised id of the registered value.
	 * @param id the registry id of the registered value.
	 * @param value the registered value.
	 */
	protected void onRegister(int serialisedId, Id id, T value) {
		this.event.invoker().onEntryAdded(value, id, serialisedId);
	}

	/**
	 * Looks up the id in the registry.
	 * @param id the specified id to look up in the registry.
	 * @return the value specified by the id in the registry, if it exists. Otherwise returns the default value.
	 */
	@Nullable
	public T getById(Id id) {
		return this.byRegistryId.getOrDefault(id, this.defaultValue);
	}

	/**
	 * Looks up the id of the value in the registry.
	 * @param value the specified value to find the id of.
	 * @return the id of the value in the registry, if it exists. Otherwise returns null.
	 */
	@Nullable
	public Id getId(T value) {
		return this.byRegistryId.inverse().get(value);
	}

	/**
	 * Looks up the int serialised id in the registry.
	 * @param serialisedId the specified serialised id to look up in the registry.
	 * @return the value specified by the serialised id in the registry, if it exists. Otherwise returns the default value.
	 */
	public T getBySerialisedId(int serialisedId) {
		return this.bySerialisedId.getOrDefault(serialisedId, this.defaultValue);
	}

	/**
	 * Looks up the int serialised id of the value in the registry.
	 * @param value the specified value to find the serialised id of.
	 * @return the int serialised id of the value in the registry, if it exists. Otherwise returns null.
	 */
	public int getSerialisedId(T value) {
		return this.bySerialisedId.inverse().get(value);
	}

	/**
	 * @return the identifier of this registry.
	 */
	public final Id getRegistryName() {
		return this.registryName;
	}

	/**
	 * @return the next serialised id to use, in the initial registry adding phase.
	 */
	protected int getNextSerialisedId() {
		return this.nextId++;
	}

	/**
	 * @return the first serialised id to use.
	 */
	protected int getStartSerialisedId() {
		return 0;
	}

	/**
	 * Remaps the registry based on the provided nbt data.
	 * @param tag the data for the remapper.
	 * @return the input compound tag, with updated data for new entries.
	 */
	public final CompoundTag remap(CompoundTag tag) {
		CompoundTag result = new CompoundTag(); // don't copy potentially missing entries

		// prepare
		this.beforeRemap();
		List<Entry<Id, T>> unmapped = new ArrayList<>();
		Set<Entry<Id, T>> toMap = this.byRegistryId.entrySet();
		this.bySerialisedId.clear();

		// remap serialised ids
		for (Entry<Id, T> entry : toMap) {
			String key = entry.getKey().toString();

			if (tag.containsKey(key)) {
				T value = entry.getValue();

				int newSerialisedId = tag.getInt(key);
				this.bySerialisedId.put(newSerialisedId, value);
				this.onRemap(value, newSerialisedId);
				result.put(key, newSerialisedId); // add to new tag
			} else {
				unmapped.add(entry);
			}
		}

		// re-add new values to the registry
		this.addNewValues(unmapped, result);

		// post remap
		this.postRemap();

		// return updated tag
		return result;
	}

	/**
	 * Called to add new values to the registry during remapping. (i.e. values that were not previously in the remapper).
	 */
	protected void addNewValues(List<Entry<Id, T>> unmapped, CompoundTag tag) {
		int serialisedId = this.getStartSerialisedId();

		for (Entry<Id, T> entry : unmapped) {
			T value = entry.getValue();

			while (this.bySerialisedId.get(serialisedId) != null) {
				++serialisedId;
			}

			// readd to registry
			this.bySerialisedId.put(serialisedId, value);
			// add to tag
			tag.put(entry.getKey().toString(), serialisedId);
			this.onRemap(value, serialisedId);
		}
	}

	/**
	 * @return a tag of all the mappings, for serialisation.
	 */
	public final CompoundTag toTag() {
		CompoundTag tag = new CompoundTag();

		for (Entry<Id, T> entry : this.byRegistryId.entrySet()) {
			tag.put(entry.getKey().toString(), this.bySerialisedId.inverse().get(entry.getValue()));
		}

		return tag;
	}

	/**
	 * Called before registry remapping for this registry.
	 * Override this to add additional preparations for registry remapping.
	 */
	protected void beforeRemap() {
	}

	/**
	 * Called before registry remapping for this registry.
	 * Override this to add finalisations for after registry remapping.
	 */
	protected void postRemap() {
	}

	/**
	 * Called when a serialised id is remapped.
	 * @param remappedValue the value that has been remapped.
	 * @param newSerialisedId the new serialised id of the value.
	 */
	protected void onRemap(T remappedValue, int newSerialisedId) {
	}

	/**
	 * Locks all registries. Used by the implementation.
	 */
	public static void lockAll() {
		RegistryRemapper.registries().forEach(r -> r.locked = true);
	}

	/**
	 *
	 * @return all objects stored in this registry.
	 */
	public Set<T> values() {
		return this.byRegistryId.values();
	}

	/**
	 *
	 * @return the ids of all objects stored in this registry.
	 */
	public Set<Id> ids() {
		return this.byRegistryId.keySet();
	}

	/**
	 *
	 * @return the serialised ids of all objects stored in this registry.
	 */
	public Set<Integer> serialisedIds() {
		return this.bySerialisedId.keySet();
	}

	/**
	 * @return the {@linkplain RegistryEntryAddedCallback} event associated with this registry.
	 */
	public final Event<RegistryEntryAddedCallback<T>> getEvent() {
		return this.event;
	}

	@Override
	@Nonnull
	public Iterator<T> iterator() {
		return this.values().iterator();
	}

	@Override
	public void forEach(Consumer<? super T> consumer) {
		this.values().forEach(consumer);
	}

	@Override
	public Spliterator<T> spliterator() {
		return this.values().spliterator();
	}
}
