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

import java.io.DataOutputStream;
import java.io.IOException;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.packet.AbstractPacket;
import net.minecraft.packet.handshake.HandshakeC2S;

@Mixin(HandshakeC2S.class)
public class MixinHandshakeC2SClient {
	@Shadow
	public int protocolVersion;
	@Shadow
	public String field_1210;
	@Shadow
	public long field_1211;
	@Shadow
	public byte field_1212;

	@Inject(at = @At("HEAD"), method = "write", cancellable = true)
	public void write(DataOutputStream dataOutputStream, CallbackInfo bruh) throws IOException {
		dataOutputStream.writeInt(this.protocolVersion);
		AbstractPacket.writeString(this.field_1210, dataOutputStream);
		dataOutputStream.writeLong(-9223372036854775808l);
		dataOutputStream.writeByte(this.field_1212);
		bruh.cancel();
	}
}