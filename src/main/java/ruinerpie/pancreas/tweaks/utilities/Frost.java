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

public class Frost extends Feature {
    private final ValueGroup sgGeneral = settings.getDefaultGroup();
    private final ValueGroup sgScreens = settings.createGroup("Screens");

    public final Value<Integer> strength = sgGeneral.add(new IntValue.Builder()
        .name("strength")
        .description("Gaussian blur intensity pass count.")
        .defaultValue(5)
        .min(1)
        .max(20)
        .sliderRange(1, 20)
        .build()
    );

    public final Value<Integer> fadeTime = sgGeneral.add(new IntValue.Builder()
        .name("fade-time")
        .description("Fade-in duration in milliseconds.")
        .defaultValue(100)
        .min(0)
        .max(500)
        .sliderRange(0, 500)
        .build()
    );

    public final Value<Boolean> pancreas = sgScreens.add(new FlagValue.Builder()
        .name("pancreas")
        .description("Blur backgrounds when Pancreas GUI screens are open.")
        .defaultValue(true)
        .build()
    );

    public final Value<Boolean> inventories = sgScreens.add(new FlagValue.Builder()
        .name("inventories")
        .description("Blur backgrounds behind inventory screens.")
        .defaultValue(true)
        .build()
    );

    public final Value<Boolean> chat = sgScreens.add(new FlagValue.Builder()
        .name("chat")
        .description("Blur background when chat screen is open.")
        .defaultValue(false)
        .build()
    );

    public final Value<Boolean> other = sgScreens.add(new FlagValue.Builder()
        .name("other")
        .description("Blur background for other vanilla screens.")
        .defaultValue(true)
        .build()
    );

    public Frost() {
        super(Group.Utilities, "frost", "Applies smooth frosted glass background blur behind open GUI menus.");
    }

    public boolean shouldBlur() {
        if (!isActive() || mc.screen == null) return false;
        return true;
    }
}