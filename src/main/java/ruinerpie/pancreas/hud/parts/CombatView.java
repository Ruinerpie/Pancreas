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

import net.minecraft.world.entity.LivingEntity;

public class CombatView extends Part {
    public CombatView() {
        super("combat-view", "Combat Target HUD", "Displays target entity name, health, and distance");
        this.anchor = Anchor.CENTER;
        this.x = 0;
        this.y = 40;
    }

    @Override
    public int getWidth() {
        return 130;
    }

    @Override
    public int getHeight() {
        return 32;
    }

    @Override
    public void render(Context ctx) {
        int renderX = getRenderX(ctx.screenWidth);
        int renderY = getRenderY(ctx.screenHeight);
        int w = getWidth();
        int h = getHeight();

        LivingEntity target = null;
        if (ctx.mc.crosshairPickEntity instanceof LivingEntity le) {
            target = le;
        }

        Panel.renderBeveledPanel(ctx.graphics, renderX, renderY, w, h, false, Theme.surface.getPacked(), 0);

        if (target != null) {
            String name = target.getName().getString();
            float hp = target.getHealth();
            float maxHp = target.getMaxHealth();
            float dist = ctx.mc.player != null ? ctx.mc.player.distanceTo(target) : 0;

            ctx.graphics.text(ctx.mc.font, name, renderX + 6, renderY + 4, Theme.textAccent.getPacked(), false);
            String distStr = String.format("%.1fm", dist);
            ctx.graphics.text(ctx.mc.font, distStr, renderX + w - ctx.mc.font.width(distStr) - 6, renderY + 4, Theme.textDisabled.getPacked(), false);

            int barW = w - 12;
            int barH = 8;
            int barX = renderX + 6;
            int barY = renderY + 18;
            Panel.renderBeveledPanel(ctx.graphics, barX, barY, barW, barH, true, Theme.surfaceAlt.getPacked(), 0);

            float pct = Math.clamp(hp / Math.max(1.0f, maxHp), 0.0f, 1.0f);
            int fillW = (int) ((barW - 2) * pct);
            if (fillW > 0) {
                ctx.graphics.fill(barX + 1, barY + 1, barX + 1 + fillW, barY + barH - 1, Theme.accentRed.getPacked());
            }
        } else {
            ctx.graphics.text(ctx.mc.font, "No Target", renderX + 6, renderY + 6, Theme.textDisabled.getPacked(), false);
            ctx.graphics.text(ctx.mc.font, "Aim at entity", renderX + 6, renderY + 18, Theme.textMuted.getPacked(), false);
        }
    }
}