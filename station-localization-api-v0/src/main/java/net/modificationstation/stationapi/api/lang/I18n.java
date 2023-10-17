package net.modificationstation.stationapi.api.lang;

import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.resource.language.LanguageManager;

/**
 * @deprecated Use {@link LanguageManager} instead.
 */
@Deprecated
public class I18n {
    public static void addLangFolder(String langFolder) {
        LanguageManager.addPath(langFolder);
    }

    public static void addLangFolder(ModID modID, String langFolder) {
        LanguageManager.addPath(langFolder, modID);
    }

    public static void changeLang(String region) {
        LanguageManager.changeLanguage(region);
    }
}
