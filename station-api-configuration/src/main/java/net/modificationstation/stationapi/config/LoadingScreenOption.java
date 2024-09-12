package net.modificationstation.stationapi.config;

public enum LoadingScreenOption {
    SHOW("gui.config.stationapi.loadingscreen.show"),
    NO_ANIMATE("gui.config.stationapi.loadingscreen.no_animate"),
    NO_RECOLOR("gui.config.stationapi.loadingscreen.no_recolor"),
    FORGE("gui.config.stationapi.loadingscreen.forge"),
    HIDE("gui.config.stationapi.loadingscreen.hide");

    public final String translationKey;

    LoadingScreenOption(String translationKey) {
        this.translationKey = translationKey;
    }

    @Override
    public String toString() {
        return translationKey;
    }
}
