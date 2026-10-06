package ruinerpie.pancreas.signals;

import ruinerpie.pancreas.Signal;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public class Render2D extends Signal {
    public final GuiGraphicsExtractor graphics;
    public final float tickDelta;
    public final int width;
    public final int height;

    public Render2D(GuiGraphicsExtractor graphics, float tickDelta, int width, int height) {
        this.graphics = graphics;
        this.tickDelta = tickDelta;
        this.width = width;
        this.height = height;
    }
}
