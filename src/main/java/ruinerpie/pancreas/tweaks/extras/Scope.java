package ruinerpie.pancreas.tweaks.extras;

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

public class Scope extends Feature {
    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    public final Value<Double> zoomFactor = sgGeneral.add(new DoubleValue.Builder()
        .name("zoom-factor")
        .description("FOV reduction factor when zooming.")
        .defaultValue(3.0)
        .min(1.0)
        .max(10.0)
        .build()
    );

    public final Value<Double> smoothSpeed = sgGeneral.add(new DoubleValue.Builder()
        .name("smooth-speed")
        .description("Transition speed.")
        .defaultValue(1.0)
        .min(0.1)
        .max(5.0)
        .build()
    );

    public Scope() {
        super(Group.Extras, "scope", "Smooth camera zoom with configurable FOV multiplier.");
    }

    public double getFov(double fov) {
        return isActive() ? fov / zoomFactor.get() : fov;
    }
}