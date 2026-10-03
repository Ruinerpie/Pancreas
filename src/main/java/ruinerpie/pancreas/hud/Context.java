package ruinerpie.pancreas.hud;

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
import net.minecraft.client.gui.GuiGraphicsExtractor;

public class Context {
    public final GuiGraphicsExtractor graphics;
    public final int screenWidth;
    public final int screenHeight;
    public final Minecraft mc;
    public final float delta;

    public Context(GuiGraphicsExtractor graphics, int screenWidth, int screenHeight, Minecraft mc, float delta) {
        this.graphics = graphics;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.mc = mc;
        this.delta = delta;
    }
}