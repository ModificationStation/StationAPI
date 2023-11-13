package net.modificationstation.stationapi.impl.world.chunk;

interface PaletteResizeListener<T> {
   int onResize(int newSize, T objectAdded);
}
