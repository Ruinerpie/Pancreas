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

import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class KeyValue extends Value<Integer> {
    public KeyValue(String name, String description, int defaultKey, Supplier<Boolean> visibleWhen, Consumer<Integer> onChanged) {
        super(name, description, defaultKey);
        if (visibleWhen != null) this.visibleWhen = visibleWhen;
        this.onChanged = onChanged;
    }

    public String getKeyName() {
        int key = get();
        if (key <= 0) return "NONE";
        String name = GLFW.glfwGetKeyName(key, 0);
        return name != null ? name.toUpperCase() : "KEY_" + key;
    }

    public static class Builder {
        private String name = "undefined";
        private String description = "";
        private int defaultValue = -1;
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

        public Builder visible(Supplier<Boolean> visibleWhen) {
            this.visibleWhen = visibleWhen;
            return this;
        }

        public Builder onChanged(Consumer<Integer> onChanged) {
            this.onChanged = onChanged;
            return this;
        }

        public KeyValue build() {
            return new KeyValue(name, description, defaultValue, visibleWhen, onChanged);
        }
    }
}