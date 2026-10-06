package ruinerpie.pancreas.kit;

import ruinerpie.pancreas.*;
import ruinerpie.pancreas.mixin.*;
import ruinerpie.pancreas.mixin.accessor.*;
import ruinerpie.pancreas.values.*;
import ruinerpie.pancreas.signals.*;
import ruinerpie.pancreas.kit.*;
import ruinerpie.pancreas.draw.*;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

public class InputKit {
    public static int getKey(KeyMapping bind) {
        if (bind == null) return -1;
        return InputConstants.getKey(bind.saveString()).getValue();
    }

    public static boolean isKeyPressed(int key) {
        if (key == -1) return false;
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow(), key);
    }
}
