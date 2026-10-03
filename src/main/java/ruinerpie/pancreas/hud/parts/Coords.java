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

import net.minecraft.world.entity.player.Player;

public class Coords extends Part {
    
    public boolean showOnlyXZ = false;

    public Coords() {
        super("coords", "Coordinates", "Displays current player position and dimension");
        this.x = 4;
        this.y = 26;
    }

    public void toggleShowOnlyXZ() {
        this.showOnlyXZ = !this.showOnlyXZ;
    }

    private String getCoordString(Player player) {
        if (player == null) return showOnlyXZ ? "XZ: 0.0, 0.0 [Overworld]" : "XYZ: 0.0, 64.0, 0.0 [Overworld]";
        String dim = "Overworld";
        if (player.level() != null) {
            var key = player.level().dimension();
            if (key == net.minecraft.world.level.Level.NETHER) dim = "Nether";
            else if (key == net.minecraft.world.level.Level.END) dim = "End";
        }
        if (showOnlyXZ) {
            return String.format("XZ: %.1f, %.1f [%s]", player.getX(), player.getZ(), dim);
        }
        return String.format("XYZ: %.1f, %.1f, %.1f [%s]", player.getX(), player.getY(), player.getZ(), dim);
    }

    @Override
    public int getWidth() {
        String text = getCoordString(ruinerpie.pancreas.Pancreas.mc.player);
        return ruinerpie.pancreas.Pancreas.mc.font.width(text) + 16;
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
        ctx.graphics.fill(renderX + 2, renderY + 2, renderX + 5, renderY + h - 2, Theme.accentMinecraft.getPacked());

        String text = getCoordString(ctx.mc.player);
        ctx.graphics.text(ctx.mc.font, text, renderX + 9, renderY + 5, Theme.text.getPacked(), false);
    }
}