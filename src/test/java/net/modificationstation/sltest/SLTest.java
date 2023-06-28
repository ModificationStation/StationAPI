package net.modificationstation.sltest;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.mine_diver.unsafeevents.EventBus;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.modificationstation.stationapi.api.event.mod.InitEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.util.Null;
import org.apache.logging.log4j.Logger;

public class SLTest {

    @Entrypoint.Logger
    public static final Logger LOGGER = Null.get();

    @Entrypoint.Instance
    public static final SLTest INSTANCE = Null.get();

    @Entrypoint.ModID
    public static final ModID MODID = Null.get();

    @EventListener
    public void init(InitEvent event) {
        SLTest.LOGGER.info(MODID.toString());
        EventBus eventBus = new EventBus();
        eventBus.register(this::onTestEvent, ListenerPriority.LOWEST.numPriority);
        eventBus.register(this::onTestEventbutCOOLER, ListenerPriority.HIGH.numPriority);
        eventBus.register(this::onTestEventButNo);
//        eventBus.register(this::lol);
        eventBus.post(TestEvent.builder().build());
//        TextureAtlas atlas = new TextureAtlas(true, null, null, 0);
//        try {
//            int firstTexture = atlas.addTexture("/assets/sltest/textures/blocks/FreezerSide.png");
//            int secondTexture = atlas.addTexture("/assets/sltest/textures/blocks/FreezerTop.png");
//            int thirdTexture = atlas.addTexture("/assets/sltest/textures/blocks/testBlock.png");
//            System.out.println(firstTexture + " " + secondTexture + " " + thirdTexture);
//            File spritesheet = new File(FabricLoader.getInstance().getGameDir().toFile(), "spritesheet.png");
//            if (!spritesheet.exists())
//                spritesheet.createNewFile();
//            ImageIO.write(atlas.getSpritesheet(), "png", spritesheet);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        {
//            int tests = 1000;
//            int listeners = 1;
//            int dispatches = 1000000;
//            long total = 0;
//            long min = Long.MAX_VALUE;
//            long max = Long.MIN_VALUE;
//            for (int i = 0; i < tests; i++) {
//                EventBus bmBus = new EventBus();
//                for (int j = 0; j < listeners; j++)
//                    bmBus.register(Listener.class);
//                long startTS = System.nanoTime();
//                for (int j = 0; j < dispatches; j++)
//                    bmBus.post(new TestEvent());
//                long stopTS = System.nanoTime();
//                long result = stopTS - startTS;
//                System.out.println("Took: " + result + "ns");
//                total += result;
//                if (result < min)
//                    min = result;
//                if (result > max)
//                    max = result;
//            }
//            System.out.println("Tests: " + tests);
//            System.out.println("Listeners: " + listeners);
//            System.out.println("Dispatches: " + dispatches);
//            System.out.println("Average: " + total / tests + "ns");
//            System.out.println("Min: " + min + "ns");
//            System.out.println("Max: " + max + "ns");
        }
    }

    public static class Listener {

        @EventListener
        public static void benchmark1(TestEvent event2) { }
//
//        @EventListener
//        public static void benchmark2(TestEvent event2) { }
//
//        @EventListener
//        public static void benchmark3(TestEvent event2) { }
    }

    public void onTestEvent(TestEvent event) {
        SLTest.LOGGER.info("oh wow, a test event");
    }

    public void onTestEventbutCOOLER(TestEvent event) {
        SLTest.LOGGER.info("am cooler that the thing that's gonna execute after me");
    }

    public void onTestEventButNo(TestEvent event) {
        SLTest.LOGGER.info("no, not me");
    }

//    public void lol(Event event) {
//        System.out.println("lol");
//    }

    @SuperBuilder
    public static class TestEvent extends Event {}
}
