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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class Oreline extends Feature {
    public enum FluidOpacity {
        None,
        Water,
        Lava,
        Both
    }

    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    public final Value<List<Block>> whitelist = sgGeneral.add(new BlockListValue.Builder()
        .name("whitelist")
        .description("Blocks rendered with WallView active.")
        .defaultValue(new ArrayList<>(Ores.ORES))
        .onChanged(b -> reloadRenderer())
        .build()
    );

    public final Value<Integer> opacity = sgGeneral.add(new IntValue.Builder()
        .name("opacity")
        .description("Wall opacity.")
        .defaultValue(25)
        .min(0)
        .max(255)
        .sliderRange(0, 255)
        .onChanged(v -> reloadRenderer())
        .build()
    );

    public final Value<FluidOpacity> fluidOpacity = sgGeneral.add(new ChoiceValue.Builder<FluidOpacity>()
        .name("fluid-opacity")
        .description("Fluid rendering visibility.")
        .defaultValue(FluidOpacity.Both)
        .onChanged(f -> reloadRenderer())
        .build()
    );

    public final Value<Boolean> exposedOnly = sgGeneral.add(new FlagValue.Builder()
        .name("exposed-only")
        .description("Only show ores exposed to air or caves.")
        .defaultValue(false)
        .onChanged(b -> reloadRenderer())
        .build()
    );

    public Oreline() {
        super(Group.Cheats, "oreline", "Only render whitelisted ores and valuable blocks.");
    }

    @Override
    public void onActivate() {
        reloadRenderer();
    }

    @Override
    public void onDeactivate() {
        reloadRenderer();
    }

    private void reloadRenderer() {
        if (mc.levelRenderer != null) {
            mc.levelRenderer.allChanged();
        }
    }

    public boolean isWhitelisted(Block block) {
        return whitelist.get().contains(block);
    }

    public boolean isExposed(BlockPos pos) {
        if (!exposedOnly.get() || mc.level == null) return true;
        for (net.minecraft.core.Direction dir : net.minecraft.core.Direction.values()) {
            if (mc.level.isEmptyBlock(pos.relative(dir))) return true;
        }
        return false;
    }
}