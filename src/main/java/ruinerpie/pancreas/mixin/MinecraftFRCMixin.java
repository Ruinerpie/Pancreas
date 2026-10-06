package ruinerpie.pancreas.mixin;

import ruinerpie.pancreas.Features;
import ruinerpie.pancreas.tweaks.utilities.FRC;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayDeque;

@Mixin(Minecraft.class)
public abstract class MinecraftFRCMixin {

    @Unique private static final int FRC$MAX_FRAME_USES_PER_TICK = 3;
    @Unique private static final int FRC$RECENT_SIZE = 8;

    @Shadow private int rightClickDelay;

    @Shadow protected abstract void startUseItem();

    @Unique private BlockPos frc$lastTargetPos;
    @Unique private Direction frc$lastTargetFace;
    @Unique private final ArrayDeque<BlockPos> frc$recentPlaced = new ArrayDeque<>();
    @Unique private int frc$frameUsesThisTick;
    @Unique private boolean frc$tickUsedThisHold;
    @Unique private boolean frc$inFrameUse;

    @Inject(method = "tick", at = @At("HEAD"))
    private void frc$onTickHead(CallbackInfo ci) {
        frc$frameUsesThisTick = 0;
        if (!((Minecraft) (Object) this).options.keyUse.isDown()) frc$tickUsedThisHold = false;
    }

    @Inject(method = "startUseItem", at = @At("HEAD"))
    private void frc$onStartUseItemHead(CallbackInfo ci) {
        Minecraft mc = (Minecraft) (Object) this;
        if (!frc$inFrameUse) frc$tickUsedThisHold = true;
        if (mc.hitResult instanceof BlockHitResult bhr && bhr.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = bhr.getBlockPos();
            frc$lastTargetPos = pos;
            frc$lastTargetFace = bhr.getDirection();
            if (mc.level != null && mc.level.getBlockState(pos).canBeReplaced()) frc$remember(pos);
            else frc$remember(pos.relative(bhr.getDirection()));
        }
    }

    @Inject(method = "startUseItem", at = @At(value = "FIELD",
        target = "Lnet/minecraft/client/Minecraft;rightClickDelay:I",
        opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER))
    private void frc$applyCooldown(CallbackInfo ci) {
        FRC frc = Features.get().get(FRC.class);
        if (frc == null || !frc.isActive()) return;

        Minecraft mc = (Minecraft) (Object) this;
        if (mc.player == null) return;
        if (!frc.appliesTo(mc.player.getMainHandItem(), mc.player.getOffhandItem())) return;

        rightClickDelay = frc.getAppliedDelay();
    }

    @Inject(method = "renderFrame", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/client/Minecraft;pick(F)V", shift = At.Shift.AFTER))
    private void frc$onFramePick(CallbackInfo ci) {
        FRC frc = Features.get().get(FRC.class);
        if (frc == null || !frc.isActive() || !frc.fillGaps()) return;

        Minecraft mc = (Minecraft) (Object) this;
        if (mc.screen != null || mc.player == null || mc.level == null || mc.gameMode == null) return;
        if (!mc.options.keyUse.isDown()) {
            frc$tickUsedThisHold = false;
            return;
        }
        if (!frc$tickUsedThisHold) return;
        if (mc.player.isUsingItem() || mc.gameMode.isDestroying()) return;
        if (frc$frameUsesThisTick >= FRC$MAX_FRAME_USES_PER_TICK) return;

        if (!(mc.player.getMainHandItem().getItem() instanceof BlockItem)
            && !(mc.player.getOffhandItem().getItem() instanceof BlockItem)) return;

        if (!(mc.hitResult instanceof BlockHitResult bhr) || bhr.getType() != HitResult.Type.BLOCK) return;

        BlockPos pos = bhr.getBlockPos();
        Direction face = bhr.getDirection();

        if (pos.equals(frc$lastTargetPos) && face == frc$lastTargetFace) return;
        if (frc$recentPlaced.contains(pos)) return;

        frc$frameUsesThisTick++;
        frc$inFrameUse = true;
        try {
            startUseItem();
        } finally {
            frc$inFrameUse = false;
        }
    }

    @Unique
    private void frc$remember(BlockPos pos) {
        frc$recentPlaced.addLast(pos.immutable());
        while (frc$recentPlaced.size() > FRC$RECENT_SIZE) frc$recentPlaced.removeFirst();
    }
}
