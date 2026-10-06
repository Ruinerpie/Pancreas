package ruinerpie.pancreas.kit;

import ruinerpie.pancreas.*;
import ruinerpie.pancreas.mixin.*;
import ruinerpie.pancreas.mixin.accessor.*;
import ruinerpie.pancreas.values.*;
import ruinerpie.pancreas.signals.*;
import ruinerpie.pancreas.kit.*;
import ruinerpie.pancreas.draw.*;

import net.minecraft.client.Minecraft;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class InventoryKit {
    public static final Minecraft mc = Minecraft.getInstance();
    public static int previousSlot = -1;

    public static FindResult find(Item item) {
        if (mc.player == null) return new FindResult(-1, 0);
        int size = mc.player.getInventory().getContainerSize();
        for (int i = 0; i < size; i++) {
            ItemStack stack = mc.player.getInventory().getItem(i);
            if (stack.getItem() == item) return new FindResult(i, stack.getCount());
        }
        return new FindResult(-1, 0);
    }

    public static FindResult find(Predicate<ItemStack> predicate) {
        if (mc.player == null) return new FindResult(-1, 0);
        int size = mc.player.getInventory().getContainerSize();
        for (int i = 0; i < size; i++) {
            ItemStack stack = mc.player.getInventory().getItem(i);
            if (predicate.test(stack)) return new FindResult(i, stack.getCount());
        }
        return new FindResult(-1, 0);
    }

    public static FindResult findInHotbar(Item item) {
        if (mc.player == null) return new FindResult(-1, 0);
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getItem(i);
            if (stack.getItem() == item) return new FindResult(i, stack.getCount());
        }
        return new FindResult(-1, 0);
    }

    public static FindResult findInHotbar(Predicate<ItemStack> predicate) {
        if (mc.player == null) return new FindResult(-1, 0);
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getItem(i);
            if (predicate.test(stack)) return new FindResult(i, stack.getCount());
        }
        return new FindResult(-1, 0);
    }

    public static FindResult findEmpty() {
        if (mc.player == null) return new FindResult(-1, 0);
        int size = mc.player.getInventory().getContainerSize();
        for (int i = 0; i < size; i++) {
            ItemStack stack = mc.player.getInventory().getItem(i);
            if (stack.isEmpty()) return new FindResult(i, 0);
        }
        return new FindResult(-1, 0);
    }

    public static boolean swap(int slot, boolean swapBack) {
        if (mc.player == null || slot < 0 || slot > 8) return false;
        if (swapBack) previousSlot = mc.player.getInventory().getSelectedSlot();
        mc.player.getInventory().setSelectedSlot(slot);
        return true;
    }

    public static boolean swapBack() {
        if (previousSlot != -1) {
            boolean res = swap(previousSlot, false);
            previousSlot = -1;
            return res;
        }
        return false;
    }

    public static class Action {
        private int fromSlot = -1;
        private int toSlot = -1;

        public Action from(int slot) { this.fromSlot = slot; return this; }
        public Action fromId(int id) { this.fromSlot = id; return this; }
        public Action to(int slot) { this.toSlot = slot; return this; }
        public Action toId(int id) { this.toSlot = id; return this; }
        public Action slot(int slot) { this.fromSlot = slot; return this; }
        public Action slotId(int slot) { this.fromSlot = slot; return this; }
        public Action fromArmor(int slot) { this.fromSlot = 8 - slot; return this; }
        public Action toArmor(int slot) { this.toSlot = 8 - slot; return this; }
        public Action fromOffhand() { this.fromSlot = 45; return this; }
        public Action toOffhand() {
            if (mc.gameMode != null && mc.player != null && fromSlot != -1) {
                mc.gameMode.handleContainerInput(mc.player.containerMenu.containerId, fromSlot, 40, ContainerInput.SWAP, mc.player);
            }
            return this;
        }
        public Action toHotbar(int slot) { this.toSlot = slot; return this; }

        public void run() {
            if (mc.gameMode != null && mc.player != null && fromSlot != -1) {
                mc.gameMode.handleContainerInput(mc.player.containerMenu.containerId, fromSlot, 0, ContainerInput.PICKUP, mc.player);
            }
        }
    }

    public static Action quickMove() { return new Action(); }
    public static Action click() { return new Action(); }
    public static Action shiftClick() { return new Action(); }
    public static Action quickSwap() { return new Action(); }
    public static Action move() { return new Action(); }
    public static Action drop() { return new Action(); }
}
