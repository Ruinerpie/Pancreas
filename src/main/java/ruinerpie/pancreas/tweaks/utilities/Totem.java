package ruinerpie.pancreas.tweaks.utilities;

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

import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class Totem extends Feature {
    public enum Mode {
        Smart,
        Strict
    }

    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    public final Value<Mode> mode = sgGeneral.add(new ChoiceValue.Builder<Mode>()
        .name("mode")
        .description("Totem replenishment strictness mode.")
        .defaultValue(Mode.Smart)
        .build()
    );

    public final Value<Integer> delay = sgGeneral.add(new IntValue.Builder()
        .name("delay")
        .description("Tick delay before moving Totem to offhand.")
        .defaultValue(0)
        .min(0)
        .max(20)
        .sliderRange(0, 20)
        .build()
    );

    public final Value<Integer> health = sgGeneral.add(new IntValue.Builder()
        .name("health")
        .description("Health threshold to equip Totem in Smart mode.")
        .defaultValue(10)
        .min(0)
        .max(36)
        .sliderRange(0, 36)
        .visible(() -> mode.get() == Mode.Smart)
        .build()
    );

    public final Value<Boolean> elytra = sgGeneral.add(new FlagValue.Builder()
        .name("elytra")
        .description("Equips Totem while gliding with Elytra.")
        .defaultValue(true)
        .visible(() -> mode.get() == Mode.Smart)
        .build()
    );

    public final Value<Boolean> fall = sgGeneral.add(new FlagValue.Builder()
        .name("fall")
        .description("Equips Totem during high falls.")
        .defaultValue(true)
        .visible(() -> mode.get() == Mode.Smart)
        .build()
    );

    public final Value<Boolean> explosion = sgGeneral.add(new FlagValue.Builder()
        .name("explosion")
        .description("Equips Totem when near primed explosives.")
        .defaultValue(true)
        .visible(() -> mode.get() == Mode.Smart)
        .build()
    );

    private int delayTimer = 0;

    public Totem() {
        super(Group.Utilities, "totem", "Automatically keeps a Totem of Undying in the offhand.");
    }

    public boolean isLocked() {
        if (!isActive()) return false;
        if (mode.get() == Mode.Strict) return true;
        if (mc.player == null) return false;
        return mc.player.getHealth() <= health.get()
            || (elytra.get() && mc.player.isFallFlying())
            || (fall.get() && mc.player.fallDistance > 3.0f);
    }

    @Listen(priority = Order.HIGHEST)
    private void onTick(ClientTick event) {
        if (mc.player == null) return;

        if (delayTimer > 0) {
            delayTimer--;
            return;
        }

        ItemStack offhand = mc.player.getOffhandItem();
        if (offhand.is(Items.TOTEM_OF_UNDYING)) return;

        if (mode.get() == Mode.Strict || isLocked()) {
            equipTotem();
        }
    }

    @Listen(priority = Order.HIGH)
    private void onPacketReceive(PacketIn event) {
        if (event.packet instanceof ClientboundEntityEventPacket packet) {
            if (mc.player != null && packet.getEntity(mc.level) == mc.player && packet.getEventId() == 35) {
                equipTotem();
            }
        }
    }

    private void equipTotem() {
        FindResult totem = InventoryKit.find(Items.TOTEM_OF_UNDYING);
        if (totem.found() && totem.slot() != 45) {
            InventoryKit.move().from(totem.slot()).toOffhand();
            delayTimer = delay.get();
        }
    }
}