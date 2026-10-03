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

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;

public class Solid {
    public static final Solid INSTANCE = new Solid();

    public void box(AABB box, Color sideColor, Color lineColor, Shape shapeMode, int exclude) {}
    public void box(double x1, double y1, double z1, double x2, double y2, double z2, Color sideColor, Color lineColor, Shape shapeMode, int exclude) {}
    public void box(BlockPos pos, Color sideColor, Color lineColor, Shape shapeMode, int exclude) {}
    public void line(double x1, double y1, double z1, double x2, double y2, double z2, Color color) {}
    public void sideHorizontal(double x1, double y, double z1, double x2, double z2, Color sideColor, Color lineColor, Shape shapeMode) {}
    public void sideVertical(double x1, double y1, double z1, double x2, double y2, double z2, Color sideColor, Color lineColor, Shape shapeMode) {}
}