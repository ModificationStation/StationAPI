package net.modificationstation.classloader;

//THIS WILL BE DELETED WHEN net.modificationstation.classloader WILL BE ABLE TO LOAD net.modificationstation.stationmodloader

public class CoreLibs implements ILibraryProvider
{
    private static Library[] libraries = {
            new Library("argo-2.25.jar", "bb672829fde76cb163004752b86b0484bd0a7f4b"),
            new Library("guava-12.0.1.jar", "b8e78b9af7bf45900e14c6f958486b6ca682195f"),
            new Library("asm-all-4.0.jar", "98308890597acb64047f7e896638e0d98753ae82")
            };

    @Override
    public Library[] getLibraries()
    {
        return libraries;
    }

    @Override
    public String getURL()
    {
        return "http://files.minecraftforge.net/fmllibs/%s";
    }

}
