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

public class Shield extends Feature {
    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    public final Value<Boolean> catchExceptions = sgGeneral.add(new FlagValue.Builder()
        .name("catch-exceptions")
        .description("Drops corrupted packets.")
        .defaultValue(false)
        .build()
    );

    public final Value<Boolean> logExceptions = sgGeneral.add(new FlagValue.Builder()
        .name("log-exceptions")
        .description("Logs caught exceptions.")
        .defaultValue(true)
        .visible(catchExceptions::get)
        .build()
    );

    public Shield() {
        super(Group.Misc, "shield", "Attempts to prevent you from being disconnected by large packets.");
    }

    public boolean catchExceptions() {
        return isActive() && catchExceptions.get();
    }

    public boolean logExceptions() {
        return isActive() && logExceptions.get();
    }
}