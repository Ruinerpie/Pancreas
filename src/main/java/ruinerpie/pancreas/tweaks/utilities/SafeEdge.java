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

public class SafeEdge extends Feature {
    private final ValueGroup sgGeneral = settings.getDefaultGroup();
    private final ValueGroup sgRender = settings.createGroup("Render");

    public final Value<Integer> minimumFallDistance = sgGeneral.add(new IntValue.Builder()
        .name("minimum-fall-distance")
        .description("Minimum fall height in blocks before edge clipping takes effect.")
        .defaultValue(1)
        .min(1)
        .max(20)
        .sliderRange(1, 10)
        .build()
    );

    public final Value<Boolean> sneak = sgGeneral.add(new FlagValue.Builder()
        .name("sneak")
        .description("Simulates sneaking at block edges instead of hard clipping.")
        .defaultValue(false)
        .build()
    );

    public final Value<Boolean> safeSneak = sgGeneral.add(new FlagValue.Builder()
        .name("safe-sneak")
        .description("Prevents unsneaking while at a dangerous ledge.")
        .defaultValue(true)
        .visible(sneak::get)
        .build()
    );

    public final Value<Boolean> sneakOnSprint = sgGeneral.add(new FlagValue.Builder()
        .name("sneak-on-sprint")
        .description("Enforces ledge stopping even while sprinting.")
        .defaultValue(true)
        .visible(sneak::get)
        .build()
    );

    public final Value<Double> edgeDistance = sgGeneral.add(new DoubleValue.Builder()
        .name("edge-distance")
        .description("Distance threshold to block ledge.")
        .defaultValue(0.30)
        .min(0.00)
        .max(0.30)
        .sliderRange(0.00, 0.30)
        .visible(sneak::get)
        .build()
    );

    public final Value<Boolean> render = sgRender.add(new FlagValue.Builder()
        .name("render")
        .description("Renders visual highlight along dangerous ledges.")
        .defaultValue(false)
        .visible(sneak::get)
        .build()
    );

    public final Value<Boolean> renderPlayerBox = sgRender.add(new FlagValue.Builder()
        .name("render-player-box")
        .description("Renders player collision bounding box at the edge.")
        .defaultValue(false)
        .visible(() -> sneak.get() && render.get())
        .build()
    );

    public SafeEdge() {
        super(Group.Utilities, "precipice", "Prevents walking or falling off block edges.");
    }

    @Listen
    private void onClipAtLedge(ClipAtLedge event) {
        if (!isActive() || mc.player == null) return;
        event.setClip(true);
    }

    @Listen
    private void onRender3D(Render3D event) {
        if (!render.get() || mc.player == null) return;
        if (renderPlayerBox.get()) {
            Solid.INSTANCE.box(mc.player.getBoundingBox(), new Color(255, 100, 0, 50), new Color(255, 100, 0, 255), Shape.Both, 0);
        }
    }
}