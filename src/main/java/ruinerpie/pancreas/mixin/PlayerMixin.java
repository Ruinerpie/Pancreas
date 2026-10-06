package ruinerpie.pancreas.mixin;

import ruinerpie.pancreas.Features;
import ruinerpie.pancreas.Pancreas;
import ruinerpie.pancreas.signals.ClipAtLedge;
import ruinerpie.pancreas.tweaks.utilities.EdgeShift;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerMixin {
    @Inject(method = "isStayingOnGroundSurface", at = @At("HEAD"), cancellable = true)
    private void onIsStayingOnGroundSurface(CallbackInfoReturnable<Boolean> cir) {
        Minecraft mc = Minecraft.getInstance();
        if (mc != null && (Object) this == mc.player) {
            EdgeShift edgeShift = Features.get().get(EdgeShift.class);
            if (edgeShift != null && edgeShift.isActive()) {
                ClipAtLedge event = new ClipAtLedge();
                Pancreas.EVENT_BUS.post(event);
                if (event.isClip()) {
                    cir.setReturnValue(true);
                }
            }
        }
    }
}
