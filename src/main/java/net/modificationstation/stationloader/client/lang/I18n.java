package net.modificationstation.stationloader.client.lang;

import net.minecraft.client.resource.language.TranslationStorage;
import net.modificationstation.stationloader.mixin.client.TranslationStorageAccessor;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class I18n {

    public static void addLangFolder(String langFolder) {
        langFolders.add(langFolder);
    }

    public static void changeLang(String region) {
        Properties translations = ((TranslationStorageAccessor) TranslationStorage.getInstance()).getTranslations();
        translations.clear();
        InputStream inputStream;
        for (String langFolder : langFolders) {
            inputStream = I18n.class.getResourceAsStream(langFolder + "/" + region + ".lang");
            if (inputStream != null)
                try {
                    translations.load(inputStream);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            inputStream = I18n.class.getResourceAsStream(langFolder + "/stats_" + region.split("_")[1] + ".lang");
            if (inputStream != null)
                try {
                    translations.load(inputStream);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
        }
    }

    private static final Set<String> langFolders = new HashSet<>();
    static {
        addLangFolder("/lang");
    }
}
