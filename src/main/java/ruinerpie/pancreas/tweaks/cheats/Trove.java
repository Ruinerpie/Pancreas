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

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.TrappedChestBlock;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Trove extends Feature {
    public enum Mode {
        Box,
        Shader
    }

    private final ValueGroup sgGeneral = settings.getDefaultGroup();
    private final ValueGroup sgColors = settings.createGroup("Colors");
    private final ValueGroup sgOpened = settings.createGroup("Opened Rendering");

    public final Value<Mode> mode = sgGeneral.add(new ChoiceValue.Builder<Mode>()
        .name("mode")
        .description("Rendering mode.")
        .defaultValue(Mode.Box)
        .build()
    );

    public final Value<List<StorageKind>> storageBlocks = sgGeneral.add(new StorageListValue.Builder()
        .name("storage-blocks")
        .description("Which storage containers to highlight.")
        .build()
    );

    public final Value<Boolean> tracers = sgGeneral.add(new FlagValue.Builder()
        .name("tracers")
        .description("Renders lines to storage blocks.")
        .defaultValue(false)
        .build()
    );

    public final Value<Shape> shapeMode = sgGeneral.add(new ChoiceValue.Builder<Shape>()
        .name("shape-mode")
        .description("How the bounding box is rendered.")
        .defaultValue(Shape.Both)
        .build()
    );

    public final Value<Integer> fillOpacity = sgGeneral.add(new IntValue.Builder()
        .name("fill-opacity")
        .description("Opacity of filled sides.")
        .defaultValue(50)
        .min(0)
        .max(255)
        .sliderRange(0, 255)
        .build()
    );

    public final Value<Double> fadeDistance = sgGeneral.add(new DoubleValue.Builder()
        .name("fade-distance")
        .description("Distance threshold to fade out highlights.")
        .defaultValue(6.0)
        .min(1.0)
        .max(32.0)
        .build()
    );

    public final Value<Tint> chest = sgColors.add(new TintValue.Builder()
        .name("chest")
        .description("Color for regular chests.")
        .defaultValue(new Tint(255, 160, 0, 255))
        .build()
    );

    public final Value<Tint> trappedChest = sgColors.add(new TintValue.Builder()
        .name("trapped-chest")
        .description("Color for trapped chests.")
        .defaultValue(new Tint(255, 0, 0, 255))
        .build()
    );

    public final Value<Tint> barrel = sgColors.add(new TintValue.Builder()
        .name("barrel")
        .description("Color for barrels.")
        .defaultValue(new Tint(255, 160, 0, 255))
        .build()
    );

    public final Value<Tint> shulker = sgColors.add(new TintValue.Builder()
        .name("shulker")
        .description("Color for shulker boxes.")
        .defaultValue(new Tint(255, 160, 0, 255))
        .build()
    );

    public final Value<Tint> enderChest = sgColors.add(new TintValue.Builder()
        .name("ender-chest")
        .description("Color for ender chests.")
        .defaultValue(new Tint(120, 0, 255, 255))
        .build()
    );

    public final Value<Tint> other = sgColors.add(new TintValue.Builder()
        .name("other")
        .description("Color for other storage blocks.")
        .defaultValue(new Tint(140, 140, 140, 255))
        .build()
    );

    public final Value<Boolean> hideOpened = sgOpened.add(new FlagValue.Builder()
        .name("hide-opened")
        .description("Hides highlight if container has been opened.")
        .defaultValue(false)
        .build()
    );

    public final Value<Tint> openedColor = sgOpened.add(new TintValue.Builder()
        .name("opened-color")
        .description("Color used when opened container is rendered.")
        .defaultValue(new Tint(203, 90, 203, 0))
        .visible(() -> !hideOpened.get())
        .build()
    );

    private final Set<BlockPos> openedBlocks = new HashSet<>();

    public Trove() {
        super(Group.Cheats, "trove", "Highlight storage block entities through walls.");
    }

    public void clearCache() {
        openedBlocks.clear();
    }

    public void markOpened(BlockPos pos) {
        openedBlocks.add(pos);
    }

    @Listen
    private void onRender3D(Render3D event) {
        if (mc.level == null || mc.player == null) return;

        int playerChunkX = mc.player.getBlockX() >> 4;
        int playerChunkZ = mc.player.getBlockZ() >> 4;
        int radius = 6;

        List<StorageKind> activeTypes = storageBlocks.get();
        int opacity = fillOpacity.get();

        for (int cx = playerChunkX - radius; cx <= playerChunkX + radius; cx++) {
            for (int cz = playerChunkZ - radius; cz <= playerChunkZ + radius; cz++) {
                LevelChunk chunk = mc.level.getChunkSource().getChunk(cx, cz, false);
                if (chunk == null) continue;

                for (BlockEntity be : chunk.getBlockEntities().values()) {
                    StorageKind type = getStorageType(be);
                    if (type == null || !activeTypes.contains(type)) continue;

                    BlockPos pos = be.getBlockPos();
                    boolean isOpened = openedBlocks.contains(pos);
                    if (isOpened && hideOpened.get()) continue;

                    Color baseColor = isOpened ? openedColor.get() : getColorForType(type);
                    Color sideColor = new Color(baseColor.r, baseColor.g, baseColor.b, opacity);
                    Color lineColor = new Color(baseColor.r, baseColor.g, baseColor.b, 255);

                    Solid.INSTANCE.box(pos, sideColor, lineColor, shapeMode.get(), 0);

                    if (tracers.get()) {
                        Solid.INSTANCE.line(
                            mc.player.getX(), mc.player.getEyeY(), mc.player.getZ(),
                            pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                            lineColor
                        );
                    }
                }
            }
        }
    }

    private StorageKind getStorageType(BlockEntity be) {
        if (be instanceof ChestBlockEntity) {
            return be.getBlockState().getBlock() instanceof TrappedChestBlock ? StorageKind.TRAPPED_CHEST : StorageKind.CHEST;
        } else if (be instanceof BarrelBlockEntity) {
            return StorageKind.BARREL;
        } else if (be instanceof ShulkerBoxBlockEntity) {
            return StorageKind.SHULKER;
        } else if (be instanceof EnderChestBlockEntity) {
            return StorageKind.ENDER_CHEST;
        } else if (be instanceof AbstractFurnaceBlockEntity || be instanceof DispenserBlockEntity || be instanceof HopperBlockEntity || be instanceof BrewingStandBlockEntity) {
            return StorageKind.OTHER;
        }
        return null;
    }

    private Color getColorForType(StorageKind type) {
        return switch (type) {
            case CHEST -> chest.get();
            case TRAPPED_CHEST -> trappedChest.get();
            case BARREL -> barrel.get();
            case SHULKER -> shulker.get();
            case ENDER_CHEST -> enderChest.get();
            default -> other.get();
        };
    }
}