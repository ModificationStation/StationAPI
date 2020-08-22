package net.modificationstation.stationloader.impl.common.lang;

import net.minecraft.client.resource.language.TranslationStorage;
import net.modificationstation.stationloader.mixin.client.accessor.TranslationStorageAccessor;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class I18n implements net.modificationstation.stationloader.api.common.lang.I18n {

    public I18n() {
        addLangFolder("/lang");
    }

    @Override
    public void addLangFolder(String langFolder) {
        langFolders.add(langFolder);
    }

    @Override
    public void changeLang(String region) {
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

    private final Set<String> langFolders = new HashSet<>();
}
