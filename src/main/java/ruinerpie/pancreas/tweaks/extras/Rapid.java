package ruinerpie.pancreas.tweaks.extras;

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

public class Rapid extends Feature {
    public enum Mode {
        Disabled,
        Hold,
        Press
    }

    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    private final Value<Boolean> inScreens = sgGeneral.add(new FlagValue.Builder()
        .name("while-in-screens")
        .description("Whether to click while a screen is open.")
        .defaultValue(true)
        .build()
    );

    private final Value<Mode> leftClickMode = sgGeneral.add(new ChoiceValue.Builder<Mode>()
        .name("mode-left")
        .description("The method of clicking for left clicks.")
        .defaultValue(Mode.Press)
        .build()
    );

    private final Value<Integer> leftClickDelay = sgGeneral.add(new IntValue.Builder()
        .name("delay-left")
        .description("The amount of delay between left clicks in ticks.")
        .defaultValue(2)
        .min(0)
        .sliderMax(60)
        .visible(() -> leftClickMode.get() == Mode.Press)
        .build()
    );

    private final Value<Mode> rightClickMode = sgGeneral.add(new ChoiceValue.Builder<Mode>()
        .name("mode-right")
        .description("The method of clicking for right clicks.")
        .defaultValue(Mode.Press)
        .build()
    );

    private final Value<Integer> rightClickDelay = sgGeneral.add(new IntValue.Builder()
        .name("delay-right")
        .description("The amount of delay between right clicks in ticks.")
        .defaultValue(2)
        .min(0)
        .sliderMax(60)
        .visible(() -> rightClickMode.get() == Mode.Press)
        .build()
    );

    private int rightClickTimer;
    private int leftClickTimer;

    public Rapid() {
        super(Group.Extras, "rapid", "Automatically clicks.");
    }

    @Override
    public void onActivate() {
        rightClickTimer = 0;
        leftClickTimer = 0;
        mc.options.keyAttack.setDown(false);
        mc.options.keyUse.setDown(false);
    }

    @Override
    public void onDeactivate() {
        mc.options.keyAttack.setDown(false);
        mc.options.keyUse.setDown(false);
    }

    @Listen
    private void onTick(WorldTick event) {
        if (!inScreens.get() && mc.screen != null) return;

        switch (leftClickMode.get()) {
            case Disabled -> {}
            case Hold -> mc.options.keyAttack.setDown(true);
            case Press -> {
                leftClickTimer++;
                if (leftClickTimer > leftClickDelay.get()) {
                    Terrain.leftClick();
                    leftClickTimer = 0;
                }
            }
        }

        switch (rightClickMode.get()) {
            case Disabled -> {}
            case Hold -> mc.options.keyUse.setDown(true);
            case Press -> {
                rightClickTimer++;
                if (rightClickTimer > rightClickDelay.get()) {
                    Terrain.rightClick();
                    rightClickTimer = 0;
                }
            }
        }
    }
}