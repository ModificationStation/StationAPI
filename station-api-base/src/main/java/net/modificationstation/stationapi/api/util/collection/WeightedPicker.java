package net.modificationstation.stationapi.api.util.collection;

import net.modificationstation.stationapi.api.util.Util;

import java.util.List;
import java.util.Random;

public class WeightedPicker {
    public static int getWeightSum(List<? extends WeightedPicker.Entry> list) {
        int i = 0;
        int j = 0;

        for(int k = list.size(); j < k; ++j) {
            WeightedPicker.Entry entry = list.get(j);
            i += entry.weight;
        }

        return i;
    }

    public static <T extends WeightedPicker.Entry> T getRandom(Random random, List<T> list, int weightSum) {
        if (weightSum <= 0) {
            throw Util.throwOrPause(new IllegalArgumentException());
        } else {
            int i = random.nextInt(weightSum);
            return getAt(list, i);
        }
    }

    public static <T extends WeightedPicker.Entry> T getAt(List<T> list, int weightMark) {
        int i = 0;

        for(int j = list.size(); i < j; ++i) {
            T entry = list.get(i);
            weightMark -= entry.weight;
            if (weightMark < 0) {
                return entry;
            }
        }

        return null;
    }

    public static <T extends WeightedPicker.Entry> T getRandom(Random random, List<T> list) {
        return getRandom(random, list, getWeightSum(list));
    }

    public static class Entry {
        protected final int weight;

        public Entry(int i) {
            this.weight = i;
        }
    }
}
