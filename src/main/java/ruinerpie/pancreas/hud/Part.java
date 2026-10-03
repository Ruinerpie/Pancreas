package ruinerpie.pancreas.hud;

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

public abstract class Part {
    public enum Anchor {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT,
        CENTER
    }

    public final String id;
    public final String name;
    public final String description;

    public boolean enabled = true;
    public int x = 5;
    public int y = 5;
    public float scale = 1.0f;
    public Anchor anchor = Anchor.TOP_LEFT;

    public Part(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public abstract void render(Context ctx);
    public abstract int getWidth();
    public abstract int getHeight();

    public boolean isVisible() {
        return enabled;
    }

    public void setEnabled(boolean state) {
        if (this.enabled == state) return;
        this.enabled = state;
        if (state) onEnable();
        else onDisable();
    }

    public void toggle() {
        setEnabled(!enabled);
    }

    public void onEnable() {}
    public void onDisable() {}

    public int getRenderX(int screenWidth) {
        int w = (int) (getWidth() * scale);
        return switch (anchor) {
            case TOP_LEFT, BOTTOM_LEFT -> x;
            case TOP_RIGHT, BOTTOM_RIGHT -> screenWidth - w - x;
            case CENTER -> (screenWidth - w) / 2 + x;
        };
    }

    public int getRenderY(int screenHeight) {
        int h = (int) (getHeight() * scale);
        return switch (anchor) {
            case TOP_LEFT, TOP_RIGHT -> y;
            case BOTTOM_LEFT, BOTTOM_RIGHT -> screenHeight - h - y;
            case CENTER -> (screenHeight - h) / 2 + y;
        };
    }
}