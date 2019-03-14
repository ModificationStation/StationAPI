package net.modificationstation.stationloader.events.common;

public abstract class Event<T> {
	public final String getEventType(){
		return getClass().getSimpleName();
	}
    public T getInvoker() {
    	return invoker;
    }
    public abstract void register(T listener);
    protected T invoker;
}
