package ruinerpie.pancreas.values;

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

public class ChoiceValue<T extends Enum<T>> extends Value<T> {
    public ChoiceValue(String name, String description, T defaultValue, Supplier<Boolean> visibleWhen, Consumer<T> onChanged) {
        super(name, description, defaultValue);
        if (visibleWhen != null) this.visibleWhen = visibleWhen;
        this.onChanged = onChanged;
    }

    public void next() {
        if (value == null) return;
        T[] constants = value.getDeclaringClass().getEnumConstants();
        if (constants != null && constants.length > 0) {
            int nextIndex = (value.ordinal() + 1) % constants.length;
            set(constants[nextIndex]);
        }
    }

    public static class Builder<T extends Enum<T>> {
        private String name = "undefined";
        private String description = "";
        private T defaultValue = null;
        private Supplier<Boolean> visibleWhen = () -> true;
        private Consumer<T> onChanged = null;

        public Builder<T> name(String name) {
            this.name = name;
            return this;
        }

        public Builder<T> description(String description) {
            this.description = description;
            return this;
        }

        public Builder<T> defaultValue(T defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public Builder<T> visible(Supplier<Boolean> visibleWhen) {
            this.visibleWhen = visibleWhen;
            return this;
        }

        public Builder<T> onChanged(Consumer<T> onChanged) {
            this.onChanged = onChanged;
            return this;
        }

        public ChoiceValue<T> build() {
            return new ChoiceValue<>(name, description, defaultValue, visibleWhen, onChanged);
        }
    }
}