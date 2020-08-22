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
            if (handler == null)
                throw new RuntimeException("Accessed StationLoader too early!");
            else
                handler.addLangFolder(langFolder);
        }

        @Override
        public void changeLang(String region) {
            if (handler == null)
                throw new RuntimeException("Accessed StationLoader too early!");
            else
                handler.changeLang(region);
        }
    };

    void addLangFolder(String langFolder);

    void changeLang(String region);
}
