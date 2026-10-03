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

import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

public class Slick extends Feature {
    public enum ListMode {
        Whitelist,
        Blacklist
    }

    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    public final Value<Double> friction = sgGeneral.add(new DoubleValue.Builder()
        .name("friction")
        .description("Friction coefficient for blocks.")
        .defaultValue(1.0)
        .min(0.01)
        .max(1.10)
        .sliderRange(0.01, 1.10)
        .build()
    );

    public final Value<ListMode> listMode = sgGeneral.add(new ChoiceValue.Builder<ListMode>()
        .name("list-mode")
        .description("Block filter mode.")
        .defaultValue(ListMode.Blacklist)
        .build()
    );

    public final Value<List<Block>> ignoredBlocks = sgGeneral.add(new BlockListValue.Builder()
        .name("ignored-blocks")
        .description("Blocks unaffected when in Blacklist mode.")
        .defaultValue(new ArrayList<>())
        .visible(() -> listMode.get() == ListMode.Blacklist)
        .build()
    );

    public final Value<List<Block>> allowedBlocks = sgGeneral.add(new BlockListValue.Builder()
        .name("allowed-blocks")
        .description("Blocks affected when in Whitelist mode.")
        .defaultValue(new ArrayList<>())
        .visible(() -> listMode.get() == ListMode.Whitelist)
        .build()
    );

    public Slick() {
        super(Group.Cheats, "slick", "Custom block friction (make any block behave like ice).");
    }

    public float getFriction(Block block) {
        if (!isActive()) return block.getFriction();
        if (listMode.get() == ListMode.Whitelist) {
            if (!allowedBlocks.get().contains(block)) return block.getFriction();
        } else {
            if (ignoredBlocks.get().contains(block)) return block.getFriction();
        }
        return (float) friction.get().doubleValue();
    }
}