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

public class DoubleValue extends Value<Double> {
    public final double min;
    public final double max;
    public final double sliderMin;
    public final double sliderMax;

    public DoubleValue(String name, String description, Double defaultValue, double min, double max, double sliderMin, double sliderMax, Supplier<Boolean> visibleWhen, Consumer<Double> onChanged) {
        super(name, description, defaultValue);
        this.min = min;
        this.max = max;
        this.sliderMin = sliderMin;
        this.sliderMax = sliderMax;
        if (visibleWhen != null) this.visibleWhen = visibleWhen;
        this.onChanged = onChanged;
    }

    public static class Builder {
        private String name = "undefined";
        private String description = "";
        private Double defaultValue = 0.0;
        private double min = -Double.MAX_VALUE;
        private double max = Double.MAX_VALUE;
        private double sliderMin = 0.0;
        private double sliderMax = 100.0;
        private Supplier<Boolean> visibleWhen = () -> true;
        private Consumer<Double> onChanged = null;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder defaultValue(double defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public Builder min(double min) {
            this.min = min;
            return this;
        }

        public Builder max(double max) {
            this.max = max;
            return this;
        }

        public Builder sliderMin(double sliderMin) {
            this.sliderMin = sliderMin;
            return this;
        }

        public Builder sliderMax(double sliderMax) {
            this.sliderMax = sliderMax;
            return this;
        }

        public Builder range(double min, double max) {
            this.min = min;
            this.max = max;
            return this;
        }

        public Builder sliderRange(double min, double max) {
            this.sliderMin = min;
            this.sliderMax = max;
            return this;
        }

        public Builder visible(Supplier<Boolean> visibleWhen) {
            this.visibleWhen = visibleWhen;
            return this;
        }

        public Builder onChanged(Consumer<Double> onChanged) {
            this.onChanged = onChanged;
            return this;
        }

        public DoubleValue build() {
            return new DoubleValue(name, description, defaultValue, min, max, sliderMin, sliderMax, visibleWhen, onChanged);
        }
    }
}