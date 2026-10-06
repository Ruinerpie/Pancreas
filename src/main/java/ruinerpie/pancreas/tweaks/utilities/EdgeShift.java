package ruinerpie.pancreas.tweaks.utilities;

import ruinerpie.pancreas.Feature;
import ruinerpie.pancreas.Group;
import ruinerpie.pancreas.Listen;
import ruinerpie.pancreas.values.ValueGroup;
import ruinerpie.pancreas.values.ChoiceValue;
import ruinerpie.pancreas.values.Value;
import ruinerpie.pancreas.signals.ClipAtLedge;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EdgeShift extends Feature {

    public enum BlockHeight {
        _1(1), _2(2), _3(3), _4(4), _5(5);

        public final int height;

        BlockHeight(int h) {
            this.height = h;
        }

        @Override
        public String toString() {
            return String.valueOf(height);
        }
    }

    public enum EdgeDistance {
        _0_06(0.06), _0_1(0.10), _0_2(0.20), _0_3(0.30);

        public final double distance;

        EdgeDistance(double d) {
            this.distance = d;
        }

        @Override
        public String toString() {
            return String.valueOf(distance);
        }
    }

    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    public final Value<BlockHeight> minimumBlockHeight = sgGeneral.add(new ChoiceValue.Builder<BlockHeight>()
        .name("Minimum Block Height")
        .description("Minimum fall height in blocks before ledge stopping activates.")
        .defaultValue(BlockHeight._1)
        .build()
    );

    public final Value<EdgeDistance> minimumEdgeDistance = sgGeneral.add(new ChoiceValue.Builder<EdgeDistance>()
        .name("Minimum Edge Distance")
        .description("Distance offset threshold before reaching an edge.")
        .defaultValue(EdgeDistance._0_06)
        .build()
    );

    public EdgeShift() {
        super(Group.Utilities, "edge-shift", "EdgeShift", "Prevents walking or falling off block edges.");
    }

    @Listen
    private void onClipAtLedge(ClipAtLedge event) {
        Minecraft client = Minecraft.getInstance();
        if (!isActive() || client == null || client.player == null || client.level == null || client.player.input == null || client.player.input.keyPresses == null) return;

        int reqHeight = minimumBlockHeight.get().height;
        if (reqHeight > 1) {
            Vec3 delta = client.player.getDeltaMovement();
            if (Math.abs(delta.x) >= 0.0001 || Math.abs(delta.z) >= 0.0001) {
                double offX = Math.signum(delta.x) * 0.5;
                double offZ = Math.signum(delta.z) * 0.5;
                Vec3 start = new Vec3(client.player.getX() + offX, client.player.getY(), client.player.getZ() + offZ);
                Vec3 end = new Vec3(start.x, start.y - reqHeight, start.z);
                BlockHitResult raycastResult = client.level.clip(new ClipContext(
                    start,
                    end,
                    ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.WATER,
                    client.player
                ));
                if (raycastResult.getType() != HitResult.Type.MISS) {
                    return;
                }
            }
        }

        double offset = minimumEdgeDistance.get().distance;
        AABB playerBox = client.player.getBoundingBox();
        AABB adjustedBox = playerBox
            .expandTowards(0, -client.player.maxUpStep(), 0)
            .inflate(-offset, 0, -offset);

        if (client.player.onGround()) {
            if (!client.player.horizontalCollision) {
                event.setClip(true);
            }
            if (client.level.noCollision(client.player, adjustedBox)) {
                client.player.input.keyPresses = new Input(
                    client.player.input.keyPresses.forward(),
                    client.player.input.keyPresses.backward(),
                    client.player.input.keyPresses.left(),
                    client.player.input.keyPresses.right(),
                    client.player.input.keyPresses.jump(),
                    true,
                    client.player.input.keyPresses.sprint()
                );
            }
        }
    }
}
