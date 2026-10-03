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

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FRC extends Feature {
    public enum Mode {
        All,
        Some
    }

    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    public final Value<Mode> mode = sgGeneral.add(new ChoiceValue.Builder<Mode>()
        .name("mode")
        .description("Which items have their use cooldown reduced.")
        .defaultValue(Mode.All)
        .build()
    );

    public final Value<List<Item>> items = sgGeneral.add(new ItemListValue.Builder()
        .name("items")
        .description("Items affected when in Some mode.")
        .defaultValue(new ArrayList<>())
        .visible(() -> mode.get() == Mode.Some)
        .build()
    );

    public final Value<Boolean> blocks = sgGeneral.add(new FlagValue.Builder()
        .name("blocks")
        .description("Applies cooldown reduction to block placements.")
        .defaultValue(false)
        .visible(() -> mode.get() == Mode.Some)
        .build()
    );

    public final Value<Integer> cooldown = sgGeneral.add(new IntValue.Builder()
        .name("cooldown")
        .description("Item use cooldown delay in ticks.")
        .defaultValue(0)
        .min(0)
        .max(4)
        .sliderRange(0, 4)
        .build()
    );

    public final Value<Boolean> onlySingleplayer = sgGeneral.add(new FlagValue.Builder()
        .name("only-singleplayer")
        .description("Only operates in singleplayer worlds to avoid server kicks.")
        .defaultValue(true)
        .build()
    );

    public FRC() {
        super(Group.Utilities, "quickcast", "Reduces right-click item use delay (Fast Right Click).");
    }

    public int getItemUseCooldown(ItemStack stack) {
        if (!isActive()) return 4;
        if (onlySingleplayer.get() && !mc.hasSingleplayerServer()) return 4;

        if (mode.get() == Mode.All) {
            return cooldown.get();
        }

        if (stack != null) {
            if (items.get().contains(stack.getItem())) return cooldown.get();
            if (blocks.get() && stack.getItem() instanceof BlockItem) return cooldown.get();
        }

        return 4;
    }
}