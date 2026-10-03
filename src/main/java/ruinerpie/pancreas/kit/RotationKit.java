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

public class RotationKit {
    public static final Minecraft mc = Minecraft.getInstance();
    public static boolean rotating = false;
    public static float serverPitch = 0.0f;
    public static float serverYaw = 0.0f;

    public static void rotate(double yaw, double pitch) {
        if (mc.player != null) {
            mc.player.setYRot((float) yaw);
            mc.player.setXRot((float) pitch);
        }
    }

    public static void rotate(double yaw, double pitch, Runnable callback) {
        rotate(yaw, pitch);
        if (callback != null) callback.run();
    }

    public static void rotate(double yaw, double pitch, int priority, Runnable callback) {
        rotate(yaw, pitch, callback);
    }

    public static void rotate(double yaw, double pitch, int priority) {
        rotate(yaw, pitch);
    }

    public static double getYaw(Entity entity) {
        return entity != null ? entity.getYRot() : 0;
    }

    public static double getPitch(Entity entity, Object target) {
        return entity != null ? entity.getXRot() : 0;
    }

    public enum Target {
        Head, Feet, Body
    }
}