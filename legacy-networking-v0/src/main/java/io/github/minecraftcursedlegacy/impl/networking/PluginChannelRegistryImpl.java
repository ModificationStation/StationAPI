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

import java.util.HashMap;

import io.github.minecraftcursedlegacy.api.networking.PluginChannel;
import net.minecraft.network.PacketHandler;

public class PluginChannelRegistryImpl {
	private PluginChannelRegistryImpl() {
	}

	private static HashMap<String, PluginChannel> channelMap = new HashMap<>();
	
	public static void registerPluginChannel(PluginChannel channel) {
		channelMap.put(channel.getChannelIdentifier().toString(), channel);
	}

	protected static void handlePacket(PacketHandler arg, PluginMessagePacket packet) {
		channelMap.get(packet.channel).onReceive(arg, packet.data);
	}
}
