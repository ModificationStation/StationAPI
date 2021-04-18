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

package io.github.minecraftcursedlegacy.impl.registry;

import io.github.minecraftcursedlegacy.accessor.registry.AccessorEntityRegistry;
import io.github.minecraftcursedlegacy.api.registry.Id;
import io.github.minecraftcursedlegacy.api.registry.Registry;
import net.minecraft.entity.Entity;

import java.util.HashMap;
import java.util.Map;

public class EntityTypeRegistry extends Registry<EntityType> {
	private int currentId = 0;

	/**
	 * Creates a new registry object.
	 *
	 * @param registryName the identifier for this registry.
	 */
	public EntityTypeRegistry(Id registryName) {
		super(EntityType.class, registryName, null);

		// add vanilla entities
		AccessorEntityRegistry.getIdToClassMap().forEach((intId, clazz) -> {
			if (clazz != null) {
				String idPart = AccessorEntityRegistry.getClassToStringIdMap().get(clazz);

				EntityType type = new EntityType(clazz, idPart == null ? "entity" : idPart);
				if (idPart == null) {
					idPart = "entity";
				} else {
					idPart = idPart.toLowerCase();
				}

				this.byRegistryId.put(new Id(idPart), type);
				this.bySerialisedId.put(intId, type);
			}
		});
	}

	@Override
	protected int getNextSerialisedId() {
		Map<Integer, Class<? extends Entity>> idToClass = AccessorEntityRegistry.getIdToClassMap();
		while (idToClass.containsKey(currentId)) {
			++currentId;
		}

		return currentId;
	}

	@Override
	protected int getStartSerialisedId() {
		return 1; //Maybe this could be changed to 0, not sure if vanilla would like an entity having 0 as an id.
	}

	@Override
	protected void beforeRemap() {
		AccessorEntityRegistry.setIdToClassMap(new HashMap<>());
		AccessorEntityRegistry.setClassToIdMap(new HashMap<>());
		AccessorEntityRegistry.setStringIdToClassMap(new HashMap<>());
		AccessorEntityRegistry.setClassToStringIdMap(new HashMap<>());
	}

	@Override
	protected void onRemap(EntityType remappedValue, int newSerialisedId) {
		AccessorEntityRegistry.callRegister(remappedValue.getClazz(), remappedValue.getVanillaRegistryStringId(), newSerialisedId);
	}
}
