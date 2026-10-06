package ruinerpie.pancreas.tweaks.utilities;

import ruinerpie.pancreas.Feature;
import ruinerpie.pancreas.Group;
import ruinerpie.pancreas.values.ValueGroup;
import ruinerpie.pancreas.values.ChoiceValue;
import ruinerpie.pancreas.values.FlagValue;
import ruinerpie.pancreas.values.Value;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

public class FRC extends Feature {

    public enum Cooldown {
        _0(0), _1(1), _2(2), _3(3), _4(4);

        public final int ticks;

        Cooldown(int t) {
            this.ticks = t;
        }

        @Override
        public String toString() {
            return String.valueOf(ticks);
        }
    }

    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    public final Value<Cooldown> cooldown = sgGeneral.add(new ChoiceValue.Builder<Cooldown>()
        .name("Cooldown")
        .description("Item use cooldown in ticks.")
        .defaultValue(Cooldown._0)
        .build()
    );

    public final Value<Boolean> blocksOnly = sgGeneral.add(new FlagValue.Builder()
        .name("Blocks Only")
        .description("Only speed up block placing.")
        .defaultValue(false)
        .build()
    );

    public final Value<Boolean> noGaps = sgGeneral.add(new FlagValue.Builder()
        .name("No Gaps")
        .description("At 0, places on every block your crosshair passes.")
        .defaultValue(true)
        .build()
    );

    public FRC() {
        super(Group.Utilities, "frc", "FRC",
            "Reduces right-click item use delay (Fast Right Click).");
    }

    public int getItemUseCooldown() {
        return cooldown.get().ticks;
    }

    public int getAppliedDelay() {
        return Math.max(1, getItemUseCooldown());
    }

    public boolean appliesTo(ItemStack main, ItemStack off) {
        if (!blocksOnly.get()) return true;
        return main.getItem() instanceof BlockItem || off.getItem() instanceof BlockItem;
    }

    public boolean fillGaps() {
        return getItemUseCooldown() == 0 && noGaps.get();
    }
}
