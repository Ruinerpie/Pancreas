package ruinerpie.pancreas.signals;

import ruinerpie.pancreas.Signal;
import net.minecraft.network.protocol.Packet;

public class PacketSent extends Signal {
    public final Packet<?> packet;

    public PacketSent(Packet<?> packet) {
        this.packet = packet;
    }
}
