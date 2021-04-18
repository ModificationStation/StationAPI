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

package io.github.minecraftcursedlegacy.api.config;

import java.io.File;
import java.io.IOException;

import javax.annotation.Nullable;

import io.github.minecraftcursedlegacy.api.registry.Id;
import net.fabricmc.loader.api.FabricLoader;
import tk.valoeghese.zoesteriaconfig.api.ZoesteriaConfig;
import tk.valoeghese.zoesteriaconfig.api.container.WritableConfig;
import tk.valoeghese.zoesteriaconfig.api.template.ConfigTemplate;

/**
 * Class for loading and saving mod configs.
 */
public final class Configs {
	private Configs() {
		// NO-OP
	}

	/**
	 * Retrieves or creates a new {@link WritableConfig}, based on the given id.
	 * @param configId the identifier of the config.
	 * @param defaults defaults for the config, which can be created with a builder.
	 * @return the {@link WritableConfig} instance with the config data in this file.
	 * @throws IOException if there is an error creating the config file.
	 */
	public static WritableConfig loadOrCreate(Id configId, @Nullable ConfigTemplate defaults) throws IOException {
		File directory = new File(FabricLoader.getInstance().getConfigDirectory(), configId.getNamespace());
		directory.mkdirs();
		File configFile = new File(directory, configId.getName() + ".cfg");
		boolean createNew = configFile.createNewFile();

		WritableConfig result = ZoesteriaConfig.loadConfigWithDefaults(configFile, defaults);

		if (createNew) {
			result.writeToFile(configFile);
		}

		return result;
	}
}
