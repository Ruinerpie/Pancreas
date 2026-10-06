package ruinerpie.pancreas.mixin;

import ruinerpie.pancreas.Pancreas;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Options;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Options.class)
public class OptionsMixin {
    @Shadow
    @Final
    @Mutable
    public KeyMapping[] keyMappings;

    @Inject(method = "load", at = @At("HEAD"))
    private void onLoad(CallbackInfo info) {
        if (this.keyMappings != null) {
            boolean found = false;
            for (KeyMapping k : this.keyMappings) {
                if (k == Pancreas.KEY_CONFIG || (k != null && Pancreas.KEY_CONFIG.getName().equals(k.getName()))) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                this.keyMappings = ArrayUtils.add(this.keyMappings, Pancreas.KEY_CONFIG);
            }
        }
    }
}
