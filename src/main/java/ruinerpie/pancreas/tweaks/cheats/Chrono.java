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

import net.minecraft.network.protocol.game.ClientboundSetTimePacket;

public class Chrono extends Feature {
    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    public final Value<Double> time = sgGeneral.add(new DoubleValue.Builder()
        .name("time")
        .description("The specified time to be set.")
        .defaultValue(0.0)
        .sliderRange(-20000, 20000)
        .build()
    );

    private long oldTime;

    public Chrono() {
        super(Group.Cheats, "chrono", "Makes you able to set a custom time.");
    }

    @Override
    public void onActivate() {
        if (mc.level != null && mc.level.getLevelData() != null) {
            oldTime = mc.level.getLevelData().getGameTime();
        }
    }

    @Override
    public void onDeactivate() {
        if (mc.level != null && mc.level.getLevelData() != null) {
            mc.level.getLevelData().setGameTime(oldTime);
        }
    }

    @Listen
    private void onPacketReceive(PacketIn event) {
        if (event.packet instanceof ClientboundSetTimePacket packet) {
            oldTime = packet.gameTime();
            event.cancel();
        }
    }

    @Listen
    private void onTick(WorldTick event) {
        if (mc.level != null && mc.level.getLevelData() != null) {
            mc.level.getLevelData().setGameTime(time.get().longValue());
        }
    }
}