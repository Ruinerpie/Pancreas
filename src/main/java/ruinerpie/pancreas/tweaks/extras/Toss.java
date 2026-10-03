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

import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class Toss extends Feature {
    public enum RepairMode {
        Armor,
        Hands,
        Both
    }

    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    public final Value<RepairMode> mode = sgGeneral.add(new ChoiceValue.Builder<RepairMode>()
        .name("mode")
        .description("Which equipment to repair.")
        .defaultValue(RepairMode.Both)
        .build()
    );

    public final Value<Boolean> replenish = sgGeneral.add(new FlagValue.Builder()
        .name("replenish")
        .description("Refills XP bottles to hotbar from inventory.")
        .defaultValue(true)
        .build()
    );

    public final Value<Boolean> onlyOnGround = sgGeneral.add(new FlagValue.Builder()
        .name("only-on-ground")
        .description("Only throws XP bottles while standing on ground.")
        .defaultValue(false)
        .build()
    );

    public final Value<Integer> expSlot = sgGeneral.add(new IntValue.Builder()
        .name("exp-slot")
        .description("Hotbar slot to refill XP bottles into (1-9).")
        .defaultValue(6)
        .min(1)
        .max(9)
        .sliderRange(1, 9)
        .visible(replenish::get)
        .build()
    );

    public final Value<Integer> minThreshold = sgGeneral.add(new IntValue.Builder()
        .name("min-threshold")
        .description("Durability percentage to start repairing.")
        .defaultValue(30)
        .min(1)
        .max(100)
        .sliderRange(1, 100)
        .build()
    );

    public final Value<Integer> maxThreshold = sgGeneral.add(new IntValue.Builder()
        .name("max-threshold")
        .description("Durability percentage to stop repairing.")
        .defaultValue(80)
        .min(1)
        .max(100)
        .sliderRange(1, 100)
        .build()
    );

    public Toss() {
        super(Group.Extras, "toss", "Automatically throws XP bottles to repair Mending equipment.");
    }

    @Listen
    private void onTick(ClientTick event) {
        if (mc.player == null || mc.gameMode == null) return;
        if (onlyOnGround.get() && !mc.player.onGround()) return;
        if (!needsRepair()) return;

        FindResult exp = InventoryKit.findInHotbar(Items.EXPERIENCE_BOTTLE);
        if (!exp.found()) {
            if (replenish.get()) {
                FindResult invExp = InventoryKit.find(Items.EXPERIENCE_BOTTLE);
                if (invExp.found()) {
                    int target = expSlot.get() - 1;
                    InventoryKit.move().from(invExp.slot()).toHotbar(target).run();
                }
            }
            return;
        }

        RotationKit.rotate(mc.player.getYRot(), 90, () -> {
            if (exp.isOffhand()) {
                mc.gameMode.useItem(mc.player, InteractionHand.OFF_HAND);
            } else {
                InventoryKit.swap(exp.slot(), true);
                mc.gameMode.useItem(mc.player, InteractionHand.MAIN_HAND);
                InventoryKit.swapBack();
            }
        });
    }

    private boolean needsRepair() {
        if (mc.player == null) return false;
        RepairMode m = mode.get();
        if (m == RepairMode.Armor || m == RepairMode.Both) {
            for (net.minecraft.world.entity.EquipmentSlot slot : new net.minecraft.world.entity.EquipmentSlot[]{
                net.minecraft.world.entity.EquipmentSlot.HEAD,
                net.minecraft.world.entity.EquipmentSlot.CHEST,
                net.minecraft.world.entity.EquipmentSlot.LEGS,
                net.minecraft.world.entity.EquipmentSlot.FEET
            }) {
                ItemStack stack = mc.player.getItemBySlot(slot);
                if (!stack.isEmpty() && stack.isDamageableItem()) {
                    double pct = (double) (stack.getMaxDamage() - stack.getDamageValue()) / stack.getMaxDamage() * 100.0;
                    if (pct < maxThreshold.get()) return true;
                }
            }
        }
        if (m == RepairMode.Hands || m == RepairMode.Both) {
            ItemStack main = mc.player.getMainHandItem();
            if (!main.isEmpty() && main.isDamageableItem()) {
                double pct = (double) (main.getMaxDamage() - main.getDamageValue()) / main.getMaxDamage() * 100.0;
                if (pct < maxThreshold.get()) return true;
            }
            ItemStack off = mc.player.getOffhandItem();
            if (!off.isEmpty() && off.isDamageableItem()) {
                double pct = (double) (off.getMaxDamage() - off.getDamageValue()) / off.getMaxDamage() * 100.0;
                if (pct < maxThreshold.get()) return true;
            }
        }
        return false;
    }
}