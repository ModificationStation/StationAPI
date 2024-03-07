package net.modificationstation.stationapi.gradle;

public class GitHelpers {

    public static void forceGoodFileAndLineEndPractises() {
        try {
            new ProcessBuilder("git", "config", "--local", "core.hooksPath", ".githooks/").start();
        } catch (Exception e) {
            System.err.println("Uh oh, git hooks weren't able to be installed!");
            e.printStackTrace();
        }
    }
}
