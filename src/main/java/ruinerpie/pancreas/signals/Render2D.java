package ruinerpie.pancreas.signals;

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

import net.minecraft.client.gui.GuiGraphicsExtractor;

public class Render2D extends Signal {
    public GuiGraphicsExtractor drawContext;
    public GuiGraphicsExtractor graphics;
    public float tickDelta;
    public int screenWidth;
    public int screenHeight;

    public Render2D(GuiGraphicsExtractor drawContext, float tickDelta, int screenWidth, int screenHeight) {
        this.drawContext = drawContext;
        this.graphics = drawContext;
        this.tickDelta = tickDelta;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }
}