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

package io.github.minecraftcursedlegacy.impl.registry.client;

import java.util.HashMap;
import java.util.Map;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.texture.TextureManager;

interface Atlas {
	String getName();

	@Environment(EnvType.CLIENT)
	int getTextureID(TextureManager manager);

	class FileAtlas implements Atlas {
		private static final Map<String, FileAtlas> ATLASI = new HashMap<>();
		private final String location;

		public static FileAtlas forAtlas(String atlas) {
			return ATLASI.computeIfAbsent(atlas, FileAtlas::new);
		}

		private FileAtlas(String location) {
			this.location = location;
		}

		@Override
		public String getName() {
			return location;
		}

		@Override
		@Environment(EnvType.CLIENT)
		public int getTextureID(TextureManager manager) {
			return manager.getTextureId(location);
		}

		@Override
		public int hashCode() {
			return location.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			return this == obj || (obj instanceof FileAtlas && location.equals(((FileAtlas) obj).location));
		}
	}
}