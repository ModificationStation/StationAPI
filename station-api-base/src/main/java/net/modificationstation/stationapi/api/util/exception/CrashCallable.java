package net.modificationstation.stationapi.api.util.exception;

import java.util.concurrent.Callable;

public interface CrashCallable<V> extends Callable<V> {
}
