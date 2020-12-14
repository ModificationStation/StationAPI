package net.modificationstation.stationloader.api.common.lang;

import net.modificationstation.stationloader.api.common.registry.ModID;
import net.modificationstation.stationloader.api.common.util.HasHandler;

public interface I18n extends HasHandler<I18n> {

    I18n INSTANCE = new I18n() {

        private I18n handler;

        @Override
        public void setHandler(I18n handler) {
            this.handler = handler;
        }

        @Override
        public void addLangFolder(String langFolder) {
            checkAccess(handler);
            handler.addLangFolder(langFolder);
        }

        @Override
        public void addLangFolder(String langFolder, ModID modID) {
            checkAccess(handler);
            handler.addLangFolder(langFolder, modID);
        }

        @Override
        public void changeLang(String region) {
            checkAccess(handler);
            handler.changeLang(region);
        }
    };

    default void addLangFolder(String langFolder) {
        addLangFolder(langFolder, null);
    }

    void addLangFolder(String langFolder, ModID modID);

    void changeLang(String region);
}
