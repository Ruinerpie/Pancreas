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

import java.util.ArrayList;

public class Filter extends Feature {
    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    public final PacketListValue s2cCategories = sgGeneral.add(new PacketListValue.Builder()
        .name("s2c-categories")
        .description("Server-to-client packet categories to drop.")
        .defaultValue(new ArrayList<>())
        .build());

    public final PacketListValue c2sCategories = sgGeneral.add(new PacketListValue.Builder()
        .name("c2s-categories")
        .description("Client-to-server packet categories to drop.")
        .defaultValue(new ArrayList<>())
        .build());

    public Filter() {
        super(Group.Misc, "filter", "Selectively drop incoming or outgoing network packets by category.");
        this.runInMainMenu = true;
    }

    @Listen(priority = Order.HIGHEST)
    private void onReceivePacket(PacketIn event) {
        PacketKind cat = PacketTag.getCategory(event.packet);
        if (cat != null && s2cCategories.get().contains(cat)) {
            event.cancel();
        }
    }

    @Listen(priority = Order.HIGHEST)
    private void onSendPacket(PacketOut event) {
        PacketKind cat = PacketTag.getCategory(event.packet);
        if (cat != null && c2sCategories.get().contains(cat)) {
            event.cancel();
        }
    }
}