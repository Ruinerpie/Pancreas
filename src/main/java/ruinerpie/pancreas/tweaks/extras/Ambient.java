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

public class Ambient extends Feature {
    private final ValueGroup sgSky = settings.createGroup("Sky");

    public final Value<Boolean> customSky = sgSky.add(new FlagValue.Builder()
        .name("custom-sky")
        .description("Enable custom sky coloring.")
        .defaultValue(false)
        .build()
    );

    public final Value<Tint> skyColor = sgSky.add(new TintValue.Builder()
        .name("sky-color")
        .description("Custom sky color.")
        .defaultValue(new Tint(100, 50, 200))
        .build()
    );

    public Ambient() {
        super(Group.Extras, "ambient", "Customizes client-side world atmosphere and colors.");
    }
}