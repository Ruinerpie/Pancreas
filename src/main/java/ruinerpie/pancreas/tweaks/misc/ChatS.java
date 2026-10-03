package ruinerpie.pancreas.tweaks.misc;

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

public class ChatS extends Feature {
    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    public final Value<String> message = sgGeneral.add(new TextValue.Builder()
        .name("message")
        .description("Message to send.")
        .defaultValue("Pancreas on Top!")
        .build()
    );

    public final Value<Integer> delay = sgGeneral.add(new IntValue.Builder()
        .name("delay")
        .description("Delay in ticks between messages.")
        .defaultValue(100)
        .min(1)
        .max(1200)
        .build()
    );

    private int timer = 0;

    public ChatS() {
        super(Group.Misc, "repeater", "Automates repeating chat messages on a timer.");
    }

    @Override
    protected void onActivate() {
        timer = 0;
    }

    @Listen
    private void onTick(WorldTick event) {
        if (mc.player == null || mc.player.connection == null) return;

        if (timer <= 0) {
            String msg = message.get();
            if (msg != null && !msg.isEmpty()) {
                if (msg.startsWith("/")) {
                    mc.player.connection.sendCommand(msg.substring(1));
                } else {
                    mc.player.connection.sendChat(msg);
                }
            }
            timer = delay.get();
        } else {
            timer--;
        }
    }
}