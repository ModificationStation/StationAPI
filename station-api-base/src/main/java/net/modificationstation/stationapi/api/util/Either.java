package net.modificationstation.stationapi.api.util;

import lombok.RequiredArgsConstructor;

public abstract class Either<L, R> {

    public static <L, R> Either<L, R> left(L left) {
        return new Either.Left<>(left);
    }

    public static <L, R> Either<L, R> right(R right) {
        return new Either.Right<>(right);
    }

    @RequiredArgsConstructor
    private static class Left<L, R> extends Either<L, R> {

        private final L left;
    }

    @RequiredArgsConstructor
    private static class Right<L, R> extends Either<L, R> {

        private final R right;
    }
}
