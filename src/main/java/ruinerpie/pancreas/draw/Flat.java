package ruinerpie.pancreas.draw;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import ruinerpie.pancreas.screens.Home;

public class Flat {
    public static final Flat COLOR = new Flat();
    public static final Identifier FONT_QRAFTYS_ID = Identifier.fromNamespaceAndPath("pancreas", "qraftys");
    public static final net.minecraft.network.chat.FontDescription FONT_QRAFTYS = new net.minecraft.network.chat.FontDescription.Resource(FONT_QRAFTYS_ID);

    public void fill(GuiGraphicsExtractor graphics, double x1, double y1, double x2, double y2, Color color) {
        if (graphics == null || color == null) return;
        graphics.fill((int) x1, (int) y1, (int) x2, (int) y2, color.getPacked());
    }

    public void fillGradient(GuiGraphicsExtractor graphics, double x1, double y1, double x2, double y2, Color color1, Color color2) {
        if (graphics == null || color1 == null || color2 == null) return;
        graphics.fillGradient((int) x1, (int) y1, (int) x2, (int) y2, color1.getPacked(), color2.getPacked());
    }

    public void outline(GuiGraphicsExtractor graphics, double x, double y, double width, double height, Color color) {
        if (graphics == null || color == null) return;
        graphics.outline((int) x, (int) y, (int) width, (int) height, color.getPacked());
    }

    public void text(GuiGraphicsExtractor graphics, Font font, String text, double x, double y, Color color, boolean shadow) {
        if (graphics == null || font == null || text == null || color == null) return;
        if (Home.guiFontChoice.get() == Home.FontChoice.Qraftys) {
            Component comp = Component.literal(text).withStyle(s -> s.withFont(FONT_QRAFTYS));
            graphics.text(font, comp, (int) x, (int) y, color.getPacked(), shadow);
        } else {
            graphics.text(font, text, (int) x, (int) y, color.getPacked(), shadow);
        }
    }

    public int width(Font font, String text) {
        if (font == null || text == null) return 0;
        if (Home.guiFontChoice.get() == Home.FontChoice.Qraftys) {
            Component comp = Component.literal(text).withStyle(s -> s.withFont(FONT_QRAFTYS));
            return font.width(comp);
        }
        return font.width(text);
    }
}
