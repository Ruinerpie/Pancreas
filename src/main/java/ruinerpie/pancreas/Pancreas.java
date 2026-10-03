package ruinerpie.pancreas;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import ruinerpie.pancreas.saved.Team;
import ruinerpie.pancreas.saved.Loadouts;
import ruinerpie.pancreas.saved.Macros;
import ruinerpie.pancreas.hud.Rig;
import ruinerpie.pancreas.screens.Home;

public class Pancreas implements ClientModInitializer {
    public static final String MOD_ID = "pancreas";
    public static final String NAME = "Pancreas";
    public static final String VERSION = "1.0.0";

    public static Pancreas INSTANCE;
    public static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(NAME);
    public static final Minecraft mc = Minecraft.getInstance();
    public static final Signals SIGNALS = Signals.get();
    public static final Signals EVENT_BUS = SIGNALS;

    public static Identifier identifier(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    @Override
    public void onInitializeClient() {
        INSTANCE = this;

        Team.get().init();
        Loadouts.get().init();
        Macros.get().init();
        Features.get().init();
        Vault.get().load();
        Rig.get().load();
    }

    public static void openGui() {
        mc.setScreen(new Home());
    }
}
