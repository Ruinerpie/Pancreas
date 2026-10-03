package ruinerpie.pancreas.signals;

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

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public class HeldItemRender extends Signal {
    public InteractionHand hand;
    public ItemStack item;
    public PoseStack matrix;

    public HeldItemRender(InteractionHand hand, ItemStack item, PoseStack matrix) {
        this.hand = hand;
        this.item = item;
        this.matrix = matrix;
    }
}