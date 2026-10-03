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

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class Arsenal extends Feature {
    public enum PreferEnchant {
        None,
        Fortune,
        SilkTouch
    }

    public enum ListMode {
        Whitelist,
        Blacklist
    }

    private final ValueGroup sgGeneral = settings.getDefaultGroup();
    private final ValueGroup sgFilter = settings.createGroup("Filter");

    public final Value<PreferEnchant> prefer = sgGeneral.add(new ChoiceValue.Builder<PreferEnchant>()
        .name("prefer")
        .description("Preferred tool enchantment bias.")
        .defaultValue(PreferEnchant.Fortune)
        .build()
    );

    public final Value<Boolean> silkTouchForEnderChest = sgGeneral.add(new FlagValue.Builder()
        .name("silk-touch-for-ender-chest")
        .description("Forces silk touch tool for Ender Chests.")
        .defaultValue(true)
        .build()
    );

    public final Value<Boolean> fortuneForOresAndCrops = sgGeneral.add(new FlagValue.Builder()
        .name("fortune-for-ores-and-crops")
        .description("Prefers Fortune on valuable ores and mature crops.")
        .defaultValue(false)
        .build()
    );

    public final Value<Boolean> antiBreak = sgGeneral.add(new FlagValue.Builder()
        .name("anti-break")
        .description("Prevents using tools near break threshold.")
        .defaultValue(false)
        .build()
    );

    public final Value<Integer> antiBreakPercentage = sgGeneral.add(new IntValue.Builder()
        .name("anti-break-percentage")
        .description("Durability percentage at which tools are protected.")
        .defaultValue(10)
        .min(1)
        .max(100)
        .sliderRange(1, 100)
        .visible(antiBreak::get)
        .build()
    );

    public final Value<Boolean> switchBack = sgGeneral.add(new FlagValue.Builder()
        .name("switch-back")
        .description("Switches back to previous slot after mining completes.")
        .defaultValue(false)
        .build()
    );

    public final Value<Integer> switchDelay = sgGeneral.add(new IntValue.Builder()
        .name("switch-delay")
        .description("Tick delay before switching back.")
        .defaultValue(0)
        .min(0)
        .max(20)
        .sliderRange(0, 20)
        .visible(switchBack::get)
        .build()
    );

    public final Value<ListMode> listMode = sgFilter.add(new ChoiceValue.Builder<ListMode>()
        .name("list-mode")
        .description("Item filter mode.")
        .defaultValue(ListMode.Blacklist)
        .build()
    );

    public final Value<List<Item>> whitelist = sgFilter.add(new ItemListValue.Builder()
        .name("whitelist")
        .description("Allowed tools in Whitelist mode.")
        .defaultValue(new ArrayList<>())
        .visible(() -> listMode.get() == ListMode.Whitelist)
        .build()
    );

    public final Value<List<Item>> blacklist = sgFilter.add(new ItemListValue.Builder()
        .name("blacklist")
        .description("Excluded tools in Blacklist mode.")
        .defaultValue(new ArrayList<>())
        .visible(() -> listMode.get() == ListMode.Blacklist)
        .build()
    );

    private boolean wasMining = false;
    private int delayTimer = 0;

    public Arsenal() {
        super(Group.Extras, "arsenal", "Automatically switches to the best tool for the targeted block.");
    }

    @Listen(priority = Order.HIGH)
    private void onStartBreaking(StartBreakBlock event) {
        if (mc.player == null || mc.level == null) return;

        BlockPos pos = event.blockPos;
        BlockState state = mc.level.getBlockState(pos);
        if (state.isAir()) return;

        int bestSlot = findBestTool(state, pos);
        if (bestSlot != -1 && bestSlot != mc.player.getInventory().getSelectedSlot()) {
            InventoryKit.swap(bestSlot, switchBack.get());
            wasMining = true;
            delayTimer = switchDelay.get();
        }
    }

    @Listen
    private void onTick(WorldTick event) {
        if (mc.player == null) return;

        if (wasMining && switchBack.get()) {
            if (mc.gameMode == null || !mc.gameMode.isDestroying()) {
                if (delayTimer > 0) {
                    delayTimer--;
                } else {
                    InventoryKit.swapBack();
                    wasMining = false;
                }
            }
        }
    }

    private int findBestTool(BlockState state, BlockPos pos) {
        int bestSlot = -1;
        float bestSpeed = 1.0f;

        boolean isEnderChest = state.is(Blocks.ENDER_CHEST);
        boolean isOre = Ores.ORES.contains(state.getBlock());

        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getItem(i);
            if (stack.isEmpty()) continue;

            Item item = stack.getItem();
            if (listMode.get() == ListMode.Whitelist && !whitelist.get().contains(item)) continue;
            if (listMode.get() == ListMode.Blacklist && blacklist.get().contains(item)) continue;

            if (antiBreak.get() && stack.isDamageableItem()) {
                double pct = (double) (stack.getMaxDamage() - stack.getDamageValue()) / stack.getMaxDamage() * 100.0;
                if (pct <= antiBreakPercentage.get()) continue;
            }

            float speed = stack.getDestroySpeed(state);
            if (speed > bestSpeed) {
                bestSpeed = speed;
                bestSlot = i;
            }
        }

        return bestSlot;
    }
}