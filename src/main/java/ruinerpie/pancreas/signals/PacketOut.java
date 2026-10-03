package ruinerpie.pancreas.signals;

import net.minecraft.network.protocol.Packet;
import ruinerpie.pancreas.Signal;

public class PacketOut extends Signal {
    public Packet<?> packet;
    public PacketOut(Packet<?> packet) { this.packet = packet; }
}
