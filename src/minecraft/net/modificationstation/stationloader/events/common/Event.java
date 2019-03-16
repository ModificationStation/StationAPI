package net.modificationstation.stationloader.events.common;

/**
 * Abstract event class so you can make your own events registering method
 * 
 * @author mine_diver
 *
 * @param <T>
 */
public abstract class Event<T> {
    public T getInvoker() {
    	return invoker;
    }
    public abstract void register(T listener);
    protected T invoker;
}
