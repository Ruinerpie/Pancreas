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
import net.minecraft.world.item.ItemStack;

public class ArmorView extends Part {
    public ArmorView() {
        super("armor-view", "Armor View", "Displays equipped armor and durability bars");
        this.anchor = Anchor.BOTTOM_RIGHT;
        this.x = 4;
        this.y = 24;
    }

    @Override
    public int getWidth() {
        return 76;
    }

    @Override
    public int getHeight() {
        return 22;
    }

    @Override
    public void render(Context ctx) {
        Player player = ctx.mc.player;
        if (player == null) return;

        int renderX = getRenderX(ctx.screenWidth);
        int renderY = getRenderY(ctx.screenHeight);
        int w = getWidth();
        int h = getHeight();

        Panel.renderBeveledPanel(ctx.graphics, renderX, renderY, w, h, false, Theme.surface.getPacked(), 0);

        int itemX = renderX + 4;
        for (int i = 3; i >= 0; i--) {
            ItemStack stack = player.getInventory().getItem(36 + i);
            if (!stack.isEmpty()) {
                ctx.graphics.fakeItem(stack, itemX, renderY + 3);
            }
            itemX += 18;
        }
    }
}