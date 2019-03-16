package net.modificationstation.classloader;

public interface IClassTransformer
{
    public byte[] transform(String name, byte[] bytes);
}
