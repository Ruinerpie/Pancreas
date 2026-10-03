package ruinerpie.pancreas;

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

import org.lwjgl.glfw.GLFW;
import net.minecraft.client.Minecraft;

public final class Keys {
    private static final Keys INSTANCE = new Keys();
    private int guiKey = GLFW.GLFW_KEY_P; 

    public static Keys get() {
        return INSTANCE;
    }

    public int getGuiKey() {
        return guiKey;
    }

    public void setGuiKey(int key) {
        this.guiKey = key;
    }

    public boolean onKeyPress(int key) {
        if (key <= 0) return false;
        if (Minecraft.getInstance().screen != null) {
            return false;
        }
        
        if (key == guiKey || key == GLFW.GLFW_KEY_RIGHT_SHIFT) {
            Pancreas.openGui();
            return true;
        }

        boolean handled = false;
        for (Feature module : Features.get().all()) {
            if (module.getKeybind() == key) {
                module.toggle();
                handled = true;
            }
        }

        for (ruinerpie.pancreas.saved.Macro action : ruinerpie.pancreas.saved.Macros.get().all()) {
            if (action.keybind == key) {
                ruinerpie.pancreas.saved.Macros.get().trigger(action.id);
                handled = true;
            }
        }

        return handled;
    }
}