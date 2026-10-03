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

import net.minecraft.world.phys.Vec3;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class VectorValue extends Value<Vec3> {
    public VectorValue(String name, String description, Vec3 defaultValue, Supplier<Boolean> visibleWhen, Consumer<Vec3> onChanged) {
        super(name, description, defaultValue != null ? defaultValue : Vec3.ZERO);
        if (visibleWhen != null) this.visibleWhen = visibleWhen;
        this.onChanged = onChanged;
    }

    public static class Builder {
        private String name = "undefined";
        private String description = "";
        private Vec3 defaultValue = Vec3.ZERO;
        private Supplier<Boolean> visibleWhen = () -> true;
        private Consumer<Vec3> onChanged = null;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder defaultValue(Vec3 defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public Builder defaultValue(double x, double y, double z) {
            this.defaultValue = new Vec3(x, y, z);
            return this;
        }

        public Builder visible(Supplier<Boolean> visibleWhen) {
            this.visibleWhen = visibleWhen;
            return this;
        }

        public Builder onChanged(Consumer<Vec3> onChanged) {
            this.onChanged = onChanged;
            return this;
        }

        public VectorValue build() {
            return new VectorValue(name, description, defaultValue, visibleWhen, onChanged);
        }
    }
}