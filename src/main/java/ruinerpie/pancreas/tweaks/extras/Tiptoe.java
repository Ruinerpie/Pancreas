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

public class Tiptoe extends Feature {
    public enum Mode {
        Packet,
        Vanilla
    }

    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    public final Value<Mode> mode = sgGeneral.add(new ChoiceValue.Builder<Mode>()
        .name("mode")
        .description("Which method to sneak.")
        .defaultValue(Mode.Vanilla)
        .build()
    );

    public Tiptoe() {
        super(Group.Extras, "tiptoe", "Sneaks for you");
    }

    public boolean doPacket() {
        return isActive() && mc.player != null && !mc.player.getAbilities().flying && mode.get() == Mode.Packet;
    }

    public boolean doVanilla() {
        return isActive() && mc.player != null && !mc.player.getAbilities().flying && mode.get() == Mode.Vanilla;
    }
}