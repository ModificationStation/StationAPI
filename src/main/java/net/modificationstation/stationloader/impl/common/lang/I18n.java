package net.modificationstation.stationloader.impl.common.lang;

import net.minecraft.client.resource.language.TranslationStorage;
import net.modificationstation.stationloader.api.common.registry.ModID;
import net.modificationstation.stationloader.mixin.common.accessor.TranslationStorageAccessor;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class I18n implements net.modificationstation.stationloader.api.common.lang.I18n {

    public I18n() {
        addLangFolder("/lang");
    }

    @Override
    public void addLangFolder(String langFolder, ModID modID) {
        langFolders.put(langFolder, modID == null ? null : modID.toString());
    }

    @Override
    public void changeLang(String region) {
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

    private void loadLang(Properties translations, String path, String modid) throws IOException {
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

    private final Map<String, String> langFolders = new HashMap<>();
}
