package net.modificationstation.stationapi.api.util.math;

import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import net.modificationstation.stationapi.api.util.Util;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@RequiredArgsConstructor
public enum Axis {

    X("x") {
        @Override
        public double get2DX(double x, double y, double z) {
            return z;
        }

        @Override
        public double get2DY(double x, double y, double z) {
            return y;
        }
    },
    Y("y") {
        @Override
        public double get2DX(double x, double y, double z) {
            return x;
        }

        @Override
        public double get2DY(double x, double y, double z) {
            return z;
        }
    },
    Z("z") {
        @Override
        public double get2DX(double x, double y, double z) {
            return x;
        }

        @Override
        public double get2DY(double x, double y, double z) {
            return y;
        }
    };

    private static final ImmutableMap<String, Axis> NAME_LOOKUP = Util.createLookupBy(axis -> axis.name, values());

    private final String name;

    @Nullable
    public static Axis fromName(String name) {
        return NAME_LOOKUP.get(name.toLowerCase(Locale.ROOT));
    }

    public abstract double get2DX(double x, double y, double z);

    public abstract double get2DY(double x, double y, double z);


    @Override
    public String toString() {
        return name;
    }
}
