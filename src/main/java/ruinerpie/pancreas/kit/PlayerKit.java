package ruinerpie.pancreas.kit;

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

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class PlayerKit {
    public static final Minecraft mc = Minecraft.getInstance();

    public static double possibleHealthReductions(boolean explosion, boolean fall) {
        return 0.0;
    }

    public static boolean isMoving() {
        if (mc.player == null) return false;
        return mc.player.xxa != 0 || mc.player.zza != 0;
    }

    public static boolean canSeeEntity(Entity entity) {
        if (mc.player == null || entity == null) return false;
        return mc.player.hasLineOfSight(entity);
    }

    public static boolean isWithin(double x, double y, double z, double range) {
        if (mc.player == null) return false;
        return mc.player.distanceToSqr(x, y, z) <= range * range;
    }

    public static boolean isWithin(Entity entity, double range) {
        if (mc.player == null || entity == null) return false;
        return mc.player.distanceToSqr(entity) <= range * range;
    }

    public static double distanceTo(Entity entity) {
        if (mc.player == null || entity == null) return 0.0;
        return mc.player.distanceTo(entity);
    }

    public static double distanceTo(Vec3 vec) {
        if (mc.player == null || vec == null) return 0.0;
        return mc.player.position().distanceTo(vec);
    }
}