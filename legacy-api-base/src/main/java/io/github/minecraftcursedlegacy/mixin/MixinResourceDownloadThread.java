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

package io.github.minecraftcursedlegacy.mixin;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map.Entry;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.base.Strings;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.github.minecraftcursedlegacy.accessor.AccessorMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ResourceDownloadThread;

@Mixin(ResourceDownloadThread.class)
public class MixinResourceDownloadThread {
	@Shadow
	public File workingDirectory;
	@Shadow
	private Minecraft field_138;
	@Shadow
	private boolean field_139;

	@Inject(method = "run", at = @At("HEAD"), remap = false, cancellable = true)
	private void go(CallbackInfo info) {
		if (field_138.isApplet && ((AccessorMinecraft) field_138).getApplet() != null) {
			String assetsDir = ((AccessorMinecraft) field_138).getApplet().getParameter("fabric.arguments.assetsDir");

			out: if (!Strings.isNullOrEmpty(assetsDir)) {
				Path assets = Paths.get(assetsDir);

				Path index = assets.resolve("indexes");
				Path assetList;
				if (Files.isDirectory(index)) {
					//The vanilla launcher will use this as the index name
					assetList = index.resolve("pre-1.6.json");

					if (!Files.isReadable(assetList)) {
						//Loom however prefixes it with the Minecraft version too
						assetList = index.resolve("b1.7.3-pre-1.6.json");
					}

					if (!Files.isReadable(assetList)) {
						System.err.println("Unable to find asset index");
						break out;
					}
				} else {
					System.err.println("Cannot find index directory (expected it to be " + index + ')');
					break out;
				}

				Path files = assets.resolve("objects");
				if (Files.isDirectory(files)) {
					JsonObject objects;
					try (Reader in = Files.newBufferedReader(assetList)) {
						objects = JsonParser.parseReader(in).getAsJsonObject().get("objects").getAsJsonObject();
					} catch (IOException | IllegalStateException e) {
						System.err.println("Error reading asset index from " + assetList);
						e.printStackTrace();
						break out;
					}

					for (Entry<String, JsonElement> entry : objects.entrySet()) {
						if (entry.getKey().indexOf('/') < 0) continue; //Minecraft doesn't expect any root resources
						File asset = new File(workingDirectory, entry.getKey());

						if (!asset.exists() || asset.length() != entry.getValue().getAsJsonObject().get("size").getAsInt()) {
							asset.getParentFile().mkdirs();

							try {
								String hash = entry.getValue().getAsJsonObject().get("hash").getAsString();
								Files.copy(files.resolve(hash.substring(0, 2)).resolve(hash), asset.toPath());
							} catch (IOException e) {
								System.err.println("Error copying asset " + entry.getKey() + " to " + asset);
								e.printStackTrace();
							}

							if (field_139) return; //Cancelled loading
						}

						field_138.loadSoundFromDir(entry.getKey(), asset);
					}
				} else {
					System.err.println("Cannot find object directory (expected it to be " + files + ')');
					break out;
				}
			} else {
				System.err.println("Assets directory not supplied, skipping loading");
			}

			info.cancel();
		}
	}
}
