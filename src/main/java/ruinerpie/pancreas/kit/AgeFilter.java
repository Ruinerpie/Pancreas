package ruinerpie.pancreas.kit;

import ruinerpie.pancreas.*;
import ruinerpie.pancreas.mixin.*;
import ruinerpie.pancreas.mixin.accessor.*;
import ruinerpie.pancreas.values.*;
import ruinerpie.pancreas.signals.*;
import ruinerpie.pancreas.kit.*;
import ruinerpie.pancreas.draw.*;

import net.minecraft.world.entity.LivingEntity;

public enum AgeFilter {
    Baby,
    Adult,
    Both;

    public boolean test(LivingEntity entity) {
        return switch (this) {
            case Baby -> entity.isBaby();
            case Adult -> !entity.isBaby();
            case Both -> true;
        };
    }
}
