package ruinerpie.pancreas.kit;

import ruinerpie.pancreas.*;
import ruinerpie.pancreas.mixin.*;
import ruinerpie.pancreas.mixin.accessor.*;
import ruinerpie.pancreas.values.*;
import ruinerpie.pancreas.signals.*;
import ruinerpie.pancreas.kit.*;
import ruinerpie.pancreas.draw.*;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;

public class DamageKit {
    public static float getAttackDamage(Player player, Entity target, ItemStack stack) {
        if (player == null || target == null) return 0.0f;
        float damage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
        return damage;
    }

    public static float getAttackDamage(Player player, Entity target) {
        return getAttackDamage(player, target, player != null ? player.getMainHandItem() : null);
    }

    public static float crystalDamage(LivingEntity target, Vec3 crystal) {
        if (target == null || crystal == null) return 0.0f;
        double distance = Math.sqrt(target.distanceToSqr(crystal));
        if (distance > 12.0) return 0.0f;
        double impact = (1.0 - (distance / 12.0));
        float rawDamage = (float) ((impact * impact + impact) / 2.0 * 7.0 * 12.0 + 1.0);
        return rawDamage;
    }
}
