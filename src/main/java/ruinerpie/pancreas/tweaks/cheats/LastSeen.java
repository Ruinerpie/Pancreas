package ruinerpie.pancreas.tweaks.cheats;

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

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LastSeen extends Feature {
    public record LogoutSpot(String name, UUID uuid, Vec3 pos, float health, float maxHealth) {}

    private final ValueGroup sgGeneral = settings.getDefaultGroup();
    private final ValueGroup sgRender = settings.createGroup("Render");

    public final DoubleValue scale = sgGeneral.add(new DoubleValue.Builder()
        .name("scale")
        .description("Label render scale.")
        .defaultValue(1.0)
        .min(0.5)
        .max(2.0)
        .build());

    public final FlagValue fullHeight = sgGeneral.add(new FlagValue.Builder()
        .name("full-height")
        .description("Renders full 1.8m player bounding box.")
        .defaultValue(true)
        .build());

    public final ChoiceValue<Shape> shapeMode = sgRender.add(new ChoiceValue.Builder<Shape>()
        .name("shape-mode")
        .description("Box render style.")
        .defaultValue(Shape.Both)
        .build());

    public final TintValue sideColor = sgRender.add(new TintValue.Builder()
        .name("side-color")
        .description("Side fill color.")
        .defaultValue(new Tint(255, 0, 255, 55))
        .build());

    public final TintValue lineColor = sgRender.add(new TintValue.Builder()
        .name("line-color")
        .description("Line outline color.")
        .defaultValue(new Tint(255, 0, 255, 255))
        .build());

    public final TintValue nameColor = sgRender.add(new TintValue.Builder()
        .name("name-color")
        .description("Text label color.")
        .defaultValue(new Tint(255, 255, 255, 255))
        .build());

    public final TintValue nameBackgroundColor = sgRender.add(new TintValue.Builder()
        .name("name-background-color")
        .description("Background box color for name label.")
        .defaultValue(new Tint(0, 0, 0, 75))
        .build());

    private final Map<UUID, LogoutSpot> logoutSpots = new HashMap<>();
    private final Map<UUID, Player> trackedPlayers = new HashMap<>();

    public LastSeen() {
        super(Group.Cheats, "last-seen", "Marks locations where players disconnect from the server.");
    }

    @Override
    public void onDeactivate() {
        logoutSpots.clear();
        trackedPlayers.clear();
    }

    @Listen
    private void onTick(WorldTick event) {
        if (Pancreas.mc.level == null) return;

        Map<UUID, Player> current = new HashMap<>();
        for (Player p : Pancreas.mc.level.players()) {
            if (p == Pancreas.mc.player) continue;
            current.put(p.getUUID(), p);
        }

        for (Map.Entry<UUID, Player> entry : trackedPlayers.entrySet()) {
            if (!current.containsKey(entry.getKey())) {
                Player p = entry.getValue();
                logoutSpots.put(p.getUUID(), new LogoutSpot(p.getName().getString(), p.getUUID(), p.position(), p.getHealth(), p.getMaxHealth()));
            }
        }

        trackedPlayers.clear();
        trackedPlayers.putAll(current);
    }

    @Listen
    private void onRender3D(Render3D event) {
        double boxH = fullHeight.get() ? 1.8 : 0.2;
        for (LogoutSpot spot : logoutSpots.values()) {
            AABB bb = new AABB(spot.pos.x - 0.3, spot.pos.y, spot.pos.z - 0.3, spot.pos.x + 0.3, spot.pos.y + boxH, spot.pos.z + 0.3);
            event.renderer.box(bb, sideColor.get(), lineColor.get(), shapeMode.get(), 0);
        }
    }
}