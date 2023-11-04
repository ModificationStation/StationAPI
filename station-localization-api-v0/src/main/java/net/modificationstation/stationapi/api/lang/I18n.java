package net.modificationstation.stationapi.api.lang;

import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.resource.language.LanguageManager;

/**
 * @deprecated Use {@link LanguageManager} instead.
 */
@Deprecated
public class I18n {
    public static void addLangFolder(String langFolder) {
        LanguageManager.addPath(langFolder);
    }

    public static void addLangFolder(Namespace namespace, String langFolder) {
        LanguageManager.addPath(langFolder, namespace);
    }

    public static void changeLang(String region) {
        LanguageManager.changeLanguage(region);
    }
}
