package net.modificationstation.stationapi.impl.effect;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.network.packet.IdentifiablePacket;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.mixin.effects.AccessorClientNetworkHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class EffectAddRemovePacket extends Packet implements IdentifiablePacket {
	private static final Identifier PACKET_ID = StationAPI.NAMESPACE.id("effect_add_remove");
	private Identifier effectID;
	private int entityID;
	private boolean add;
	private int size = 8;
	
	public EffectAddRemovePacket() {}
	
	public EffectAddRemovePacket(int entityID, Identifier effectID, boolean add) {
		this.effectID = effectID;
		this.entityID = entityID;
		this.add = add;
	}
	
	@Override
	public void read(DataInputStream stream) {
		try {
			entityID = stream.readInt();
			effectID = Identifier.of(stream.readUTF());
			add = stream.readBoolean();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void write(DataOutputStream stream) {
		try {
			stream.writeInt(entityID);
			stream.writeUTF(effectID.toString());
			stream.writeBoolean(add);
			size = stream.size();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void apply(NetworkHandler networkHandler) {
		if (FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT) return;
		AccessorClientNetworkHandler handler = (AccessorClientNetworkHandler) networkHandler;
		Entity entity = handler.stationapi_getEntityByID(entityID);
		if (add) entity.addEffect(effectID);
		else entity.removeEffect(effectID);
	}
	
	@Override
	public int size() {
		return size;
	}
	
	@Override
	public Identifier getId() {
		return PACKET_ID;
	}
	
	public static void register() {
		IdentifiablePacket.register(PACKET_ID, false, true, EffectAddRemovePacket::new);
	}
}
