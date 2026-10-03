package ruinerpie.pancreas.tweaks.misc;

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

import net.minecraft.network.chat.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.regex.Pattern;

public class ChatPlus extends Feature {
    private final ValueGroup sgGeneral = settings.getDefaultGroup();
    private final ValueGroup sgFilter = settings.createGroup("Filter");
    private final ValueGroup sgLongerChat = settings.createGroup("Longer Chat");
    private final ValueGroup sgPrefix = settings.createGroup("Prefix");
    private final ValueGroup sgSuffix = settings.createGroup("Suffix");

    public final FlagValue annoy = sgGeneral.add(new FlagValue.Builder()
        .name("annoy")
        .description("Alternates case of sent messages.")
        .defaultValue(false)
        .build());

    public final FlagValue fancyChat = sgGeneral.add(new FlagValue.Builder()
        .name("fancy-chat")
        .description("Converts normal text to full-width unicode characters.")
        .defaultValue(false)
        .build());

    public final FlagValue timestamps = sgGeneral.add(new FlagValue.Builder()
        .name("timestamps")
        .description("Prepends timestamps to chat messages.")
        .defaultValue(false)
        .build());

    public final FlagValue showSeconds = sgGeneral.add(new FlagValue.Builder()
        .name("show-seconds")
        .description("Includes seconds in timestamps.")
        .defaultValue(false)
        .visible(timestamps::get)
        .build());

    public final FlagValue playerHeads = sgGeneral.add(new FlagValue.Builder()
        .name("player-heads")
        .description("Renders player avatars next to chat messages.")
        .defaultValue(true)
        .build());

    public final FlagValue coordsProtection = sgGeneral.add(new FlagValue.Builder()
        .name("coords-protection")
        .description("Prevents accidentally sending coordinate patterns in public chat.")
        .defaultValue(true)
        .build());

    public final FlagValue keepHistory = sgGeneral.add(new FlagValue.Builder()
        .name("keep-history")
        .description("Retains chat history across server disconnects.")
        .defaultValue(true)
        .build());

    public final FlagValue antiSpam = sgFilter.add(new FlagValue.Builder()
        .name("anti-spam")
        .description("Merges duplicate consecutive messages.")
        .defaultValue(true)
        .build());

    public final IntValue depth = sgFilter.add(new IntValue.Builder()
        .name("depth")
        .description("Number of previous messages to check for duplicates.")
        .defaultValue(20)
        .min(1)
        .max(100)
        .build());

    public final FlagValue antiClear = sgFilter.add(new FlagValue.Builder()
        .name("anti-clear")
        .description("Prevents empty or blank newline spam intended to clear chat.")
        .defaultValue(true)
        .build());

    public final FlagValue filterRegex = sgFilter.add(new FlagValue.Builder()
        .name("filter-regex")
        .description("Filters messages matching configured regular expressions.")
        .defaultValue(false)
        .build());

    public final TextListValue regexFilter = sgFilter.add(new TextListValue.Builder()
        .name("regex-filter")
        .description("List of regex patterns to hide from chat.")
        .defaultValue(new java.util.ArrayList<>())
        .visible(filterRegex::get)
        .build());

    public final FlagValue infiniteChatBox = sgLongerChat.add(new FlagValue.Builder()
        .name("infinite-chat-box")
        .description("Removes the 256 character input limit.")
        .defaultValue(true)
        .build());

    public final FlagValue longerChatHistory = sgLongerChat.add(new FlagValue.Builder()
        .name("longer-chat-history")
        .description("Expands maximum visible chat history lines.")
        .defaultValue(true)
        .build());

    public final IntValue extraLines = sgLongerChat.add(new IntValue.Builder()
        .name("extra-lines")
        .description("Number of additional lines to retain.")
        .defaultValue(1000)
        .min(0)
        .max(10000)
        .visible(longerChatHistory::get)
        .build());

    public final FlagValue prefix = sgPrefix.add(new FlagValue.Builder()
        .name("prefix")
        .description("Prepends text to sent chat messages.")
        .defaultValue(false)
        .build());

    public final FlagValue prefixRandom = sgPrefix.add(new FlagValue.Builder()
        .name("random")
        .description("Prepends a random emoji or character.")
        .defaultValue(false)
        .visible(prefix::get)
        .build());

    public final TextValue prefixText = sgPrefix.add(new TextValue.Builder()
        .name("text")
        .description("Prefix to prepend to messages.")
        .defaultValue("> ")
        .visible(() -> prefix.get() && !prefixRandom.get())
        .build());

    public final FlagValue prefixSmallCaps = sgPrefix.add(new FlagValue.Builder()
        .name("small-caps")
        .description("Converts prefix to small-caps font.")
        .defaultValue(false)
        .visible(() -> prefix.get() && !prefixRandom.get())
        .build());

    public final FlagValue suffix = sgSuffix.add(new FlagValue.Builder()
        .name("suffix")
        .description("Appends text to sent chat messages.")
        .defaultValue(false)
        .build());

    public final FlagValue suffixRandom = sgSuffix.add(new FlagValue.Builder()
        .name("random")
        .description("Appends a random string.")
        .defaultValue(false)
        .visible(suffix::get)
        .build());

    public final TextValue suffixText = sgSuffix.add(new TextValue.Builder()
        .name("text")
        .description("Suffix to append to messages.")
        .defaultValue("")
        .visible(() -> suffix.get() && !suffixRandom.get())
        .build());

    public final FlagValue suffixSmallCaps = sgSuffix.add(new FlagValue.Builder()
        .name("small-caps")
        .description("Converts suffix to small-caps font.")
        .defaultValue(true)
        .visible(() -> suffix.get() && !suffixRandom.get())
        .build());

    private final Deque<String> recentMessages = new ArrayDeque<>();
    private static final Pattern COORDS_PATTERN = Pattern.compile("(?i)(?:x:?\\s*-?\\d{2,}[,\\s]+y:?\\s*-?\\d{1,}[,\\s]+z:?\\s*-?\\d{2,}|-?\\d{3,}[,\\s]+(?:~|-?\\d{1,})[,\\s]+-?\\d{3,})");

    public ChatPlus() {
        super(Group.Misc, "chatter", "Chat overhaul with timestamps, anti-spam, and formatting.");
    }

    @Listen
    private void onReceiveMessage(MessageReceived event) {
        String raw = event.getMessage() != null ? event.getMessage().getString() : "";

        if (antiClear.get() && raw.trim().isEmpty() && raw.length() > 5) {
            event.cancel();
            return;
        }

        if (filterRegex.get()) {
            for (String reg : regexFilter.get()) {
                try {
                    if (Pattern.compile(reg).matcher(raw).find()) {
                        event.cancel();
                        return;
                    }
                } catch (Exception ignored) {}
            }
        }

        if (antiSpam.get()) {
            if (recentMessages.contains(raw)) {
                event.cancel();
                return;
            }
            if (recentMessages.size() >= depth.get()) {
                recentMessages.removeFirst();
            }
            recentMessages.addLast(raw);
        }

        if (timestamps.get() && event.getMessage() != null) {
            String timeFormat = showSeconds.get() ? "HH:mm:ss" : "HH:mm";
            String stamp = "[" + LocalTime.now().format(DateTimeFormatter.ofPattern(timeFormat)) + "] ";
            Component newComp = Component.literal(stamp).append(event.getMessage());
            event.setMessage(newComp);
        }
    }

    @Listen
    private void onSendMessage(MessageSent event) {
        String msg = event.message;
        if (msg == null || msg.startsWith("/")) return;

        if (coordsProtection.get() && COORDS_PATTERN.matcher(msg).find()) {
            event.cancel();
            Toasts.get().warning("Coords Protection", "Blocked coordinate leak in chat.");
            return;
        }

        if (annoy.get()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < msg.length(); i++) {
                char c = msg.charAt(i);
                sb.append(i % 2 == 0 ? Character.toUpperCase(c) : Character.toLowerCase(c));
            }
            msg = sb.toString();
        }

        if (prefix.get()) {
            String pfx = prefixRandom.get() ? "> " : prefixText.get();
            msg = pfx + msg;
        }

        if (suffix.get() && !suffixText.get().isEmpty()) {
            String sfx = " " + suffixText.get();
            msg = msg + sfx;
        }

        event.message = msg;
    }
}