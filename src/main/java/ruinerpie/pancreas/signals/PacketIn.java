package ruinerpie.pancreas.signals;

import ruinerpie.pancreas.Signal;
import net.minecraft.network.protocol.Packet;

public class PacketIn extends Signal {
    public final Packet<?> packet;

    public PacketIn(Packet<?> packet) {
        this.packet = packet;
    }
}
