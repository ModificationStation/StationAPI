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

package io.github.minecraftcursedlegacy.api.networking;

import io.github.minecraftcursedlegacy.api.registry.Id;
import io.github.minecraftcursedlegacy.impl.networking.PluginMessagePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayer;
import net.minecraft.network.PacketHandler;

public abstract class PluginChannel {
	/**
	 * Returns the unique channel identifier of this channel in the form of modid:channelname
	 * This should always have the same return value.
	 * @return Channel Identifier
	 */
	public abstract Id getChannelIdentifier();

	/**
	 * Called when this channel receives a packet.
	 * @param arg The local PacketHandler
	 * @param data The data of the packet
	 */
	public abstract void onReceive(PacketHandler arg, byte[] data);

	/**
	 * Server packet send method.
	 * @param data The data to send
	 * @param player The player to send the data to
	 */
	public void send(byte[] data, ServerPlayer player) {
		player.packetHandler.send(new PluginMessagePacket(getChannelIdentifier().toString(), data));
	}

	/**
	 * Client packet send method.
	 * @param data The data to send
	 * @param mc The local Minecraft
	 */
	public void send(byte[] data, Minecraft mc) {
		mc.method_2145().sendPacket(new PluginMessagePacket(getChannelIdentifier().toString(), data));
	}
}