package net.modificationstation.stationapi.api.util.math;

public enum Axis {

    X {
        @Override
        public double get2DX(double x, double y, double z) {
            return z;
        }

        @Override
        public double get2DY(double x, double y, double z) {
            return y;
        }
    },
    Y {
        @Override
        public double get2DX(double x, double y, double z) {
            return x;
        }

        @Override
        public double get2DY(double x, double y, double z) {
            return z;
        }
    },
    Z {
        @Override
        public double get2DX(double x, double y, double z) {
            return x;
        }

        @Override
        public double get2DY(double x, double y, double z) {
            return y;
        }
    };

    public abstract double get2DX(double x, double y, double z);

    public abstract double get2DY(double x, double y, double z);
}
