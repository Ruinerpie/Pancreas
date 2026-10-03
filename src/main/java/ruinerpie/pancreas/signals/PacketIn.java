package ruinerpie.pancreas.signals;

import net.minecraft.network.protocol.Packet;
import ruinerpie.pancreas.Signal;

public class PacketIn extends Signal {
    public Packet<?> packet;
    public PacketIn(Packet<?> packet) { this.packet = packet; }
}
