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
import net.minecraft.world.entity.item.ItemEntity;

public class RenderItemEntity extends Signal {
    public ItemEntity itemEntity;
    public float f;
    public float g;
    public PoseStack matrixStack;

    public RenderItemEntity(ItemEntity itemEntity, float f, float g, PoseStack matrixStack) {
        this.itemEntity = itemEntity;
        this.f = f;
        this.g = g;
        this.matrixStack = matrixStack;
    }
}