package ruinerpie.pancreas.mixin;

import ruinerpie.pancreas.Keys;
import ruinerpie.pancreas.Pancreas;
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
    @Inject(method = "keyPress", at = @At("HEAD"))
    private void onKeyPress(long windowPointer, int action, KeyEvent event, CallbackInfo info) {
        if (action == GLFW.GLFW_PRESS) {
            if (Pancreas.KEY_CONFIG.matches(event) || event.key() == GLFW.GLFW_KEY_P) {
                Minecraft mc = Minecraft.getInstance();
                if (mc != null && mc.screen == null) {
                    Pancreas.openGui();
                }
            } else {
                Keys.get().onKeyPress(event.key());
            }
        }
    }
}
