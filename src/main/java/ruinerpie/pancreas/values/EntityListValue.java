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

import net.minecraft.world.entity.EntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class EntityListValue extends Value<List<EntityType<?>>> {
    public final boolean onlyAttackable;

    public EntityListValue(String name, String description, List<EntityType<?>> defaultValue, boolean onlyAttackable, Supplier<Boolean> visibleWhen, Consumer<List<EntityType<?>>> onChanged) {
        super(name, description, defaultValue != null ? new ArrayList<>(defaultValue) : new ArrayList<>());
        this.onlyAttackable = onlyAttackable;
        if (visibleWhen != null) this.visibleWhen = visibleWhen;
        this.onChanged = onChanged;
    }

    public static class Builder {
        private String name = "undefined";
        private String description = "";
        private List<EntityType<?>> defaultValue = new ArrayList<>();
        private boolean onlyAttackable = false;
        private Supplier<Boolean> visibleWhen = () -> true;
        private Consumer<List<EntityType<?>>> onChanged = null;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder defaultValue(List<EntityType<?>> defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public Builder onlyAttackable() {
            this.onlyAttackable = true;
            return this;
        }

        public Builder visible(Supplier<Boolean> visibleWhen) {
            this.visibleWhen = visibleWhen;
            return this;
        }

        public Builder onChanged(Consumer<List<EntityType<?>>> onChanged) {
            this.onChanged = onChanged;
            return this;
        }

        public EntityListValue build() {
            return new EntityListValue(name, description, defaultValue, onlyAttackable, visibleWhen, onChanged);
        }
    }
}