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

import net.minecraft.world.entity.EntityType;

import java.util.ArrayList;

public class Silhouette extends Feature {
    public enum ShaderMode {
        None,
        Tint
    }

    private final ValueGroup sgThroughWalls = settings.createGroup("Through Walls");
    private final ValueGroup sgPlayers = settings.createGroup("Players");
    private final ValueGroup sgHand = settings.createGroup("Hand");

    public final EntityListValue entities = sgThroughWalls.add(new EntityListValue.Builder()
        .name("entities")
        .description("Entities to render through solid terrain.")
        .defaultValue(new ArrayList<>())
        .build());

    public final ChoiceValue<ShaderMode> shader = sgThroughWalls.add(new ChoiceValue.Builder<ShaderMode>()
        .name("shader")
        .description("Shader tint mode.")
        .defaultValue(ShaderMode.Tint)
        .build());

    public final TintValue color = sgThroughWalls.add(new TintValue.Builder()
        .name("color")
        .description("Tint color for through-wall entities.")
        .defaultValue(new Tint(255, 255, 255, 150))
        .visible(() -> shader.get() != ShaderMode.None)
        .build());

    public final FlagValue ignoreSelf = sgThroughWalls.add(new FlagValue.Builder()
        .name("ignore-self")
        .description("Ignore the local player entity.")
        .defaultValue(true)
        .build());

    public final FlagValue players = sgPlayers.add(new FlagValue.Builder()
        .name("players")
        .description("Applies special chams to other players.")
        .defaultValue(false)
        .build());

    public final FlagValue playersIgnoreSelf = sgPlayers.add(new FlagValue.Builder()
        .name("ignore-self")
        .description("Ignore self in player chams.")
        .defaultValue(false)
        .visible(players::get)
        .build());

    public final FlagValue playersTexture = sgPlayers.add(new FlagValue.Builder()
        .name("texture")
        .description("Keeps player skin texture while tinting.")
        .defaultValue(false)
        .visible(players::get)
        .build());

    public final TintValue playersColor = sgPlayers.add(new TintValue.Builder()
        .name("color")
        .description("Color for player chams.")
        .defaultValue(new Tint(198, 135, 254, 150))
        .visible(players::get)
        .build());

    public final DoubleValue playersScale = sgPlayers.add(new DoubleValue.Builder()
        .name("scale")
        .description("Model scale multiplier.")
        .defaultValue(1.0)
        .min(0.5)
        .max(2.0)
        .visible(players::get)
        .build());

    public final FlagValue handEnabled = sgHand.add(new FlagValue.Builder()
        .name("enabled")
        .description("Applies solid tinting to held hand/item.")
        .defaultValue(false)
        .build());

    public final FlagValue handTexture = sgHand.add(new FlagValue.Builder()
        .name("texture")
        .description("Renders hand texture under tint.")
        .defaultValue(false)
        .visible(handEnabled::get)
        .build());

    public final TintValue handColor = sgHand.add(new TintValue.Builder()
        .name("hand-color")
        .description("Color for hand chams.")
        .defaultValue(new Tint(198, 135, 254, 150))
        .visible(handEnabled::get)
        .build());

    public Silhouette() {
        super(Group.Cheats, "silhouette", "Renders entities and hands through walls with custom tints.");
    }

    public boolean shouldRenderThroughWalls(EntityType<?> type) {
        return isActive() && entities.get().contains(type);
    }
}