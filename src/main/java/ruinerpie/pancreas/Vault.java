package ruinerpie.pancreas;

import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;
import ruinerpie.pancreas.draw.Tint;
import ruinerpie.pancreas.values.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Path;
import java.util.Map;

public final class Vault {
    private static final Vault INSTANCE = new Vault();
    public static Vault get() { return INSTANCE; }

    public static final int CONFIG_VERSION = 1;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final Path configDir = FabricLoader.getInstance().getConfigDir().resolve("pancreas");

    private File getFile(String name) {
        if (!configDir.toFile().exists()) {
            configDir.toFile().mkdirs();
        }
        return configDir.resolve(name).toFile();
    }

    public void load() {
        loadModules();
        loadSettings();
        loadKeybinds();
        loadGui();

        save();
    }

    public void save() {
        saveModules();
        saveSettings();
        saveKeybinds();
        saveGui();
    }

    private void loadModules() {
        File file = getFile("modules.json");
        if (!file.exists()) return;

        try (FileReader reader = new FileReader(file)) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            if (json.has("modules")) {
                JsonObject mods = json.getAsJsonObject("modules");
                for (Map.Entry<String, JsonElement> entry : mods.entrySet()) {
                    Feature m = Features.get().get(entry.getKey());
                    if (m != null && entry.getValue().isJsonPrimitive()) {
                        boolean enabled = entry.getValue().getAsBoolean();
                        if (enabled && !m.isActive()) {
                            m.enable();
                        } else if (!enabled && m.isActive()) {
                            m.disable();
                        }
                    }
                }
            }
        } catch (Exception e) {
            Pancreas.LOG.warn("Failed to load modules.json, falling back to defaults.", e);
        }
    }

    private void saveModules() {
        File file = getFile("modules.json");
        JsonObject json = new JsonObject();
        json.addProperty("configVersion", CONFIG_VERSION);

        JsonObject mods = new JsonObject();
        for (Feature m : Features.get().all()) {
            mods.addProperty(m.id, m.isActive());
        }
        json.add("modules", mods);

        try (FileWriter writer = new FileWriter(file)) {
            GSON.toJson(json, writer);
        } catch (Exception e) {
            Pancreas.LOG.warn("Failed to save modules.json", e);
        }
    }

    private void loadSettings() {
        File file = getFile("settings.json");
        if (!file.exists()) return;

        try (FileReader reader = new FileReader(file)) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            if (json.has("settings")) {
                JsonObject mods = json.getAsJsonObject("settings");
                for (Map.Entry<String, JsonElement> entry : mods.entrySet()) {
                    Feature m = Features.get().get(entry.getKey());
                    if (m != null && entry.getValue().isJsonObject()) {
                        JsonObject sObj = entry.getValue().getAsJsonObject();
                        for (ValueGroup group : m.settings.groups()) {
                            for (Value<?> setting : group.getSettings()) {
                                if (sObj.has(setting.name)) {
                                    applySettingValue(setting, sObj.get(setting.name));
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Pancreas.LOG.warn("Failed to load settings.json, falling back to defaults.", e);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void applySettingValue(Value<?> setting, JsonElement elem) {
        try {
            if (setting instanceof FlagValue bs && elem.isJsonPrimitive()) {
                bs.set(elem.getAsBoolean());
            } else if (setting instanceof IntValue is && elem.isJsonPrimitive()) {
                int val = elem.getAsInt();
                if (val >= is.min && val <= is.max) {
                    is.set(val);
                }
            } else if (setting instanceof DoubleValue ds && elem.isJsonPrimitive()) {
                double val = elem.getAsDouble();
                if (val >= ds.min && val <= ds.max) {
                    ds.set(val);
                }
            } else if (setting instanceof TextValue ss && elem.isJsonPrimitive()) {
                ss.set(elem.getAsString());
            } else if (setting instanceof ChoiceValue cv && elem.isJsonPrimitive()) {
                String str = elem.getAsString();
                if (cv.get() != null) {
                    Class<?> enumClass = ((Enum<?>) cv.get()).getDeclaringClass();
                    Object[] constants = enumClass.getEnumConstants();
                    if (constants != null) {
                        for (Object c : constants) {
                            if (c instanceof Enum<?> e && e.name().equalsIgnoreCase(str)) {
                                cv.set(e);
                                break;
                            }
                        }
                    }
                }
            } else if (setting instanceof TintValue tv && elem.isJsonPrimitive()) {
                tv.set(new Tint(elem.getAsInt()));
            }
        } catch (Exception ignored) {}
    }

    private void saveSettings() {
        File file = getFile("settings.json");
        JsonObject json = new JsonObject();
        json.addProperty("configVersion", CONFIG_VERSION);

        JsonObject modsObj = new JsonObject();
        for (Feature m : Features.get().all()) {
            JsonObject sObj = new JsonObject();
            for (ValueGroup group : m.settings.groups()) {
                for (Value<?> s : group.getSettings()) {
                    if (s.get() instanceof Boolean b) sObj.addProperty(s.name, b);
                    else if (s.get() instanceof Number n) sObj.addProperty(s.name, n);
                    else if (s.get() instanceof String str) sObj.addProperty(s.name, str);
                    else if (s.get() instanceof Enum<?> e) sObj.addProperty(s.name, e.name());
                    else if (s.get() instanceof Tint t) sObj.addProperty(s.name, t.getPacked());
                }
            }
            if (sObj.size() > 0) {
                modsObj.add(m.id, sObj);
            }
        }
        json.add("settings", modsObj);

        try (FileWriter writer = new FileWriter(file)) {
            GSON.toJson(json, writer);
        } catch (Exception e) {
            Pancreas.LOG.warn("Failed to save settings.json", e);
        }
    }

    private void loadKeybinds() {
        File file = getFile("keybinds.json");
        if (!file.exists()) return;

        try (FileReader reader = new FileReader(file)) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            if (json.has("keybinds")) {
                JsonObject binds = json.getAsJsonObject("keybinds");
                for (Map.Entry<String, JsonElement> entry : binds.entrySet()) {
                    Feature m = Features.get().get(entry.getKey());
                    if (m != null && entry.getValue().isJsonPrimitive()) {
                        m.setKeybind(entry.getValue().getAsInt());
                    }
                }
            }
        } catch (Exception e) {
            Pancreas.LOG.warn("Failed to load keybinds.json, falling back to defaults.", e);
        }
    }

    private void saveKeybinds() {
        File file = getFile("keybinds.json");
        JsonObject json = new JsonObject();
        json.addProperty("configVersion", CONFIG_VERSION);

        JsonObject binds = new JsonObject();
        for (Feature m : Features.get().all()) {
            binds.addProperty(m.id, m.getKeybind());
        }
        json.add("keybinds", binds);

        try (FileWriter writer = new FileWriter(file)) {
            GSON.toJson(json, writer);
        } catch (Exception e) {
            Pancreas.LOG.warn("Failed to save keybinds.json", e);
        }
    }

    private void loadGui() {
        ruinerpie.pancreas.theme.Prefs.get().load();
    }

    private void saveGui() {
        ruinerpie.pancreas.theme.Prefs.get().save();
    }
}