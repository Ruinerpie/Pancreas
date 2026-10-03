package ruinerpie.pancreas.tweaks.cheats;

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

import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class Companion extends Feature {
    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    public final DoubleValue scale = sgGeneral.add(new DoubleValue.Builder()
        .name("scale")
        .description("Nametag render scale.")
        .defaultValue(1.0)
        .min(0.5)
        .max(3.0)
        .build());

    public Companion() {
        super(Group.Cheats, "companion", "Displays the owner username above tamed pets and thrown projectiles.");
    }

    @Listen
    private void onRender2D(Render2D event) {
        if (Pancreas.mc.level == null) return;
        
    }

    public String resolveOwnerName(UUID ownerUuid) {
        if (ownerUuid == null || Pancreas.mc.level == null) return "Unknown";
        Player owner = Pancreas.mc.level.getPlayerByUUID(ownerUuid);
        if (owner != null) return owner.getName().getString();
        return ownerUuid.toString().substring(0, 8);
    }
}