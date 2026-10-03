package ruinerpie.pancreas.tweaks.extras;

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

import net.minecraft.world.item.Items;

public class Flask extends Feature {
    public Flask() {
        super(Group.Extras, "flask", "Automatically throws XP bottles from your hotbar.");
    }

    @Listen
    private void onTick(ClientTick event) {
        if (mc.player == null || mc.gameMode == null) return;
        FindResult exp = InventoryKit.findInHotbar(Items.EXPERIENCE_BOTTLE);
        if (!exp.found()) return;

        RotationKit.rotate(mc.player.getYRot(), 90, () -> {
            if (exp.getHand() != null) {
                mc.gameMode.useItem(mc.player, exp.getHand());
            } else {
                InventoryKit.swap(exp.slot(), true);
                mc.gameMode.useItem(mc.player, exp.getHand());
                InventoryKit.swapBack();
            }
        });
    }
}