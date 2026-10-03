package ruinerpie.pancreas.tweaks.cheats;

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

import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;

public class Sustenance extends Feature {
    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    public final FlagValue sprint = sgGeneral.add(new FlagValue.Builder()
        .name("sprint")
        .description("Prevents sprint hunger exhaustion by canceling sprint packet notifications.")
        .defaultValue(true)
        .build());

    public final FlagValue onGround = sgGeneral.add(new FlagValue.Builder()
        .name("on-ground")
        .description("Spoofs on-ground state to reduce jumping hunger exhaustion.")
        .defaultValue(true)
        .build());

    public Sustenance() {
        super(Group.Cheats, "sustenance", "Reduces hunger depletion by spoofing movement packets. Note: server-detectable on strict anti-cheats.");
    }

    @Listen
    private void onSendPacket(PacketOut event) {
        if (sprint.get() && event.packet instanceof ServerboundPlayerCommandPacket p) {
            if (p.getAction() == ServerboundPlayerCommandPacket.Action.START_SPRINTING || p.getAction() == ServerboundPlayerCommandPacket.Action.STOP_SPRINTING) {
                event.cancel();
            }
        }
    }

    @Listen
    private void onSendMovementPackets(SendMovementPackets.Pre event) {
        
    }
}