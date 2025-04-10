package net.modificationstation.stationapi.api.client.event.render.model;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.modificationstation.stationapi.api.client.render.model.ModelLoader;
import net.modificationstation.stationapi.api.client.render.model.UnbakedModel;
import net.modificationstation.stationapi.api.util.Identifier;

import java.io.IOException;

@SuperBuilder
public class PreLoadUnbakedModelEvent extends Event {
    @FunctionalInterface
    public interface Loader {
        UnbakedModel load(Identifier identifier) throws IOException;
    }

    public final Identifier identifier;
    public final ModelLoader modelLoader;
    public Loader loader;
}
