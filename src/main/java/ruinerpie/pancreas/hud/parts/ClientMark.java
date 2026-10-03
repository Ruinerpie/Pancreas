package ruinerpie.pancreas.hud.parts;

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

public class ClientMark extends Part {
    public enum Layout {
        FULL,
        COMPACT,
        ICON_ONLY
    }

    public Layout layout = Layout.FULL;

    public ClientMark() {
        super("client-mark", "Watermark", "Displays Pancreas client version, fps, and status");
        this.x = 4;
        this.y = 4;
    }

    public void cycleLayout() {
        this.layout = switch (layout) {
            case FULL -> Layout.COMPACT;
            case COMPACT -> Layout.ICON_ONLY;
            case ICON_ONLY -> Layout.FULL;
        };
    }

    private String getMarkText() {
        int fps = Pancreas.mc.getFps();
        int ping = -1;
        if (Pancreas.mc.getConnection() != null && Pancreas.mc.player != null) {
            var entry = Pancreas.mc.getConnection().getPlayerInfo(Pancreas.mc.player.getUUID());
            if (entry != null) ping = entry.getLatency();
        }

        return switch (layout) {
            case FULL -> "Pancreas " + Pancreas.VERSION + " [" + fps + " FPS" + (ping >= 0 ? " | " + ping + "ms" : "") + "]";
            case COMPACT -> "P " + Pancreas.VERSION + " [" + fps + " FPS]";
            case ICON_ONLY -> "P";
        };
    }

    @Override
    public int getWidth() {
        if (layout == Layout.ICON_ONLY) return 20;
        return Pancreas.mc.font.width(getMarkText()) + 16;
    }

    @Override
    public int getHeight() {
        return 18;
    }

    @Override
    public void render(Context ctx) {
        int renderX = getRenderX(ctx.screenWidth);
        int renderY = getRenderY(ctx.screenHeight);
        int w = getWidth();
        int h = getHeight();

        Panel.renderBeveledPanel(ctx.graphics, renderX, renderY, w, h, false, Theme.surface.getPacked(), 0);
        ctx.graphics.fill(renderX + 2, renderY + 2, renderX + 5, renderY + h - 2, Theme.accentRed.getPacked());

        String text = getMarkText();
        if (layout == Layout.ICON_ONLY) {
            ctx.graphics.centeredText(ctx.mc.font, "P", renderX + 11, renderY + 5, Theme.accentRed.getPacked());
        } else {
            ctx.graphics.text(ctx.mc.font, text, renderX + 9, renderY + 5, Theme.textAccent.getPacked(), false);
        }
    }
}