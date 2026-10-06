package ruinerpie.pancreas.kit;

import ruinerpie.pancreas.*;
import ruinerpie.pancreas.mixin.*;
import ruinerpie.pancreas.mixin.accessor.*;
import ruinerpie.pancreas.values.*;
import ruinerpie.pancreas.signals.*;
import ruinerpie.pancreas.kit.*;
import ruinerpie.pancreas.draw.*;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class EntityKit {
    private static final Minecraft mc = Minecraft.getInstance();

    public static boolean isAttackable(Entity entity, boolean ignoreSelf) {
        if (entity == null || entity == mc.player && ignoreSelf) return false;
        return entity.isAlive() && !entity.isInvulnerable();
    }

    public static String getName(Entity entity) {
        return entity != null ? entity.getName().getString() : "";
    }
}
