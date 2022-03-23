package net.modificationstation.stationapi.impl.level.chunk;

import net.minecraft.util.io.ByteArrayTag;

public class NibbleArray {
	private final byte[] data;
	
	public NibbleArray(int capacity) {
		data = new byte[capacity >> 1];
	}
	
	public int getValue(int index) {
		short value = (short) (data[index >> 1] & 255);
		return (index & 1) == 0 ? value & 15 : value >> 4;
	}
	
	public void setValue(int index, int value) {
		int index2 = index >> 1;
		short internal = (short) (data[index2] & 255);
		if ((index & 1) == 0) {
			internal = (short) (value | internal & 0xF0);
		}
		else {
			internal = (short) (value << 4 | internal & 15);
		}
		data[index2] = (byte) internal;
	}
	
	public ByteArrayTag toTag() {
		return new ByteArrayTag(data);
	}
	
	public void copyArray(byte[] array) {
		if (array.length != data.length) return;
		for (int i = 0; i < data.length; i++) {
			data[i] = array[i];
		}
	}
}
