package net.modificationstation.stationloader.coremod;

import java.util.Map;

import net.modificationstation.classloader.ICoreMod;

public class StationCoreMod implements ICoreMod{
    
    @Override
    public String[] getLibraryRequestClass() {
        return null;
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[] {
                "net.modificationstation.stationloader.coremod.EventsInjectorTransformer"
                };
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setData(Map<String, Object> data) {
        
    }
    
}
