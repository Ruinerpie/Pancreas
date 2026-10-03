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

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LightLayer;

import java.util.ArrayList;
import java.util.List;

public class Grid extends Feature {
    private final ValueGroup sgGeneral = settings.getDefaultGroup();
    private final ValueGroup sgColors = settings.createGroup("Colors");

    public final Value<Integer> horizontalRange = sgGeneral.add(new IntValue.Builder()
        .name("horizontal-range")
        .description("Horizontal block scan radius.")
        .defaultValue(8)
        .min(0)
        .max(32)
        .sliderRange(0, 32)
        .build()
    );

    public final Value<Integer> verticalRange = sgGeneral.add(new IntValue.Builder()
        .name("vertical-range")
        .description("Vertical block scan radius.")
        .defaultValue(4)
        .min(0)
        .max(16)
        .sliderRange(0, 16)
        .build()
    );

    public final Value<Boolean> seeThroughBlocks = sgGeneral.add(new FlagValue.Builder()
        .name("see-through-blocks")
        .description("Renders spawn indicators through walls.")
        .defaultValue(false)
        .build()
    );

    public final Value<Integer> lightLevel = sgGeneral.add(new IntValue.Builder()
        .name("light-level")
        .description("Maximum light level considered mob-spawnable.")
        .defaultValue(0)
        .min(0)
        .max(15)
        .sliderRange(0, 15)
        .build()
    );

    public final Value<Tint> color = sgColors.add(new TintValue.Builder()
        .name("color")
        .description("Color for definite spawn blocks (zero light).")
        .defaultValue(new Tint(225, 25, 25, 255))
        .build()
    );

    public final Value<Tint> potentialColor = sgColors.add(new TintValue.Builder()
        .name("potential-color")
        .description("Color for night-time only spawn blocks (sky light present).")
        .defaultValue(new Tint(225, 225, 25, 255))
        .build()
    );

    private final List<SpawnPoint> cachedPoints = new ArrayList<>();

    public Grid() {
        super(Group.Extras, "grid", "Displays mob spawning light level overlay on top of blocks.");
    }

    @Listen
    private void onTick(ClientTick event) {
        if (mc.player == null || mc.level == null) return;

        cachedPoints.clear();
        int px = mc.player.getBlockX();
        int py = mc.player.getBlockY();
        int pz = mc.player.getBlockZ();

        int hr = horizontalRange.get();
        int vr = verticalRange.get();
        int threshold = lightLevel.get();

        for (int x = px - hr; x <= px + hr; x++) {
            for (int z = pz - hr; z <= pz + hr; z++) {
                for (int y = py - vr; y <= py + vr; y++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (BlockKit.isValidMobSpawn(mc.level, pos)) {
                        int blockLight = mc.level.getBrightness(LightLayer.BLOCK, pos);
                        int skyLight = mc.level.getBrightness(LightLayer.SKY, pos);

                        if (blockLight <= threshold) {
                            boolean potential = (skyLight > 0 && blockLight == 0);
                            cachedPoints.add(new SpawnPoint(pos, potential));
                        }
                    }
                }
            }
        }
    }

    @Listen
    private void onRender3D(Render3D event) {
        if (mc.level == null || cachedPoints.isEmpty()) return;

        for (SpawnPoint point : cachedPoints) {
            Color c = point.potential ? potentialColor.get() : color.get();
            double x = point.pos.getX();
            double y = point.pos.getY() + 0.02;
            double z = point.pos.getZ();

            Solid.INSTANCE.line(x, y, z, x + 1.0, y, z + 1.0, c);
            Solid.INSTANCE.line(x + 1.0, y, z, x, y, z + 1.0, c);
        }
    }

    private record SpawnPoint(BlockPos pos, boolean potential) {}
}