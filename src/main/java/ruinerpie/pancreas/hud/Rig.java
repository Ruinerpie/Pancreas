package ruinerpie.pancreas.hud;

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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Path;
import java.util.*;

public final class Rig {
    private static final Rig INSTANCE = new Rig();
    public static Rig get() { return INSTANCE; }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Path configFile = FabricLoader.getInstance().getConfigDir().resolve("pancreas").resolve("hud.json");

    private final Map<String, Part> elements = new LinkedHashMap<>();

    private Rig() {
        
        register(new ClientMark());
        register(new Coords());
        register(new ActiveList());
        register(new ArmorView());
        register(new InvPeek());
        register(new CombatView());
        register(new RadarText());
        register(new NotifStack());
        register(new KeybindList());
        register(new PotionList());
    }

    public void register(Part element) {
        elements.put(element.id, element);
    }

    public List<Part> all() {
        return new java.util.ArrayList<>(elements.values());
    }

    public Part get(String id) {
        return elements.get(id);
    }

    public void render(Context ctx) {
        if (ruinerpie.pancreas.theme.Prefs.get().hud.hideInDebug && ctx.mc.gui.getDebugOverlay().showDebugScreen()) {
            return;
        }

        for (Part el : elements.values()) {
            if (el.isVisible()) {
                int rx = el.getRenderX(ctx.screenWidth);
                int ry = el.getRenderY(ctx.screenHeight);
                int rw = (int) (el.getWidth() * el.scale);
                int rh = (int) (el.getHeight() * el.scale);

                if (rx + rw <= 0 || rx >= ctx.screenWidth || ry + rh <= 0 || ry >= ctx.screenHeight) {
                    continue;
                }

                try {
                    el.render(ctx);
                } catch (Exception e) {
                    
                }
            }
        }
    }

    public void load() {
        File file = configFile.toFile();
        if (!file.exists()) {
            save();
            return;
        }

        try (FileReader reader = new FileReader(file)) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            if (json.has("elements")) {
                JsonObject els = json.getAsJsonObject("elements");
                for (Map.Entry<String, com.google.gson.JsonElement> entry : els.entrySet()) {
                    Part el = get(entry.getKey());
                    if (el != null && entry.getValue().isJsonObject()) {
                        JsonObject obj = entry.getValue().getAsJsonObject();
                        if (obj.has("enabled")) el.enabled = obj.get("enabled").getAsBoolean();
                        if (obj.has("x")) el.x = obj.get("x").getAsInt();
                        if (obj.has("y")) el.y = obj.get("y").getAsInt();
                        if (obj.has("scale")) el.scale = obj.get("scale").getAsFloat();
                        if (obj.has("anchor")) {
                            try {
                                el.anchor = Part.Anchor.valueOf(obj.get("anchor").getAsString());
                            } catch (Exception ignored) {}
                        }
                    }
                }
            }
        } catch (Exception e) {
            Pancreas.LOG.warn("Failed to load hud.json", e);
        }
    }

    public void save() {
        try {
            File parent = configFile.toFile().getParentFile();
            if (parent != null && !parent.exists()) parent.mkdirs();

            JsonObject json = new JsonObject();
            json.addProperty("configVersion", 1);

            JsonObject els = new JsonObject();
            for (Part el : elements.values()) {
                JsonObject obj = new JsonObject();
                obj.addProperty("enabled", el.enabled);
                obj.addProperty("x", el.x);
                obj.addProperty("y", el.y);
                obj.addProperty("scale", el.scale);
                obj.addProperty("anchor", el.anchor.name());
                els.add(el.id, obj);
            }
            json.add("elements", els);

            try (FileWriter writer = new FileWriter(configFile.toFile())) {
                GSON.toJson(json, writer);
            }
        } catch (Exception e) {
            Pancreas.LOG.warn("Failed to save hud.json", e);
        }
    }
}