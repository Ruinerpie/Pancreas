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

public class Steady extends Feature {
    public enum LockMode {
        Smart,
        Simple,
        None
    }

    private final ValueGroup sgYaw = settings.createGroup("Yaw");
    private final ValueGroup sgPitch = settings.createGroup("Pitch");

    private final Value<LockMode> yawLockMode = sgYaw.add(new ChoiceValue.Builder<LockMode>()
        .name("yaw-lock-mode")
        .description("The way in which your yaw is locked.")
        .defaultValue(LockMode.Simple)
        .build()
    );

    private final Value<Double> yawAngle = sgYaw.add(new DoubleValue.Builder()
        .name("yaw-angle")
        .description("Yaw angle in degrees.")
        .defaultValue(0.0)
        .sliderMax(360)
        .max(360)
        .visible(() -> yawLockMode.get() == LockMode.Simple)
        .build()
    );

    private final Value<LockMode> pitchLockMode = sgPitch.add(new ChoiceValue.Builder<LockMode>()
        .name("pitch-lock-mode")
        .description("The way in which your pitch is locked.")
        .defaultValue(LockMode.Simple)
        .build()
    );

    private final Value<Double> pitchAngle = sgPitch.add(new DoubleValue.Builder()
        .name("pitch-angle")
        .description("Pitch angle in degrees.")
        .defaultValue(0.0)
        .range(-90, 90)
        .sliderRange(-90, 90)
        .visible(() -> pitchLockMode.get() == LockMode.Simple)
        .build()
    );

    public Steady() {
        super(Group.Extras, "steady", "Changes/locks your yaw and pitch.");
    }

    @Override
    public void onActivate() {
        applyLock();
    }

    @Listen
    private void onTick(WorldTick event) {
        applyLock();
    }

    private void applyLock() {
        if (mc.player == null) return;

        switch (yawLockMode.get()) {
            case Simple -> setYawAngle(yawAngle.get().floatValue());
            case Smart -> setYawAngle(getSmartYawDirection());
            case None -> {}
        }

        switch (pitchLockMode.get()) {
            case Simple -> mc.player.setXRot(pitchAngle.get().floatValue());
            case Smart -> mc.player.setXRot(getSmartPitchDirection());
            case None -> {}
        }
    }

    private float getSmartYawDirection() {
        if (mc.player == null) return 0f;
        return Math.round((mc.player.getYRot() + 1f) / 45f) * 45f;
    }

    private float getSmartPitchDirection() {
        if (mc.player == null) return 0f;
        return Math.round((mc.player.getXRot() + 1f) / 30f) * 30f;
    }

    private void setYawAngle(float yaw) {
        if (mc.player == null) return;
        mc.player.setYRot(yaw);
        mc.player.yHeadRot = yaw;
        mc.player.yBodyRot = yaw;
    }
}