package ruinerpie.pancreas.tweaks.extras;

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

import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public class Forge extends Feature {
    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    public final Value<List<Item>> fuelItems = sgGeneral.add(new ItemListValue.Builder()
        .name("fuel-items")
        .description("Allowed furnace fuels.")
        .defaultValue(new ArrayList<>(List.of(Items.COAL, Items.CHARCOAL)))
        .build()
    );

    public final Value<List<Item>> smeltableItems = sgGeneral.add(new ItemListValue.Builder()
        .name("smeltable-items")
        .description("Items to automatically insert for smelting.")
        .defaultValue(new ArrayList<>(List.of(
            Items.RAW_IRON, Items.RAW_GOLD, Items.RAW_COPPER,
            Items.IRON_ORE, Items.DEEPSLATE_IRON_ORE,
            Items.GOLD_ORE, Items.DEEPSLATE_GOLD_ORE,
            Items.COPPER_ORE, Items.DEEPSLATE_COPPER_ORE
        )))
        .build()
    );

    public final Value<Boolean> disableWhenOutOfItems = sgGeneral.add(new FlagValue.Builder()
        .name("disable-when-out-of-items")
        .description("Automatically disable module when no smeltable items remain.")
        .defaultValue(true)
        .build()
    );

    public Forge() {
        super(Group.Extras, "forge", "Automatically inserts fuel and smeltables into furnaces.");
    }

    public void tickFurnace(AbstractFurnaceMenu menu) {
        if (!isActive() || mc.player == null) return;

        ItemStack outputStack = menu.getSlot(2).getItem();
        if (!outputStack.isEmpty()) {
            InventoryKit.quickMove().fromId(2).run();
        }

        ItemStack fuelStack = menu.getSlot(1).getItem();
        if (fuelStack.isEmpty() || fuelStack.getCount() < 16) {
            for (int i = 3; i < menu.slots.size(); i++) {
                ItemStack invStack = menu.getSlot(i).getItem();
                if (fuelItems.get().contains(invStack.getItem())) {
                    InventoryKit.quickMove().fromId(i).toId(1).run();
                    break;
                }
            }
        }

        ItemStack inputStack = menu.getSlot(0).getItem();
        if (inputStack.isEmpty() || inputStack.getCount() < 16) {
            boolean found = false;
            for (int i = 3; i < menu.slots.size(); i++) {
                ItemStack invStack = menu.getSlot(i).getItem();
                if (smeltableItems.get().contains(invStack.getItem())) {
                    InventoryKit.quickMove().fromId(i).toId(0).run();
                    found = true;
                    break;
                }
            }

            if (!found && inputStack.isEmpty() && disableWhenOutOfItems.get()) {
                Toasts.get().warning("SmeltLoop", "Out of items to smelt. Disabling.");
                toggle();
            }
        }
    }
}