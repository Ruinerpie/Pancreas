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

import net.minecraft.world.phys.Vec3;

public class Handwork extends Feature {
    public enum SwingMode {
        Offhand,
        Mainhand,
        None
    }

    private final ValueGroup sgGeneral = settings.getDefaultGroup();
    private final ValueGroup sgMainHand = settings.createGroup("Main Hand");
    private final ValueGroup sgOffHand = settings.createGroup("Off Hand");
    private final ValueGroup sgArm = settings.createGroup("Arm");

    public final Value<Boolean> serverRotations = sgGeneral.add(new FlagValue.Builder()
        .name("server-rotations")
        .description("Renders hand according to server-side head rotations.")
        .defaultValue(false)
        .build()
    );

    public final Value<Boolean> oldAnimations = sgGeneral.add(new FlagValue.Builder()
        .name("old-animations")
        .description("Legacy 1.8 style attack/block animations.")
        .defaultValue(false)
        .build()
    );

    public final Value<Boolean> skipSwappingAnimation = sgGeneral.add(new FlagValue.Builder()
        .name("skip-swapping-animation")
        .description("Removes the item re-equip dip animation on slot switch.")
        .defaultValue(false)
        .build()
    );

    public final Value<Boolean> disableEatingAnimation = sgGeneral.add(new FlagValue.Builder()
        .name("disable-eating-animation")
        .description("Disables item bouncing animation while eating.")
        .defaultValue(false)
        .build()
    );

    public final Value<Boolean> swordSlash = sgGeneral.add(new FlagValue.Builder()
        .name("sword-slash")
        .description("Forces sweeping attack slash animation.")
        .defaultValue(false)
        .build()
    );

    public final Value<SwingMode> swingMode = sgGeneral.add(new ChoiceValue.Builder<SwingMode>()
        .name("swing-mode")
        .description("Which hand swings on attack.")
        .defaultValue(SwingMode.None)
        .build()
    );

    public final Value<Integer> swingSpeed = sgGeneral.add(new IntValue.Builder()
        .name("swing-speed")
        .description("Hand swing speed modifier.")
        .defaultValue(6)
        .min(0)
        .max(20)
        .sliderRange(0, 20)
        .build()
    );

    public final Value<Double> mainHandProgress = sgGeneral.add(new DoubleValue.Builder()
        .name("main-hand-progress")
        .description("Fixed main-hand swing progress (0-1).")
        .defaultValue(0.0)
        .min(0.0)
        .max(1.0)
        .sliderRange(0.0, 1.0)
        .build()
    );

    public final Value<Double> offHandProgress = sgGeneral.add(new DoubleValue.Builder()
        .name("off-hand-progress")
        .description("Fixed off-hand swing progress (0-1).")
        .defaultValue(0.0)
        .min(0.0)
        .max(1.0)
        .sliderRange(0.0, 1.0)
        .build()
    );

    public final Value<Vec3> mainScale = sgMainHand.add(new VectorValue.Builder()
        .name("scale")
        .description("Main hand scale.")
        .defaultValue(1.0, 1.0, 1.0)
        .build()
    );

    public final Value<Vec3> mainPosition = sgMainHand.add(new VectorValue.Builder()
        .name("position")
        .description("Main hand position offset.")
        .defaultValue(0.0, 0.0, 0.0)
        .build()
    );

    public final Value<Vec3> mainRotation = sgMainHand.add(new VectorValue.Builder()
        .name("rotation")
        .description("Main hand rotation angles.")
        .defaultValue(0.0, 0.0, 0.0)
        .build()
    );

    public final Value<Vec3> offScale = sgOffHand.add(new VectorValue.Builder()
        .name("scale")
        .description("Off hand scale.")
        .defaultValue(1.0, 1.0, 1.0)
        .build()
    );

    public final Value<Vec3> offPosition = sgOffHand.add(new VectorValue.Builder()
        .name("position")
        .description("Off hand position offset.")
        .defaultValue(0.0, 0.0, 0.0)
        .build()
    );

    public final Value<Vec3> offRotation = sgOffHand.add(new VectorValue.Builder()
        .name("rotation")
        .description("Off hand rotation angles.")
        .defaultValue(0.0, 0.0, 0.0)
        .build()
    );

    public final Value<Vec3> armScale = sgArm.add(new VectorValue.Builder()
        .name("scale")
        .description("Arm scale.")
        .defaultValue(1.0, 1.0, 1.0)
        .build()
    );

    public final Value<Vec3> armPosition = sgArm.add(new VectorValue.Builder()
        .name("position")
        .description("Arm position offset.")
        .defaultValue(0.0, 0.0, 0.0)
        .build()
    );

    public final Value<Vec3> armRotation = sgArm.add(new VectorValue.Builder()
        .name("rotation")
        .description("Arm rotation angles.")
        .defaultValue(0.0, 0.0, 0.0)
        .build()
    );

    public Handwork() {
        super(Group.Extras, "handwork", "Customizable first-person hand and held item view models.");
    }
}