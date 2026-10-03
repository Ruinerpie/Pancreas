package ruinerpie.pancreas.theme;

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

public final class Prefs {
    private static final Prefs INSTANCE = new Prefs();
    public static Prefs get() { return INSTANCE; }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Path configFile = FabricLoader.getInstance().getConfigDir().resolve("pancreas").resolve("gui.json");

    public int configVersion = 1;

    public static class ThemeConfig {
        public String preset = "stone";
        public String accent = "#E53935";
        public int backgroundOpacity = 235;
        public int borderThickness = 2;
    }

    public static class TypographyConfig {
        public String fontFamily = "minecraft";
        public float scale = 1.0f;
    }

    public static class MotionConfig {
        public boolean enabled = true;
        public float speed = 1.0f;
        public boolean soundEnabled = true;
    }

    public static class LayoutConfig {
        public String density = "comfortable"; 
        public boolean showIcons = true;
    }

    public static class AccessibilityConfig {
        public boolean reduceMotion = false;
        public boolean highContrast = false;
    }

    public static class HudConfig {
        public float globalScale = 1.0f;
        public int bgOpacity = 235;
        public boolean hideInDebug = false;
        public boolean showGrid = true;
    }

    public ThemeConfig theme = new ThemeConfig();
    public TypographyConfig typography = new TypographyConfig();
    public MotionConfig motion = new MotionConfig();
    public LayoutConfig layout = new LayoutConfig();
    public AccessibilityConfig accessibility = new AccessibilityConfig();
    public HudConfig hud = new HudConfig();

    public void load() {
        File file = configFile.toFile();
        if (!file.exists()) {
            apply();
            save();
            return;
        }

        try (FileReader reader = new FileReader(file)) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            if (json.has("configVersion")) configVersion = json.get("configVersion").getAsInt();

            if (json.has("theme")) {
                JsonObject t = json.getAsJsonObject("theme");
                if (t.has("preset")) theme.preset = t.get("preset").getAsString();
                if (t.has("accent")) theme.accent = t.get("accent").getAsString();
                if (t.has("backgroundOpacity")) theme.backgroundOpacity = t.get("backgroundOpacity").getAsInt();
                if (t.has("borderThickness")) theme.borderThickness = t.get("borderThickness").getAsInt();
            }

            if (json.has("typography")) {
                JsonObject typ = json.getAsJsonObject("typography");
                if (typ.has("fontFamily")) typography.fontFamily = typ.get("fontFamily").getAsString();
                if (typ.has("scale")) typography.scale = typ.get("scale").getAsFloat();
            }

            if (json.has("motion")) {
                JsonObject m = json.getAsJsonObject("motion");
                if (m.has("enabled")) motion.enabled = m.get("enabled").getAsBoolean();
                if (m.has("speed")) motion.speed = m.get("speed").getAsFloat();
                if (m.has("soundEnabled")) motion.soundEnabled = m.get("soundEnabled").getAsBoolean();
            }

            if (json.has("layout")) {
                JsonObject l = json.getAsJsonObject("layout");
                if (l.has("density")) layout.density = l.get("density").getAsString();
                if (l.has("showIcons")) layout.showIcons = l.get("showIcons").getAsBoolean();
            }

            if (json.has("accessibility")) {
                JsonObject a = json.getAsJsonObject("accessibility");
                if (a.has("reduceMotion")) accessibility.reduceMotion = a.get("reduceMotion").getAsBoolean();
                if (a.has("highContrast")) accessibility.highContrast = a.get("highContrast").getAsBoolean();
            }

            if (json.has("hud")) {
                JsonObject h = json.getAsJsonObject("hud");
                if (h.has("globalScale")) hud.globalScale = h.get("globalScale").getAsFloat();
                if (h.has("bgOpacity")) hud.bgOpacity = h.get("bgOpacity").getAsInt();
                if (h.has("hideInDebug")) hud.hideInDebug = h.get("hideInDebug").getAsBoolean();
                if (h.has("showGrid")) hud.showGrid = h.get("showGrid").getAsBoolean();
            }
        } catch (Exception e) {
            Pancreas.LOG.warn("Failed to parse gui.json, restoring defaults silently.");
            resetToDefaults();
        }

        apply();
    }

    public void save() {
        try {
            File parent = configFile.toFile().getParentFile();
            if (parent != null && !parent.exists()) parent.mkdirs();

            JsonObject json = new JsonObject();
            json.addProperty("configVersion", configVersion);

            JsonObject t = new JsonObject();
            t.addProperty("preset", theme.preset);
            t.addProperty("accent", theme.accent);
            t.addProperty("backgroundOpacity", theme.backgroundOpacity);
            t.addProperty("borderThickness", theme.borderThickness);
            json.add("theme", t);

            JsonObject typ = new JsonObject();
            typ.addProperty("fontFamily", typography.fontFamily);
            typ.addProperty("scale", typography.scale);
            json.add("typography", typ);

            JsonObject m = new JsonObject();
            m.addProperty("enabled", motion.enabled);
            m.addProperty("speed", motion.speed);
            m.addProperty("soundEnabled", motion.soundEnabled);
            json.add("motion", m);

            JsonObject l = new JsonObject();
            l.addProperty("density", layout.density);
            l.addProperty("showIcons", layout.showIcons);
            json.add("layout", l);

            JsonObject a = new JsonObject();
            a.addProperty("reduceMotion", accessibility.reduceMotion);
            a.addProperty("highContrast", accessibility.highContrast);
            json.add("accessibility", a);

            JsonObject h = new JsonObject();
            h.addProperty("globalScale", hud.globalScale);
            h.addProperty("bgOpacity", hud.bgOpacity);
            h.addProperty("hideInDebug", hud.hideInDebug);
            h.addProperty("showGrid", hud.showGrid);
            json.add("hud", h);

            try (FileWriter writer = new FileWriter(configFile.toFile())) {
                GSON.toJson(json, writer);
            }
        } catch (Exception e) {
            Pancreas.LOG.warn("Failed to save gui.json", e);
        }
    }

    public void apply() {
        
        switch (theme.preset.toLowerCase()) {
            case "deepslate" -> {
                Theme.background.set(new Color(0x11, 0x11, 0x14, theme.backgroundOpacity));
                Theme.surface.set(new Color(0x1E, 0x1E, 0x22, 255));
                Theme.surfaceAlt.set(new Color(0x26, 0x26, 0x2B, 255));
                Theme.surfaceRaised.set(new Color(0x30, 0x30, 0x36, 255));
                Theme.border.set(new Color(0x0B, 0x0B, 0x0E, 255));
                Theme.borderLight.set(new Color(0x3C, 0x3C, 0x44, 255));
            }
            case "forest" -> {
                Theme.background.set(new Color(0x15, 0x1B, 0x15, theme.backgroundOpacity));
                Theme.surface.set(new Color(0x22, 0x2D, 0x22, 255));
                Theme.surfaceAlt.set(new Color(0x2B, 0x38, 0x2B, 255));
                Theme.surfaceRaised.set(new Color(0x36, 0x45, 0x36, 255));
                Theme.border.set(new Color(0x10, 0x16, 0x10, 255));
                Theme.borderLight.set(new Color(0x42, 0x57, 0x42, 255));
            }
            case "nether" -> {
                Theme.background.set(new Color(0x1E, 0x15, 0x15, theme.backgroundOpacity));
                Theme.surface.set(new Color(0x30, 0x20, 0x20, 255));
                Theme.surfaceAlt.set(new Color(0x3B, 0x28, 0x28, 255));
                Theme.surfaceRaised.set(new Color(0x47, 0x30, 0x30, 255));
                Theme.border.set(new Color(0x16, 0x0E, 0x0E, 255));
                Theme.borderLight.set(new Color(0x59, 0x3A, 0x3A, 255));
            }
            case "end" -> {
                Theme.background.set(new Color(0x18, 0x15, 0x1E, theme.backgroundOpacity));
                Theme.surface.set(new Color(0x27, 0x20, 0x30, 255));
                Theme.surfaceAlt.set(new Color(0x32, 0x29, 0x3E, 255));
                Theme.surfaceRaised.set(new Color(0x3E, 0x33, 0x4D, 255));
                Theme.border.set(new Color(0x12, 0x0E, 0x16, 255));
                Theme.borderLight.set(new Color(0x4E, 0x3E, 0x61, 255));
            }
            case "custom" -> {
                
                Theme.background.a(theme.backgroundOpacity);
            }
            default -> { 
                Theme.background.set(new Color(0x1A, 0x1A, 0x1E, theme.backgroundOpacity));
                Theme.surface.set(new Color(0x2B, 0x2B, 0x30, 255));
                Theme.surfaceAlt.set(new Color(0x35, 0x35, 0x3B, 255));
                Theme.surfaceRaised.set(new Color(0x3E, 0x3E, 0x44, 255));
                Theme.border.set(new Color(0x14, 0x14, 0x1A, 255));
                Theme.borderLight.set(new Color(0x4A, 0x4A, 0x52, 255));
            }
        }

        try {
            int parsed = parseHexColor(theme.accent);
            Theme.accentRed.set(new Color(parsed));
            Theme.categoryCheats.set(new Color(parsed));
            Theme.error.set(new Color(parsed));
        } catch (Exception ignored) {}

        if (accessibility.highContrast) {
            Theme.text.set(new Color(0xFF, 0xFF, 0xFF, 255));
            Theme.textAccent.set(new Color(0xFF, 0xFF, 0xFF, 255));
            Theme.textMuted.set(new Color(0xEE, 0xEE, 0xEE, 255));
            Theme.border.set(new Color(0x00, 0x00, 0x00, 255));
            Theme.borderLight.set(new Color(0xFF, 0xFF, 0xFF, 255));
        } else {
            Theme.text.set(new Color(0xE8, 0xE4, 0xDA, 255));
            Theme.textAccent.set(new Color(0xFF, 0xF4, 0xD6, 255));
            Theme.textMuted.set(new Color(0xA8, 0xA2, 0x9A, 255));
        }
    }

    public void resetToDefaults() {
        configVersion = 1;
        theme.preset = "stone";
        theme.accent = "#E53935";
        theme.backgroundOpacity = 235;
        theme.borderThickness = 2;
        typography.fontFamily = "minecraft";
        typography.scale = 1.0f;
        motion.enabled = true;
        motion.speed = 1.0f;
        motion.soundEnabled = true;
        layout.density = "comfortable";
        layout.showIcons = true;
        accessibility.reduceMotion = false;
        accessibility.highContrast = false;
        hud.globalScale = 1.0f;
        hud.bgOpacity = 235;
        hud.hideInDebug = false;
        hud.showGrid = true;
        apply();
    }

    public static int parseHexColor(String hex) {
        String clean = hex.startsWith("#") ? hex.substring(1) : hex;
        if (clean.length() == 6) {
            int rgb = Integer.parseInt(clean, 16);
            return 0xFF000000 | rgb;
        } else if (clean.length() == 8) {
            return (int) Long.parseLong(clean, 16);
        }
        return 0xFFE53935;
    }
}