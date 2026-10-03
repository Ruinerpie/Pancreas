package ruinerpie.pancreas.tweaks.cheats;

import ruinerpie.pancreas.mixin.*;
import ruinerpie.pancreas.*;
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

import net.minecraft.world.phys.AABB;

public class Nudge extends Feature {
    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    public final Value<Double> edgeDistance = sgGeneral.add(new DoubleValue.Builder()
        .name("edge-distance")
        .description("Distance from block edge before jumping.")
        .defaultValue(0.001)
        .min(0.001)
        .max(0.1)
        .sliderRange(0.001, 0.1)
        .build()
    );

    public Nudge() {
        super(Group.Cheats, "nudge", "Automatically jumps at block edges.");
    }

    @Listen
    private void onTick(WorldTick event) {
        if (mc.player == null || mc.level == null) return;
        if (!mc.player.onGround() || mc.player.isCrouching()) return;
        if (mc.options.keyJump.isDown()) return;

        double dist = edgeDistance.get();
        AABB box = mc.player.getBoundingBox().move(0, -0.5, 0).deflate(dist, 0, dist);
        if (mc.level.noCollision(mc.player, box)) {
            ((LivingEntityMixin) mc.player).pancreas$jumpFromGround();
        }
    }
}