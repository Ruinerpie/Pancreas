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

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public class Type {
    public static final Type INSTANCE = new Type();

    public void render(GuiGraphicsExtractor graphics, String text, double x, double y, Color color, boolean shadow) {
        if (graphics == null || text == null || color == null) return;
        Font font = Minecraft.getInstance().font;
        graphics.text(font, text, (int) x, (int) y, color.getPacked(), shadow);
    }
}