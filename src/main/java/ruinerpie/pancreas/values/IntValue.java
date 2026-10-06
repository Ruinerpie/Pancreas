package ruinerpie.pancreas.values;

import java.util.function.Supplier;

public class IntValue extends Value<Integer> {
    public final int min;
    public final int max;

    public IntValue(String name, String description, Integer defaultValue, int min, int max, Supplier<Boolean> visible) {
        super(name, description, defaultValue, visible);
        this.min = min;
        this.max = max;
    }

    public int getMin() { return min; }
    public int getMax() { return max; }

    public static class Builder {
        private String name = "";
        private String description = "";
        private Integer defaultValue = 0;
        private int min = 0;
        private int max = 100;
        private Supplier<Boolean> visible;

        public Builder name(String name) { this.name = name; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder defaultValue(Integer defaultValue) { this.defaultValue = defaultValue; return this; }
        public Builder min(int min) { this.min = min; return this; }
        public Builder max(int max) { this.max = max; return this; }
        public Builder sliderRange(int min, int max) { this.min = min; this.max = max; return this; }
        public Builder visible(Supplier<Boolean> visible) { this.visible = visible; return this; }

        public IntValue build() {
            return new IntValue(name, description, defaultValue, min, max, visible);
        }
    }
}
