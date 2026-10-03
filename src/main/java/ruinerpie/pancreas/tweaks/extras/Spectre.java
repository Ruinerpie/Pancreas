package ruinerpie.pancreas.tweaks.extras;

import ruinerpie.pancreas.Feature;
import ruinerpie.pancreas.Group;
import ruinerpie.pancreas.Listen;
import ruinerpie.pancreas.Toasts;
import ruinerpie.pancreas.Value;
import ruinerpie.pancreas.ValueGroup;
import ruinerpie.pancreas.signals.WorldTick;
import ruinerpie.pancreas.values.DoubleValue;
import ruinerpie.pancreas.values.FlagValue;

public class Spectre extends Feature {
    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    public final Value<Double> speed = sgGeneral.add(new DoubleValue.Builder()
        .name("speed")
        .description("Spectre flight speed.")
        .defaultValue(1.0)
        .min(0.1)
        .max(10.0)
        .sliderRange(0.1, 10.0)
        .build()
    );

    public final Value<Double> speedScrollSensitivity = sgGeneral.add(new DoubleValue.Builder()
        .name("speed-scroll-sensitivity")
        .description("Mouse scroll speed adjustment sensitivity.")
        .defaultValue(0.0)
        .min(0.0)
        .max(5.0)
        .build()
    );

    public final Value<Boolean> staySneaking = sgGeneral.add(new FlagValue.Builder()
        .name("stay-sneaking")
        .description("Keeps the real player body sneaking.")
        .defaultValue(true)
        .build()
    );

    public final Value<Boolean> toggleOnDamage = sgGeneral.add(new FlagValue.Builder()
        .name("toggle-on-damage")
        .description("Disables GhostEye when player takes damage.")
        .defaultValue(false)
        .build()
    );

    public final Value<Boolean> toggleOnDeath = sgGeneral.add(new FlagValue.Builder()
        .name("toggle-on-death")
        .description("Disables GhostEye upon player death.")
        .defaultValue(false)
        .build()
    );

    public final Value<Boolean> toggleOnLog = sgGeneral.add(new FlagValue.Builder()
        .name("toggle-on-log")
        .description("Disables GhostEye when disconnecting.")
        .defaultValue(true)
        .build()
    );

    public final Value<Boolean> reloadChunks = sgGeneral.add(new FlagValue.Builder()
        .name("reload-chunks")
        .description("Reloads world chunks to render culled geometry.")
        .defaultValue(true)
        .build()
    );

    public final Value<Boolean> showHands = sgGeneral.add(new FlagValue.Builder()
        .name("show-hands")
        .description("Renders player hands in freecam.")
        .defaultValue(true)
        .build()
    );

    public final Value<Boolean> rotate = sgGeneral.add(new FlagValue.Builder()
        .name("rotate")
        .description("Sends freecam view rotations to the server.")
        .defaultValue(false)
        .build()
    );

    public final Value<Boolean> staticPos = sgGeneral.add(new FlagValue.Builder()
        .name("static")
        .description("Prevents the real player body from receiving physics updates.")
        .defaultValue(true)
        .build()
    );

    public double camX, camY, camZ;
    public float camYaw, camPitch;

    public Spectre() {
        super(Group.Extras, "spectre", "Detached free camera view.");
    }

    @Override
    public void onActivate() {
        var player = mc.player;
        if (player != null) {
            camX = player.getX();
            camY = player.getY();
            camZ = player.getZ();
            camYaw = player.getYRot();
            camPitch = player.getXRot();
        }

        if (reloadChunks.get() && mc.levelRenderer != null) {
            mc.levelRenderer.allChanged();
        }
    }

    @Override
    public void onDeactivate() {
        if (reloadChunks.get() && mc.levelRenderer != null) {
            mc.levelRenderer.allChanged();
        }
    }

    @Listen
    private void onTick(WorldTick event) {
        var player = mc.player;
        if (player == null) return;

        if (toggleOnDeath.get() && player.isDeadOrDying()) {
            Toasts.get().info("GhostEye", "Disabled due to player death.");
            toggle();
            return;
        }

        if (toggleOnDamage.get() && player.hurtTime > 0) {
            Toasts.get().info("GhostEye", "Disabled due to incoming damage.");
            toggle();
            return;
        }
    }

    public void changeLookDirection(double dy, double dx) {
        camYaw += (float) (dx * 0.15);
        camPitch += (float) (dy * 0.15);
        camPitch = Math.max(-90.0f, Math.min(90.0f, camPitch));
    }
}