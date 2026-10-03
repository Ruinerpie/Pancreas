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
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.tags.ItemTags;

public class Reload extends Feature {
    private final ValueGroup sgGeneral = settings.getDefaultGroup();
    private final ValueGroup sgRender = settings.createGroup("Render");

    public final IntValue delay = sgGeneral.add(new IntValue.Builder()
        .name("delay")
        .description("Tick delay before sending packet re-break.")
        .defaultValue(0)
        .min(0)
        .max(20)
        .build());

    public final FlagValue onlyPick = sgGeneral.add(new FlagValue.Builder()
        .name("only-pick")
        .description("Only re-breaks blocks when holding a pickaxe.")
        .defaultValue(true)
        .build());

    public final FlagValue rotate = sgGeneral.add(new FlagValue.Builder()
        .name("rotate")
        .description("Face towards the block when re-breaking.")
        .defaultValue(true)
        .build());

    public final FlagValue render = sgRender.add(new FlagValue.Builder()
        .name("render")
        .description("Renders outline around currently targeted re-break block.")
        .defaultValue(true)
        .build());

    public final ChoiceValue<Shape> shapeMode = sgRender.add(new ChoiceValue.Builder<Shape>()
        .name("shape-mode")
        .description("Box render style.")
        .defaultValue(Shape.Both)
        .visible(render::get)
        .build());

    public final TintValue sideColor = sgRender.add(new TintValue.Builder()
        .name("side-color")
        .description("Side fill color.")
        .defaultValue(new Tint(204, 0, 0, 10))
        .visible(render::get)
        .build());

    public final TintValue lineColor = sgRender.add(new TintValue.Builder()
        .name("line-color")
        .description("Line outline color.")
        .defaultValue(new Tint(204, 0, 0, 255))
        .visible(render::get)
        .build());

    private BlockPos targetPos = null;
    private Direction targetDirection = Direction.UP;
    private int timer = 0;

    public Reload() {
        super(Group.Cheats, "reload", "Instantly re-breaks recently mined blocks on delay.");
    }

    @Override
    public void onDeactivate() {
        targetPos = null;
    }

    @Listen
    private void onStartBreakingBlock(StartBreakBlock event) {
        if (Pancreas.mc.player == null) return;
        if (onlyPick.get() && !(Pancreas.mc.player.getMainHandItem().is(ItemTags.PICKAXES))) {
            return;
        }

        this.targetPos = event.blockPos;
        this.targetDirection = event.direction;
        this.timer = delay.get();
    }

    @Listen
    private void onTick(ClientTick event) {
        if (targetPos == null || Pancreas.mc.player == null || Pancreas.mc.getConnection() == null) return;

        if (timer > 0) {
            timer--;
            return;
        }

        if (onlyPick.get() && !(Pancreas.mc.player.getMainHandItem().is(ItemTags.PICKAXES))) {
            return;
        }

        Pancreas.mc.getConnection().send(new ServerboundPlayerActionPacket(
            ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK,
            targetPos,
            targetDirection
        ));
    }

    @Listen
    private void onRender3D(Render3D event) {
        if (render.get() && targetPos != null) {
            event.renderer.box(targetPos, sideColor.get(), lineColor.get(), shapeMode.get(), 0);
        }
    }
}