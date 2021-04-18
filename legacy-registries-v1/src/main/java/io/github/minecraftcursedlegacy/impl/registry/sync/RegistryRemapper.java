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

package io.github.minecraftcursedlegacy.impl.registry.sync;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.annotation.Nullable;

import io.github.minecraftcursedlegacy.api.registry.Registry;
import net.minecraft.util.io.AbstractTag;
import net.minecraft.util.io.CompoundTag;

public class RegistryRemapper {
	private static final List<Registry<?>> REGISTRIES = new ArrayList<>();

	public static void addRegistry(Registry<?> registry) {
		REGISTRIES.add(registry);
	}

	public static void readAndWrite(File file, @Nullable CompoundTag cache) {
		// just in case
		file.getParentFile().mkdirs();

		try {
			if (!file.createNewFile()) {
				CompoundTag data = null;

				// read
				LOGGER.info("Reading Registry Data.");
				try (DataInputStream dis = new DataInputStream(new GZIPInputStream(new FileInputStream(file)))) {
					data = (CompoundTag) AbstractTag.readTag(dis);
				}

				if (cache == null) { // "cache" acts as newData to write
					cache = new CompoundTag();
				}

				remap(data, cache);

				LOGGER.info("Writing Registry Data.");
				// write
				try (DataOutputStream dos = new DataOutputStream(new GZIPOutputStream(new FileOutputStream(file)))) {
					AbstractTag.writeTag(cache, dos);
				}
			} else {
				if (cache == null) { // "cache" acts as the collected data to write
					cache = new CompoundTag();
				}

				// add data
				LOGGER.info("Collecting Registry Data.");

				for (Registry<?> registry : REGISTRIES) {
					cache.put(registry.getRegistryName().toString(), registry.toTag());
				}

				// write
				LOGGER.info("Writing Registry Data.");
				try (DataOutputStream dos = new DataOutputStream(new GZIPOutputStream(new FileOutputStream(file)))) {
					AbstractTag.writeTag(cache, dos);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException("[Cursed Legacy API] Error while running registry remapper", e);
		}
	}

	public static void remap(CompoundTag readFrom, @Nullable CompoundTag writeTo) {
		Iterator<Registry<?>> iter = REGISTRIES.iterator();
		boolean isNull = writeTo == null; // null check

		// remap and add new data
		LOGGER.info(isNull ? "Remapping Registries." : "Remapping Registries and storing registry updates.");
		while (iter.hasNext()) {
			Registry<?> registry = iter.next(); // get next registry
			String key = registry.getRegistryName().toString(); // get name

			if (isNull) {
				if (readFrom.containsKey(key)) { // Otherwise, just remap
					registry.remap(readFrom.getCompoundTag(key)); // Duplicate earlier line
				}
			} else {
				// If we are writing to a tag, we special-case it
				// We still will need to write, in the case of new entries in a registry, or a new registry!

				if (readFrom.containsKey(key)) {
					writeTo.put(key, registry.remap(readFrom.getCompoundTag(key)));
				} else {
					writeTo.put(key, registry.toTag());
				}
			}
		}
	}

	public static Stream<Registry<?>> registries() {
		return REGISTRIES.stream();
	}

	public static final Logger LOGGER = Logger.getLogger("Cursed Legacy API");
}
