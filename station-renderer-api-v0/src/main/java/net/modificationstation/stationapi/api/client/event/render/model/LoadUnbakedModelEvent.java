package net.modificationstation.stationapi.api.client.event.render.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.mine_diver.unsafeevents.Event;
import net.modificationstation.stationapi.api.client.render.model.UnbakedModel;
import net.modificationstation.stationapi.api.registry.Identifier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LoadUnbakedModelEvent extends Event {

    @Getter
    private Identifier identifier;
    public UnbakedModel model;

    private static final Maker MAKER = new Maker(new LoadUnbakedModelEvent());

    public static Maker maker() {
        return MAKER.clear();
    }

    @SuppressWarnings("ClassCanBeRecord")
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Maker {

        private final LoadUnbakedModelEvent event;

        private Maker clear() {
            event.identifier = null;
            event.model = null;
            return this;
        }

        public Maker identifier(Identifier identifier) {
            event.identifier = identifier;
            return this;
        }

        public Maker model(UnbakedModel model) {
            event.model = model;
            return this;
        }

        public LoadUnbakedModelEvent make() {
            return event;
        }
    }

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
