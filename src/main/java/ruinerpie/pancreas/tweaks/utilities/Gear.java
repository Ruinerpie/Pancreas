package ruinerpie.pancreas.tweaks.utilities;

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
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.equipment.Equippable;

public class Gear extends Feature {
    public enum ProtectionPreference {
        Protection,
        BlastProtection,
        FireProtection,
        ProjectileProtection
    }

    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    public final Value<ProtectionPreference> preferredProtection = sgGeneral.add(new ChoiceValue.Builder<ProtectionPreference>()
        .name("preferred-protection")
        .description("Priority enchantment bias for armor pieces.")
        .defaultValue(ProtectionPreference.Protection)
        .build()
    );

    public final Value<Integer> swapDelay = sgGeneral.add(new IntValue.Builder()
        .name("swap-delay")
        .description("Tick delay between armor swaps.")
        .defaultValue(1)
        .min(0)
        .max(5)
        .sliderRange(0, 5)
        .build()
    );

    public final Value<Boolean> blastProtLeggings = sgGeneral.add(new FlagValue.Builder()
        .name("blast-prot-leggings")
        .description("Prefers Blast Protection specifically for leggings.")
        .defaultValue(true)
        .build()
    );

    public final Value<Boolean> antiBreak = sgGeneral.add(new FlagValue.Builder()
        .name("anti-break")
        .description("Avoids equipping armor that is nearly broken.")
        .defaultValue(false)
        .build()
    );

    public final Value<Boolean> ignoreElytra = sgGeneral.add(new FlagValue.Builder()
        .name("ignore-elytra")
        .description("Does not swap chestplate if an Elytra is equipped.")
        .defaultValue(true)
        .build()
    );

    private int delayTimer = 0;

    public Gear() {
        super(Group.Utilities, "gear", "Automatically equips the strongest armor pieces from inventory.");
    }

    @Listen
    private void onTick(ClientTick event) {
        if (mc.player == null) return;

        if (delayTimer > 0) {
            delayTimer--;
            return;
        }

        EquipmentSlot[] slots = new EquipmentSlot[]{
            EquipmentSlot.HEAD,
            EquipmentSlot.CHEST,
            EquipmentSlot.LEGS,
            EquipmentSlot.FEET
        };

        for (int i = 0; i < slots.length; i++) {
            EquipmentSlot slot = slots[i];
            if (slot == EquipmentSlot.CHEST && ignoreElytra.get() && mc.player.getItemBySlot(slot).is(Items.ELYTRA)) {
                continue;
            }

            ItemStack current = mc.player.getItemBySlot(slot);
            int bestInvSlot = findBestArmorSlot(slot, current);

            if (bestInvSlot != -1) {
                
                InventoryKit.move().from(bestInvSlot).toArmor(i).run();
                delayTimer = swapDelay.get();
                return;
            }
        }
    }

    private int findBestArmorSlot(EquipmentSlot targetSlot, ItemStack current) {
        int bestSlot = -1;
        int currentScore = getArmorScore(current);
        int bestScore = currentScore;

        int size = mc.player.getInventory().getContainerSize();
        for (int i = 0; i < size; i++) {
            ItemStack stack = mc.player.getInventory().getItem(i);
            if (stack.isEmpty() || !stack.has(DataComponents.EQUIPPABLE)) continue;

            Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
            if (equippable == null || equippable.slot() != targetSlot) continue;

            if (antiBreak.get() && stack.isDamageableItem()) {
                if (stack.getMaxDamage() - stack.getDamageValue() < 10) continue;
            }

            int score = getArmorScore(stack);
            if (score > bestScore) {
                bestScore = score;
                bestSlot = i;
            }
        }

        return bestSlot;
    }

    private int getArmorScore(ItemStack stack) {
        if (stack.isEmpty() || !stack.has(DataComponents.EQUIPPABLE)) return 0;
        String id = stack.getItem().toString().toLowerCase();
        int tier = 5;
        if (id.contains("netherite")) tier = 50;
        else if (id.contains("diamond")) tier = 40;
        else if (id.contains("iron")) tier = 30;
        else if (id.contains("chainmail")) tier = 20;
        else if (id.contains("gold")) tier = 15;
        else if (id.contains("leather")) tier = 10;
        return tier;
    }
}