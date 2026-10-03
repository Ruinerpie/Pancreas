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

public class Portal extends Feature {
    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    public final Value<Boolean> allowGui = sgGeneral.add(new FlagValue.Builder()
        .name("allow-gui")
        .description("Allows opening inventories and chat while inside a portal.")
        .defaultValue(true)
        .build()
    );

    public final Value<Boolean> noNausea = sgGeneral.add(new FlagValue.Builder()
        .name("no-nausea")
        .description("Prevents the portal distortion and nausea screen effect.")
        .defaultValue(true)
        .build()
    );

    public Portal() {
        super(Group.Extras, "portal", "Allows interacting with GUIs normally while standing inside a Nether Portal.");
    }

    public boolean canOpenGui() {
        return isActive() && allowGui.get();
    }

    public boolean shouldPreventNausea() {
        return isActive() && noNausea.get();
    }
}