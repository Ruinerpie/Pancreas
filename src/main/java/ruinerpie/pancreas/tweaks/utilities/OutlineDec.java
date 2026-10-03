package ruinerpie.pancreas.tweaks.utilities;

import ruinerpie.pancreas.mixin.*;
import ruinerpie.pancreas.*;
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
import net.minecraft.server.level.BlockDestructionProgress;

public class OutlineDec extends Feature {
    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    public final Value<Shape> shapeMode = sgGeneral.add(new ChoiceValue.Builder<Shape>()
        .name("shape-mode")
        .description("Break progress highlight geometry.")
        .defaultValue(Shape.Both)
        .build()
    );

    public final Value<Boolean> packetMine = sgGeneral.add(new FlagValue.Builder()
        .name("packet-mine")
        .description("Renders packet mining progress.")
        .defaultValue(true)
        .build()
    );

    public final Value<Tint> startSideColor = sgGeneral.add(new TintValue.Builder()
        .name("start-side-color")
        .description("Initial fill color at 0% breaking progress.")
        .defaultValue(new Tint(25, 252, 25, 150))
        .build()
    );

    public final Value<Tint> startLineColor = sgGeneral.add(new TintValue.Builder()
        .name("start-line-color")
        .description("Initial outline color at 0% breaking progress.")
        .defaultValue(new Tint(25, 252, 25, 150))
        .build()
    );

    public final Value<Tint> endSideColor = sgGeneral.add(new TintValue.Builder()
        .name("end-side-color")
        .description("Final fill color near 100% breaking progress.")
        .defaultValue(new Tint(255, 25, 25, 150))
        .build()
    );

    public final Value<Tint> endLineColor = sgGeneral.add(new TintValue.Builder()
        .name("end-line-color")
        .description("Final outline color near 100% breaking progress.")
        .defaultValue(new Tint(255, 25, 25, 150))
        .build()
    );

    public OutlineDec() {
        super(Group.Utilities, "outline", "Displays smooth color-interpolated block destruction progress indicators.");
    }

    @Listen
    private void onRender3D(Render3D event) {
        if (mc.level == null || mc.player == null) return;

        if (mc.gameMode instanceof GameModeMixin gpm) {
            BlockPos currentPos = gpm.pancreas$getCurrentBreakingBlockPos();
            float progress = gpm.pancreas$getBreakingProgress();
            if (currentPos != null && progress > 0.0f) {
                renderProgress(currentPos, Math.min(1.0f, progress));
            }
        }

        if (mc.levelRenderer instanceof LevelDataMixin lra) {
            for (BlockDestructionProgress bdp : lra.pancreas$getDestroyingBlocks().values()) {
                int stage = bdp.getUpdatedRenderTick();
                float progress = Math.min(1.0f, (float) stage / 9.0f);
                renderProgress(bdp.getPos(), progress);
            }
        }
    }

    private void renderProgress(BlockPos pos, float progress) {
        Color side = interpolate(startSideColor.get(), endSideColor.get(), progress);
        Color line = interpolate(startLineColor.get(), endLineColor.get(), progress);
        Solid.INSTANCE.box(pos, side, line, shapeMode.get(), 0);
    }

    private Color interpolate(Color c1, Color c2, float t) {
        int r = (int) (c1.r + (c2.r - c1.r) * t);
        int g = (int) (c1.g + (c2.g - c1.g) * t);
        int b = (int) (c1.b + (c2.b - c1.b) * t);
        int a = (int) (c1.a + (c2.a - c1.a) * t);
        return new Color(r, g, b, a);
    }
}