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
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Vein extends Feature {
    public enum Style {
        Lines,
        Offscreen
    }

    public enum TargetPoint {
        Head,
        Body,
        Feet
    }

    private final ValueGroup sgGeneral = settings.getDefaultGroup();
    private final ValueGroup sgAppearance = settings.createGroup("Appearance");
    private final ValueGroup sgColors = settings.createGroup("Colors");

    public final Value<List<EntityType<?>>> entities = sgGeneral.add(new EntityListValue.Builder()
        .name("entities")
        .description("Entities to draw tracers to.")
        .defaultValue(new ArrayList<>(List.of(EntityType.PLAYER)))
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
        .description("Ignores crew members.")
        .defaultValue(false)
        .build()
    );

    public final Value<Boolean> showInvisible = sgGeneral.add(new FlagValue.Builder()
        .name("show-invisible")
        .description("Draws tracers to invisible entities.")
        .defaultValue(true)
        .build()
    );

    public final Value<Style> style = sgAppearance.add(new ChoiceValue.Builder<Style>()
        .name("style")
        .description("Tracer visual style.")
        .defaultValue(Style.Lines)
        .build()
    );

    public final Value<TargetPoint> target = sgAppearance.add(new ChoiceValue.Builder<TargetPoint>()
        .name("target")
        .description("Part of the target entity to connect to.")
        .defaultValue(TargetPoint.Body)
        .visible(() -> style.get() == Style.Lines)
        .build()
    );

    public final Value<Boolean> stem = sgAppearance.add(new FlagValue.Builder()
        .name("stem")
        .description("Draws vertical line from entity feet to target point.")
        .defaultValue(true)
        .visible(() -> style.get() == Style.Lines)
        .build()
    );

    public final Value<Integer> maxDistance = sgAppearance.add(new IntValue.Builder()
        .name("max-distance")
        .description("Maximum distance to draw tracers.")
        .defaultValue(256)
        .min(1)
        .max(512)
        .sliderRange(1, 512)
        .build()
    );

    public final Value<Integer> distanceOffscreen = sgAppearance.add(new IntValue.Builder()
        .name("distance-offscreen")
        .description("Distance from screen center for offscreen arrows.")
        .defaultValue(200)
        .min(50)
        .max(600)
        .visible(() -> style.get() == Style.Offscreen)
        .build()
    );

    public final Value<Integer> sizeOffscreen = sgAppearance.add(new IntValue.Builder()
        .name("size-offscreen")
        .description("Size of offscreen tracer indicators.")
        .defaultValue(10)
        .min(2)
        .max(50)
        .visible(() -> style.get() == Style.Offscreen)
        .build()
    );

    public final Value<Boolean> blinkOffscreen = sgAppearance.add(new FlagValue.Builder()
        .name("blink-offscreen")
        .description("Blinks offscreen indicators.")
        .defaultValue(true)
        .visible(() -> style.get() == Style.Offscreen)
        .build()
    );

    public final Value<Double> blinkOffscreenSpeed = sgAppearance.add(new DoubleValue.Builder()
        .name("blink-offscreen-speed")
        .description("Blink speed multiplier.")
        .defaultValue(4.0)
        .min(0.5)
        .max(10.0)
        .visible(() -> style.get() == Style.Offscreen && blinkOffscreen.get())
        .build()
    );

    public final Value<Boolean> distanceColors = sgColors.add(new FlagValue.Builder()
        .name("distance-colors")
        .description("Colors tracers dynamically based on distance.")
        .defaultValue(false)
        .build()
    );

    public final Value<Boolean> showFriendColors = sgColors.add(new FlagValue.Builder()
        .name("show-friend-colors")
        .description("Always use Team color for crew members.")
        .defaultValue(true)
        .visible(() -> !ignoreFriends.get())
        .build()
    );

    public final Value<Tint> playersColor = sgColors.add(new TintValue.Builder()
        .name("players-colors")
        .description("Color for player tracers.")
        .defaultValue(new Tint(205, 205, 205, 127))
        .visible(() -> !distanceColors.get())
        .build()
    );

    public final Value<Tint> animalsColor = sgColors.add(new TintValue.Builder()
        .name("animals-color")
        .description("Color for peaceful animals.")
        .defaultValue(new Tint(0, 255, 0, 127))
        .visible(() -> !distanceColors.get())
        .build()
    );

    public final Value<Tint> waterAnimalsColor = sgColors.add(new TintValue.Builder()
        .name("water-animals-color")
        .description("Color for aquatic creatures.")
        .defaultValue(new Tint(0, 100, 255, 127))
        .visible(() -> !distanceColors.get())
        .build()
    );

    public final Value<Tint> monstersColor = sgColors.add(new TintValue.Builder()
        .name("monsters-color")
        .description("Color for monsters.")
        .defaultValue(new Tint(255, 0, 0, 127))
        .visible(() -> !distanceColors.get())
        .build()
    );

    public final Value<Tint> ambientColor = sgColors.add(new TintValue.Builder()
        .name("ambient-color")
        .description("Color for ambient creatures.")
        .defaultValue(new Tint(100, 100, 100, 127))
        .visible(() -> !distanceColors.get())
        .build()
    );

    public final Value<Tint> miscColor = sgColors.add(new TintValue.Builder()
        .name("misc-color")
        .description("Color for other entities.")
        .defaultValue(new Tint(200, 200, 200, 127))
        .visible(() -> !distanceColors.get())
        .build()
    );

    public Vein() {
        super(Group.Cheats, "vein", "Draws lines connecting crosshair to entities.");
    }

    @Listen
    private void onRender3D(Render3D event) {
        if (mc.level == null || mc.player == null || style.get() != Style.Lines) return;

        double px = mc.player.getX();
        double py = mc.player.getEyeY();
        double pz = mc.player.getZ();

        double maxDistSq = maxDistance.get() * maxDistance.get();
        List<EntityType<?>> filter = entities.get();

        for (Entity entity : mc.level.entitiesForRendering()) {
            if (entity == null || entity == mc.player) continue;
            if (!filter.contains(entity.getType())) continue;
            if (!showInvisible.get() && entity.isInvisible()) continue;

            boolean isCrew = entity instanceof Player player && Team.get().isFriend(player.getUUID());
            if (isCrew && ignoreFriends.get()) continue;

            double distSq = mc.player.distanceToSqr(entity);
            if (distSq > maxDistSq) continue;

            double targetY = switch (target.get()) {
                case Head -> entity.getY() + entity.getEyeHeight();
                case Body -> entity.getY() + (entity.getBbHeight() / 2.0);
                case Feet -> entity.getY();
            };

            Color color = getColor(entity, Math.sqrt(distSq), isCrew);

            Solid.INSTANCE.line(px, py, pz, entity.getX(), targetY, entity.getZ(), color);

            if (stem.get()) {
                Solid.INSTANCE.line(entity.getX(), entity.getY(), entity.getZ(), entity.getX(), entity.getY() + entity.getBbHeight(), entity.getZ(), color);
            }
        }
    }

    @Listen
    private void onRender2D(Render2D event) {
        if (mc.level == null || mc.player == null || style.get() != Style.Offscreen) return;
        
    }

    private Color getColor(Entity entity, double dist, boolean isCrew) {
        if (isCrew && showFriendColors.get()) {
            return Team.CREW_COLOR;
        }

        if (distanceColors.get()) {
            float frac = (float) Math.min(1.0, dist / maxDistance.get());
            int r = (int) (255 * frac);
            int g = (int) (255 * (1.0f - frac));
            return new Color(r, g, 0, 180);
        }

        if (entity instanceof Player) return playersColor.get();
        if (entity instanceof Monster) return monstersColor.get();
        net.minecraft.world.entity.MobCategory cat = entity.getType().getCategory();
        if (cat == net.minecraft.world.entity.MobCategory.WATER_CREATURE || cat == net.minecraft.world.entity.MobCategory.WATER_AMBIENT || cat == net.minecraft.world.entity.MobCategory.UNDERGROUND_WATER_CREATURE) {
            return waterAnimalsColor.get();
        }
        if (entity instanceof Animal) return animalsColor.get();
        if (cat == net.minecraft.world.entity.MobCategory.AMBIENT) return ambientColor.get();
        return miscColor.get();
    }
}