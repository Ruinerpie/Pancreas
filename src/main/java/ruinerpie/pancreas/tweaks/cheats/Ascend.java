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

public class Ascend extends Feature {
    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    public final DoubleValue climbSpeed = sgGeneral.add(new DoubleValue.Builder()
        .name("climb-speed")
        .description("Upward speed multiplier when climbing ladders or vines.")
        .defaultValue(0.2872)
        .min(0.1)
        .max(1.0)
        .build());

    public Ascend() {
        super(Group.Cheats, "ascend", "Allows climbing ladders and vines faster than vanilla speed.");
    }

    @Listen
    private void onTick(ClientTick event) {
        if (Pancreas.mc.player == null) return;

        if (Pancreas.mc.player.onClimbable()) {
            if (Pancreas.mc.player.input != null && Pancreas.mc.player.input.keyPresses.forward()) {
                var delta = Pancreas.mc.player.getDeltaMovement();
                Pancreas.mc.player.setDeltaMovement(delta.x, climbSpeed.get(), delta.z);
            }
        }
    }
}