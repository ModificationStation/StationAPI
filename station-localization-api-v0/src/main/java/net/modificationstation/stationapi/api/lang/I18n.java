package net.modificationstation.stationapi.api.lang;

import net.minecraft.client.resource.language.TranslationStorage;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.mixin.lang.TranslationStorageAccessor;

import java.io.*;
import java.util.*;

public class I18n {

    private static final Map<String, String> langFolders = new HashMap<>();

    static {
        addLangFolder("/lang");
    }

    public static void addLangFolder(String langFolder) {
        addLangFolder(null, langFolder);
    }

    public static void addLangFolder(ModID modID, String langFolder) {
        langFolders.put(langFolder, modID == null ? null : modID.toString());
    }

    public static void changeLang(String region) {
        Properties translations = ((TranslationStorageAccessor) TranslationStorage.getInstance()).getTranslations();
        translations.clear();
        langFolders.forEach((key, value) -> {
            try {
                loadLang(translations, key + "/" + region + ".lang", value);
                loadLang(translations, key + "/stats_" + region.split("_")[1] + ".lang", value);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static void loadLang(Properties translations, String path, String modid) throws IOException {
        InputStream inputStream = I18n.class.getResourceAsStream(path);
        if (inputStream != null) {
            Properties properties = new Properties();
            properties.load(inputStream);
            if (modid != null) {
                Properties rawProp = properties;
                properties = new Properties();
                Properties finalProperties = properties;
                rawProp.forEach((key, value) -> {
                    if (key instanceof String) {
                        String[] strings = ((String) key).split("\\.");
                        if (strings.length > 1)
                            strings[1] = modid + ":" + strings[1];
                        key = String.join(".", strings);
                    }
                    finalProperties.put(key, value);
                });
            }
            translations.putAll(properties);
        }
    }
}
