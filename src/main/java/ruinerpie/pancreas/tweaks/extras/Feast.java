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

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Map;

public class Feast extends Feature {
    private final ValueGroup sgGeneral = settings.getDefaultGroup();
    private final ValueGroup sgPotions = settings.createGroup("Potions");
    private final ValueGroup sgHealth = settings.createGroup("Health");

    private final Value<Boolean> allowEgap = sgGeneral.add(new FlagValue.Builder()
        .name("allow-egap")
        .description("Allow eating E-Gaps over Gaps if found.")
        .defaultValue(true)
        .build()
    );

    private final Value<Boolean> always = sgGeneral.add(new FlagValue.Builder()
        .name("always")
        .description("If it should always eat.")
        .defaultValue(false)
        .build()
    );

    private final Value<Boolean> pauseBaritone = sgGeneral.add(new FlagValue.Builder()
        .name("pause-baritone")
        .description("Pause baritone when eating.")
        .defaultValue(true)
        .build()
    );

    private final Value<Boolean> beforeExpiry = sgPotions.add(new FlagValue.Builder()
        .name("before-expiry")
        .description("If it should eat before potion effects expire.")
        .defaultValue(false)
        .build()
    );

    private final Value<Integer> expiryThreshold = sgPotions.add(new IntValue.Builder()
        .name("expiry-threshold")
        .description("Time in ticks before the potion effect expires to start eating.")
        .defaultValue(60)
        .min(0)
        .sliderMax(200)
        .visible(beforeExpiry::get)
        .build()
    );

    private final Value<Boolean> potionsRegeneration = sgPotions.add(new FlagValue.Builder()
        .name("potions-regeneration")
        .description("If it should eat when Regeneration runs out.")
        .defaultValue(false)
        .build()
    );

    private final Value<Boolean> potionsFireResistance = sgPotions.add(new FlagValue.Builder()
        .name("potions-fire-resistance")
        .description("If it should eat when Fire Resistance runs out. Requires E-Gaps.")
        .defaultValue(true)
        .visible(allowEgap::get)
        .build()
    );

    private final Value<Boolean> potionsAbsorption = sgPotions.add(new FlagValue.Builder()
        .name("potions-absorption")
        .description("If it should eat when Absorption runs out. Requires E-Gaps.")
        .defaultValue(false)
        .visible(allowEgap::get)
        .build()
    );

    private final Value<Boolean> healthEnabled = sgHealth.add(new FlagValue.Builder()
        .name("health-enabled")
        .description("If it should eat when health drops below threshold.")
        .defaultValue(true)
        .build()
    );

    private final Value<Integer> healthThreshold = sgHealth.add(new IntValue.Builder()
        .name("health-threshold")
        .description("Health threshold to eat at. Includes absorption.")
        .defaultValue(20)
        .min(0)
        .sliderMax(40)
        .build()
    );

    private boolean requiresEGap;
    private boolean eating;
    private int slot, prevSlot;
    private boolean wasBaritone;

    public Feast() {
        super(Group.Extras, "feast", "Automatically eats Gaps or E-Gaps.");
    }

    @Override
    public void onDeactivate() {
        if (eating) stopEating();
    }

    @Listen
    private void onTick(ClientTick event) {
        if (mc.player == null) return;

        if (eating) {
            if (shouldEat()) {
                if (isNotGapOrEGap(mc.player.getInventory().getItem(slot))) {
                    int nextSlot = findSlot();
                    if (nextSlot == -1) {
                        stopEating();
                        return;
                    } else {
                        changeSlot(nextSlot);
                    }
                }
                eat();
            } else {
                stopEating();
            }
        } else {
            if (shouldEat()) {
                slot = findSlot();
                if (slot != -1) startEating();
            }
        }
    }

    @Listen
    private void onItemUseCrosshairTarget(ItemUseCrosshairTarget event) {
        if (eating) event.target = null;
    }

    private void startEating() {
        if (mc.player == null) return;
        prevSlot = mc.player.getInventory().getSelectedSlot();
        eat();

        wasBaritone = false;
        wasBaritone = false;
    }

    private void eat() {
        if (mc.player == null) return;
        changeSlot(slot);
        setPressed(true);
        if (!mc.player.isUsingItem()) Terrain.rightClick();

        eating = true;
    }

    private void stopEating() {
        changeSlot(prevSlot);
        setPressed(false);

        eating = false;

    }

    private void setPressed(boolean pressed) {
        mc.options.keyUse.setDown(pressed);
    }

    private void changeSlot(int slot) {
        InventoryKit.swap(slot, false);
        this.slot = slot;
    }

    private boolean shouldEat() {
        requiresEGap = false;

        if (always.get()) return true;
        if (shouldEatPotions()) return true;
        return shouldEatHealth();
    }

    private boolean shouldEatPotions() {
        if (mc.player == null) return false;
        Map<Holder<MobEffect>, MobEffectInstance> effects = mc.player.getActiveEffectsMap();

        if (potionsRegeneration.get()) {
            MobEffectInstance effect = effects.get(MobEffects.REGENERATION);
            if (effect == null || (beforeExpiry.get() && effect.getDuration() <= expiryThreshold.get())) return true;
        }

        if (potionsFireResistance.get()) {
            MobEffectInstance effect = effects.get(MobEffects.FIRE_RESISTANCE);
            if (effect == null || (beforeExpiry.get() && effect.getDuration() <= expiryThreshold.get())) {
                requiresEGap = true;
                return true;
            }
        }

        if (potionsAbsorption.get()) {
            MobEffectInstance effect = effects.get(MobEffects.ABSORPTION);
            if (effect == null || (beforeExpiry.get() && effect.getDuration() <= expiryThreshold.get())) {
                requiresEGap = true;
                return true;
            }
        }

        return false;
    }

    private boolean shouldEatHealth() {
        if (!healthEnabled.get() || mc.player == null) return false;

        int health = Math.round(mc.player.getHealth() + mc.player.getAbsorptionAmount());
        return health < healthThreshold.get();
    }

    private int findSlot() {
        if (mc.player == null) return -1;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getItem(i);
            if (stack.isEmpty()) continue;
            if (isNotGapOrEGap(stack)) continue;

            Item item = stack.getItem();
            if (item == Items.ENCHANTED_GOLDEN_APPLE && allowEgap.get()) return i;
            if (item == Items.GOLDEN_APPLE && !requiresEGap) return i;
        }

        return -1;
    }

    private boolean isNotGapOrEGap(ItemStack stack) {
        Item item = stack.getItem();
        return item != Items.GOLDEN_APPLE && item != Items.ENCHANTED_GOLDEN_APPLE;
    }

    public boolean isEating() {
        return isActive() && eating;
    }
}