package ruinerpie.pancreas.values;

import java.util.function.Supplier;

public class TextValue extends Value<String> {
    public TextValue(String name, String description, String defaultValue, Supplier<Boolean> visible) {
        super(name, description, defaultValue, visible);
    }

    public static class Builder {
        private String name = "";
        private String description = "";
        private String defaultValue = "";
        private Supplier<Boolean> visible;

        public Builder name(String name) { this.name = name; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder defaultValue(String defaultValue) { this.defaultValue = defaultValue; return this; }
        public Builder visible(Supplier<Boolean> visible) { this.visible = visible; return this; }

        public TextValue build() {
            return new TextValue(name, description, defaultValue, visible);
        }
    }
}
