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

public class TextValue extends Value<String> {
    public TextValue(String name, String description, String defaultValue, Supplier<Boolean> visibleWhen, Consumer<String> onChanged) {
        super(name, description, defaultValue);
        if (visibleWhen != null) this.visibleWhen = visibleWhen;
        this.onChanged = onChanged;
    }

    public static class Builder {
        private String name = "undefined";
        private String description = "";
        private String defaultValue = "";
        private Supplier<Boolean> visibleWhen = () -> true;
        private Consumer<String> onChanged = null;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder defaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public Builder visible(Supplier<Boolean> visibleWhen) {
            this.visibleWhen = visibleWhen;
            return this;
        }

        public Builder onChanged(Consumer<String> onChanged) {
            this.onChanged = onChanged;
            return this;
        }

        public TextValue build() {
            return new TextValue(name, description, defaultValue, visibleWhen, onChanged);
        }
    }
}