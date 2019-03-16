package net.modificationstation.classloader;

public class Library {
    public Library(String fileName, String fileHash) {
        libraryName = fileName;
        hash = fileHash;
    }
    public String getName() {
        return libraryName;
    }
    public String getHash() {
        return hash;
    }
    private final String libraryName;
    private final String hash;
}
