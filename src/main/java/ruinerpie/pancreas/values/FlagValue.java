package ruinerpie.pancreas.values;

import java.util.function.Supplier;

public class FlagValue extends Value<Boolean> {
    public FlagValue(String name, String description, Boolean defaultValue, Supplier<Boolean> visible) {
        super(name, description, defaultValue, visible);
    }

    public static class Builder {
        private String name = "";
        private String description = "";
        private Boolean defaultValue = false;
        private Supplier<Boolean> visible;

        public Builder name(String name) { this.name = name; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder defaultValue(Boolean defaultValue) { this.defaultValue = defaultValue; return this; }
        public Builder visible(Supplier<Boolean> visible) { this.visible = visible; return this; }

        public FlagValue build() {
            return new FlagValue(name, description, defaultValue, visible);
        }
    }
}
