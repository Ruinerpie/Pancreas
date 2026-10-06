package ruinerpie.pancreas;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;
import ruinerpie.pancreas.screens.Home;

public class Pancreas implements ClientModInitializer {
    public static final String MOD_ID = "pancreas";
    public static final String NAME = "Pancreas";
    public static final String VERSION = "1.0.0";

    public static Pancreas INSTANCE;
    public static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(NAME);
    public static final Signals SIGNALS = Signals.get();
    public static final Signals EVENT_BUS = SIGNALS;

    public static final KeyMapping.Category CATEGORY_PANCREAS = KeyMapping.Category.register(Identifier.fromNamespaceAndPath(MOD_ID, "main"));
    public static final KeyMapping KEY_CONFIG = new KeyMapping("key.pancreas.config", GLFW.GLFW_KEY_P, CATEGORY_PANCREAS);

    public static Identifier identifier(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    @Override
    public void onInitializeClient() {
        INSTANCE = this;
        Feature.mc = Minecraft.getInstance();
        Features.get().init();
        Config.load();
        Runtime.getRuntime().addShutdownHook(new Thread(Config::save));
    }

    public static void openGui() {
        Minecraft client = Minecraft.getInstance();
        if (client != null) {
            Feature.mc = client;
            client.execute(() -> client.setScreen(new Home()));
        }
    }
}
