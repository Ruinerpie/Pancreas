package ruinerpie.pancreas.kit;

import ruinerpie.pancreas.*;
import ruinerpie.pancreas.mixin.*;
import ruinerpie.pancreas.mixin.accessor.*;
import ruinerpie.pancreas.values.*;
import ruinerpie.pancreas.signals.*;
import ruinerpie.pancreas.kit.*;
import ruinerpie.pancreas.draw.*;

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
