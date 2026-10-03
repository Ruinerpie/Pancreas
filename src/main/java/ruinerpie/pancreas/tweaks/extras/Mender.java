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

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.List;

public class Mender extends Feature {
    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    public final Value<List<Item>> blacklist = sgGeneral.add(new ItemListValue.Builder()
        .name("blacklist")
        .description("Item blacklist.")
        .filter(item -> item.components().get(DataComponents.DAMAGE) != null)
        .build()
    );

    public final Value<Boolean> force = sgGeneral.add(new FlagValue.Builder()
        .name("force")
        .description("Replaces item in offhand even if there is some other non-repairable item.")
        .defaultValue(false)
        .build()
    );

    public final Value<Boolean> autoDisable = sgGeneral.add(new FlagValue.Builder()
        .name("auto-disable")
        .description("Automatically disables when there are no more items to repair.")
        .defaultValue(true)
        .build()
    );

    private boolean didMove;

    public Mender() {
        super(Group.Extras, "mender", "Automatically replaces items in your offhand with mending when fully repaired.");
    }

    @Override
    public void onActivate() {
        didMove = false;
    }

    @Listen
    private void onTick(ClientTick event) {
        if (mc.player == null || shouldWait()) return;

        int slot = getSlot();

        if (slot == -1) {
            if (autoDisable.get()) {
                info("Repaired all items, disabling");

                if (didMove) {
                    int emptySlot = getEmptySlot();
                    InventoryKit.move().fromOffhand().to(emptySlot);
                }

                toggle();
            }
        } else {
            InventoryKit.move().from(slot).toOffhand();
            didMove = true;
        }
    }

    private boolean shouldWait() {
        if (mc.player == null) return false;
        ItemStack itemStack = mc.player.getOffhandItem();

        if (itemStack.isEmpty()) return false;

        if (Terrain.hasEnchantments(itemStack, Enchantments.MENDING)) {
            return itemStack.getDamageValue() != 0;
        }

        return !force.get();
    }

    private int getSlot() {
        if (mc.player == null) return -1;
        for (int i = 0; i < mc.player.getInventory().getNonEquipmentItems().size(); i++) {
            ItemStack itemStack = mc.player.getInventory().getItem(i);
            if (blacklist.get().contains(itemStack.getItem())) continue;

            if (Terrain.hasEnchantments(itemStack, Enchantments.MENDING) && itemStack.getDamageValue() > 0) {
                return i;
            }
        }

        return -1;
    }

    private int getEmptySlot() {
        if (mc.player == null) return -1;
        for (int i = 0; i < mc.player.getInventory().getNonEquipmentItems().size(); i++) {
            if (mc.player.getInventory().getItem(i).isEmpty()) return i;
        }

        return -1;
    }
}