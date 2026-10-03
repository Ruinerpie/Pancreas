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

import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.KeyEvent;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class KeyboardMixin {
    @Inject(method = "keyPress", at = @At("HEAD"), cancellable = true)
    private void onKeyPress(long window, int action, KeyEvent keyEvent, CallbackInfo info) {
        int key = keyEvent.key();
        if (key != GLFW.GLFW_KEY_UNKNOWN) {
            ActionKind keyAction = ActionKind.get(action);
            KeyPress event = new KeyPress(key, keyAction);
            Pancreas.EVENT_BUS.post(event);
            if (event.isCancelled()) {
                info.cancel();
                return;
            }

            if (action == GLFW.GLFW_PRESS) {
                if (ruinerpie.pancreas.Keys.get().onKeyPress(key)) {
                    info.cancel();
                    return;
                }
            }
        }
    }
}