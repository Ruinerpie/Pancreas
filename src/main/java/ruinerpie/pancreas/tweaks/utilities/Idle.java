package ruinerpie.pancreas.tweaks.utilities;

import ruinerpie.pancreas.mixin.*;
import ruinerpie.pancreas.*;
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

import net.minecraft.world.InteractionHand;

import java.util.ArrayList;
import java.util.List;

public class Idle extends Feature {
    public enum SpinMode {
        Server,
        Client
    }

    private final ValueGroup sgActions = settings.createGroup("Actions");
    private final ValueGroup sgMessages = settings.createGroup("Messages");

    public final Value<Boolean> jump = sgActions.add(new FlagValue.Builder()
        .name("jump")
        .description("Jumps periodically.")
        .defaultValue(true)
        .build()
    );

    public final Value<Boolean> swing = sgActions.add(new FlagValue.Builder()
        .name("swing")
        .description("Swings hand periodically.")
        .defaultValue(false)
        .build()
    );

    public final Value<Boolean> sneak = sgActions.add(new FlagValue.Builder()
        .name("sneak")
        .description("Sneaks periodically.")
        .defaultValue(false)
        .build()
    );

    public final Value<Integer> sneakTime = sgActions.add(new IntValue.Builder()
        .name("sneak-time")
        .description("Duration in ticks to hold sneak.")
        .defaultValue(5)
        .min(1)
        .max(60)
        .visible(sneak::get)
        .build()
    );

    public final Value<Boolean> strafe = sgActions.add(new FlagValue.Builder()
        .name("strafe")
        .description("Strafes slightly left and right.")
        .defaultValue(false)
        .build()
    );

    public final Value<Boolean> spin = sgActions.add(new FlagValue.Builder()
        .name("spin")
        .description("Continuously spins player head rotation.")
        .defaultValue(true)
        .build()
    );

    public final Value<SpinMode> spinMode = sgActions.add(new ChoiceValue.Builder<SpinMode>()
        .name("spin-mode")
        .description("Whether to rotate client camera or send server rotation packets.")
        .defaultValue(SpinMode.Server)
        .visible(spin::get)
        .build()
    );

    public final Value<Integer> speed = sgActions.add(new IntValue.Builder()
        .name("speed")
        .description("Head spin rotation speed per tick.")
        .defaultValue(7)
        .min(1)
        .max(30)
        .visible(spin::get)
        .build()
    );

    public final Value<Integer> pitch = sgActions.add(new IntValue.Builder()
        .name("pitch")
        .description("Fixed pitch angle while spinning.")
        .defaultValue(0)
        .min(-90)
        .max(90)
        .sliderRange(-90, 90)
        .visible(() -> spin.get() && spinMode.get() == SpinMode.Server)
        .build()
    );

    public final Value<Boolean> sendMessages = sgMessages.add(new FlagValue.Builder()
        .name("send-messages")
        .description("Periodically sends anti-AFK chat messages.")
        .defaultValue(false)
        .build()
    );

    public final Value<Boolean> random = sgMessages.add(new FlagValue.Builder()
        .name("random")
        .description("Selects random message from list instead of sequential.")
        .defaultValue(false)
        .visible(sendMessages::get)
        .build()
    );

    public final Value<Integer> delay = sgMessages.add(new IntValue.Builder()
        .name("delay")
        .description("Minutes between anti-AFK chat messages.")
        .defaultValue(15)
        .min(0)
        .max(30)
        .visible(sendMessages::get)
        .build()
    );

    public final Value<List<String>> messages = sgMessages.add(new TextListValue.Builder()
        .name("messages")
        .description("List of anti-AFK chat strings to broadcast.")
        .defaultValue(new ArrayList<>())
        .visible(sendMessages::get)
        .build()
    );

    private int timer = 0;
    private int messageTimer = 0;
    private float currentYaw = 0;

    public Idle() {
        super(Group.Utilities, "idle", "Prevents AFK timeout kicks by simulating player activity.");
    }

    @Listen
    private void onTick(ClientTick event) {
        if (mc.player == null) return;
        timer++;

        if (jump.get() && mc.player.onGround() && timer % 60 == 0) {
            ((LivingEntityMixin) mc.player).pancreas$jumpFromGround();
        }

        if (swing.get() && timer % 40 == 0) {
            mc.player.swing(InteractionHand.MAIN_HAND);
        }

        if (spin.get()) {
            currentYaw = (currentYaw + speed.get()) % 360;
            if (spinMode.get() == SpinMode.Server) {
                RotationKit.rotate(currentYaw, pitch.get());
            } else {
                mc.player.setYRot(currentYaw);
            }
        }

        if (sendMessages.get()) {
            messageTimer++;
            int targetTicks = Math.max(1, delay.get()) * 60 * 20;
            if (messageTimer >= targetTicks) {
                messageTimer = 0;
                List<String> list = messages.get();
                if (list.isEmpty()) {
                    Toasts.get().warning("IdleGuard", "Message list is empty.");
                } else {
                    int index = random.get() ? (int) (Math.random() * list.size()) : 0;
                    mc.player.connection.sendChat(list.get(index));
                }
            }
        }
    }
}