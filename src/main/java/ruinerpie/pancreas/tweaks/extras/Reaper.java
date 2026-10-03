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

import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;

public class Reaper extends Feature {
    public enum Weapon {
        Sword,
        Axe
    }

    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    private final Value<Weapon> weapon = sgGeneral.add(new ChoiceValue.Builder<Weapon>()
        .name("weapon")
        .description("What type of weapon to use.")
        .defaultValue(Weapon.Sword)
        .build()
    );

    private final Value<Integer> threshold = sgGeneral.add(new IntValue.Builder()
        .name("threshold")
        .description("If the non-preferred weapon produces this much damage this will favor it over your preferred weapon.")
        .defaultValue(4)
        .build()
    );

    private final Value<Boolean> antiBreak = sgGeneral.add(new FlagValue.Builder()
        .name("anti-break")
        .description("Prevents you from breaking your weapon.")
        .defaultValue(false)
        .build()
    );

    public Reaper() {
        super(Group.Extras, "reaper", "Finds the best weapon to use in your hotbar.");
    }

    @Listen
    private void onAttack(AttackEntity event) {
        if (event.entity instanceof LivingEntity livingEntity && mc.player != null) {
            InventoryKit.swap(getBestWeapon(livingEntity), false);
        }
    }

    private int getBestWeapon(LivingEntity target) {
        if (mc.player == null) return 0;
        int slotS = mc.player.getInventory().getSelectedSlot();
        int slotA = mc.player.getInventory().getSelectedSlot();
        double damageS = 0;
        double damageA = 0;
        double currentDamageS;
        double currentDamageA;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getItem(i);
            if (stack.is(ItemTags.SWORDS)
                && (!antiBreak.get() || (stack.getMaxDamage() - stack.getDamageValue()) > 10)) {
                currentDamageS = DamageKit.getAttackDamage(mc.player, target, stack);
                if (currentDamageS > damageS) {
                    damageS = currentDamageS;
                    slotS = i;
                }
            } else if (stack.getItem() instanceof AxeItem
                && (!antiBreak.get() || (stack.getMaxDamage() - stack.getDamageValue()) > 10)) {
                currentDamageA = DamageKit.getAttackDamage(mc.player, target, stack);
                if (currentDamageA > damageA) {
                    damageA = currentDamageA;
                    slotA = i;
                }
            }
        }
        if (weapon.get() == Weapon.Sword && threshold.get() > damageA - damageS) return slotS;
        else if (weapon.get() == Weapon.Axe && threshold.get() > damageS - damageA) return slotA;
        else if (weapon.get() == Weapon.Sword && threshold.get() < damageA - damageS) return slotA;
        else if (weapon.get() == Weapon.Axe && threshold.get() < damageS - damageA) return slotS;
        else return mc.player.getInventory().getSelectedSlot();
    }
}