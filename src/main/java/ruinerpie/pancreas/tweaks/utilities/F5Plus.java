package ruinerpie.pancreas.tweaks.utilities;

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

public class F5Plus extends Feature {
    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    public final Value<Double> distance = sgGeneral.add(new DoubleValue.Builder()
        .name("distance")
        .description("Third person camera distance.")
        .defaultValue(4.0)
        .min(1.0)
        .max(15.0)
        .build()
    );

    public final Value<Boolean> clip = sgGeneral.add(new FlagValue.Builder()
        .name("clip")
        .description("Camera clips through blocks.")
        .defaultValue(false)
        .build()
    );

    public F5Plus() {
        super(Group.Utilities, "vantage", "Customizes third-person camera distance and behavior.");
    }
}