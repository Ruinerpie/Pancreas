package ruinerpie.pancreas;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import ruinerpie.pancreas.draw.Color;
import ruinerpie.pancreas.screens.Home;
import ruinerpie.pancreas.values.*;

import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public final class Config {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_DIR = FabricLoader.getInstance().getConfigDir();
    private static final Path CONFIG_PATH = CONFIG_DIR.resolve("pancreas.json");
    private static final Path TEMP_PATH = CONFIG_DIR.resolve("pancreas.json.tmp");

    private Config() {}

    public static synchronized void load() {
        try {
            if (!Files.exists(CONFIG_PATH)) {
                save();
                return;
            }

            try (Reader reader = Files.newBufferedReader(CONFIG_PATH, StandardCharsets.UTF_8)) {
                JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
                if (root == null) return;

                int version = root.has("version") ? root.get("version").getAsInt() : 1;

                if (root.has("gui")) {
                    JsonObject gui = root.getAsJsonObject("gui");
                    if (gui.has("transparency")) {
                        double trans = gui.get("transparency").getAsDouble();
                        if (trans == 100.0) trans = 30.0;
                        Home.bgTransparency = trans;
                    } else {
                        Home.bgTransparency = 30.0;
                    }
                    if (gui.has("font")) {
                        try {
                            Home.guiFontChoice.set(Home.FontChoice.valueOf(gui.get("font").getAsString()));
                        } catch (Exception ignored) {}
                    }
                    if (gui.has("text_target")) {
                        try {
                            Home.guiTextChoice.set(Home.TextTarget.valueOf(gui.get("text_target").getAsString()));
                        } catch (Exception ignored) {}
                    }
                    if (gui.has("tab_target")) {
                        try {
                            Home.guiTabChoice.set(Home.TabTarget.valueOf(gui.get("tab_target").getAsString()));
                        } catch (Exception ignored) {}
                    }
                    if (gui.has("element_target")) {
                        try {
                            Home.guiElementChoice.set(Home.ElementTarget.valueOf(gui.get("element_target").getAsString()));
                        } catch (Exception ignored) {}
                    }
                    if (gui.has("button_target")) {
                        try {
                            Home.guiButtonChoice.set(Home.ButtonTarget.valueOf(gui.get("button_target").getAsString()));
                        } catch (Exception ignored) {}
                    }
                    if (gui.has("picker_mode")) {
                        try {
                            Home.guiPickerMode.set(Home.PickerMode.valueOf(gui.get("picker_mode").getAsString()));
                        } catch (Exception ignored) {}
                    }
                    if (version >= 2 && gui.has("colors")) {
                        JsonObject colors = gui.getAsJsonObject("colors");
                        loadColor(colors, "panel_bg", Home.BG_MAIN);
                        loadColor(colors, "header_bg", Home.BG_HEAD);
                        loadColor(colors, "border_main", Home.BORDER_MAIN);
                        loadColor(colors, "border_tabs", Home.BORDER_TABS);
                        loadColor(colors, "border_buttons", Home.BORDER_BUTTONS);
                        loadColor(colors, "search_bg", Home.SEARCH_BG);
                        loadColor(colors, "search_border", Home.SEARCH_BORDER);

                        loadColor(colors, "text_selected", Home.TEXT_SELECTED);
                        loadColor(colors, "text_hovered", Home.TEXT_HOVERED);
                        loadColor(colors, "text_normal", Home.TEXT_NORMAL);
                        loadColor(colors, "text_other", Home.TEXT_OTHER);

                        loadColor(colors, "tab_selected_bg", Home.BG_TAB_SELECTED);
                        loadColor(colors, "tab_hovered_bg", Home.BG_TAB_HOVERED);
                        loadColor(colors, "tab_normal_bg", Home.BG_TAB_NORMAL);

                        loadColor(colors, "element_selected_bg", Home.BG_ELEMENT_SELECTED);
                        loadColor(colors, "element_hovered_bg", Home.BG_ELEMENT_HOVERED);
                        loadColor(colors, "element_normal_bg", Home.BG_ELEMENT_NORMAL);

                        loadColor(colors, "button_selected_bg", Home.BG_BUTTON_SELECTED);
                        loadColor(colors, "button_hovered_bg", Home.BG_BUTTON_HOVERED);
                        loadColor(colors, "button_normal_bg", Home.BG_BUTTON_NORMAL);
                        loadColor(colors, "button_other_bg", Home.BG_BUTTON_OTHER);

                        loadColor(colors, "toggle_on", Home.TOGGLE_ON);
                        loadColor(colors, "toggle_off", Home.TOGGLE_OFF);
                        loadColor(colors, "hover_row", Home.HOVER_ROW);
                        if (Home.BG_MAIN.r == 20 && Home.BG_MAIN.g == 23 && Home.BG_MAIN.b == 31) {
                            Home.BG_MAIN.set(new Color(0, 0, 0, 255));
                        }
                        if (Home.BG_HEAD.r == 26 && Home.BG_HEAD.g == 30 && Home.BG_HEAD.b == 42) {
                            Home.BG_HEAD.set(new Color(15, 0, 32, 255));
                        }
                        if (Home.BORDER_MAIN.r == 35 && Home.BORDER_MAIN.g == 40 && Home.BORDER_MAIN.b == 54) {
                            Home.BORDER_MAIN.set(new Color(37, 0, 46, 255));
                        }
                        if (Home.SEARCH_BG.r == 15 && Home.SEARCH_BG.g == 17 && Home.SEARCH_BG.b == 24) {
                            Home.SEARCH_BG.set(new Color(15, 0, 32, 255));
                        }
                        if (Home.SEARCH_BORDER.r == 45 && Home.SEARCH_BORDER.g == 52 && Home.SEARCH_BORDER.b == 70) {
                            Home.SEARCH_BORDER.set(new Color(37, 0, 46, 255));
                        }
                        if (Home.TEXT_SELECTED.r == 255 && Home.TEXT_SELECTED.g == 255 && Home.TEXT_SELECTED.b == 255) {
                            Home.TEXT_SELECTED.set(new Color(255, 0, 0, 255));
                        }
                        if (Home.BG_TAB_NORMAL.r == 25 && Home.BG_TAB_NORMAL.g == 0 && Home.BG_TAB_NORMAL.b == 0) {
                            Home.BG_TAB_NORMAL.set(new Color(37, 0, 0, 255));
                        }
                        if (Home.BG_ELEMENT_NORMAL.r == 18 && Home.BG_ELEMENT_NORMAL.g == 0 && Home.BG_ELEMENT_NORMAL.b == 22) {
                            Home.BG_ELEMENT_NORMAL.set(new Color(27, 0, 32, 255));
                        }
                    }
                    if (gui.has("favorites")) {
                        JsonArray favs = gui.getAsJsonArray("favorites");
                        Home.savedFavorites.clear();
                        for (JsonElement elem : favs) {
                            Home.savedFavorites.add(elem.getAsString());
                        }
                    }
                    if (gui.has("favorite_settings")) {
                        JsonArray favSettings = gui.getAsJsonArray("favorite_settings");
                        Home.savedFavoriteSettings.clear();
                        for (JsonElement elem : favSettings) {
                            String s = elem.getAsString();
                            if ("Transparent GUI".equals(s)) s = "GUI Opacity";
                            if (!Home.savedFavoriteSettings.contains(s)) {
                                Home.savedFavoriteSettings.add(s);
                            }
                        }
                    }
                }

                if (root.has("features")) {
                    JsonObject featuresObj = root.getAsJsonObject("features");
                    for (Feature f : Features.get().all()) {
                        String fid = featuresObj.has(f.id) ? f.id : (f.id.equals("edge-shift") && featuresObj.has("safe-edge") ? "safe-edge" : null);
                        if (fid != null) {
                            JsonObject fObj = featuresObj.getAsJsonObject(fid);
                            if (fObj.has("active")) {
                                f.setActive(fObj.get("active").getAsBoolean());
                            }
                            if (fObj.has("keybind")) {
                                f.setKeybind(fObj.get("keybind").getAsInt());
                            }
                            if (fObj.has("settings")) {
                                JsonObject sObj = fObj.getAsJsonObject("settings");
                                for (ValueGroup vg : f.settings.groups()) {
                                    for (Value<?> val : vg.getSettings()) {
                                        if (sObj.has(val.getName())) {
                                            loadValue(val, sObj.get(val.getName()));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Pancreas.LOG.info("Configuration loaded from {}", CONFIG_PATH);
        } catch (Exception e) {
            Pancreas.LOG.error("Failed to load configuration", e);
        }
    }

    public static synchronized void save() {
        try {
            Files.createDirectories(CONFIG_DIR);

            JsonObject root = new JsonObject();
            root.addProperty("version", 2);

            JsonObject gui = new JsonObject();
            gui.addProperty("transparency", Home.bgTransparency);
            gui.addProperty("font", Home.guiFontChoice.get().name());
            gui.addProperty("text_target", Home.guiTextChoice.get().name());
            gui.addProperty("tab_target", Home.guiTabChoice.get().name());
            gui.addProperty("element_target", Home.guiElementChoice.get().name());
            gui.addProperty("button_target", Home.guiButtonChoice.get().name());
            gui.addProperty("picker_mode", Home.guiPickerMode.get().name());

            JsonObject colors = new JsonObject();
            saveColor(colors, "panel_bg", Home.BG_MAIN);
            saveColor(colors, "header_bg", Home.BG_HEAD);
            saveColor(colors, "border_main", Home.BORDER_MAIN);
            saveColor(colors, "border_tabs", Home.BORDER_TABS);
            saveColor(colors, "border_buttons", Home.BORDER_BUTTONS);
            saveColor(colors, "search_bg", Home.SEARCH_BG);
            saveColor(colors, "search_border", Home.SEARCH_BORDER);

            saveColor(colors, "text_selected", Home.TEXT_SELECTED);
            saveColor(colors, "text_hovered", Home.TEXT_HOVERED);
            saveColor(colors, "text_normal", Home.TEXT_NORMAL);
            saveColor(colors, "text_other", Home.TEXT_OTHER);
            saveColor(colors, "text_primary", Home.TEXT_NORMAL);
            saveColor(colors, "text_muted", Home.TEXT_OTHER);
            saveColor(colors, "accent_blue", Home.TEXT_SELECTED);

            saveColor(colors, "tab_selected_bg", Home.BG_TAB_SELECTED);
            saveColor(colors, "tab_hovered_bg", Home.BG_TAB_HOVERED);
            saveColor(colors, "tab_normal_bg", Home.BG_TAB_NORMAL);
            saveColor(colors, "tab_active_bg", Home.BG_TAB_SELECTED);
            saveColor(colors, "tab_inactive_bg", Home.BG_TAB_NORMAL);

            saveColor(colors, "element_selected_bg", Home.BG_ELEMENT_SELECTED);
            saveColor(colors, "element_hovered_bg", Home.BG_ELEMENT_HOVERED);
            saveColor(colors, "element_normal_bg", Home.BG_ELEMENT_NORMAL);

            saveColor(colors, "button_selected_bg", Home.BG_BUTTON_SELECTED);
            saveColor(colors, "button_hovered_bg", Home.BG_BUTTON_HOVERED);
            saveColor(colors, "button_normal_bg", Home.BG_BUTTON_NORMAL);
            saveColor(colors, "button_other_bg", Home.BG_BUTTON_OTHER);

            saveColor(colors, "toggle_on", Home.TOGGLE_ON);
            saveColor(colors, "toggle_off", Home.TOGGLE_OFF);
            saveColor(colors, "hover_row", Home.HOVER_ROW);
            gui.add("colors", colors);

            JsonArray favs = new JsonArray();
            for (String fid : Home.savedFavorites) {
                favs.add(fid);
            }
            gui.add("favorites", favs);

            JsonArray favSettings = new JsonArray();
            for (String sName : Home.savedFavoriteSettings) {
                favSettings.add(sName);
            }
            gui.add("favorite_settings", favSettings);

            root.add("gui", gui);

            JsonObject featuresObj = new JsonObject();
            for (Feature f : Features.get().all()) {
                JsonObject fObj = new JsonObject();
                fObj.addProperty("active", f.isActive());
                fObj.addProperty("keybind", f.getKeybind());

                JsonObject sObj = new JsonObject();
                for (ValueGroup vg : f.settings.groups()) {
                    for (Value<?> val : vg.getSettings()) {
                        saveValue(sObj, val);
                    }
                }
                fObj.add("settings", sObj);
                featuresObj.add(f.id, fObj);
            }
            root.add("features", featuresObj);

            try (Writer writer = Files.newBufferedWriter(TEMP_PATH, StandardCharsets.UTF_8)) {
                GSON.toJson(root, writer);
            }
            try {
                Files.move(TEMP_PATH, CONFIG_PATH, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (Exception atomicEx) {
                Files.move(TEMP_PATH, CONFIG_PATH, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            Pancreas.LOG.error("Failed to save configuration", e);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void loadValue(Value<?> val, JsonElement elem) {
        try {
            if (val instanceof ChoiceValue choice) {
                String str = elem.getAsString();
                Enum<?> def = (Enum<?>) choice.getDefaultValue();
                for (Enum<?> constant : def.getDeclaringClass().getEnumConstants()) {
                    if (constant.name().equalsIgnoreCase(str) || constant.toString().equalsIgnoreCase(str)) {
                        choice.set(constant);
                        break;
                    }
                }
            } else if (val instanceof FlagValue flag) {
                flag.set(elem.getAsBoolean());
            } else if (val instanceof IntValue intVal) {
                intVal.set(elem.getAsInt());
            } else if (val instanceof DoubleValue doubleVal) {
                doubleVal.set(elem.getAsDouble());
            } else if (val instanceof TextValue textVal) {
                textVal.set(elem.getAsString());
            }
        } catch (Exception ignored) {}
    }

    private static void saveValue(JsonObject obj, Value<?> val) {
        if (val instanceof ChoiceValue<?> choice) {
            obj.addProperty(val.getName(), choice.get().name());
        } else if (val instanceof FlagValue flag) {
            obj.addProperty(val.getName(), flag.get());
        } else if (val instanceof IntValue intVal) {
            obj.addProperty(val.getName(), intVal.get());
        } else if (val instanceof DoubleValue doubleVal) {
            obj.addProperty(val.getName(), doubleVal.get());
        } else if (val instanceof TextValue textVal) {
            obj.addProperty(val.getName(), textVal.get());
        }
    }

    private static void saveColor(JsonObject parent, String key, Color color) {
        if (color == null) return;
        JsonObject c = new JsonObject();
        c.addProperty("r", color.r);
        c.addProperty("g", color.g);
        c.addProperty("b", color.b);
        c.addProperty("a", color.a);
        parent.add(key, c);
    }

    private static void loadColor(JsonObject parent, String key, Color color) {
        if (parent.has(key) && color != null) {
            JsonObject c = parent.getAsJsonObject(key);
            if (c.has("r")) color.r = c.get("r").getAsInt();
            if (c.has("g")) color.g = c.get("g").getAsInt();
            if (c.has("b")) color.b = c.get("b").getAsInt();
            if (c.has("a")) color.a = c.get("a").getAsInt();
        }
    }
}
