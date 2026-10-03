package ruinerpie.pancreas.kit;

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

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.registries.BuiltInRegistries;

public class Names {
    public static String get(Item item) {
        return item != null ? BuiltInRegistries.ITEM.getKey(item).getPath() : "";
    }

    public static String get(Block block) {
        return block != null ? BuiltInRegistries.BLOCK.getKey(block).getPath() : "";
    }
}