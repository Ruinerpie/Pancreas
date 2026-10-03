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

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;

public class Key {
    private int key = -1;
    private boolean isButton = false;

    public static Key fromKey(int key) {
        Key k = new Key();
        k.key = key;
        return k;
    }

    public static Key fromButton(int button) {
        Key k = new Key();
        k.key = button;
        k.isButton = true;
        return k;
    }

    public static Key none() {
        return new Key();
    }

    public int getValue() { return key; }
    public boolean isSet() { return key != -1; }

    public void set(int key, boolean isButton) {
        this.key = key;
        this.isButton = isButton;
    }

    public boolean isPressed() {
        if (key == -1) return false;
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow(), key);
    }

    public boolean matches(boolean isButton, int value) {
        return this.isButton == isButton && this.key == value;
    }

    public boolean matches(int key) {
        return !isButton && this.key == key;
    }

    public boolean matches(Object event) {
        return false;
    }
}