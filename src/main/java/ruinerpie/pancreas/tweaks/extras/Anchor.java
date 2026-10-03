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

import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;

public class Anchor extends Feature {
    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    public final Value<Boolean> preserveYaw = sgGeneral.add(new FlagValue.Builder()
        .name("preserve-yaw")
        .description("Keeps current player yaw when teleported or rotated by server.")
        .defaultValue(true)
        .build()
    );

    public final Value<Boolean> preservePitch = sgGeneral.add(new FlagValue.Builder()
        .name("preserve-pitch")
        .description("Keeps current player pitch when rotated by server.")
        .defaultValue(true)
        .build()
    );

    public Anchor() {
        super(Group.Extras, "anchor", "Prevents server from forcibly changing player rotation/look direction.");
    }

    @Listen
    private void onPacketReceive(PacketIn event) {
        if (mc.player == null) return;
        if (event.packet instanceof ClientboundPlayerPositionPacket) {
            
        }
    }
}