package ruinerpie.pancreas.values;

import java.util.function.Supplier;

public class ChoiceValue<T extends Enum<T>> extends Value<T> {
    public ChoiceValue(String name, String description, T defaultValue, Supplier<Boolean> visible) {
        super(name, description, defaultValue, visible);
    }

    public static class Builder<T extends Enum<T>> {
        private String name = "";
        private String description = "";
        private T defaultValue;
        private Supplier<Boolean> visible;

        public Builder<T> name(String name) { this.name = name; return this; }
        public Builder<T> description(String description) { this.description = description; return this; }
        public Builder<T> defaultValue(T defaultValue) { this.defaultValue = defaultValue; return this; }
        public Builder<T> visible(Supplier<Boolean> visible) { this.visible = visible; return this; }

        public ChoiceValue<T> build() {
            return new ChoiceValue<>(name, description, defaultValue, visible);
        }
    }
}
