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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class StorageListValue extends Value<List<StorageKind>> {
    public StorageListValue(String name, String description, List<StorageKind> defaultValue, Supplier<Boolean> visibleWhen, Consumer<List<StorageKind>> onChanged) {
        super(name, description, defaultValue != null ? new ArrayList<>(defaultValue) : new ArrayList<>(Arrays.asList(StorageKind.values())));
        if (visibleWhen != null) this.visibleWhen = visibleWhen;
        this.onChanged = onChanged;
    }

    public static class Builder {
        private String name = "undefined";
        private String description = "";
        private List<StorageKind> defaultValue = new ArrayList<>(Arrays.asList(StorageKind.values()));
        private Supplier<Boolean> visibleWhen = () -> true;
        private Consumer<List<StorageKind>> onChanged = null;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder defaultValue(List<StorageKind> defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public Builder visible(Supplier<Boolean> visibleWhen) {
            this.visibleWhen = visibleWhen;
            return this;
        }

        public Builder onChanged(Consumer<List<StorageKind>> onChanged) {
            this.onChanged = onChanged;
            return this;
        }

        public StorageListValue build() {
            return new StorageListValue(name, description, defaultValue, visibleWhen, onChanged);
        }
    }
}