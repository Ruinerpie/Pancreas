package ruinerpie.pancreas.tweaks.misc;

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

public class Alert extends Feature {
    public Alert() {
        super(Group.Misc, "alert", "Sends notifications about game events.");
    }

    public static String formatCoords(Vec3 pos) {
        if (pos == null) return "(0, 0, 0)";
        return String.format("(%.1f, %.1f, %.1f)", pos.x, pos.y, pos.z);
    }

    public void notify(String title, String message) {
        if (isActive()) {
            Toasts.get().add(title, message, Toast.Type.INFO);
        }
    }
}