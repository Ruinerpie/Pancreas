package ruinerpie.pancreas.tweaks.extras;

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

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.lwjgl.glfw.GLFW;

public class Pivot extends Feature {
    public enum Mode {
        Pearl,
        XP,
        Rocket,
        WindCharge,
        Bow,
        Gap,
        EGap,
        Chorus,
        AddCrew
    }

    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    public final Value<Mode> mode = sgGeneral.add(new ChoiceValue.Builder<Mode>()
        .name("mode")
        .description("Action executed upon middle click.")
        .defaultValue(Mode.Pearl)
        .build()
    );

    public final Value<Boolean> sendMessage = sgGeneral.add(new FlagValue.Builder()
        .name("send-message")
        .description("Sends a chat message to the player when added to Team.")
        .defaultValue(false)
        .visible(() -> mode.get() == Mode.AddCrew)
        .build()
    );

    public final Value<String> messageToSend = sgGeneral.add(new TextValue.Builder()
        .name("message-to-send")
        .description("Chat message to send when adding to Team.")
        .defaultValue("")
        .visible(() -> mode.get() == Mode.AddCrew && sendMessage.get())
        .build()
    );

    public final Value<Boolean> quickSwap = sgGeneral.add(new FlagValue.Builder()
        .name("quick-swap")
        .description("Swaps to item and uses it immediately.")
        .defaultValue(false)
        .visible(() -> mode.get() != Mode.AddCrew)
        .build()
    );

    public final Value<Boolean> swapBack = sgGeneral.add(new FlagValue.Builder()
        .name("swap-back")
        .description("Restores previous selected slot after action.")
        .defaultValue(false)
        .visible(() -> mode.get() != Mode.AddCrew && !quickSwap.get())
        .build()
    );

    public final Value<Boolean> disableInCreative = sgGeneral.add(new FlagValue.Builder()
        .name("disable-in-creative")
        .description("Disables middle click action when in creative mode.")
        .defaultValue(true)
        .build()
    );

    public final Value<Boolean> notify = sgGeneral.add(new FlagValue.Builder()
        .name("notify")
        .description("Sends toast notifications on action status.")
        .defaultValue(true)
        .visible(() -> mode.get() != Mode.AddCrew)
        .build()
    );

    public Pivot() {
        super(Group.Extras, "pivot", "Performs customizable actions on middle mouse click.");
    }

    @Listen
    private void onMouseClick(MouseClick event) {
        if (mc.player == null || mc.gameMode == null || mc.screen != null) return;
        if (event.button != GLFW.GLFW_MOUSE_BUTTON_MIDDLE || event.action != ActionKind.Press) return;
        if (disableInCreative.get() && mc.player.isCreative()) return;

        if (mode.get() == Mode.AddCrew) {
            HitResult hit = mc.hitResult;
            if (hit instanceof EntityHitResult entityHit && entityHit.getEntity() instanceof Player targetPlayer) {
                String name = targetPlayer.getName().getString();
                Team.get().add(targetPlayer.getUUID(), name);
                Toasts.get().info("Team", "Added " + name + " to Team.");

                if (sendMessage.get() && !messageToSend.get().isEmpty()) {
                    mc.player.connection.sendChat(messageToSend.get());
                }
            }
            return;
        }

        Item item = getItemForMode(mode.get());
        if (item == null) return;

        FindResult result = InventoryKit.findInHotbar(item);
        if (!result.found()) {
            if (notify.get()) {
                Toasts.get().warning("MidAction", "Unable to find " + mode.get() + " in hotbar.");
            }
            return;
        }

        if (result.isOffhand()) {
            mc.gameMode.useItem(mc.player, InteractionHand.OFF_HAND);
        } else {
            InventoryKit.swap(result.slot(), swapBack.get());
            mc.gameMode.useItem(mc.player, InteractionHand.MAIN_HAND);
            if (swapBack.get()) {
                InventoryKit.swapBack();
            }
        }
    }

    private Item getItemForMode(Mode m) {
        return switch (m) {
            case Pearl -> Items.ENDER_PEARL;
            case XP -> Items.EXPERIENCE_BOTTLE;
            case Rocket -> Items.FIREWORK_ROCKET;
            case WindCharge -> Items.WIND_CHARGE;
            case Bow -> Items.BOW;
            case Gap -> Items.GOLDEN_APPLE;
            case EGap -> Items.ENCHANTED_GOLDEN_APPLE;
            case Chorus -> Items.CHORUS_FRUIT;
            default -> null;
        };
    }
}