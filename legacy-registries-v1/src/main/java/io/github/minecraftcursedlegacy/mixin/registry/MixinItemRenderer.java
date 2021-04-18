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

package io.github.minecraftcursedlegacy.mixin.registry;

import org.objectweb.asm.Opcodes;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.minecraftcursedlegacy.impl.registry.client.AtlasMapper;
import net.minecraft.client.render.TextRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.ItemEntity;

@Mixin(ItemRenderer.class)
@Deprecated
abstract class MixinItemRenderer extends EntityRenderer {
	@Inject(method = "render",
			at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/Tessellator;INSTANCE:Lnet/minecraft/client/render/Tessellator;", opcode = Opcodes.GETSTATIC)
	)
	private void fixAtlas(ItemEntity entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo info) {
		AtlasMapper.getAtlas(dispatcher.textureManager, entity.item.itemId, entity.item.getDamage()).ifPresent(dispatcher.textureManager::bindTexture);
	}

	@Inject(method = "method_1486",
			at = @At(value = "FIELD", target = "Lnet/minecraft/item/ItemType;byId:[Lnet/minecraft/item/ItemType;", opcode = Opcodes.GETSTATIC)
	)
	private void fixAtlas(TextRenderer textManager, TextureManager textureManager, int itemID, int meta, int texturePosition, int x, int y, CallbackInfo info) {
		AtlasMapper.getAtlas(textureManager, itemID, meta).ifPresent(textureManager::bindTexture);
	}
}