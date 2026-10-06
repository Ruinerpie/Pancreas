package ruinerpie.pancreas.mixin;

import ruinerpie.pancreas.Pancreas;
import ruinerpie.pancreas.signals.ClientTick;
import ruinerpie.pancreas.signals.WorldTick;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void onTickHead(CallbackInfo info) {
        while (Pancreas.KEY_CONFIG.consumeClick()) {
            Pancreas.openGui();
        }
        Pancreas.EVENT_BUS.post(new ClientTick());
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTickTail(CallbackInfo info) {
        Pancreas.EVENT_BUS.post(new WorldTick());
    }
}
