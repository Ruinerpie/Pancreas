package ruinerpie.pancreas;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public final class Signals {
    private static final Signals INSTANCE = new Signals();

    public static Signals get() {
        return INSTANCE;
    }

    public static final class ListenerEntry {
        public final Object listener;
        public final Method method;
        public final Order priority;

        public ListenerEntry(Object listener, Method method, Order priority) {
            this.listener = listener;
            this.method = method;
            this.priority = priority;
            this.method.setAccessible(true);
        }
    }

    private final Map<Class<?>, List<ListenerEntry>> listenerMap = new ConcurrentHashMap<>();
    private final Map<Class<?>, List<ListenerEntry>> dispatchCache = new ConcurrentHashMap<>();

    private void invalidateCache() {
        dispatchCache.clear();
    }

    public void subscribe(Object listener) {
        if (listener == null) return;
        unsubscribe(listener);
        Class<?> clazz = listener.getClass();
        while (clazz != null && clazz != Object.class) {
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Listen.class) && method.getParameterCount() == 1) {
                    Class<?> eventType = method.getParameterTypes()[0];
                    Listen handler = method.getAnnotation(Listen.class);
                    Order priority = (handler != null) ? handler.priority() : Order.NORMAL;
                    ListenerEntry entry = new ListenerEntry(listener, method, priority);

                    listenerMap.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add(entry);
                    listenerMap.get(eventType).sort((a, b) -> a.priority.compareTo(b.priority));
                }
            }
            clazz = clazz.getSuperclass();
        }
        invalidateCache();
    }

    public void unsubscribe(Object listener) {
        if (listener == null) return;
        for (List<ListenerEntry> list : listenerMap.values()) {
            list.removeIf(entry -> entry.listener == listener);
        }
        invalidateCache();
    }

    public void post(Object event) {
        if (event == null) return;
        boolean isSignal = event instanceof Signal;
        Class<?> eventClass = event.getClass();

        List<ListenerEntry> entries = dispatchCache.computeIfAbsent(eventClass, clazz -> {
            List<ListenerEntry> resolved = new ArrayList<>();
            for (Map.Entry<Class<?>, List<ListenerEntry>> entryList : listenerMap.entrySet()) {
                if (entryList.getKey().isAssignableFrom(clazz)) {
                    resolved.addAll(entryList.getValue());
                }
            }
            resolved.sort((a, b) -> a.priority.compareTo(b.priority));
            return Collections.unmodifiableList(resolved);
        });

        for (ListenerEntry entry : entries) {
            if (isSignal && ((Signal) event).isCancelled()) {
                return;
            }
            try {
                entry.method.invoke(entry.listener, event);
            } catch (Exception e) {
                Pancreas.LOG.error("Error dispatching signal " + eventClass.getSimpleName(), e);
            }
        }
    }
}
