package ruinerpie.pancreas.draw;

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

public class Flat {
    public static final Flat COLOR = new Flat();

    public void fill(GuiGraphicsExtractor graphics, double x1, double y1, double x2, double y2, Color color) {
        if (graphics == null || color == null) return;
        graphics.fill((int) x1, (int) y1, (int) x2, (int) y2, color.getPacked());
    }

    public void fillGradient(GuiGraphicsExtractor graphics, double x1, double y1, double x2, double y2, Color color1, Color color2) {
        if (graphics == null || color1 == null || color2 == null) return;
        graphics.fillGradient((int) x1, (int) y1, (int) x2, (int) y2, color1.getPacked(), color2.getPacked());
    }

    public void outline(GuiGraphicsExtractor graphics, double x1, double y1, double x2, double y2, Color color) {
        if (graphics == null || color == null) return;
        graphics.outline((int) x1, (int) y1, (int) (x2 - x1), (int) (y2 - y1), color.getPacked());
    }

    public void text(GuiGraphicsExtractor graphics, Font font, String text, double x, double y, Color color, boolean shadow) {
        if (graphics == null || font == null || text == null || color == null) return;
        graphics.text(font, text, (int) x, (int) y, color.getPacked(), shadow);
    }
}