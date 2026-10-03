package ruinerpie.pancreas;

import ruinerpie.pancreas.*;
import ruinerpie.pancreas.mixin.*;
import ruinerpie.pancreas.mixin.accessor.*;
import ruinerpie.pancreas.values.*;
import ruinerpie.pancreas.signals.*;
import ruinerpie.pancreas.kit.*;
import ruinerpie.pancreas.draw.*;
import ruinerpie.pancreas.saved.*;
import ruinerpie.pancreas.stalk.*;
import ruinerpie.pancreas.theme.*;
import ruinerpie.pancreas.screens.*;
import ruinerpie.pancreas.screens.widgets.*;
import ruinerpie.pancreas.hud.*;
import ruinerpie.pancreas.hud.parts.*;
import ruinerpie.pancreas.tweaks.cheats.*;
import ruinerpie.pancreas.tweaks.extras.*;
import ruinerpie.pancreas.tweaks.misc.*;
import ruinerpie.pancreas.tweaks.utilities.*;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class Value<T> {
    public final String name;
    public final String description;
    protected T value;
    protected final T defaultValue;
    protected Supplier<Boolean> visibleWhen = () -> true;
    protected Consumer<T> onChanged;

    public Value(String name, String description, T defaultValue) {
        this.name = name;
        this.description = description;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    public T get() {
        return value;
    }

    public void set(T v) {
        this.value = v;
        if (onChanged != null) onChanged.accept(v);
    }

    public void reset() {
        set(defaultValue);
    }

    public boolean isVisible() {
        return visibleWhen.get();
    }

    public T getDefaultValue() {
        return defaultValue;
    }
}