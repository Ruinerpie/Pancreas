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

public class Leap extends Feature {
    public enum Mode {
        Jump,
        LowHop
    }

    public enum JumpWhen {
        Sprinting,
        Walking,
        Always
    }

    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    public final Value<Mode> mode = sgGeneral.add(new ChoiceValue.Builder<Mode>()
        .name("mode")
        .description("The method of jumping.")
        .defaultValue(Mode.Jump)
        .build()
    );

    public final Value<JumpWhen> jumpIf = sgGeneral.add(new ChoiceValue.Builder<JumpWhen>()
        .name("jump-if")
        .description("Jump if.")
        .defaultValue(JumpWhen.Always)
        .build()
    );

    public final Value<Double> velocityHeight = sgGeneral.add(new DoubleValue.Builder()
        .name("velocity-height")
        .description("The distance that velocity mode moves you.")
        .defaultValue(0.25)
        .min(0.0)
        .sliderMax(2.0)
        .build()
    );

    public Leap() {
        super(Group.Utilities, "leap", "Automatically jumps.");
    }

    private boolean jump() {
        if (mc.player == null) return false;
        return switch (jumpIf.get()) {
            case Sprinting -> mc.player.isSprinting() && (mc.player.zza != 0 || mc.player.xxa != 0);
            case Walking -> mc.player.zza != 0 || mc.player.xxa != 0;
            case Always -> true;
        };
    }

    @Listen
    private void onTick(ClientTick event) {
        if (mc.player == null || !mc.player.onGround() || mc.player.isShiftKeyDown() || !jump()) return;

        if (mode.get() == Mode.Jump) {
            mc.player.jumpFromGround();
        } else {
            mc.player.setDeltaMovement(mc.player.getDeltaMovement().x, velocityHeight.get(), mc.player.getDeltaMovement().z);
        }
    }
}