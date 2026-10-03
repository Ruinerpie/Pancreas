package ruinerpie.pancreas.kit;

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

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;

import java.util.function.Consumer;

public class BlockIterator {
    private static final Minecraft mc = Minecraft.getInstance();

    public static void register(int hRange, int vRange, Consumer<BlockPos> action) {
        if (mc.player == null || mc.level == null || action == null) return;
        BlockPos playerPos = mc.player.blockPosition();

        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        for (int x = -hRange; x <= hRange; x++) {
            for (int y = -vRange; y <= vRange; y++) {
                for (int z = -hRange; z <= hRange; z++) {
                    mutable.set(playerPos.getX() + x, playerPos.getY() + y, playerPos.getZ() + z);
                    action.accept(mutable);
                }
            }
        }
    }
}