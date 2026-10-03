package ruinerpie.pancreas.tweaks.utilities;

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

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class Edge extends Feature {
    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    public final Value<Boolean> advanced = sgGeneral.add(new FlagValue.Builder()
        .name("advanced")
        .description("Enables advanced bounding geometry.")
        .defaultValue(true)
        .build()
    );

    public final Value<Boolean> singleSide = sgGeneral.add(new FlagValue.Builder()
        .name("single-side")
        .description("Renders only the targeted block face.")
        .defaultValue(false)
        .build()
    );

    public final Value<Shape> shapeMode = sgGeneral.add(new ChoiceValue.Builder<Shape>()
        .name("shape-mode")
        .description("How the block outline is rendered.")
        .defaultValue(Shape.Both)
        .build()
    );

    public final Value<Tint> sideColor = sgGeneral.add(new TintValue.Builder()
        .name("side-color")
        .description("Fill color for block surfaces.")
        .defaultValue(new Tint(255, 255, 255, 50))
        .build()
    );

    public final Value<Tint> lineColor = sgGeneral.add(new TintValue.Builder()
        .name("line-color")
        .description("OutlineDec color for block edges.")
        .defaultValue(new Tint(255, 255, 255, 255))
        .build()
    );

    public final Value<Boolean> hideWhenInsideBlock = sgGeneral.add(new FlagValue.Builder()
        .name("hide-when-inside-block")
        .description("Hides outline if player camera is submerged inside a solid block.")
        .defaultValue(true)
        .build()
    );

    public Edge() {
        super(Group.Utilities, "edge", "Customizes the targeted block highlight outline and face colors.");
    }

    @Listen
    private void onRender3D(Render3D event) {
        if (mc.level == null || mc.player == null) return;
        if (!(mc.hitResult instanceof BlockHitResult hit) || hit.getType() != HitResult.Type.BLOCK) return;

        BlockPos pos = hit.getBlockPos();
        if (hideWhenInsideBlock.get() && mc.level.getBlockState(BlockPos.containing(mc.player.getEyePosition())).isSolid()) {
            return;
        }

        if (singleSide.get()) {
            Direction dir = hit.getDirection();
            double x = pos.getX();
            double y = pos.getY();
            double z = pos.getZ();

            if (dir.getAxis() == Direction.Axis.Y) {
                double faceY = dir == Direction.UP ? y + 1.0 : y;
                Solid.INSTANCE.sideHorizontal(x, faceY, z, x + 1.0, z + 1.0, sideColor.get(), lineColor.get(), shapeMode.get());
            } else {
                Solid.INSTANCE.sideVertical(x, y, z, x + 1.0, y + 1.0, z + 1.0, sideColor.get(), lineColor.get(), shapeMode.get());
            }
        } else {
            Solid.INSTANCE.box(pos, sideColor.get(), lineColor.get(), shapeMode.get(), 0);
        }
    }
}