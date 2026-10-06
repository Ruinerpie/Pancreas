package ruinerpie.pancreas;

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

        if (key == guiKey || (Pancreas.KEY_CONFIG != null && ruinerpie.pancreas.kit.InputKit.getKey(Pancreas.KEY_CONFIG) == key)) {
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

        return handled;
    }
}
