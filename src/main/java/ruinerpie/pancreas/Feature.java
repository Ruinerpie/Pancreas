package ruinerpie.pancreas;

import ruinerpie.pancreas.values.Value;
import ruinerpie.pancreas.values.Values;
import ruinerpie.pancreas.values.ValueGroup;

import net.minecraft.client.Minecraft;

public abstract class Feature {
    public static Minecraft mc = Minecraft.getInstance();

    public final String name;
    public final String id;
    public final Group category;
    public final String description;
    public final Values settings = new Values();

    private boolean active;
    private int keybind = -1;   
    public boolean runInMainMenu = false;

    public Feature(Group category, String id, String description) {
        this.category = category;
        this.id = id;
        this.description = description;
        this.name = deriveDisplayName(id);   
    }

    public Feature(Group category, String id, String name, String description) {
        this.category = category;
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public final void toggle()   { setActive(!active); }
    public final void enable()   { setActive(true); }
    public final void disable()  { setActive(false); }
    public final boolean isActive() { return active; }

    public void setActive(boolean state) {
        if (state == active) return;
        active = state;
        if (state) {
            Signals.get().subscribe(this);
            onActivate();
        } else {
            onDeactivate();
            Signals.get().unsubscribe(this);
        }
    }

    protected void onActivate()   {}
    protected void onDeactivate() {}

    public int getKeybind()           { return keybind; }
    public void setKeybind(int key)   { this.keybind = key; }

    public String getKeybindName() {
        if (keybind <= 0) return "NONE";
        String glfwName = org.lwjgl.glfw.GLFW.glfwGetKeyName(keybind, 0);
        if (glfwName != null && !glfwName.isEmpty()) return glfwName.toUpperCase();
        return switch (keybind) {
            case org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE -> "SPACE";
            case org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT -> "LSHIFT";
            case org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT_SHIFT -> "RSHIFT";
            case org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_CONTROL -> "LCTRL";
            case org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT_CONTROL -> "RCTRL";
            case org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_ALT -> "LALT";
            case org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT_ALT -> "RALT";
            case org.lwjgl.glfw.GLFW.GLFW_KEY_TAB -> "TAB";
            default -> "KEY_" + keybind;
        };
    }

    public void resetSettings() {
        for (ValueGroup sg : settings.groups()) {
            for (Value<?> s : sg.getSettings()) {
                s.reset();
            }
        }
    }

    public void info(String msg) {
        Toasts.get().info(name, msg);
    }

    public void warning(String msg) {
        Toasts.get().warning(name, msg);
    }

    public void error(String msg) {
        Toasts.get().error(name, msg);
    }

    private static String deriveDisplayName(String id) {
        StringBuilder sb = new StringBuilder();
        for (String word : id.split("-")) {
            if (!word.isEmpty()) {
                sb.append(Character.toUpperCase(word.charAt(0)))
                  .append(word.substring(1)).append(' ');
            }
        }
        return sb.toString().trim();
    }
}
