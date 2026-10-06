package ruinerpie.pancreas.mixin;

import ruinerpie.pancreas.Pancreas;
import ruinerpie.pancreas.signals.Render2D;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {
    @Inject(method = "extractRenderState", at = @At("RETURN"))
    private void onExtractRenderState(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo info) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui) return;

        float tickDelta = deltaTracker.getGameTimeDeltaPartialTick(false);
        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();

        Render2D event = new Render2D(graphics, tickDelta, width, height);
        Pancreas.EVENT_BUS.post(event);
    }
}
