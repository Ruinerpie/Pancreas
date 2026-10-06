package ruinerpie.pancreas.kit;

import ruinerpie.pancreas.*;
import ruinerpie.pancreas.mixin.*;
import ruinerpie.pancreas.mixin.accessor.*;
import ruinerpie.pancreas.values.*;
import ruinerpie.pancreas.signals.*;
import ruinerpie.pancreas.kit.*;
import ruinerpie.pancreas.draw.*;

public class SlotKit {
    public static final int OFFHAND = 45;
    public static final int MAIN_START = 9;
    public static final int MAIN_END = 35;
    public static final int HOTBAR_START = 0;
    public static final int HOTBAR_END = 8;
    public static final int ARMOR_START = 36;
    public static final int ARMOR_END = 39;

    public static boolean isArmor(int slot) {
        return (slot >= 5 && slot <= 8) || (slot >= ARMOR_START && slot <= ARMOR_END);
    }

    public static boolean isHotbar(int slot) {
        return (slot >= 0 && slot < 9) || (slot >= 36 && slot <= 44);
    }

    public static int indexToId(int index) {
        if (index >= 0 && index < 9) return index + 36;
        return index;
    }
}
