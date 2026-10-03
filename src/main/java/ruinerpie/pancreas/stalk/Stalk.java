package ruinerpie.pancreas.stalk;

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

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public final class Stalk {
    private static final Stalk INSTANCE = new Stalk();
    public static Stalk get() { return INSTANCE; }

    private StalkTarget currentTarget = null;

    private Stalk() {}

    public void follow(UUID uuid, String name) {
        if (uuid == null) {
            stop();
            return;
        }
        Vec3 initialPos = null;
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) {
            Player p = mc.level.getPlayerByUUID(uuid);
            if (p != null) {
                initialPos = p.position();
            } else {
                for (Entity e : mc.level.entitiesForRendering()) {
                    if (e.getUUID().equals(uuid)) {
                        initialPos = e.position();
                        break;
                    }
                }
            }
        }
        if (initialPos == null) {
            initialPos = mc.player != null ? mc.player.position() : Vec3.ZERO;
        }
        this.currentTarget = new StalkTarget(uuid, name != null ? name : uuid.toString().substring(0, 8), initialPos, System.currentTimeMillis());
    }

    public void followPlayer(Player player) {
        if (player == null) {
            stop();
            return;
        }
        follow(player.getUUID(), player.getName().getString());
    }

    public void followCombatTarget() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.crosshairPickEntity instanceof LivingEntity le) {
            follow(le.getUUID(), le.getName().getString());
        }
    }

    public void stop() {
        this.currentTarget = null;
    }

    public boolean isActive() {
        return currentTarget != null;
    }

    public StalkTarget current() {
        return currentTarget;
    }

    public void tick() {
        if (currentTarget == null) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        Player player = mc.level.getPlayerByUUID(currentTarget.uuid);
        if (player != null) {
            currentTarget.lastKnownPos = player.position();
            currentTarget.lastSeen = System.currentTimeMillis();
            return;
        }

        for (Entity e : mc.level.entitiesForRendering()) {
            if (e.getUUID().equals(currentTarget.uuid)) {
                currentTarget.lastKnownPos = e.position();
                currentTarget.lastSeen = System.currentTimeMillis();
                return;
            }
        }
        
    }
}