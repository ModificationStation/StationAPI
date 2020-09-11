package net.modificationstation.stationloader.api.common.lang;

import net.modificationstation.stationloader.api.common.util.HasHandler;

public interface I18n  extends HasHandler<I18n> {

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
        public void changeLang(String region) {
            checkAccess(handler);
            handler.changeLang(region);
        }
    };

    void addLangFolder(String langFolder);

    void changeLang(String region);
}
