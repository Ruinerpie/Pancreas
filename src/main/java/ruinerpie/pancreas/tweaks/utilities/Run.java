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

import net.minecraft.network.protocol.game.ServerboundAttackPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;

public class Run extends Feature {
    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    public enum Mode {
        Strict,
        Rage
    }

    public final Value<Mode> mode = sgGeneral.add(new ChoiceValue.Builder<Mode>()
        .name("sprint-mode")
        .description("What mode of sprinting.")
        .defaultValue(Mode.Strict)
        .build()
    );

    public final Value<Boolean> sprint = sgGeneral.add(new FlagValue.Builder()
        .name("sprint")
        .description("Sprint automatically.")
        .defaultValue(true)
        .build()
    );

    private final Value<Boolean> keepSprint = sgGeneral.add(new FlagValue.Builder()
        .name("keep-sprint")
        .description("Whether to keep sprinting after attacking.")
        .defaultValue(false)
        .build()
    );

    private final Value<Boolean> unsprintOnHit = sgGeneral.add(new FlagValue.Builder()
        .name("unsprint-on-hit")
        .description("Whether to stop sprinting before attacking, to ensure you get crits and sweep attacks.")
        .defaultValue(false)
        .build()
    );

    public final Value<Boolean> unsprintInWater = sgGeneral.add(new FlagValue.Builder()
        .name("unsprint-in-water")
        .description("Whether to stop sprinting when in water.")
        .defaultValue(true)
        .visible(() -> mode.get() == Mode.Rage)
        .build()
    );

    private final Value<Boolean> permaSprint = sgGeneral.add(new FlagValue.Builder()
        .name("sprint-while-stationary")
        .description("Sprint even when not moving.")
        .defaultValue(false)
        .visible(() -> mode.get() == Mode.Rage)
        .build()
    );

    public Run() {
        super(Group.Utilities, "run", "Automatically sprints.");
    }

    @Listen(priority = Order.HIGH)
    private void onTickMovement(WorldTick event) {
        if (mc.player == null) return;
        if (unsprintInWater.get() && mc.player.isInWater()) return;

        mc.player.setSprinting(shouldSprint());
    }

    @Listen(priority = Order.HIGH)
    private void onPacketSend(PacketOut event) {
        if (mc.player == null || mc.getConnection() == null) return;
        if (!unsprintOnHit.get()) return;
        if (!(event.packet instanceof ServerboundAttackPacket)) return;

        mc.getConnection().send(new ServerboundPlayerCommandPacket(mc.player, ServerboundPlayerCommandPacket.Action.STOP_SPRINTING));
        mc.player.setSprinting(false);
    }

    @Listen
    private void onPacketSent(PacketSent event) {
        if (mc.player == null || mc.getConnection() == null) return;
        if (!unsprintOnHit.get() || !keepSprint.get()) return;
        if (!(event.packet instanceof ServerboundAttackPacket)) return;
        if (!shouldSprint() || mc.player.isSprinting()) return;

        mc.getConnection().send(new ServerboundPlayerCommandPacket(mc.player, ServerboundPlayerCommandPacket.Action.START_SPRINTING));
        mc.player.setSprinting(true);
    }

    public boolean shouldSprint() {
        if (mc.player == null) return false;
        InMove inMove = Features.get().get(InMove.class);
        if (mc.screen != null && (inMove == null || !inMove.isActive() || !inMove.sprint.get())) return false;

        float movement = mode.get() == Mode.Rage
            ? (Math.abs(mc.player.zza) + Math.abs(mc.player.xxa))
            : mc.player.zza;

        if (movement <= (mc.player.isUnderWater() ? 1.0E-5F : 0.8)) {
            if (mode.get() == Mode.Strict || !permaSprint.get()) return false;
        }

        boolean strictSprint = !(mc.player.isInShallowWater())
            && (mc.player.isPassenger() ? (mc.player.getVehicle() != null && mc.player.getVehicle().isLocalInstanceAuthoritative()) : mc.player.getFoodData().hasEnoughFood())
            && (!mc.player.horizontalCollision || mc.player.minorHorizontalCollision);

        return isActive() && (mode.get() == Mode.Rage || strictSprint);
    }

    public boolean rageSprint() {
        return isActive() && mode.get() == Mode.Rage;
    }

    public boolean unsprintInWater() {
        return isActive() && unsprintInWater.get();
    }

    public boolean stopSprinting() {
        return !isActive() || !keepSprint.get();
    }
}