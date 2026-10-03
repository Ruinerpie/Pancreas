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

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class Sidearm extends Feature {
    public enum OffhandItem {
        EGap,
        Gap,
        Crystal,
        Totem,
        Shield,
        Potion
    }

    private final ValueGroup sgCombat = settings.createGroup("Combat");
    private final ValueGroup sgTotem = settings.createGroup("Totem");

    public final Value<Integer> itemSwitchDelay = sgCombat.add(new IntValue.Builder()
        .name("item-switch-delay")
        .description("Delay in ticks before swapping offhand items.")
        .defaultValue(0)
        .min(0)
        .max(20)
        .sliderRange(0, 20)
        .build()
    );

    public final Value<OffhandItem> item = sgCombat.add(new ChoiceValue.Builder<OffhandItem>()
        .name("item")
        .description("Default item to maintain in the offhand.")
        .defaultValue(OffhandItem.Crystal)
        .build()
    );

    public final Value<Boolean> hotbar = sgCombat.add(new FlagValue.Builder()
        .name("hotbar")
        .description("Only search hotbar for offhand candidates.")
        .defaultValue(false)
        .build()
    );

    public final Value<Boolean> rightGapple = sgCombat.add(new FlagValue.Builder()
        .name("right-gapple")
        .description("Swaps to golden apple when right-clicking.")
        .defaultValue(false)
        .build()
    );

    public final Value<Boolean> swordGapple = sgCombat.add(new FlagValue.Builder()
        .name("sword-gapple")
        .description("Only right-click gapple when holding a sword.")
        .defaultValue(false)
        .visible(rightGapple::get)
        .build()
    );

    public final Value<Boolean> alwaysGapOnSword = sgCombat.add(new FlagValue.Builder()
        .name("always-gap-on-sword")
        .description("Always equips golden apple when holding a sword.")
        .defaultValue(false)
        .visible(() -> !rightGapple.get())
        .build()
    );

    public final Value<Boolean> alwaysPotOnSword = sgCombat.add(new FlagValue.Builder()
        .name("always-pot-on-sword")
        .description("Always equips potion when holding a sword.")
        .defaultValue(false)
        .visible(() -> !rightGapple.get() && !alwaysGapOnSword.get())
        .build()
    );

    public final Value<Boolean> swordPot = sgCombat.add(new FlagValue.Builder()
        .name("sword-pot")
        .description("Equips potion when sword is held.")
        .defaultValue(false)
        .build()
    );

    public final Value<Double> minHealth = sgTotem.add(new DoubleValue.Builder()
        .name("min-health")
        .description("Health threshold below which a Totem is strictly forced.")
        .defaultValue(10.0)
        .min(0.0)
        .max(36.0)
        .sliderRange(0.0, 36.0)
        .build()
    );

    public final Value<Boolean> elytra = sgTotem.add(new FlagValue.Builder()
        .name("elytra")
        .description("Forces Totem into offhand while gliding with Elytra.")
        .defaultValue(false)
        .build()
    );

    public final Value<Boolean> falling = sgTotem.add(new FlagValue.Builder()
        .name("falling")
        .description("Forces Totem into offhand during high fall distance.")
        .defaultValue(false)
        .build()
    );

    public final Value<Boolean> explosion = sgTotem.add(new FlagValue.Builder()
        .name("explosion")
        .description("Forces Totem into offhand when near primed explosives.")
        .defaultValue(true)
        .build()
    );

    private int delayTimer = 0;

    public Sidearm() {
        super(Group.Extras, "sidearm", "Manages offhand item dynamically based on combat conditions.");
    }

    @Listen
    private void onTick(ClientTick event) {
        if (mc.player == null) return;

        Totem totemLoop = Features.get().get(Totem.class);
        if (totemLoop != null && totemLoop.isLocked()) return;

        if (delayTimer > 0) {
            delayTimer--;
            return;
        }

        boolean needTotem = (mc.player.getHealth() <= minHealth.get())
            || (elytra.get() && mc.player.isFallFlying())
            || (falling.get() && mc.player.fallDistance > 3.0f);

        Item desiredItem;
        if (needTotem) {
            desiredItem = Items.TOTEM_OF_UNDYING;
        } else {
            desiredItem = getItemType(item.get());
        }

        ItemStack currentOffhand = mc.player.getOffhandItem();
        if (currentOffhand.is(desiredItem)) return;

        FindResult result = hotbar.get() ? InventoryKit.findInHotbar(desiredItem) : InventoryKit.find(desiredItem);
        if (result.found() && result.slot() != 45) {
            InventoryKit.move().from(result.slot()).toOffhand();
            delayTimer = itemSwitchDelay.get();
        }
    }

    private Item getItemType(OffhandItem item) {
        return switch (item) {
            case EGap -> Items.ENCHANTED_GOLDEN_APPLE;
            case Gap -> Items.GOLDEN_APPLE;
            case Crystal -> Items.END_CRYSTAL;
            case Totem -> Items.TOTEM_OF_UNDYING;
            case Shield -> Items.SHIELD;
            case Potion -> Items.SPLASH_POTION;
        };
    }
}