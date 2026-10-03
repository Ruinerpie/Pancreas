package ruinerpie.pancreas.tweaks.utilities;

import ruinerpie.pancreas.Feature;
import ruinerpie.pancreas.Group;
import ruinerpie.pancreas.Listen;
import ruinerpie.pancreas.Value;
import ruinerpie.pancreas.ValueGroup;
import ruinerpie.pancreas.values.DoubleValue;
import ruinerpie.pancreas.values.FlagValue;
import ruinerpie.pancreas.signals.RenderBossBar;

import net.minecraft.network.chat.Component;

public class BarStack extends Feature {
    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    public final Value<Boolean> stack = sgGeneral.add(new FlagValue.Builder()
        .name("stack")
        .description("Stacks identical boss bars into a single bar with a multiplier tag.")
        .defaultValue(true)
        .build()
    );

    public final Value<Boolean> hideName = sgGeneral.add(new FlagValue.Builder()
        .name("hide-name")
        .description("Hides boss name text above the health bar.")
        .defaultValue(false)
        .build()
    );

    public final Value<Double> barSpacing = sgGeneral.add(new DoubleValue.Builder()
        .name("bar-spacing")
        .description("Vertical spacing between multiple boss bars.")
        .defaultValue(10.0)
        .min(0.0)
        .max(30.0)
        .sliderRange(0.0, 30.0)
        .build()
    );

    public BarStack() {
        super(Group.Utilities, "bar-stack", "Compactly stacks and customizes boss health bars.");
    }

    @Listen
    private void onBossText(RenderBossBar.BossText event) {
        if (hideName.get()) {
            event.name = Component.empty();
        }
    }

    @Listen
    private void onBossSpacing(RenderBossBar.BossSpacing event) {
        event.spacing = barSpacing.get().intValue();
    }

    @Listen
    private void onBossIterator(RenderBossBar.BossIterator event) {
        
    }
}