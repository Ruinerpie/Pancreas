package ruinerpie.pancreas.tweaks.misc;

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

public class Beacon extends Feature {
    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    public final Value<Boolean> autoSelect = sgGeneral.add(new FlagValue.Builder()
        .name("auto-select")
        .description("Automatically selects primary effect based on beacon pyramid status.")
        .defaultValue(true)
        .build()
    );

    public final Value<Boolean> closeOnClick = sgGeneral.add(new FlagValue.Builder()
        .name("close-on-payment")
        .description("Automatically confirms and closes screen once payment item is placed.")
        .defaultValue(false)
        .build()
    );

    public Beacon() {
        super(Group.Misc, "beacon", "Automates beacon power selection and closing.");
    }
}