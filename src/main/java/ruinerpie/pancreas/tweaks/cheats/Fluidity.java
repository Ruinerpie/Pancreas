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

public class Fluidity extends Feature {
    public enum WebMode {
        Vanilla,
        None
    }

    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    public final Value<Boolean> items = sgGeneral.add(new FlagValue.Builder()
        .name("items")
        .description("Prevents item use slowdown (eating, drinking, bow drawing).")
        .defaultValue(true)
        .build()
    );

    public final Value<WebMode> web = sgGeneral.add(new ChoiceValue.Builder<WebMode>()
        .name("web")
        .description("Cobweb slowdown mode.")
        .defaultValue(WebMode.Vanilla)
        .build()
    );

    public final Value<Boolean> honeyBlock = sgGeneral.add(new FlagValue.Builder()
        .name("honey-block")
        .description("Prevents honey block slowdown.")
        .defaultValue(true)
        .build()
    );

    public final Value<Boolean> soulSand = sgGeneral.add(new FlagValue.Builder()
        .name("soul-sand")
        .description("Prevents soul sand slowdown.")
        .defaultValue(true)
        .build()
    );

    public final Value<Boolean> slimeBlock = sgGeneral.add(new FlagValue.Builder()
        .name("slime-block")
        .description("Prevents slime block slowdown.")
        .defaultValue(true)
        .build()
    );

    public final Value<Boolean> berryBush = sgGeneral.add(new FlagValue.Builder()
        .name("berry-bush")
        .description("Prevents sweet berry bush slowdown.")
        .defaultValue(true)
        .build()
    );

    public final Value<Boolean> airStrict = sgGeneral.add(new FlagValue.Builder()
        .name("air-strict")
        .description("Strict air slowdown bypass.")
        .defaultValue(false)
        .build()
    );

    public final Value<Boolean> fluidDrag = sgGeneral.add(new FlagValue.Builder()
        .name("fluid-drag")
        .description("Prevents fluid movement drag.")
        .defaultValue(false)
        .build()
    );

    public final Value<Boolean> sneaking = sgGeneral.add(new FlagValue.Builder()
        .name("sneaking")
        .description("Prevents sneak speed reduction.")
        .defaultValue(false)
        .build()
    );

    public final Value<Boolean> hunger = sgGeneral.add(new FlagValue.Builder()
        .name("hunger")
        .description("Allows sprinting with low hunger.")
        .defaultValue(false)
        .build()
    );

    public final Value<Boolean> slowness = sgGeneral.add(new FlagValue.Builder()
        .name("slowness")
        .description("Prevents slowness potion effect slowdown.")
        .defaultValue(false)
        .build()
    );

    public Fluidity() {
        super(Group.Cheats, "fluidity", "Remove various movement slowdowns.");
    }
}