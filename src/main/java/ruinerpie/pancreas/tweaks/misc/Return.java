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

import net.minecraft.client.multiplayer.ServerData;

public class Return extends Feature {
    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    public final Value<Double> delay = sgGeneral.add(new DoubleValue.Builder()
        .name("delay")
        .description("Delay in seconds before reconnecting.")
        .defaultValue(4.0)
        .min(0.5)
        .max(30.0)
        .build()
    );

    public static ServerData lastServer = null;

    public Return() {
        super(Group.Misc, "return", "Automatically reconnects after server disconnect.");
    }

    @Listen
    private void onServerConnect(ServerConnectBegin event) {
        lastServer = event.address;
    }
}