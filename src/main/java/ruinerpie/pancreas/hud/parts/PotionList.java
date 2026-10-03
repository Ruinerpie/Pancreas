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

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;

import java.util.Collection;

public class PotionList extends Part {
    public PotionList() {
        super("potion-list", "Active Potions", "Displays active potion status effects and timers");
        this.anchor = Anchor.TOP_LEFT;
        this.x = 4;
        this.y = 70;
    }

    @Override
    public int getWidth() {
        return 120;
    }

    @Override
    public int getHeight() {
        Player player = ruinerpie.pancreas.Pancreas.mc.player;
        if (player == null || player.getActiveEffects().isEmpty()) return 18;
        return player.getActiveEffects().size() * 16 + 4;
    }

    @Override
    public void render(Context ctx) {
        Player player = ctx.mc.player;
        if (player == null) return;
        Collection<MobEffectInstance> effects = player.getActiveEffects();
        if (effects.isEmpty()) return;

        int renderX = getRenderX(ctx.screenWidth);
        int curY = getRenderY(ctx.screenHeight);

        for (MobEffectInstance effect : effects) {
            String name = effect.getEffect().value().getDisplayName().getString();
            int duration = effect.getDuration() / 20;
            String time = String.format("%d:%02d", duration / 60, duration % 60);

            Panel.renderBeveledPanel(ctx.graphics, renderX, curY, getWidth(), 15, false, Theme.surface.getPacked(), 0);
            ctx.graphics.text(ctx.mc.font, name, renderX + 4, curY + 4, Theme.text.getPacked(), false);
            ctx.graphics.text(ctx.mc.font, time, renderX + getWidth() - ctx.mc.font.width(time) - 4, curY + 4, Theme.accentPurple.getPacked(), false);

            curY += 16;
        }
    }
}