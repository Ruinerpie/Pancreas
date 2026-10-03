package ruinerpie.pancreas.tweaks.cheats;

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
import net.minecraft.world.phys.AABB;
import org.lwjgl.glfw.GLFW;

public class Delve extends Feature {
    private final ValueGroup sgGeneral = settings.getDefaultGroup();
    private final ValueGroup sgRender = settings.createGroup("Render");

    public final KeyValue selectionBind = sgGeneral.add(new KeyValue.Builder()
        .name("selection-bind")
        .description("Key to set excavation corner positions.")
        .defaultValue(GLFW.GLFW_KEY_R)
        .build());

    public final FlagValue logSelection = sgGeneral.add(new FlagValue.Builder()
        .name("log-selection")
        .description("Shows notification toast when setting corners.")
        .defaultValue(true)
        .build());

    public final FlagValue keepActive = sgGeneral.add(new FlagValue.Builder()
        .name("keep-active")
        .description("Keeps the module active after completing an excavation.")
        .defaultValue(false)
        .build());

    public final ChoiceValue<Shape> shapeMode = sgRender.add(new ChoiceValue.Builder<Shape>()
        .name("shape-mode")
        .description("How the excavation box is drawn.")
        .defaultValue(Shape.Both)
        .build());

    public final TintValue sideColor = sgRender.add(new TintValue.Builder()
        .name("side-color")
        .description("Side fill color.")
        .defaultValue(new Tint(255, 255, 255, 50))
        .build());

    public final TintValue lineColor = sgRender.add(new TintValue.Builder()
        .name("line-color")
        .description("OutlineDec line color.")
        .defaultValue(new Tint(255, 255, 255, 255))
        .build());

    private BlockPos pos1 = null;
    private BlockPos pos2 = null;

    public Delve() {
        super(Group.Cheats, "delve", "Area excavation box selection and radius dig assistance.");
    }

    public void setPos1(BlockPos p) {
        this.pos1 = p;
        if (logSelection.get()) {
            Toasts.get().info("Bulk Dig", "Position 1 set: " + p.toShortString());
        }
    }

    public void setPos2(BlockPos p) {
        this.pos2 = p;
        if (logSelection.get()) {
            Toasts.get().info("Bulk Dig", "Position 2 set: " + p.toShortString());
        }
    }

    @Override
    public void onDeactivate() {
        if (!keepActive.get()) {
            pos1 = null;
            pos2 = null;
        }
    }

    @Listen
    private void onRender3D(Render3D event) {
        if (pos1 != null && pos2 != null) {
            AABB box = new AABB(
                Math.min(pos1.getX(), pos2.getX()),
                Math.min(pos1.getY(), pos2.getY()),
                Math.min(pos1.getZ(), pos2.getZ()),
                Math.max(pos1.getX(), pos2.getX()) + 1,
                Math.max(pos1.getY(), pos2.getY()) + 1,
                Math.max(pos1.getZ(), pos2.getZ()) + 1
            );
            event.renderer.box(box, sideColor.get(), lineColor.get(), shapeMode.get(), 0);
        } else if (pos1 != null) {
            event.renderer.box(pos1, sideColor.get(), lineColor.get(), shapeMode.get(), 0);
        }
    }
}