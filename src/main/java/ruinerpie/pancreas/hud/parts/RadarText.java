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

public class RadarText extends Part {
    public RadarText() {
        super("radar-text", "Text Radar", "Displays counts of nearby entities");
        this.x = 4;
        this.y = 48;
    }

    private String getRadarText(ruinerpie.pancreas.hud.Context ctx) {
        if (ctx.mc.level == null || ctx.mc.player == null) return "Radar: 0 Entities";
        int players = 0;
        int entities = 0;
        for (net.minecraft.world.entity.Entity e : ctx.mc.level.entitiesForRendering()) {
            if (e == ctx.mc.player) continue;
            if (ctx.mc.player.distanceTo(e) <= 32) {
                entities++;
                if (e instanceof net.minecraft.world.entity.player.Player) players++;
            }
        }
        if (players > 0) return "Radar: " + players + " Players, " + entities + " Ent";
        return "Radar: " + entities + " Entities";
    }

    @Override
    public int getWidth() {
        return 120;
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
        ctx.graphics.fill(renderX + 2, renderY + 2, renderX + 5, renderY + h - 2, Theme.accentGold.getPacked());

        String text = getRadarText(ctx);
        ctx.graphics.text(ctx.mc.font, text, renderX + 9, renderY + 5, Theme.text.getPacked(), false);
    }
}