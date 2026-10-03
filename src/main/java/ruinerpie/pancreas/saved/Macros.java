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
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Path;
import java.util.*;

public final class Macros {
    private static final Macros INSTANCE = new Macros();
    public static Macros get() { return INSTANCE; }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Path configFile = FabricLoader.getInstance().getConfigDir().resolve("pancreas").resolve("quickactions.json");
    private final Map<String, Macro> actions = new LinkedHashMap<>();

    private static class PendingChain {
        final List<MacroStep> steps;
        int nextIndex;
        int waitTicksRemaining;

        PendingChain(List<MacroStep> steps, int startIndex, int waitTicks) {
            this.steps = steps;
            this.nextIndex = startIndex;
            this.waitTicksRemaining = waitTicks;
        }
    }

    private final List<PendingChain> pendingChains = new ArrayList<>();

    private Macros() {}

    public void init() {
        load();
    }

    public List<Macro> all() {
        return new ArrayList<>(actions.values());
    }

    public Macro get(String id) {
        if (id == null) return null;
        return actions.get(id.toLowerCase());
    }

    public void create(String name) {
        if (name == null || name.trim().isEmpty()) return;
        String id = name.trim().toLowerCase().replaceAll("[^a-z0-9_-]", "-");
        if (id.isEmpty()) id = "action-" + (actions.size() + 1);

        Macro action = new Macro(id, name.trim());
        actions.put(id, action);
        save();
    }

    public void delete(String id) {
        if (id == null) return;
        if (actions.remove(id.toLowerCase()) != null) {
            save();
        }
    }

    public void addStep(String id, MacroStep step) {
        Macro action = get(id);
        if (action != null && step != null) {
            action.steps.add(step);
            save();
        }
    }

    public void removeStep(String id, int index) {
        Macro action = get(id);
        if (action != null && index >= 0 && index < action.steps.size()) {
            action.steps.remove(index);
            save();
        }
    }

    public void bindKey(String id, int keycode) {
        Macro action = get(id);
        if (action != null) {
            action.keybind = keycode;
            save();
        }
    }

    public void trigger(String id) {
        Macro action = get(id);
        if (action == null || action.steps.isEmpty()) return;

        executeSteps(action.steps, 0);
    }

    private void executeSteps(List<MacroStep> steps, int startIndex) {
        Minecraft mc = Minecraft.getInstance();

        for (int i = startIndex; i < steps.size(); i++) {
            MacroStep step = steps.get(i);
            if (step == null || step.kind == null) continue;

            if (step.kind == MacroStep.Kind.WAIT_TICKS) {
                int ticks = 1;
                try {
                    ticks = Integer.parseInt(step.target);
                } catch (Exception ignored) {}
                
                pendingChains.add(new PendingChain(steps, i + 1, Math.max(1, ticks)));
                return;
            }

            executeSingleStep(step, mc);
        }
    }

    private void executeSingleStep(MacroStep step, Minecraft mc) {
        try {
            switch (step.kind) {
                case TOGGLE_TWEAK -> {
                    Feature m = Features.get().get(step.target);
                    if (m != null) m.toggle();
                }
                case ENABLE_TWEAK -> {
                    Feature m = Features.get().get(step.target);
                    if (m != null) m.enable();
                }
                case DISABLE_TWEAK -> {
                    Feature m = Features.get().get(step.target);
                    if (m != null) m.disable();
                }
                case OPEN_SCREEN -> {
                    openNamedScreen(step.target, mc);
                }
                case RUN_COMMAND -> {
                    if (mc.player != null && mc.player.connection != null && step.target != null) {
                        String cmd = step.target.trim();
                        if (cmd.startsWith("/")) cmd = cmd.substring(1);
                        if (!cmd.isEmpty()) {
                            mc.player.connection.sendCommand(cmd);
                        }
                    }
                }
                case WAIT_TICKS -> {}
            }
        } catch (Exception e) {
            Pancreas.LOG.warn("Failed to execute quickaction step: {}", e.getMessage());
        }
    }

    private void openNamedScreen(String screenName, Minecraft mc) {
        if (screenName == null) return;
        String s = screenName.toLowerCase().trim();
        switch (s) {
            case "pancreas", "main" -> mc.setScreen(new ruinerpie.pancreas.screens.Home());
            case "crew" -> mc.setScreen(new ruinerpie.pancreas.screens.TeamScreen(mc.screen));
            case "shadow" -> mc.setScreen(new ruinerpie.pancreas.screens.StalkScreen(mc.screen));
            case "loadouts" -> mc.setScreen(new ruinerpie.pancreas.screens.LoadoutScreen(mc.screen));
            case "quickactions" -> mc.setScreen(new ruinerpie.pancreas.screens.MacroScreen(mc.screen));
            case "hud" -> mc.setScreen(new ruinerpie.pancreas.hud.Studio(mc.screen));
            case "settings" -> mc.setScreen(new ruinerpie.pancreas.screens.GlobalPrefs(mc.screen));
            case "gui" -> mc.setScreen(new ruinerpie.pancreas.screens.StylePrefs(mc.screen));
            default -> {
                Feature m = Features.get().get(screenName);
                if (m != null) {
                    mc.setScreen(new ruinerpie.pancreas.screens.TweakSettings(m, mc.screen));
                }
            }
        }
    }

    public void tick() {
        if (pendingChains.isEmpty()) return;

        List<PendingChain> toRemove = new ArrayList<>();
        List<PendingChain> toExecute = new ArrayList<>();

        for (PendingChain chain : pendingChains) {
            chain.waitTicksRemaining--;
            if (chain.waitTicksRemaining <= 0) {
                toRemove.add(chain);
                toExecute.add(chain);
            }
        }

        pendingChains.removeAll(toRemove);

        for (PendingChain chain : toExecute) {
            executeSteps(chain.steps, chain.nextIndex);
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
            actions.clear();
            if (json.has("actions")) {
                JsonArray arr = json.getAsJsonArray("actions");
                for (JsonElement el : arr) {
                    if (el.isJsonObject()) {
                        JsonObject obj = el.getAsJsonObject();
                        String id = obj.has("id") ? obj.get("id").getAsString() : "";
                        String name = obj.has("name") ? obj.get("name").getAsString() : id;
                        int keybind = obj.has("keybind") ? obj.get("keybind").getAsInt() : -1;

                        List<MacroStep> steps = new ArrayList<>();
                        if (obj.has("steps") && obj.get("steps").isJsonArray()) {
                            for (JsonElement se : obj.getAsJsonArray("steps")) {
                                if (se.isJsonObject()) {
                                    JsonObject sObj = se.getAsJsonObject();
                                    String kindStr = sObj.has("kind") ? sObj.get("kind").getAsString() : "";
                                    String target = sObj.has("target") ? sObj.get("target").getAsString() : "";
                                    MacroStep.Kind kind = MacroStep.Kind.valueOf(kindStr);
                                    steps.add(new MacroStep(kind, target));
                                }
                            }
                        }

                        if (!id.isEmpty()) {
                            actions.put(id.toLowerCase(), new Macro(id, name, keybind, steps));
                        }
                    }
                }
            }
        } catch (Exception e) {
            actions.clear();
            Pancreas.LOG.warn("Failed to load quickactions.json: {}", e.getMessage());
        }
    }

    public void save() {
        try {
            File parent = configFile.toFile().getParentFile();
            if (parent != null && !parent.exists()) parent.mkdirs();

            JsonObject json = new JsonObject();
            json.addProperty("configVersion", 1);

            JsonArray arr = new JsonArray();
            for (Macro a : actions.values()) {
                JsonObject obj = new JsonObject();
                obj.addProperty("id", a.id);
                obj.addProperty("name", a.name);
                obj.addProperty("keybind", a.keybind);

                JsonArray stepsArr = new JsonArray();
                for (MacroStep s : a.steps) {
                    JsonObject sObj = new JsonObject();
                    sObj.addProperty("kind", s.kind.name());
                    sObj.addProperty("target", s.target != null ? s.target : "");
                    stepsArr.add(sObj);
                }
                obj.add("steps", stepsArr);
                arr.add(obj);
            }
            json.add("actions", arr);

            try (FileWriter writer = new FileWriter(configFile.toFile())) {
                GSON.toJson(json, writer);
            }
        } catch (Exception e) {
            Pancreas.LOG.warn("Failed to save quickactions.json: {}", e.getMessage());
        }
    }
}