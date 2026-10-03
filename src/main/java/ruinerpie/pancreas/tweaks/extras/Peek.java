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

public class Peek extends Feature {
    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    public final Value<Boolean> invertX = sgGeneral.add(new FlagValue.Builder()
        .name("invert-x")
        .description("Invert horizontal rotation.")
        .defaultValue(false)
        .build()
    );

    public final Value<Boolean> invertY = sgGeneral.add(new FlagValue.Builder()
        .name("invert-y")
        .description("Invert vertical rotation.")
        .defaultValue(false)
        .build()
    );

    public float cameraYaw;
    public float cameraPitch;

    public Peek() {
        super(Group.Extras, "peek", "Look around freely in 3rd person without changing movement direction.");
    }

    @Override
    public void onActivate() {
        if (mc.player != null) {
            cameraYaw = mc.player.getYRot();
            cameraPitch = mc.player.getXRot();
        }
    }
}