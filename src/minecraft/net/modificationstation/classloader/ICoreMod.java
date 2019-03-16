package net.modificationstation.classloader;

import java.util.Map;

public interface ICoreMod
{
    String[] getLibraryRequestClass();
    
    String[] getASMTransformerClass();

    String getModContainerClass();

    String getSetupClass();

    void setData(Map<String, Object> data);
}
