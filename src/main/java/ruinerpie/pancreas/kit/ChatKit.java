package ruinerpie.pancreas.kit;

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
import net.minecraft.network.chat.Component;

public class ChatKit {
    public static final Minecraft mc = Minecraft.getInstance();

    public static void info(String msg) {
        send(Component.literal("§7[§cPANCREAS§7] §f" + msg));
    }

    public static void warning(String msg) {
        send(Component.literal("§7[§ePANCREAS§7] §e" + msg));
    }

    public static void error(String msg) {
        send(Component.literal("§7[§4PANCREAS§7] §c" + msg));
    }

    public static void send(Component message) {
        if (mc.gui != null && mc.gui.getChat() != null) {
            mc.gui.getChat().addClientSystemMessage(message);
        }
    }

    public static void sendMsg(Component message) {
        send(message);
    }

    public static void sendPlayerMsg(String message) {
        if (mc.player != null && message != null) {
            if (message.startsWith("/")) {
                mc.player.connection.sendCommand(message.substring(1));
            } else {
                mc.player.connection.sendChat(message);
            }
        }
    }
}