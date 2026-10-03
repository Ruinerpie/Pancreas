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

import net.minecraft.core.component.DataComponents;

public class Feed extends Feature {
    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    public final Value<Integer> hungerThreshold = sgGeneral.add(new IntValue.Builder()
        .name("hunger-threshold")
        .description("Hunger level at which eating starts.")
        .defaultValue(16)
        .min(1)
        .max(19)
        .build()
    );

    public final Value<Boolean> pauseOnCombat = sgGeneral.add(new FlagValue.Builder()
        .name("pause-on-combat")
        .description("Pause eating during combat.")
        .defaultValue(true)
        .build()
    );

    private boolean eating = false;

    public Feed() {
        super(Group.Utilities, "feed", "Automatically consumes food when hungry or low health.");
    }

    @Listen
    private void onTick(ClientTick event) {
        if (mc.player == null || mc.gameMode == null) return;

        if (mc.player.getFoodData().getFoodLevel() <= hungerThreshold.get()) {
            FindResult food = InventoryKit.find(s -> s.has(DataComponents.FOOD));
            if (food.found() && !eating) {
                eating = true;
            }
        }
    }
}