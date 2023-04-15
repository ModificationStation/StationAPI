package net.modificationstation.sltest;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class PreLaunchTest implements PreLaunchEntrypoint {
    @Override
    public void onPreLaunch() {
//        Field entrypointField;
//        try {
//            entrypointField = MinecraftGameProvider.class.getDeclaredField("entrypoint");
//        } catch (NoSuchFieldException e) {
//            throw new RuntimeException(e);
//        }
//        entrypointField.setAccessible(true);
//        try {
//            entrypointField.set(FabricLoaderImpl.INSTANCE.getGameProvider(), "net.minecraft.client.Minecraft");
//        } catch (IllegalAccessException e) {
//            throw new RuntimeException(e);
//        }
    }
}
