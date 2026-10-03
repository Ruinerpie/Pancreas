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

import net.minecraft.world.phys.Vec3;

import java.util.ArrayDeque;
import java.util.Deque;

public class Breadcrumb extends Feature {
    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    public final Value<Tint> color = sgGeneral.add(new TintValue.Builder()
        .name("color")
        .description("Color of the breadcrumb path line.")
        .defaultValue(new Tint(225, 25, 25, 255))
        .build()
    );

    public final Value<Integer> maxSections = sgGeneral.add(new IntValue.Builder()
        .name("max-sections")
        .description("Maximum recorded position segments.")
        .defaultValue(1000)
        .min(1)
        .max(5000)
        .sliderRange(100, 3000)
        .build()
    );

    public final Value<Double> sectionLength = sgGeneral.add(new DoubleValue.Builder()
        .name("section-length")
        .description("Minimum distance moved before recording a new position.")
        .defaultValue(0.5)
        .min(0.0)
        .max(1.0)
        .sliderRange(0.1, 1.0)
        .build()
    );

    private final Deque<Vec3> positions = new ArrayDeque<>();

    public Breadcrumb() {
        super(Group.Utilities, "breadcrumb", "Records and displays a continuous trail of your movement path.");
    }

    @Override
    public void onActivate() {
        positions.clear();
    }

    @Listen
    private void onTick(WorldTick event) {
        if (mc.player == null) return;

        Vec3 current = mc.player.position();
        if (positions.isEmpty()) {
            positions.add(current);
            return;
        }

        Vec3 last = positions.peekLast();
        if (last != null && last.distanceTo(current) >= sectionLength.get()) {
            positions.addLast(current);
            while (positions.size() > maxSections.get()) {
                positions.pollFirst();
            }
        }
    }

    @Listen
    private void onRender3D(Render3D event) {
        if (positions.size() < 2) return;

        Vec3 prev = null;
        for (Vec3 pos : positions) {
            if (prev != null) {
                Solid.INSTANCE.line(prev.x, prev.y, prev.z, pos.x, pos.y, pos.z, color.get());
            }
            prev = pos;
        }
    }
}