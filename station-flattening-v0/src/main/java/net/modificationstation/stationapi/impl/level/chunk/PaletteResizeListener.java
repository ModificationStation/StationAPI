package net.modificationstation.stationapi.impl.level.chunk;

interface PaletteResizeListener<T> {
   int onResize(int newSize, T objectAdded);
}
