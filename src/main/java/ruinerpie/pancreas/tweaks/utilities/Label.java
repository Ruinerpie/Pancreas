package ruinerpie.pancreas.tweaks.utilities;

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
import java.util.List;

public class Label extends Feature {
    public enum DurabilityFormat {
        None,
        Total,
        Percentage
    }

    public enum EnchantPos {
        Above,
        OnTop
    }

    public enum DistanceColorMode {
        Gradient,
        Flat
    }

    private final ValueGroup sgGeneral = settings.getDefaultGroup();
    private final ValueGroup sgPlayers = settings.createGroup("Players");
    private final ValueGroup sgItems = settings.createGroup("Items");
    private final ValueGroup sgRender = settings.createGroup("Render");

    public final Value<List<EntityType<?>>> entities = sgGeneral.add(new EntityListValue.Builder()
        .name("entities")
        .description("Entities with expanded nametags.")
        .defaultValue(new ArrayList<>(List.of(EntityType.PLAYER, EntityType.ITEM)))
        .build()
    );

    public final Value<Double> scale = sgGeneral.add(new DoubleValue.Builder()
        .name("scale")
        .description("Nametag render scale multiplier.")
        .defaultValue(1.1)
        .min(0.5)
        .max(3.0)
        .sliderRange(0.5, 2.5)
        .build()
    );

    public final Value<Boolean> ignoreSelf = sgGeneral.add(new FlagValue.Builder()
        .name("ignore-self")
        .description("Ignores the local player.")
        .defaultValue(true)
        .build()
    );

    public final Value<Boolean> ignoreFriends = sgGeneral.add(new FlagValue.Builder()
        .name("ignore-friends")
        .description("Ignores Team allies.")
        .defaultValue(false)
        .build()
    );

    public final Value<Boolean> ignoreBots = sgGeneral.add(new FlagValue.Builder()
        .name("ignore-bots")
        .description("Hides nametags for server bot NPCs.")
        .defaultValue(true)
        .build()
    );

    public final Value<Boolean> culling = sgGeneral.add(new FlagValue.Builder()
        .name("culling")
        .description("Culls distant or excessive nametags for performance.")
        .defaultValue(false)
        .build()
    );

    public final Value<Double> cullingRange = sgGeneral.add(new DoubleValue.Builder()
        .name("culling-range")
        .description("Maximum distance beyond which nametags are hidden.")
        .defaultValue(20.0)
        .min(5.0)
        .max(100.0)
        .visible(culling::get)
        .build()
    );

    public final Value<Integer> cullingCount = sgGeneral.add(new IntValue.Builder()
        .name("culling-count")
        .description("Maximum simultaneously rendered nametags.")
        .defaultValue(50)
        .min(10)
        .max(200)
        .visible(culling::get)
        .build()
    );

    public final Value<Boolean> health = sgPlayers.add(new FlagValue.Builder()
        .name("health")
        .description("Shows numeric player health.")
        .defaultValue(true)
        .build()
    );

    public final Value<Boolean> useDisplayName = sgPlayers.add(new FlagValue.Builder()
        .name("use-display-name")
        .description("Uses formatted display name with team/rank prefixes.")
        .defaultValue(false)
        .build()
    );

    public final Value<Boolean> gamemode = sgPlayers.add(new FlagValue.Builder()
        .name("gamemode")
        .description("Shows player game mode.")
        .defaultValue(false)
        .build()
    );

    public final Value<Boolean> distance = sgPlayers.add(new FlagValue.Builder()
        .name("distance")
        .description("Shows distance in meters.")
        .defaultValue(false)
        .build()
    );

    public final Value<Boolean> ping = sgPlayers.add(new FlagValue.Builder()
        .name("ping")
        .description("Shows network ping latency.")
        .defaultValue(true)
        .build()
    );

    public final Value<Boolean> items = sgPlayers.add(new FlagValue.Builder()
        .name("items")
        .description("Shows equipped armor and held items.")
        .defaultValue(true)
        .build()
    );

    public final Value<Double> itemSpacing = sgPlayers.add(new DoubleValue.Builder()
        .name("item-spacing")
        .description("Horizontal pixel spacing between item icons.")
        .defaultValue(2.0)
        .min(0.0)
        .max(10.0)
        .visible(items::get)
        .build()
    );

    public final Value<Boolean> ignoreEmptySlots = sgPlayers.add(new FlagValue.Builder()
        .name("ignore-empty-slots")
        .description("Hides icons for empty equipment slots.")
        .defaultValue(true)
        .visible(items::get)
        .build()
    );

    public final Value<DurabilityFormat> durability = sgPlayers.add(new ChoiceValue.Builder<DurabilityFormat>()
        .name("durability")
        .description("Equipment durability counter format.")
        .defaultValue(DurabilityFormat.None)
        .visible(items::get)
        .build()
    );

    public final Value<Boolean> displayEnchants = sgPlayers.add(new FlagValue.Builder()
        .name("display-enchants")
        .description("Shows item enchantment abbreviations.")
        .defaultValue(false)
        .visible(items::get)
        .build()
    );

    public final Value<EnchantPos> enchantmentPosition = sgPlayers.add(new ChoiceValue.Builder<EnchantPos>()
        .name("enchantment-position")
        .description("Enchantment text position relative to item icons.")
        .defaultValue(EnchantPos.Above)
        .visible(() -> items.get() && displayEnchants.get())
        .build()
    );

    public final Value<Integer> enchantNameLength = sgPlayers.add(new IntValue.Builder()
        .name("enchant-name-length")
        .description("Character abbreviation length for enchantment names.")
        .defaultValue(3)
        .min(1)
        .max(5)
        .visible(() -> items.get() && displayEnchants.get())
        .build()
    );

    public final Value<Double> enchantTextScale = sgPlayers.add(new DoubleValue.Builder()
        .name("enchant-text-scale")
        .description("Font scale for enchantment labels.")
        .defaultValue(1.0)
        .min(0.1)
        .max(2.0)
        .visible(() -> items.get() && displayEnchants.get())
        .build()
    );

    public final Value<Boolean> showCount = sgItems.add(new FlagValue.Builder()
        .name("show-count")
        .description("Shows item stack count on ground item nametags.")
        .defaultValue(true)
        .build()
    );

    public final Value<Tint> backgroundColor = sgRender.add(new TintValue.Builder()
        .name("background-color")
        .description("Nametag background box color.")
        .defaultValue(new Tint(0, 0, 0, 75))
        .build()
    );

    public final Value<Tint> nameColor = sgRender.add(new TintValue.Builder()
        .name("name-color")
        .description("Username text color.")
        .defaultValue(new Tint(255, 255, 255, 255))
        .build()
    );

    public final Value<Tint> pingColor = sgRender.add(new TintValue.Builder()
        .name("ping-color")
        .description("Ping text color.")
        .defaultValue(new Tint(20, 170, 170, 255))
        .visible(ping::get)
        .build()
    );

    public final Value<Tint> gamemodeColor = sgRender.add(new TintValue.Builder()
        .name("gamemode-color")
        .description("Gamemode text color.")
        .defaultValue(new Tint(232, 185, 35, 255))
        .visible(gamemode::get)
        .build()
    );

    public final Value<DistanceColorMode> distanceColorMode = sgRender.add(new ChoiceValue.Builder<DistanceColorMode>()
        .name("distance-color-mode")
        .description("Distance text coloration mode.")
        .defaultValue(DistanceColorMode.Gradient)
        .visible(distance::get)
        .build()
    );

    public final Value<Tint> distanceColor = sgRender.add(new TintValue.Builder()
        .name("distance-color")
        .description("Distance text color in Flat mode.")
        .defaultValue(new Tint(150, 150, 150, 255))
        .visible(() -> distance.get() && distanceColorMode.get() == DistanceColorMode.Flat)
        .build()
    );

    public Label() {
        super(Group.Utilities, "label", "Advanced custom 2D nametags displaying equipment, health, ping, and enchants.");
    }

    @Listen
    private void onTick(WorldTick event) {
        
    }

    @Listen
    private void onRender2D(Render2D event) {
        
    }
}