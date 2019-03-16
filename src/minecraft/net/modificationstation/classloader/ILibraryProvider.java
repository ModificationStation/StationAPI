package net.modificationstation.classloader;

public interface ILibraryProvider
{
    Library[] getLibraries();
    
    String getURL();
}
