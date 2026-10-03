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

import java.util.Comparator;
import java.util.List;

public class ActiveList extends Part {
    public ActiveList() {
        super("array-list", "Active Tweaks List", "Displays active tweaks sorted by name length");
        this.anchor = Anchor.TOP_RIGHT;
        this.x = 4;
        this.y = 4;
    }

    private List<Feature> getActiveList() {
        return Features.get().all().stream()
            .filter(Feature::isActive)
            .sorted(Comparator.comparingInt((Feature m) -> Pancreas.mc.font.width(m.name)).reversed())
            .toList();
    }

    @Override
    public int getWidth() {
        List<Feature> list = getActiveList();
        if (list.isEmpty()) return 60;
        int maxW = 0;
        for (Feature m : list) {
            maxW = Math.max(maxW, Pancreas.mc.font.width(m.name));
        }
        return maxW + 14;
    }

    @Override
    public int getHeight() {
        List<Feature> list = getActiveList();
        if (list.isEmpty()) return 18;
        return list.size() * 16 + 4;
    }

    @Override
    public void render(Context ctx) {
        List<Feature> active = getActiveList();
        if (active.isEmpty()) return;

        int renderX = getRenderX(ctx.screenWidth);
        int curY = getRenderY(ctx.screenHeight);

        int index = 0;
        for (Feature m : active) {
            int nameW = ctx.mc.font.width(m.name);
            int rowW = nameW + 12;
            int rowX = renderX + (getWidth() - rowW);
            int catColor = Theme.getCategoryColor(m.category).getPacked();
            int fill = (index % 2 == 0) ? Theme.surface.getPacked() : Theme.surfaceAlt.getPacked();

            Panel.renderBeveledPanel(ctx.graphics, rowX, curY, rowW, 15, false, fill, 0);
            ctx.graphics.fill(rowX + rowW - 3, curY + 2, rowX + rowW - 1, curY + 13, catColor);
            ctx.graphics.text(ctx.mc.font, m.name, rowX + 5, curY + 4, Theme.textAccent.getPacked(), false);

            curY += 16;
            index++;
        }
    }
}