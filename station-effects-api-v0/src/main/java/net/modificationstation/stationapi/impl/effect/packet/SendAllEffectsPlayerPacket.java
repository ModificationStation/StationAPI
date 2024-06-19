package net.modificationstation.stationapi.impl.effect.packet;

import it.unimi.dsi.fastutil.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.effect.EffectRegistry;
import net.modificationstation.stationapi.api.effect.EntityEffect;
import net.modificationstation.stationapi.api.network.packet.IdentifiablePacket;
import net.modificationstation.stationapi.api.util.Identifier;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class SendAllEffectsPlayerPacket extends Packet implements IdentifiablePacket {
	private static final Identifier PACKET_ID = StationAPI.NAMESPACE.id("send_all_effects_player");
	private Collection<Pair<Identifier, Integer>> effects;
	private int size = 8;
	
	public SendAllEffectsPlayerPacket() {
		effects = new ArrayList<>();
	}
	
	public SendAllEffectsPlayerPacket(Collection<Pair<Identifier, Integer>> effects) {
		this.effects = effects;
	}
	
	@Override
	public void read(DataInputStream stream) {
		try {
			int count = stream.readShort();
			for (int i = 0; i < count; i++) {
				Identifier id = Identifier.of(stream.readUTF());
				int ticks = stream.readInt();
				effects.add(Pair.of(id, ticks));
			}
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void write(DataOutputStream stream) {
		try {
			stream.writeShort(effects.size());
			for (Pair<Identifier, Integer> pair : effects) {
				stream.writeUTF(pair.first().toString());
				stream.writeInt(pair.second());
			}
			size = stream.size();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void apply(NetworkHandler networkHandler) {
		if (FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT) return;
		applyEffects();
	}
	
	@Override
	public int size() {
		return size;
	}
	
	@Override
	public Identifier getId() {
		return PACKET_ID;
	}
	
	@Environment(EnvType.CLIENT)
	private void applyEffects() {
		@SuppressWarnings("deprecation")
		PlayerEntity player = ((Minecraft) FabricLoader.getInstance().getGameInstance()).player;
		for (Pair<Identifier, Integer> pair : effects) {
			EntityEffect<? extends Entity> effect = EffectRegistry.makeEffect(player, pair.first());
			if (effect == null) {
				assert StationAPI.LOGGER != null;
				StationAPI.LOGGER.warn("Missing effect: " + pair.first());
				continue;
			}
			effect.setTicks(pair.second());
			player.addEffect(effect);
		}
	}
	
	public static void register() {
		IdentifiablePacket.register(PACKET_ID, true, true, SendAllEffectsPlayerPacket::new);
	}
}
