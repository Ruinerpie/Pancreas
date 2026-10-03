package ruinerpie.pancreas;

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

public enum Group {
    Cheats(
        "cheats",
        "Cheats",
        "Optional gameplay and visual utilities.",
        0xFFE53935       
    ),
    Extras(
        "extras",
        "Extras",
        "Camera, movement, inventory, rendering, and automation tools.",
        0xFF6500FF       
    ),
    Misc(
        "misc",
        "Misc",
        "Chat, reconnect, inventory, packet, sound, and general utilities.",
        0xFF5D9B3C       
    ),
    Utilities(
        "utilities",
        "Utilities",
        "HUD, tooltips, waypoints, and everyday helpers.",
        0xFFF2B233       
    );

    public final String id;
    public final String displayName;
    public final String description;
    public final int accentColor;

    Group(String id, String displayName,
               String description, int accentColor) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
        this.accentColor = accentColor;
    }

    public int getAccentColor() {
        return Theme.getCategoryColor(this).getPacked();
    }
}