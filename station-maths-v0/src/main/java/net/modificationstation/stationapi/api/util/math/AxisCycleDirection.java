package net.modificationstation.stationapi.api.util.math;

public enum AxisCycleDirection {
    NONE {

        @Override
        public int choose(int x, int y, int z, Direction.Axis axis) {
            return axis.choose(x, y, z);
        }

        @Override
        public double choose(double x, double y, double z, Direction.Axis axis) {
            return axis.choose(x, y, z);
        }

        @Override
        public Direction.Axis cycle(Direction.Axis axis) {
            return axis;
        }

        @Override
        public AxisCycleDirection opposite() {
            return this;
        }
    },
    FORWARD {

        @Override
        public int choose(int x, int y, int z, Direction.Axis axis) {
            return axis.choose(z, x, y);
        }

        @Override
        public double choose(double x, double y, double z, Direction.Axis axis) {
            return axis.choose(z, x, y);
        }

        @Override
        public Direction.Axis cycle(Direction.Axis axis) {
            return switch (axis.ordinal()) {
                case 0 -> Direction.Axis.Y;
                case 1 -> Direction.Axis.Z;
                case 2 -> Direction.Axis.X;
                default -> throw new IllegalArgumentException();
            };
        }

        @Override
        public AxisCycleDirection opposite() {
            return BACKWARD;
        }
    },
    BACKWARD {

        @Override
        public int choose(int x, int y, int z, Direction.Axis axis) {
            return axis.choose(y, z, x);
        }

        @Override
        public double choose(double x, double y, double z, Direction.Axis axis) {
            return axis.choose(y, z, x);
        }

        @Override
        public Direction.Axis cycle(Direction.Axis axis) {
            return switch (axis.ordinal()) {
                case 0 -> Direction.Axis.Z;
                case 1 -> Direction.Axis.X;
                case 2 -> Direction.Axis.Y;
                default -> throw new IllegalArgumentException();
            };
        }

        @Override
        public AxisCycleDirection opposite() {
            return FORWARD;
        }
    };

    public static final Direction.Axis[] AXES;
    public static final AxisCycleDirection[] VALUES;

    public abstract int choose(int var1, int var2, int var3, Direction.Axis var4);

    public abstract double choose(double var1, double var3, double var5, Direction.Axis var7);

    public abstract Direction.Axis cycle(Direction.Axis var1);

    public abstract AxisCycleDirection opposite();

    public static AxisCycleDirection between(Direction.Axis from, Direction.Axis to) {
        return VALUES[Math.floorMod(to.ordinal() - from.ordinal(), 3)];
    }

    static {
        AXES = Direction.Axis.values();
        VALUES = AxisCycleDirection.values();
    }
}

