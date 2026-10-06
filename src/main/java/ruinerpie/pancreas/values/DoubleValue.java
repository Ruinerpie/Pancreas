package ruinerpie.pancreas.values;

import java.util.function.Supplier;

public class DoubleValue extends Value<Double> {
    public final double min;
    public final double max;

    public DoubleValue(String name, String description, Double defaultValue, double min, double max, Supplier<Boolean> visible) {
        super(name, description, defaultValue, visible);
        this.min = min;
        this.max = max;
    }

    public double getMin() { return min; }
    public double getMax() { return max; }

    public static class Builder {
        private String name = "";
        private String description = "";
        private Double defaultValue = 0.0;
        private double min = 0.0;
        private double max = 1.0;
        private Supplier<Boolean> visible;

        public Builder name(String name) { this.name = name; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder defaultValue(Double defaultValue) { this.defaultValue = defaultValue; return this; }
        public Builder min(double min) { this.min = min; return this; }
        public Builder max(double max) { this.max = max; return this; }
        public Builder sliderRange(double min, double max) { this.min = min; this.max = max; return this; }
        public Builder visible(Supplier<Boolean> visible) { this.visible = visible; return this; }

        public DoubleValue build() {
            return new DoubleValue(name, description, defaultValue, min, max, visible);
        }
    }
}
