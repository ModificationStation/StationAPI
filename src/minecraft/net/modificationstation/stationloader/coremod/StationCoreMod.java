package net.modificationstation.stationloader.coremod;

import java.util.Map;

import net.modificationstation.classloader.ICoreMod;

/**
 * StationLoader's coremod that provides some class transformers
 * 
 * @author mine_diver
 *
 */
public class StationCoreMod implements ICoreMod{
    
    //Don't really need to provide anything because ASM should be already there with net.modificationstation.classloader 
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

    //It's FML feature. Will be removed after net.modificationstation.classloader's reformat
    @Override
    public String getModContainerClass() {
        return null;
    }

    //It's FML feature. Will be removed after net.modificationstation.classloader's reformat
    @Override
    public String getSetupClass() {
        return null;
    }
    
    //Will be used to get the list of already loaded coremods to load them as StationLoader's mods
    @Override
    public void setData(Map<String, Object> data) {
        
    }
    
}
