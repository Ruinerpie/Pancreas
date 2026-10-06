package ruinerpie.pancreas.kit;

import ruinerpie.pancreas.*;
import ruinerpie.pancreas.mixin.*;
import ruinerpie.pancreas.mixin.accessor.*;
import ruinerpie.pancreas.values.*;
import ruinerpie.pancreas.signals.*;
import ruinerpie.pancreas.kit.*;
import ruinerpie.pancreas.draw.*;

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
        if (enchantments == null || key == null) return 0;
        for (var entry : enchantments.object2IntEntrySet()) {
            if (entry.getKey() != null && entry.getKey().is(key)) {
                return entry.getIntValue();
            }
        }
        return 0;
    }

    public static int getEnchantmentLevel(ItemStack stack, ResourceKey<Enchantment> key) {
        if (stack == null || stack.isEmpty() || key == null) return 0;
        var enchantments = stack.get(net.minecraft.core.component.DataComponents.ENCHANTMENTS);
        if (enchantments == null) return 0;
        for (var entry : enchantments.entrySet()) {
            if (entry.getKey() != null && entry.getKey().is(key)) {
                return entry.getIntValue();
            }
        }
        return 0;
    }

    public static void getEnchantments(ItemStack stack, Object2IntMap<Holder<Enchantment>> map) {
        if (stack == null || stack.isEmpty() || map == null) return;
        var enchantments = stack.get(net.minecraft.core.component.DataComponents.ENCHANTMENTS);
        if (enchantments == null) return;
        for (var entry : enchantments.entrySet()) {
            map.put(entry.getKey(), entry.getIntValue());
        }
    }

    public static boolean hasEnchantments(ItemStack stack, ResourceKey<Enchantment> key) {
        return getEnchantmentLevel(stack, key) > 0;
    }

    public static boolean hasEnchantment(ItemStack stack, ResourceKey<Enchantment> key) {
        return getEnchantmentLevel(stack, key) > 0;
    }
}
