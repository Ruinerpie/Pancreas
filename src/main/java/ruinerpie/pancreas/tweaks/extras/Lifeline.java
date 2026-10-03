package ruinerpie.pancreas.tweaks.extras;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import ruinerpie.pancreas.Feature;
import ruinerpie.pancreas.Features;
import ruinerpie.pancreas.Group;
import ruinerpie.pancreas.Listen;
import ruinerpie.pancreas.Value;
import ruinerpie.pancreas.ValueGroup;
import ruinerpie.pancreas.saved.Team;
import ruinerpie.pancreas.signals.PacketIn;
import ruinerpie.pancreas.signals.WorldTick;
import ruinerpie.pancreas.tweaks.misc.Return;
import ruinerpie.pancreas.values.DoubleValue;
import ruinerpie.pancreas.values.EntityListValue;
import ruinerpie.pancreas.values.FlagValue;
import ruinerpie.pancreas.values.IntValue;

import java.util.ArrayList;
import java.util.List;

public class Lifeline extends Feature {
    private final ValueGroup sgGeneral = settings.getDefaultGroup();
    private final ValueGroup sgEntities = settings.createGroup("Entities");

    public final Value<Integer> health = sgGeneral.add(new IntValue.Builder()
        .name("health")
        .description("Disconnects when health drops below this value.")
        .defaultValue(6)
        .min(0)
        .max(19)
        .sliderRange(0, 19)
        .build()
    );

    public final Value<Boolean> predictIncomingDamage = sgGeneral.add(new FlagValue.Builder()
        .name("predict-incoming-damage")
        .description("Attempts to predict lethal incoming damage.")
        .defaultValue(true)
        .build()
    );

    public final Value<Integer> totemPops = sgGeneral.add(new IntValue.Builder()
        .name("totem-pops")
        .description("Disconnects after popping this many totems (0 to disable).")
        .defaultValue(0)
        .min(0)
        .max(10)
        .sliderRange(0, 10)
        .build()
    );

    public final Value<Boolean> onlyTrusted = sgGeneral.add(new FlagValue.Builder()
        .name("only-trusted")
        .description("Only logs out if un-crewed players are nearby.")
        .defaultValue(false)
        .build()
    );

    public final Value<Boolean> thirtyTwoK = sgGeneral.add(new FlagValue.Builder()
        .name("32K")
        .description("Disconnects if a player holding an over-enchanted weapon approaches.")
        .defaultValue(false)
        .build()
    );

    public final Value<Boolean> smartToggle = sgGeneral.add(new FlagValue.Builder()
        .name("smart-toggle")
        .description("Automatically toggles off after triggering.")
        .defaultValue(false)
        .build()
    );

    public final Value<Boolean> toggleOff = sgGeneral.add(new FlagValue.Builder()
        .name("toggle-off")
        .description("Disables Lifeline when disconnected.")
        .defaultValue(true)
        .build()
    );

    public final Value<Boolean> toggleServerReturn = sgGeneral.add(new FlagValue.Builder()
        .name("toggle-server-return")
        .description("Disables Return on trigger.")
        .defaultValue(true)
        .build()
    );

    public final Value<List<EntityType<?>>> entities = sgEntities.add(new EntityListValue.Builder()
        .name("entities")
        .description("Entities that trigger disconnect when nearby.")
        .defaultValue(new ArrayList<>(List.of(EntityType.END_CRYSTAL)))
        .build()
    );

    public final Value<Boolean> useTotalCount = sgEntities.add(new FlagValue.Builder()
        .name("use-total-count")
        .description("Uses combined threat entity count instead of per-type.")
        .defaultValue(true)
        .build()
    );

    public final Value<Integer> combinedThreshold = sgEntities.add(new IntValue.Builder()
        .name("combined-entity-threshold")
        .description("Combined entity count to trigger logout.")
        .defaultValue(10)
        .min(1)
        .max(50)
        .visible(useTotalCount::get)
        .build()
    );

    public final Value<Integer> individualThreshold = sgEntities.add(new IntValue.Builder()
        .name("individual-entity-threshold")
        .description("Entity count per-type to trigger logout.")
        .defaultValue(2)
        .min(1)
        .max(20)
        .visible(() -> !useTotalCount.get())
        .build()
    );

    public final Value<Integer> range = sgEntities.add(new IntValue.Builder()
        .name("range")
        .description("Threat detection radius.")
        .defaultValue(5)
        .min(1)
        .max(20)
        .build()
    );

    private int poppedTotems = 0;

    public Lifeline() {
        super(Group.Extras, "lifeline", "Automatically disconnects on low health or nearby danger.");
    }

    @Override
    public void onActivate() {
        poppedTotems = 0;
    }

    @Listen
    private void onPacketReceive(PacketIn event) {
        if (event.packet instanceof ClientboundEntityEventPacket packet) {
            var player = mc.player;
            var level = mc.level;
            if (player != null && level != null && packet.getEntity(level) == player && packet.getEventId() == 35) {
                poppedTotems++;
                if (totemPops.get() > 0 && poppedTotems >= totemPops.get()) {
                    triggerDisconnect("Popped " + poppedTotems + " totems");
                }
            }
        }
    }

    @Listen
    private void onTick(WorldTick event) {
        var player = mc.player;
        var level = mc.level;
        if (player == null || level == null) return;

        if (onlyTrusted.get()) {
            boolean hasUntrusted = false;
            for (Player other : level.players()) {
                if (other != null && other != player && !Team.get().isFriend(other.getUUID())) {
                    hasUntrusted = true;
                    break;
                }
            }
            if (!hasUntrusted) return;
        }

        if (player.getHealth() <= health.get()) {
            triggerDisconnect("Health below threshold (" + (int) player.getHealth() + " HP)");
            return;
        }

        double maxDistSq = range.get() * range.get();
        int threatCount = 0;
        List<EntityType<?>> watched = entities.get();

        for (Entity e : level.entitiesForRendering()) {
            if (e == null || e == player) continue;
            if (watched.contains(e.getType()) && player.distanceToSqr(e) <= maxDistSq) {
                threatCount++;
            }
        }

        if (useTotalCount.get() && threatCount >= combinedThreshold.get()) {
            triggerDisconnect("Nearby threat entity count (" + threatCount + ")");
        } else if (!useTotalCount.get() && threatCount >= individualThreshold.get()) {
            triggerDisconnect("Nearby threat entities (" + threatCount + ")");
        }
    }

    private void triggerDisconnect(String reason) {
        if (toggleServerReturn.get()) {
            Return sr = Features.get().get(Return.class);
            if (sr != null && sr.isActive()) sr.toggle();
        }

        if (toggleOff.get() || smartToggle.get()) {
            toggle();
        }

        var conn = mc.getConnection();
        if (conn != null && conn.getConnection() != null) {
            conn.getConnection().disconnect(Component.literal("Lifeline: " + reason));
        }
    }
}