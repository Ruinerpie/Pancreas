package ruinerpie.pancreas.screens;

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

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public class Canvas {
    public static void fill(GuiGraphicsExtractor graphics, int x1, int y1, int x2, int y2, int color) {
        if (graphics != null) graphics.fill(x1, y1, x2, y2, color);
    }

    public static void outline(GuiGraphicsExtractor graphics, int x, int y, int w, int h, int color) {
        if (graphics != null) graphics.outline(x, y, w, h, color);
    }

    public static void text(GuiGraphicsExtractor graphics, Font font, String text, int x, int y, int color, boolean shadow) {
        if (graphics != null && font != null && text != null) graphics.text(font, text, x, y, color, shadow);
    }
}