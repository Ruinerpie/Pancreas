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

public class KeybindList extends Part {
    public KeybindList() {
        super("keybind-list", "Keybinds List", "Displays active tweaks and bound keys");
        this.anchor = Anchor.BOTTOM_LEFT;
        this.x = 4;
        this.y = 50;
    }

    private List<Feature> getBoundActiveList() {
        return Features.get().all().stream()
            .filter(m -> m.isActive() && m.getKeybind() > 0)
            .toList();
    }

    @Override
    public int getWidth() {
        return 110;
    }

    @Override
    public int getHeight() {
        List<Feature> list = getBoundActiveList();
        if (list.isEmpty()) return 18;
        return list.size() * 16 + 4;
    }

    @Override
    public void render(Context ctx) {
        List<Feature> list = getBoundActiveList();
        if (list.isEmpty()) return;

        int renderX = getRenderX(ctx.screenWidth);
        int curY = getRenderY(ctx.screenHeight);

        for (Feature m : list) {
            Panel.renderBeveledPanel(ctx.graphics, renderX, curY, getWidth(), 15, false, Theme.surface.getPacked(), 0);
            ctx.graphics.text(ctx.mc.font, m.name, renderX + 4, curY + 4, Theme.text.getPacked(), false);
            String bind = "[" + m.getKeybindName() + "]";
            ctx.graphics.text(ctx.mc.font, bind, renderX + getWidth() - ctx.mc.font.width(bind) - 4, curY + 4, Theme.accentLapis.getPacked(), false);
            curY += 16;
        }
    }
}