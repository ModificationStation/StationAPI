package net.modificationstation.sltest.entity.player;

import net.minecraft.entity.player.PlayerEntity;

public class PlayerHandler implements net.modificationstation.stationapi.api.entity.player.PlayerHandler {

    private final PlayerEntity player;

    public PlayerHandler(PlayerEntity playerBase) {
        player = playerBase;
    }

    @Override
    public boolean jump() {
//        int tests = 1000;
//        int listeners = 1;
//        int dispatches = 1000000;
//        long total = 0;
//        long min = Long.MAX_VALUE;
//        long max = Long.MIN_VALUE;
//        EventBus bmBus = StationAPI.EVENT_BUS;
//        bmBus.register(SLTest.Listener.class);
//        for (int i = 0; i < tests; i++) {
////            for (int j = 0; j < listeners; j++)
//            long startTS = System.nanoTime();
//            for (int j = 0; j < dispatches; j++)
//                bmBus.post(new SLTest.TestEvent());
//            long stopTS = System.nanoTime();
//            long result = stopTS - startTS;
//            System.out.println("Took: " + result + "ns");
//            total += result;
//            if (result < min)
//                min = result;
//            if (result > max)
//                max = result;
//        }
//        System.out.println("Tests: " + tests);
//        System.out.println("Listeners: " + listeners);
//        System.out.println("Dispatches: " + dispatches);
//        System.out.println("Average: " + total / tests + "ns");
//        System.out.println("Min: " + min + "ns");
//        System.out.println("Max: " + max + "ns");
        return false;
    }
}
