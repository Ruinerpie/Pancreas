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

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class pESP extends Feature {
    public enum Mode {
        Box,
        TwoDimensional
    }

    public enum ColorMode {
        EntityType,
        Distance,
        Health
    }

    private final ValueGroup sgGeneral = settings.getDefaultGroup();
    private final ValueGroup sgColors = settings.createGroup("Colors");

    public final ChoiceValue<Mode> mode = sgGeneral.add(new ChoiceValue.Builder<Mode>()
        .name("mode")
        .description("ESP rendering style.")
        .defaultValue(Mode.Box)
        .build());

    public final FlagValue highlightTarget = sgGeneral.add(new FlagValue.Builder()
        .name("highlight-target")
        .description("Highlights current crosshair target.")
        .defaultValue(false)
        .build());

    public final FlagValue targetHitbox = sgGeneral.add(new FlagValue.Builder()
        .name("target-hitbox")
        .description("Renders bounding hitbox of current target.")
        .defaultValue(true)
        .visible(highlightTarget::get)
        .build());

    public final FlagValue ignoreSelf = sgGeneral.add(new FlagValue.Builder()
        .name("ignore-self")
        .description("Ignore the local player entity.")
        .defaultValue(true)
        .build());

    public final ChoiceValue<Shape> shapeMode = sgGeneral.add(new ChoiceValue.Builder<Shape>()
        .name("shape-mode")
        .description("Render outlines, filled surfaces, or both.")
        .defaultValue(Shape.Both)
        .build());

    public final DoubleValue fillOpacity = sgGeneral.add(new DoubleValue.Builder()
        .name("fill-opacity")
        .description("Opacity multiplier for filled surfaces.")
        .defaultValue(0.3)
        .min(0.0)
        .max(1.0)
        .build());

    public final DoubleValue fadeDistance = sgGeneral.add(new DoubleValue.Builder()
        .name("fade-distance")
        .description("Distance from camera where ESP begins fading.")
        .defaultValue(3.0)
        .min(0.0)
        .max(20.0)
        .build());

    public final EntityListValue entities = sgGeneral.add(new EntityListValue.Builder()
        .name("entities")
        .description("Entity types to render.")
        .defaultValue(new ArrayList<>(List.of(EntityType.PLAYER)))
        .build());

    public final ChoiceValue<ColorMode> colorMode = sgColors.add(new ChoiceValue.Builder<ColorMode>()
        .name("color-mode")
        .description("How colors are determined.")
        .defaultValue(ColorMode.EntityType)
        .build());

    public final FlagValue showCrewColors = sgColors.add(new FlagValue.Builder()
        .name("show-crew-colors")
        .description("Uses crew accent color for players in your crew.")
        .defaultValue(true)
        .build());

    public final TintValue playersColor = sgColors.add(new TintValue.Builder()
        .name("players-color")
        .description("Color for player entities.")
        .defaultValue(new Tint(255, 255, 255, 255))
        .build());

    public final TintValue animalsColor = sgColors.add(new TintValue.Builder()
        .name("animals-color")
        .description("Color for peaceful animals.")
        .defaultValue(new Tint(50, 220, 50, 255))
        .build());

    public final TintValue waterAnimalsColor = sgColors.add(new TintValue.Builder()
        .name("water-animals-color")
        .description("Color for aquatic entities.")
        .defaultValue(new Tint(50, 150, 255, 255))
        .build());

    public final TintValue monstersColor = sgColors.add(new TintValue.Builder()
        .name("monsters-color")
        .description("Color for hostile mobs.")
        .defaultValue(new Tint(255, 50, 50, 255))
        .build());

    public final TintValue miscColor = sgColors.add(new TintValue.Builder()
        .name("misc-color")
        .description("Color for other entity types.")
        .defaultValue(new Tint(180, 180, 180, 255))
        .build());

    public pESP() {
        super(Group.Cheats, "pesp", "Highlights entities through walls with customizable bounding boxes and colors.");
    }

    public Color getEntityColor(Entity entity) {
        if (entity instanceof Player player) {
            if (showCrewColors.get() && Team.get().isFriend(player.getUUID())) {
                return Team.CREW_COLOR;
            }
            return playersColor.get();
        }
        if (entity instanceof Monster) return monstersColor.get();
        if (entity instanceof Animal) return animalsColor.get();
        return miscColor.get();
    }

    @Listen
    private void onRender3D(Render3D event) {
        if (Pancreas.mc.level == null || Pancreas.mc.player == null) return;

        for (Entity entity : Pancreas.mc.level.entitiesForRendering()) {
            if (entity == Pancreas.mc.player && ignoreSelf.get()) continue;
            if (!entities.get().contains(entity.getType())) continue;

            Color baseCol = getEntityColor(entity);
            Color sideCol = new Color(baseCol.r, baseCol.g, baseCol.b, (int) (baseCol.a * fillOpacity.get()));
            Color lineCol = baseCol;

            var bb = entity.getBoundingBox();
            event.renderer.box(bb, sideCol, lineCol, shapeMode.get(), 0);
        }
    }
}