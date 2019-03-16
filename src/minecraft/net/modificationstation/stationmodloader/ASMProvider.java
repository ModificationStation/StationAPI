package net.modificationstation.stationmodloader;

import net.modificationstation.classloader.ILibraryProvider;
import net.modificationstation.classloader.Library;

public class ASMProvider implements ILibraryProvider{

    @Override
    public Library[] getLibraries() {
        return new Library[] {
                new Library("asm-5.2.jar", "4ce3ecdc7115bcbf9d4ff4e6ec638e60760819df")
                };
    }

    @Override
    public String getURL() {
        return "https://repository.ow2.org/nexus/service/local/repositories/releases/content/org/ow2/asm/asm/5.2/%s";
    }
    
}
