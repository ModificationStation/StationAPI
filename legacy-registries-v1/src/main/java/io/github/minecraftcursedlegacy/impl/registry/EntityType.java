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

import io.github.minecraftcursedlegacy.api.registry.Id;
import net.minecraft.entity.Entity;

public class EntityType {
	private final Class<? extends Entity> clazz;
	private final String vanillaRegistryStringId;

	/**
	 * Protected constructor used only for vanilla entities.
	 * @param clazz the entity class.
	 * @param vanillaRegistryStringId the vanilla name of the entity.
	 */
	protected EntityType(Class<? extends Entity> clazz, String vanillaRegistryStringId) {
		this.clazz = clazz;
		this.vanillaRegistryStringId = vanillaRegistryStringId;
	}

	public EntityType(Class<? extends Entity> clazz, Id id) {
		this.clazz = clazz;
		this.vanillaRegistryStringId = id.toString();
	}

	public Class<? extends Entity> getClazz() {
		return clazz;
	}

	public String getVanillaRegistryStringId() {
		return vanillaRegistryStringId;
	}

	public Id getId() {
		return new Id(vanillaRegistryStringId);
	}
}
