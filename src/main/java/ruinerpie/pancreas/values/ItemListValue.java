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

import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ItemListValue extends Value<List<Item>> {
    public ItemListValue(String name, String description, List<Item> defaultValue, Supplier<Boolean> visibleWhen, Consumer<List<Item>> onChanged) {
        super(name, description, defaultValue != null ? new ArrayList<>(defaultValue) : new ArrayList<>());
        if (visibleWhen != null) this.visibleWhen = visibleWhen;
        this.onChanged = onChanged;
    }

    public static class Builder {
        private String name = "undefined";
        private String description = "";
        private List<Item> defaultValue = new ArrayList<>();
        private Predicate<Item> filter = item -> true;
        private Supplier<Boolean> visibleWhen = () -> true;
        private Consumer<List<Item>> onChanged = null;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder defaultValue(List<Item> defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public Builder filter(Predicate<Item> filter) {
            this.filter = filter;
            return this;
        }

        public Builder visible(Supplier<Boolean> visibleWhen) {
            this.visibleWhen = visibleWhen;
            return this;
        }

        public Builder onChanged(Consumer<List<Item>> onChanged) {
            this.onChanged = onChanged;
            return this;
        }

        public ItemListValue build() {
            return new ItemListValue(name, description, defaultValue, visibleWhen, onChanged);
        }
    }
}