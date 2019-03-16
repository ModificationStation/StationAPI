package net.modificationstation.stationloader.events.common;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.function.Function;

/**
 * Actual event class that contains all registered handlers, eventFunc that will be executed if there are more than 1 handlers
 * registered (if there is only 1 handler registered, it'll be executed directly), and it's type
 * 
 * @author mine_diver
 *
 * @param <T>
 */
public class InstancedEvent<T> extends Event<T> {
	private T[] handlers;
	private final Class<T> type;
	private final Function<T[], T> eventFunc;
	public InstancedEvent(Class<T> type, Function<T[], T> eventFunc) {
		this.type = type;
		this.eventFunc = eventFunc;
		update();
	}
	
	@SuppressWarnings("unchecked")
	void update() {
		if (handlers == null)
			invoker = eventFunc.apply((T[]) Array.newInstance(type, 0));
		else if (handlers.length == 1)
			invoker = handlers[0];
		else
			invoker = eventFunc.apply(handlers);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void register(T listener) {
		if (handlers == null) {
			handlers = (T[]) Array.newInstance(type, 1);
			handlers[0] = listener;
		}
		else {
			handlers = Arrays.copyOf(handlers, handlers.length + 1);
			handlers[handlers.length - 1] = listener;
		}
  		update();
	}
}
