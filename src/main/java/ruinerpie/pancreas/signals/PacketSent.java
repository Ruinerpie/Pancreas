package ruinerpie.pancreas.signals;

import net.minecraft.network.protocol.Packet;
import ruinerpie.pancreas.Signal;

public class PacketSent extends Signal {
    public Packet<?> packet;
    public PacketSent(Packet<?> packet) { this.packet = packet; }
}
