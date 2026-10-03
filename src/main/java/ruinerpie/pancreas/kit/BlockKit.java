package ruinerpie.pancreas.kit;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockKit {
    public static final Minecraft mc = Minecraft.getInstance();

    public static boolean canBreak(BlockPos pos, BlockState state) {
        var level = mc.level;
        if (level == null || state == null || pos == null) return false;
        return state.getDestroySpeed(level, pos) >= 0;
    }

    @SuppressWarnings("deprecation")
    public static boolean isValidMobSpawn(net.minecraft.world.level.Level level, BlockPos pos) {
        if (level == null || pos == null) return false;
        BlockState below = level.getBlockState(pos.below());
        if (!below.blocksMotion()) return false;
        BlockState state = level.getBlockState(pos);
        if (!state.isAir()) return false;
        BlockState above = level.getBlockState(pos.above());
        return above.isAir();
    }
}