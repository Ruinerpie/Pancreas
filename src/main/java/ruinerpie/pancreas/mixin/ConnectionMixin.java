package ruinerpie.pancreas.mixin;

import ruinerpie.pancreas.Pancreas;
import ruinerpie.pancreas.signals.PacketIn;
import ruinerpie.pancreas.signals.PacketOut;
import ruinerpie.pancreas.signals.PacketSent;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public class ConnectionMixin {
    @Inject(method = "send(Lnet/minecraft/network/protocol/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void onSendPacket(Packet<?> packet, CallbackInfo info) {
        PacketOut event = new PacketOut(packet);
        Pancreas.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            info.cancel();
        }
    }

    @Inject(method = "send(Lnet/minecraft/network/protocol/Packet;)V", at = @At("TAIL"))
    private void onSentPacket(Packet<?> packet, CallbackInfo info) {
        Pancreas.EVENT_BUS.post(new PacketSent(packet));
    }

    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/protocol/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void onReceivePacket(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo info) {
        PacketIn event = new PacketIn(packet);
        Pancreas.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            info.cancel();
        }
    }
}