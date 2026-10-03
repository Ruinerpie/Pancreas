package ruinerpie.pancreas.kit;

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

import net.minecraft.world.InteractionHand;

public record FindResult(int slot, int count) {
    public boolean found() {
        return slot != -1;
    }

    public boolean isHotbar() {
        return slot >= 0 && slot <= 8;
    }

    public boolean isMain() {
        return slot >= 9 && slot <= 35;
    }

    public boolean isOffhand() {
        return slot == 45;
    }

    public InteractionHand getHand() {
        if (slot == 45) return InteractionHand.OFF_HAND;
        if (slot >= 0 && slot <= 8) return InteractionHand.MAIN_HAND;
        return null;
    }

    public boolean isMainHand() {
        return getHand() == InteractionHand.MAIN_HAND;
    }

    public boolean isOffHand() {
        return getHand() == InteractionHand.OFF_HAND;
    }

    public int getSlot() {
        return slot;
    }

    public int getCount() {
        return count;
    }
}