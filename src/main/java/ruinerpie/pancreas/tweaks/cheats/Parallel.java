package ruinerpie.pancreas.tweaks.cheats;

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

public class Parallel extends Feature {
    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    public final Value<Boolean> attackingEntities = sgGeneral.add(new FlagValue.Builder()
        .name("attacking-entities")
        .description("Allows attacking entities while using an item.")
        .defaultValue(true)
        .build()
    );

    public Parallel() {
        super(Group.Cheats, "parallel", "Use items while attacking.");
    }

    public static boolean canAttack() {
        Parallel mod = Features.get().get(Parallel.class);
        return mod != null && mod.isActive() && mod.attackingEntities.get();
    }
}