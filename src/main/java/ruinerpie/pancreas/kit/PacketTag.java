package ruinerpie.pancreas.kit;

import ruinerpie.pancreas.*;
import ruinerpie.pancreas.mixin.*;
import ruinerpie.pancreas.mixin.accessor.*;
import ruinerpie.pancreas.values.*;
import ruinerpie.pancreas.signals.*;
import ruinerpie.pancreas.kit.*;
import ruinerpie.pancreas.draw.*;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundKeepAlivePacket;
import net.minecraft.network.protocol.common.ServerboundKeepAlivePacket;
import net.minecraft.network.protocol.game.*;

public final class PacketTag {
    public static PacketKind getCategory(Packet<?> packet) {
        if (packet == null) return null;

        if (packet instanceof ServerboundKeepAlivePacket || packet instanceof ClientboundKeepAlivePacket) {
            return PacketKind.KeepAlive;
        }

        if (packet instanceof ServerboundChatPacket || packet instanceof ClientboundSystemChatPacket || packet instanceof ClientboundPlayerChatPacket) {
            return PacketKind.Chat;
        }

        if (packet instanceof ServerboundMovePlayerPacket || packet instanceof ClientboundPlayerPositionPacket) {
            return PacketKind.Movement;
        }

        if (packet instanceof ServerboundPlayerActionPacket || packet instanceof ServerboundPlayerCommandPacket) {
            return PacketKind.PlayerAction;
        }

        if (packet instanceof ServerboundInteractPacket || packet instanceof ServerboundUseItemPacket || packet instanceof ServerboundUseItemOnPacket) {
            return PacketKind.Interact;
        }

        if (packet instanceof ServerboundContainerClickPacket || packet instanceof ServerboundContainerClosePacket || packet instanceof ClientboundContainerSetSlotPacket || packet instanceof ClientboundContainerSetContentPacket) {
            return PacketKind.Inventory;
        }

        return null;
    }
}
