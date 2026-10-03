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

import net.minecraft.world.entity.player.Player;

public class Greet extends Feature {
    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    public final Value<String> message = sgGeneral.add(new TextValue.Builder()
        .name("message")
        .description("The specified message sent to the player.")
        .defaultValue("Pancreas on Top!")
        .build()
    );

    public final Value<Boolean> ignoreFriends = sgGeneral.add(new FlagValue.Builder()
        .name("ignore-friends")
        .description("Will not send any messages to people friended.")
        .defaultValue(false)
        .build()
    );

    public Greet() {
        super(Group.Misc, "greet", "Sends a specified message to any player that enters render distance.");
    }

    @Listen
    private void onEntityAdded(EntityAdded event) {
        if (mc.player == null || mc.player.connection == null) return;
        if (!(event.entity instanceof Player player) || player.getUUID().equals(mc.player.getUUID())) return;

        if (!ignoreFriends.get() || !Team.get().isFriend(player.getUUID())) {
            mc.player.connection.sendCommand("msg " + player.getName().getString() + " " + message.get());
        }
    }
}