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

public class IntValue extends Value<Integer> {
    public final int min;
    public final int max;
    public final int sliderMin;
    public final int sliderMax;

    public IntValue(String name, String description, Integer defaultValue, int min, int max, int sliderMin, int sliderMax, Supplier<Boolean> visibleWhen, Consumer<Integer> onChanged) {
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
        private Integer defaultValue = 0;
        private int min = Integer.MIN_VALUE;
        private int max = Integer.MAX_VALUE;
        private int sliderMin = 0;
        private int sliderMax = 100;
        private Supplier<Boolean> visibleWhen = () -> true;
        private Consumer<Integer> onChanged = null;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder defaultValue(int defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public Builder min(int min) {
            this.min = min;
            return this;
        }

        public Builder max(int max) {
            this.max = max;
            return this;
        }

        public Builder sliderMin(int sliderMin) {
            this.sliderMin = sliderMin;
            return this;
        }

        public Builder sliderMax(int sliderMax) {
            this.sliderMax = sliderMax;
            return this;
        }

        public Builder range(int min, int max) {
            this.min = min;
            this.max = max;
            return this;
        }

        public Builder sliderRange(int min, int max) {
            this.sliderMin = min;
            this.sliderMax = max;
            return this;
        }

        public Builder visible(Supplier<Boolean> visibleWhen) {
            this.visibleWhen = visibleWhen;
            return this;
        }

        public Builder onChanged(Consumer<Integer> onChanged) {
            this.onChanged = onChanged;
            return this;
        }

        public IntValue build() {
            return new IntValue(name, description, defaultValue, min, max, sliderMin, sliderMax, visibleWhen, onChanged);
        }
    }
}