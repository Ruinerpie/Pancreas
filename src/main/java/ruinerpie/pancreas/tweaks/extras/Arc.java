package ruinerpie.pancreas.tweaks.extras;

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

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public class Arc extends Feature {
    private final ValueGroup sgGeneral = settings.getDefaultGroup();
    private final ValueGroup sgRender = settings.createGroup("Render");

    public final Value<List<Item>> items = sgGeneral.add(new ItemListValue.Builder()
        .name("items")
        .description("Projectiles to predict trajectories for.")
        .defaultValue(new ArrayList<>(List.of(
            Items.BOW, Items.CROSSBOW, Items.TRIDENT,
            Items.ENDER_PEARL, Items.EXPERIENCE_BOTTLE,
            Items.SPLASH_POTION, Items.LINGERING_POTION,
            Items.SNOWBALL, Items.EGG, Items.WIND_CHARGE
        )))
        .build()
    );

    public final Value<Boolean> otherPlayers = sgGeneral.add(new FlagValue.Builder()
        .name("other-players")
        .description("Predicts trajectories for other players holding projectiles.")
        .defaultValue(true)
        .build()
    );

    public final Value<Boolean> firedProjectiles = sgGeneral.add(new FlagValue.Builder()
        .name("fired-projectiles")
        .description("Predicts path of in-flight projectile entities.")
        .defaultValue(false)
        .build()
    );

    public final Value<Boolean> ignoreWitherSkulls = sgGeneral.add(new FlagValue.Builder()
        .name("ignore-wither-skulls")
        .description("Ignores projectile path calculation for wither skulls.")
        .defaultValue(false)
        .visible(firedProjectiles::get)
        .build()
    );

    public final Value<Boolean> accurate = sgGeneral.add(new FlagValue.Builder()
        .name("accurate")
        .description("High-precision physics simulation step.")
        .defaultValue(false)
        .build()
    );

    public final Value<Integer> simulationSteps = sgGeneral.add(new IntValue.Builder()
        .name("simulation-steps")
        .description("Maximum simulated path calculation steps.")
        .defaultValue(500)
        .min(10)
        .max(2000)
        .sliderRange(50, 1000)
        .build()
    );

    public final Value<Integer> ignoreRenderingFirstTicks = sgRender.add(new IntValue.Builder()
        .name("ignore-rendering-first-ticks")
        .description("Ignores initial projectile simulation ticks for visual cleanliness.")
        .defaultValue(3)
        .min(0)
        .max(20)
        .build()
    );

    public final Value<Shape> shapeMode = sgRender.add(new ChoiceValue.Builder<Shape>()
        .name("shape-mode")
        .description("Hitbox rendering mode at the impact point.")
        .defaultValue(Shape.Both)
        .build()
    );

    public final Value<Tint> sideColor = sgRender.add(new TintValue.Builder()
        .name("side-color")
        .description("Color for trajectory sides.")
        .defaultValue(new Tint(255, 150, 0, 35))
        .build()
    );

    public final Value<Tint> lineColor = sgRender.add(new TintValue.Builder()
        .name("line-color")
        .description("Color for trajectory arc lines.")
        .defaultValue(new Tint(255, 150, 0, 255))
        .build()
    );

    public final Value<Boolean> renderPositionBoxes = sgRender.add(new FlagValue.Builder()
        .name("render-position-boxes")
        .description("Renders small marker boxes along trajectory arc steps.")
        .defaultValue(false)
        .build()
    );

    public final Value<Double> positionBoxSize = sgRender.add(new DoubleValue.Builder()
        .name("position-box-size")
        .description("Size of marker boxes along trajectory arc.")
        .defaultValue(0.02)
        .min(0.01)
        .max(0.2)
        .visible(renderPositionBoxes::get)
        .build()
    );

    public final Value<Tint> positionSideColor = sgRender.add(new TintValue.Builder()
        .name("position-side-color")
        .description("Side color of marker boxes along arc.")
        .defaultValue(new Tint(255, 150, 0, 35))
        .visible(renderPositionBoxes::get)
        .build()
    );

    public final Value<Tint> positionLineColor = sgRender.add(new TintValue.Builder()
        .name("position-line-color")
        .description("OutlineDec color of marker boxes along arc.")
        .defaultValue(new Tint(255, 150, 0, 255))
        .visible(renderPositionBoxes::get)
        .build()
    );

    public Arc() {
        super(Group.Extras, "arc", "Predicts and displays trajectory arcs of thrown and shot projectiles.");
    }

    @Listen
    private void onRender3D(Render3D event) {
        
    }
}