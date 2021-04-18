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

package io.github.minecraftcursedlegacy.impl.networking;

import net.minecraft.network.PacketHandler;
import net.minecraft.packet.AbstractPacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

//Similar to Plugin Message but supports 
public class PluginMessagePacket extends AbstractPacket {
	public String channel;
	public byte[] data;

	public PluginMessagePacket() {
	}

	public PluginMessagePacket(String channel, byte[] data) {
		this.channel = channel;
		this.data = data;
	}

	@Override
	public void read(DataInputStream dataInputStream) {
		try {
			short channellength = dataInputStream.readShort();
			// Vanilla code is complicated so just duplicate logic here
			StringBuilder buffer = new StringBuilder();
			for (int i = 0; i < channellength; i++) {
				buffer.append(dataInputStream.readChar());
			}

			channel = buffer.toString();

			int datalength = dataInputStream.readInt();
			data = new byte[datalength];
			dataInputStream.read(data, 0, datalength);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void write(DataOutputStream dataOutputStream) {
		try {
			dataOutputStream.writeShort(channel.length());
			dataOutputStream.writeChars(channel);
			dataOutputStream.writeInt(data.length);
			dataOutputStream.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handle(PacketHandler arg) {
		PluginChannelRegistryImpl.handlePacket(arg, this);
	}

	@Override
	public int length() {
		return 2 /* short length */ + channel.length() + 2 /* short length */ + data.length;
	}
}