package ruinerpie.pancreas.saved;

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

import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Path;
import java.util.*;

public final class Loadouts {
    private static final Loadouts INSTANCE = new Loadouts();
    public static Loadouts get() { return INSTANCE; }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Path loadoutsDir = FabricLoader.getInstance().getConfigDir().resolve("pancreas").resolve("loadouts");
    private final Map<String, Loadout> loadouts = new LinkedHashMap<>();

    private Loadouts() {}

    public void init() {
        loadAll();
    }

    public List<Loadout> all() {
        return new ArrayList<>(loadouts.values());
    }

    public Loadout get(String name) {
        if (name == null) return null;
        for (Loadout l : loadouts.values()) {
            if (l.name.equalsIgnoreCase(name)) return l;
        }
        return null;
    }

    public void save(String name) {
        if (name == null || name.trim().isEmpty()) return;
        name = name.trim();

        Map<String, Boolean> states = new LinkedHashMap<>();
        Map<String, Integer> keybinds = new LinkedHashMap<>();
        Map<String, Map<String, Object>> settingValues = new LinkedHashMap<>();

        for (Feature m : Features.get().all()) {
            states.put(m.id, m.isActive());
            keybinds.put(m.id, m.getKeybind());

            Map<String, Object> sMap = new LinkedHashMap<>();
            for (ValueGroup group : m.settings.groups()) {
                for (Value<?> s : group.getSettings()) {
                    sMap.put(s.name, s.get());
                }
            }
            if (!sMap.isEmpty()) {
                settingValues.put(m.id, sMap);
            }
        }

        Loadout loadout = new Loadout(name, System.currentTimeMillis(), states, settingValues, keybinds);
        loadouts.put(name.toLowerCase(), loadout);
        writeLoadoutToFile(loadout);
    }

    public void load(String name) {
        Loadout loadout = get(name);
        if (loadout == null) return;

        if (loadout.tweakStates != null) {
            for (Map.Entry<String, Boolean> entry : loadout.tweakStates.entrySet()) {
                Feature m = Features.get().get(entry.getKey());
                if (m != null) {
                    m.setActive(entry.getValue());
                }
            }
        }

        if (loadout.keybinds != null) {
            for (Map.Entry<String, Integer> entry : loadout.keybinds.entrySet()) {
                Feature m = Features.get().get(entry.getKey());
                if (m != null) {
                    m.setKeybind(entry.getValue());
                }
            }
        }

        if (loadout.settingValues != null) {
            for (Map.Entry<String, Map<String, Object>> entry : loadout.settingValues.entrySet()) {
                Feature m = Features.get().get(entry.getKey());
                if (m != null && entry.getValue() != null) {
                    Map<String, Object> sMap = entry.getValue();
                    for (ValueGroup group : m.settings.groups()) {
                        for (Value<?> s : group.getSettings()) {
                            if (sMap.containsKey(s.name)) {
                                applySettingObject(s, sMap.get(s.name));
                            }
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void applySettingObject(Value<?> setting, Object obj) {
        try {
            if (setting instanceof FlagValue bs && obj instanceof Boolean b) {
                bs.set(b);
            } else if (setting instanceof IntValue is && obj instanceof Number n) {
                is.set(Math.clamp(n.intValue(), is.min, is.max));
            } else if (setting instanceof DoubleValue ds && obj instanceof Number n) {
                ds.set(Math.clamp(n.doubleValue(), ds.min, ds.max));
            } else if (setting instanceof TextValue ss && obj != null) {
                ss.set(obj.toString());
            }
        } catch (Exception ignored) {}
    }

    public void delete(String name) {
        Loadout loadout = get(name);
        if (loadout != null) {
            loadouts.remove(loadout.name.toLowerCase());
            File file = getLoadoutFile(loadout.name);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    public void rename(String oldName, String newName) {
        if (oldName == null || newName == null || newName.trim().isEmpty()) return;
        Loadout loadout = get(oldName);
        if (loadout != null) {
            delete(oldName);
            loadout.name = newName.trim();
            loadouts.put(loadout.name.toLowerCase(), loadout);
            writeLoadoutToFile(loadout);
        }
    }

    private File getLoadoutFile(String name) {
        String sanitized = name.replaceAll("[^a-zA-Z0-9._-]", "_");
        return loadoutsDir.resolve(sanitized + ".json").toFile();
    }

    private void writeLoadoutToFile(Loadout loadout) {
        try {
            File dir = loadoutsDir.toFile();
            if (!dir.exists()) dir.mkdirs();

            File file = getLoadoutFile(loadout.name);
            JsonObject json = new JsonObject();
            json.addProperty("name", loadout.name);
            json.addProperty("savedAt", loadout.savedAt);

            JsonObject statesObj = new JsonObject();
            if (loadout.tweakStates != null) {
                for (Map.Entry<String, Boolean> e : loadout.tweakStates.entrySet()) {
                    statesObj.addProperty(e.getKey(), e.getValue());
                }
            }
            json.add("tweakStates", statesObj);

            JsonObject bindsObj = new JsonObject();
            if (loadout.keybinds != null) {
                for (Map.Entry<String, Integer> e : loadout.keybinds.entrySet()) {
                    bindsObj.addProperty(e.getKey(), e.getValue());
                }
            }
            json.add("keybinds", bindsObj);

            JsonObject settingsObj = new JsonObject();
            if (loadout.settingValues != null) {
                for (Map.Entry<String, Map<String, Object>> e : loadout.settingValues.entrySet()) {
                    JsonObject sMapObj = new JsonObject();
                    for (Map.Entry<String, Object> se : e.getValue().entrySet()) {
                        if (se.getValue() instanceof Boolean b) sMapObj.addProperty(se.getKey(), b);
                        else if (se.getValue() instanceof Number n) sMapObj.addProperty(se.getKey(), n);
                        else if (se.getValue() != null) sMapObj.addProperty(se.getKey(), se.getValue().toString());
                    }
                    settingsObj.add(e.getKey(), sMapObj);
                }
            }
            json.add("settingValues", settingsObj);

            try (FileWriter writer = new FileWriter(file)) {
                GSON.toJson(json, writer);
            }
        } catch (Exception e) {
            Pancreas.LOG.warn("Failed to save loadout {}: {}", loadout.name, e.getMessage());
        }
    }

    private void loadAll() {
        loadouts.clear();
        File dir = loadoutsDir.toFile();
        if (!dir.exists()) {
            dir.mkdirs();
            
            save("Default");
            return;
        }

        File[] files = dir.listFiles((d, name) -> name.endsWith(".json"));
        if (files == null || files.length == 0) {
            save("Default");
            return;
        }

        for (File file : files) {
            try (FileReader reader = new FileReader(file)) {
                JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                String name = json.has("name") ? json.get("name").getAsString() : file.getName().replace(".json", "");
                long savedAt = json.has("savedAt") ? json.get("savedAt").getAsLong() : file.lastModified();

                Map<String, Boolean> states = new LinkedHashMap<>();
                if (json.has("tweakStates")) {
                    JsonObject obj = json.getAsJsonObject("tweakStates");
                    for (Map.Entry<String, JsonElement> e : obj.entrySet()) {
                        states.put(e.getKey(), e.getValue().getAsBoolean());
                    }
                }

                Map<String, Integer> binds = new LinkedHashMap<>();
                if (json.has("keybinds")) {
                    JsonObject obj = json.getAsJsonObject("keybinds");
                    for (Map.Entry<String, JsonElement> e : obj.entrySet()) {
                        binds.put(e.getKey(), e.getValue().getAsInt());
                    }
                }

                Map<String, Map<String, Object>> settings = new LinkedHashMap<>();
                if (json.has("settingValues")) {
                    JsonObject obj = json.getAsJsonObject("settingValues");
                    for (Map.Entry<String, JsonElement> e : obj.entrySet()) {
                        if (e.getValue().isJsonObject()) {
                            Map<String, Object> sMap = new LinkedHashMap<>();
                            for (Map.Entry<String, JsonElement> se : e.getValue().getAsJsonObject().entrySet()) {
                                if (se.getValue().isJsonPrimitive()) {
                                    JsonPrimitive prim = se.getValue().getAsJsonPrimitive();
                                    if (prim.isBoolean()) sMap.put(se.getKey(), prim.getAsBoolean());
                                    else if (prim.isNumber()) sMap.put(se.getKey(), prim.getAsNumber());
                                    else sMap.put(se.getKey(), prim.getAsString());
                                }
                            }
                            settings.put(e.getKey(), sMap);
                        }
                    }
                }

                Loadout loadout = new Loadout(name, savedAt, states, settings, binds);
                loadouts.put(name.toLowerCase(), loadout);
            } catch (Exception e) {
                Pancreas.LOG.warn("Failed to load loadout file {}: {}", file.getName(), e.getMessage());
            }
        }
    }
}