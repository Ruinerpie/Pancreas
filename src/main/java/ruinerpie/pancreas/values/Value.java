package ruinerpie.pancreas.values;

import java.util.function.Supplier;

public abstract class Value<T> {
    public final String name;
    public final String description;
    public final T defaultValue;
    private T value;
    public final Supplier<Boolean> visible;

    public Value(String name, String description, T defaultValue, Supplier<Boolean> visible) {
        this.name = name;
        this.description = description;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.visible = visible != null ? visible : () -> true;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public T getDefaultValue() { return defaultValue; }
    public T get() { return value; }
    public void set(T val) { this.value = val; }
    public void reset() { this.value = defaultValue; }
    public boolean isVisible() { return visible.get(); }
}
