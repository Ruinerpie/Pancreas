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

public class TabPlus extends Feature {
    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    public final Value<Integer> tabSize = sgGeneral.add(new IntValue.Builder()
        .name("tab-size")
        .description("Maximum players shown in tab list.")
        .defaultValue(100)
        .min(1)
        .max(500)
        .build()
    );

    public final Value<Boolean> showPing = sgGeneral.add(new FlagValue.Builder()
        .name("show-ping")
        .description("Display numeric ping beside player names.")
        .defaultValue(true)
        .build()
    );

    public TabPlus() {
        super(Group.Utilities, "roster", "Enhances player tab list display and limits.");
    }
}