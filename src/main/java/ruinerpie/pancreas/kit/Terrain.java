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

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import java.util.Random;

public class Terrain {
    public static final Minecraft mc = Minecraft.getInstance();
    private static final Random RANDOM = new Random();

    public static boolean canUpdate() {
        return mc != null && mc.player != null && mc.level != null;
    }

    public static int random(int min, int max) {
        if (min >= max) return min;
        return min + RANDOM.nextInt(max - min);
    }

    public static double random(double min, double max) {
        if (min >= max) return min;
        return min + (max - min) * RANDOM.nextDouble();
    }

    public static void leftClick() {
        if (mc != null) {
            try {
                java.lang.reflect.Method m = Minecraft.class.getDeclaredMethod("startAttack");
                m.setAccessible(true);
                m.invoke(mc);
            } catch (Exception ignored) {}
        }
    }

    public static void rightClick() {
        if (mc != null) {
            try {
                java.lang.reflect.Method m = Minecraft.class.getDeclaredMethod("startUseItem");
                m.setAccessible(true);
                m.invoke(mc);
            } catch (Exception ignored) {}
        }
    }

    public static int getEnchantmentLevel(Object2IntMap<Holder<Enchantment>> enchantments, ResourceKey<Enchantment> key) {
        return 0;
    }

    public static int getEnchantmentLevel(ItemStack stack, ResourceKey<Enchantment> key) {
        return 0;
    }

    public static void getEnchantments(ItemStack stack, Object2IntMap<Holder<Enchantment>> map) {
    }

    public static boolean hasEnchantments(ItemStack stack, ResourceKey<Enchantment> key) {
        return false;
    }

    public static boolean hasEnchantment(ItemStack stack, ResourceKey<Enchantment> key) {
        return false;
    }
}
