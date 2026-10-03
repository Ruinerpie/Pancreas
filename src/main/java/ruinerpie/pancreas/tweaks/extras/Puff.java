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

import net.minecraft.core.particles.ParticleTypes;

public class Puff extends Feature {
    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    public final Value<Boolean> pauseWhenStationary = sgGeneral.add(new FlagValue.Builder()
        .name("pause-when-stationary")
        .description("Pauses particle emissions when not moving.")
        .defaultValue(true)
        .build()
    );

    public Puff() {
        super(Group.Extras, "puff", "Leaves a customizable particle trail behind your movement path.");
    }

    @Listen
    private void onTick(WorldTick event) {
        if (mc.player == null || mc.level == null) return;

        if (pauseWhenStationary.get() && mc.player.getDeltaMovement().lengthSqr() < 0.001) {
            return;
        }

        double x = mc.player.getX();
        double y = mc.player.getY() + 0.1;
        double z = mc.player.getZ();

        mc.level.addParticle(ParticleTypes.DRIPPING_OBSIDIAN_TEAR, x, y, z, 0.0, 0.0, 0.0);
        mc.level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 0.0, 0.02, 0.0);
    }
}