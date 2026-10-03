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

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class Glide extends Feature {
    public enum ChestplateMode {
        Diamond,
        Netherite,
        PreferDiamond,
        PreferNetherite
    }

    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    public final Value<ChestplateMode> chestplate = sgGeneral.add(new ChoiceValue.Builder<ChestplateMode>()
        .name("chestplate")
        .description("Preferred chestplate material.")
        .defaultValue(ChestplateMode.PreferNetherite)
        .build()
    );

    public final Value<Boolean> stayOn = sgGeneral.add(new FlagValue.Builder()
        .name("stay-on")
        .description("Keeps module enabled after swapping.")
        .defaultValue(false)
        .build()
    );

    public final Value<Boolean> closeInventory = sgGeneral.add(new FlagValue.Builder()
        .name("close-inventory")
        .description("Closes the inventory screen if open after swapping.")
        .defaultValue(true)
        .build()
    );

    public Glide() {
        super(Group.Extras, "glide", "Instantly swaps between chestplate and elytra.");
    }

    @Override
    public void onActivate() {
        if (mc.player == null || mc.gameMode == null) return;

        ItemStack currentChest = mc.player.getItemBySlot(EquipmentSlot.CHEST);
        boolean hasElytraEquipped = currentChest.is(Items.ELYTRA);

        int targetSlot = -1;
        if (hasElytraEquipped) {
            targetSlot = findChestplateSlot();
        } else {
            targetSlot = findItemSlot(Items.ELYTRA);
        }

        if (targetSlot != -1) {
            
            int containerId = mc.player.inventoryMenu.containerId;
            mc.gameMode.handleContainerInput(containerId, targetSlot, 0, ContainerInput.PICKUP, mc.player);
            mc.gameMode.handleContainerInput(containerId, 6, 0, ContainerInput.PICKUP, mc.player);
            mc.gameMode.handleContainerInput(containerId, targetSlot, 0, ContainerInput.PICKUP, mc.player);
        }

        if (closeInventory.get() && mc.screen != null) {
            mc.player.closeContainer();
        }

        if (!stayOn.get()) {
            toggle();
        }
    }

    private int findChestplateSlot() {
        ChestplateMode mode = chestplate.get();
        int netherite = findItemSlot(Items.NETHERITE_CHESTPLATE);
        int diamond = findItemSlot(Items.DIAMOND_CHESTPLATE);

        return switch (mode) {
            case Diamond -> diamond != -1 ? diamond : -1;
            case Netherite -> netherite != -1 ? netherite : -1;
            case PreferDiamond -> diamond != -1 ? diamond : netherite;
            case PreferNetherite -> netherite != -1 ? netherite : diamond;
        };
    }

    private int findItemSlot(net.minecraft.world.item.Item item) {
        for (int i = 9; i < 45; i++) {
            ItemStack stack = mc.player.inventoryMenu.getSlot(i).getItem();
            if (stack.is(item)) {
                return i;
            }
        }
        return -1;
    }
}