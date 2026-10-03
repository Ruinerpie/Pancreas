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

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Stockpile extends Feature {
    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    public final Value<Integer> minCount = sgGeneral.add(new IntValue.Builder()
        .name("min-count")
        .description("Refill trigger count threshold.")
        .defaultValue(8)
        .min(1)
        .max(63)
        .sliderRange(1, 63)
        .build()
    );

    public final Value<Integer> delay = sgGeneral.add(new IntValue.Builder()
        .name("delay")
        .description("Tick delay between replenishments.")
        .defaultValue(1)
        .min(0)
        .max(20)
        .sliderRange(0, 20)
        .build()
    );

    public final Value<Boolean> offhand = sgGeneral.add(new FlagValue.Builder()
        .name("offhand")
        .description("Refill offhand slot.")
        .defaultValue(true)
        .build()
    );

    public final Value<Boolean> unstackable = sgGeneral.add(new FlagValue.Builder()
        .name("unstackable")
        .description("Refill unstackable items when broken or consumed.")
        .defaultValue(true)
        .build()
    );

    public final Value<Boolean> sameEnchants = sgGeneral.add(new FlagValue.Builder()
        .name("same-enchants")
        .description("Require matching enchantments for unstackable items.")
        .defaultValue(true)
        .visible(unstackable::get)
        .build()
    );

    public final Value<Boolean> searchHotbar = sgGeneral.add(new FlagValue.Builder()
        .name("search-hotbar")
        .description("Include other hotbar slots when searching for refills.")
        .defaultValue(false)
        .build()
    );

    public final Value<List<Item>> excludedItems = sgGeneral.add(new ItemListValue.Builder()
        .name("excluded-items")
        .description("Items never to replenish.")
        .defaultValue(new ArrayList<>())
        .build()
    );

    private int tickTimer = 0;

    public Stockpile() {
        super(Group.Extras, "stockpile", "Automatically refills depleted hotbar and offhand stacks.");
    }

    @Listen
    private void onTick(ClientTick event) {
        if (mc.player == null || mc.player.containerMenu == null) return;

        if (tickTimer > 0) {
            tickTimer--;
            return;
        }

        Totem totemLoop = Features.get().get(Totem.class);
        boolean totemLocked = totemLoop != null && totemLoop.isLocked();

        if (offhand.get() && !totemLocked) {
            ItemStack offhandStack = mc.player.getOffhandItem();
            if (shouldRefill(offhandStack)) {
                if (refillSlot(45, offhandStack.getItem())) {
                    tickTimer = delay.get();
                    return;
                }
            }
        }

        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getItem(i);
            if (shouldRefill(stack)) {
                if (refillSlot(i, stack.getItem())) {
                    tickTimer = delay.get();
                    return;
                }
            }
        }
    }

    private boolean shouldRefill(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (excludedItems.get().contains(stack.getItem())) return false;
        if (stack.isStackable()) {
            return stack.getCount() <= minCount.get();
        }
        return false;
    }

    private boolean refillSlot(int destSlot, Item item) {
        int invSize = mc.player.getInventory().getContainerSize();
        int startSlot = searchHotbar.get() ? 0 : 9;
        for (int i = startSlot; i < invSize; i++) {
            if (i == destSlot || (i < 9 && !searchHotbar.get())) continue;
            ItemStack stack = mc.player.getInventory().getItem(i);
            if (stack.getItem() == item) {
                InventoryKit.quickMove().from(i).to(destSlot).run();
                return true;
            }
        }
        return false;
    }
}