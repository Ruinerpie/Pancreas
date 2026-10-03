package ruinerpie.pancreas.mixin;

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

import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class PlayerMixin {
    @Inject(method = "sendPosition", at = @At("HEAD"), cancellable = true)
    private void onSendMovementPacketsPre(CallbackInfo info) {
        SendMovementPackets.Pre event = new SendMovementPackets.Pre();
        Pancreas.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            info.cancel();
        }
    }

    @Inject(method = "sendPosition", at = @At("TAIL"))
    private void onSendMovementPacketsPost(CallbackInfo info) {
        Pancreas.EVENT_BUS.post(new SendMovementPackets.Post());
    }
}