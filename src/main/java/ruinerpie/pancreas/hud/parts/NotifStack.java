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

import java.util.List;

public class NotifStack extends Part {
    public NotifStack() {
        super("notif-stack", "Toast Feed", "Displays recent client notifications");
        this.anchor = Anchor.BOTTOM_RIGHT;
        this.x = 4;
        this.y = 50;
    }

    @Override
    public int getWidth() {
        return 160;
    }

    @Override
    public int getHeight() {
        return 58;
    }

    @Override
    public void render(Context ctx) {
        List<Toast> all = Toasts.get().getNotifications();
        if (all.isEmpty()) return;

        long now = System.currentTimeMillis();
        
        List<Toast> active = all.stream()
            .filter(n -> (now - n.timestamp <= 5000) && n.type != Toast.Type.INFO)
            .toList();

        if (active.isEmpty()) return;

        int baseX = getRenderX(ctx.screenWidth);
        int curY = getRenderY(ctx.screenHeight);

        int start = Math.max(0, active.size() - 4);
        for (int i = start; i < active.size(); i++) {
            Toast n = active.get(i);
            long age = now - n.timestamp;

            int slideOffset = 0;
            if (age < 150) {
                float progress = age / 150.0f;
                float eased = 1.0f - (1.0f - progress) * (1.0f - progress);
                slideOffset = (int) (60 * (1.0f - eased));
            }

            int renderX = baseX + slideOffset;
            int rowH = 18;
            int accent = switch (n.type) {
                case INFO -> Theme.accentDiamond.getPacked();
                case WARNING -> Theme.accentGold.getPacked();
                case ERROR -> Theme.accentRed.getPacked();
                case SUCCESS -> Theme.accentEmerald.getPacked();
            };

            Panel.renderBeveledPanel(ctx.graphics, renderX, curY, getWidth(), rowH, false, Theme.surface.getPacked(), 0);
            ctx.graphics.fill(renderX + 2, curY + 2, renderX + 5, curY + rowH - 2, accent);
            String text = n.title + ": " + n.message;
            if (ctx.mc.font.width(text) > getWidth() - 14) {
                text = ctx.mc.font.plainSubstrByWidth(text, getWidth() - 20) + "...";
            }
            ctx.graphics.text(ctx.mc.font, text, renderX + 8, curY + 5, Theme.text.getPacked(), false);

            curY += rowH + 3; 
        }
    }
}