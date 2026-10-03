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

import net.minecraft.world.level.block.state.BlockState;

public class Truestone extends Feature {
    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    public final Value<Boolean> breaking = sgGeneral.add(new FlagValue.Builder()
        .name("breaking")
        .description("Whether to apply for block breaking actions.")
        .defaultValue(true)
        .build()
    );

    public final Value<Boolean> placing = sgGeneral.add(new FlagValue.Builder()
        .name("placing")
        .description("Whether to apply for block placement actions.")
        .defaultValue(true)
        .build()
    );

    public Truestone() {
        super(Group.Cheats, "truestone", "Attempts to prevent ghost blocks arising.");
    }

    @Listen
    private void onBreakBlock(BreakBlock event) {
        if (mc.isLocalServer() || !breaking.get() || mc.level == null || mc.player == null) return;

        event.cancel();

        BlockState blockState = mc.level.getBlockState(event.blockPos);
        blockState.getBlock().playerWillDestroy(mc.level, event.blockPos, blockState, mc.player);
    }

    @Listen
    private void onPlaceBlock(PlaceBlock event) {
        if (!placing.get()) return;

        event.cancel();
    }
}