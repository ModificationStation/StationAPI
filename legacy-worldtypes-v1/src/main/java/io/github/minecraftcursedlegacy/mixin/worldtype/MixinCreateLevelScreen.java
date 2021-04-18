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

package io.github.minecraftcursedlegacy.mixin.worldtype;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.minecraftcursedlegacy.impl.worldtype.WorldTypeImpl;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.screen.CreateLevelScreen;
import net.minecraft.client.gui.widgets.Button;
import net.minecraft.client.resource.language.TranslationStorage;

@Mixin(CreateLevelScreen.class)
public abstract class MixinCreateLevelScreen extends Screen {
	private int api_getWTHeight() {
		return this.height / 4 + 96; // Just Above Old Create World Button Pos
	}

	private boolean api_hasModdedTypesCache;

	@SuppressWarnings("unchecked")
	@Inject(at = @At("RETURN"), method = "init")
	private void api_onInit(CallbackInfo info) {
		if (this.api_hasModdedTypesCache = WorldTypeImpl.hasModdedTypes()) {
			// TODO use TranslationStorage.getInstance()
			this.buttons.add(new Button(2, this.width / 2, api_getWTHeight(), 100, 20,
					TranslationStorage.getInstance().translate(WorldTypeImpl.getSelected().toString()))); // Add button
			((Button) this.buttons.get(0)).y += 15; // Shift the create world and cancel buttons down
			((Button) this.buttons.get(1)).y += 15;
		}
	}

	@Inject(at = @At("RETURN"), method = "render")
	private void api_onRender(int mouseX, int mouseY, float tickDelta, CallbackInfo info) {
		if (this.api_hasModdedTypesCache) {
			this.drawTextWithShadow(this.textManager, "World Type", this.width / 2 - 100, api_getWTHeight() + 5, 10526880); // Draw text (with shadow)
		}
	}

	@Inject(at = @At("HEAD"), method = "buttonClicked", cancellable = true)
	private void api_buttonClicked(Button button, CallbackInfo info) {
		if (this.api_hasModdedTypesCache && button.active && button.id == 2) {
			// Cycle world type
			button.text = TranslationStorage.getInstance().translate(WorldTypeImpl.cycle().toString());
		}
	}
}
