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

import net.minecraft.world.entity.monster.EnderMan;

public class Stare extends Feature {
    public enum LookMode {
        At,
        Away
    }

    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    public final Value<LookMode> lookMode = sgGeneral.add(new ChoiceValue.Builder<LookMode>()
        .name("look-mode")
        .description("Whether to look directly at or avoid looking at Endermen.")
        .defaultValue(LookMode.Away)
        .build()
    );

    public final Value<Boolean> stunHostiles = sgGeneral.add(new FlagValue.Builder()
        .name("stun-hostiles")
        .description("Looks at angry Endermen to freeze and stun them when in Away mode.")
        .defaultValue(true)
        .visible(() -> lookMode.get() == LookMode.Away)
        .build()
    );

    public Stare() {
        super(Group.Extras, "stare", "Prevents or forces looking at Endermen eyes.");
    }

    @Listen
    private void onTick(ClientTick event) {
        if (mc.player == null || mc.level == null) return;

        double nearestDistSq = Double.MAX_VALUE;
        EnderMan target = null;

        for (net.minecraft.world.entity.Entity entity : mc.level.entitiesForRendering()) {
            if (entity instanceof EnderMan enderman) {
                double distSq = mc.player.distanceToSqr(enderman);
                if (distSq < 1024.0 && distSq < nearestDistSq) {
                    nearestDistSq = distSq;
                    target = enderman;
                }
            }
        }

        if (target == null) return;

        double yaw = RotationKit.getYaw(target);
        double pitch = RotationKit.getPitch(target, RotationKit.Target.Head);

        if (lookMode.get() == LookMode.At) {
            RotationKit.rotate(yaw, pitch);
        } else {
            if (stunHostiles.get() && target.isCreepy()) {
                RotationKit.rotate(yaw, pitch);
            } else {
                double playerPitch = mc.player.getXRot();
                if (playerPitch < 30.0) {
                    RotationKit.rotate(mc.player.getYRot(), 65.0);
                }
            }
        }
    }
}