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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.entity.player.Player;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Path;
import java.util.*;

public final class Team {
    private static final Team INSTANCE = new Team();
    public static Team get() { return INSTANCE; }

    public static final ruinerpie.pancreas.draw.Color CREW_COLOR = new ruinerpie.pancreas.draw.Color(0, 255, 180, 255);
    public static final ruinerpie.pancreas.draw.Color FRIEND_COLOR = CREW_COLOR;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Path configFile = FabricLoader.getInstance().getConfigDir().resolve("pancreas").resolve("crew.json");
    private final Map<String, Teammate> members = new LinkedHashMap<>();

    private Team() {}

    public void init() {
        load();
    }

    public List<Teammate> all() {
        return new ArrayList<>(members.values());
    }

    public boolean isFriend(UUID uuid) {
        if (uuid == null) return false;
        return members.containsKey(uuid.toString());
    }

    public boolean isFriend(String name) {
        if (name == null || name.isEmpty()) return false;
        for (Teammate m : members.values()) {
            if (m.name.equalsIgnoreCase(name)) return true;
        }
        return false;
    }

    public boolean shouldAttack(Player player) {
        if (player == null) return false;
        return !isFriend(player.getUUID()) && !isFriend(player.getName().getString());
    }

    public void add(UUID uuid, String name) {
        if (uuid == null || name == null) return;
        Teammate member = new Teammate(uuid.toString(), name, System.currentTimeMillis());
        members.put(uuid.toString(), member);
        save();
    }

    public void remove(UUID uuid) {
        if (uuid == null) return;
        if (members.remove(uuid.toString()) != null) {
            save();
        }
    }

    public void remove(String uuidStr) {
        if (uuidStr == null) return;
        if (members.remove(uuidStr) != null) {
            save();
        }
    }

    public void clear() {
        members.clear();
        save();
    }

    public List<Teammate> sortedAlphabetically() {
        List<Teammate> list = all();
        list.sort(Comparator.comparing(m -> m.name.toLowerCase()));
        return list;
    }

    public List<Teammate> sortedByAdded() {
        List<Teammate> list = all();
        list.sort(Comparator.comparingLong((Teammate m) -> m.addedAt).reversed());
        return list;
    }

    public void load() {
        File file = configFile.toFile();
        if (!file.exists()) {
            save();
            return;
        }

        try (FileReader reader = new FileReader(file)) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            members.clear();
            if (json.has("members")) {
                JsonArray arr = json.getAsJsonArray("members");
                for (JsonElement el : arr) {
                    if (el.isJsonObject()) {
                        JsonObject obj = el.getAsJsonObject();
                        String uuid = obj.has("uuid") ? obj.get("uuid").getAsString() : "";
                        String name = obj.has("name") ? obj.get("name").getAsString() : "";
                        long addedAt = obj.has("addedAt") ? obj.get("addedAt").getAsLong() : System.currentTimeMillis();
                        if (!uuid.isEmpty() && !name.isEmpty()) {
                            members.put(uuid, new Teammate(uuid, name, addedAt));
                        }
                    }
                }
            }
        } catch (Exception e) {
            members.clear();
            Pancreas.LOG.warn("Failed to load crew.json; starting with empty crew list: {}", e.getMessage());
        }
    }

    public void save() {
        try {
            File parent = configFile.toFile().getParentFile();
            if (parent != null && !parent.exists()) parent.mkdirs();

            JsonObject json = new JsonObject();
            json.addProperty("configVersion", 1);

            JsonArray arr = new JsonArray();
            for (Teammate m : members.values()) {
                JsonObject obj = new JsonObject();
                obj.addProperty("uuid", m.uuid);
                obj.addProperty("name", m.name);
                obj.addProperty("addedAt", m.addedAt);
                arr.add(obj);
            }
            json.add("members", arr);

            try (FileWriter writer = new FileWriter(configFile.toFile())) {
                GSON.toJson(json, writer);
            }
        } catch (Exception e) {
            Pancreas.LOG.warn("Failed to save crew.json: {}", e.getMessage());
        }
    }
}