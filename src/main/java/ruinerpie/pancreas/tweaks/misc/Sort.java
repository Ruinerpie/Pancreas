package ruinerpie.pancreas.tweaks.misc;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;
import ruinerpie.pancreas.Feature;
import ruinerpie.pancreas.Group;
import ruinerpie.pancreas.Listen;
import ruinerpie.pancreas.Pancreas;
import ruinerpie.pancreas.Value;
import ruinerpie.pancreas.ValueGroup;
import ruinerpie.pancreas.signals.ClientTick;
import ruinerpie.pancreas.signals.ScreenOpen;
import ruinerpie.pancreas.values.ChoiceValue;
import ruinerpie.pancreas.values.FlagValue;
import ruinerpie.pancreas.values.IntValue;
import ruinerpie.pancreas.values.ItemListValue;
import ruinerpie.pancreas.values.KeyValue;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

public class Sort extends Feature {
    public enum FilterMode {
        Whitelist,
        Blacklist,
        None
    }

    private final ValueGroup sgGeneral = settings.getDefaultGroup();
    private final ValueGroup sgRisky = settings.createGroup("Risky");
    private final ValueGroup sgSorting = settings.createGroup("Sorting");
    private final ValueGroup sgAntiDrop = settings.createGroup("AntiDrop");
    private final ValueGroup sgAutoDrop = settings.createGroup("AutoDrop");
    private final ValueGroup sgStealDump = settings.createGroup("StealDump");
    private final ValueGroup sgAutoSteal = settings.createGroup("AutoSteal");

    public final FlagValue mouseDragItemMove = sgGeneral.add(new FlagValue.Builder()
        .name("mouse-drag-item-move")
        .description("Enables moving items by dragging.")
        .defaultValue(true)
        .build());

    public final FlagValue uncapBundleScrolling = sgGeneral.add(new FlagValue.Builder()
        .name("uncap-bundle-scrolling")
        .description("Removes scroll speed limitations inside bundle tooltips.")
        .defaultValue(true)
        .build());

    public final FlagValue xcarry = sgRisky.add(new FlagValue.Builder()
        .name("xcarry")
        .description("Preserves items placed in crafting slots after closing inventory.")
        .defaultValue(true)
        .build());

    public final FlagValue frameInputHandling = sgRisky.add(new FlagValue.Builder()
        .name("frame-input-handling")
        .description("Process inputs outside of vanilla inventory tick bounds.")
        .defaultValue(false)
        .build());

    public final FlagValue sortingEnabled = sgSorting.add(new FlagValue.Builder()
        .name("sorting-enabled")
        .description("Sorts inventory items by item registry order.")
        .defaultValue(true)
        .build());

    public final KeyValue sortingKey = sgSorting.add(new KeyValue.Builder()
        .name("sorting-key")
        .description("Key to trigger inventory sorting.")
        .defaultValue(GLFW.GLFW_MOUSE_BUTTON_MIDDLE)
        .visible(sortingEnabled::get)
        .build());

    public final IntValue sortingDelay = sgSorting.add(new IntValue.Builder()
        .name("sorting-delay")
        .description("Tick delay between slot swaps.")
        .defaultValue(1)
        .min(0)
        .max(10)
        .visible(sortingEnabled::get)
        .build());

    public final FlagValue disableInCreative = sgSorting.add(new FlagValue.Builder()
        .name("disable-in-creative")
        .description("Disables sorting while in creative game mode.")
        .defaultValue(true)
        .visible(sortingEnabled::get)
        .build());

    public final ItemListValue antiDropItems = sgAntiDrop.add(new ItemListValue.Builder()
        .name("anti-drop-items")
        .description("List of items protected from accidental dropping.")
        .defaultValue(new ArrayList<>())
        .build());

    public final FlagValue itemFrames = sgAntiDrop.add(new FlagValue.Builder()
        .name("item-frames")
        .description("Prevents placing protected items into item frames.")
        .defaultValue(true)
        .build());

    public final KeyValue overrideBind = sgAntiDrop.add(new KeyValue.Builder()
        .name("override-bind")
        .description("Key to hold to bypass drop protection.")
        .defaultValue(-1)
        .build());

    public final ItemListValue autoDropItems = sgAutoDrop.add(new ItemListValue.Builder()
        .name("auto-drop-items")
        .description("Items to automatically toss from inventory.")
        .defaultValue(new ArrayList<>())
        .build());

    public final FlagValue excludeEquipped = sgAutoDrop.add(new FlagValue.Builder()
        .name("exclude-equipped")
        .description("Never auto-drop equipped armor or offhand.")
        .defaultValue(true)
        .build());

    public final FlagValue excludeHotbar = sgAutoDrop.add(new FlagValue.Builder()
        .name("exclude-hotbar")
        .description("Never auto-drop hotbar slots.")
        .defaultValue(false)
        .build());

    public final FlagValue onlyFullStacks = sgAutoDrop.add(new FlagValue.Builder()
        .name("only-full-stacks")
        .description("Only drop when stack is full.")
        .defaultValue(false)
        .build());

    public final FlagValue inventoryButtons = sgStealDump.add(new FlagValue.Builder()
        .name("inventory-buttons")
        .description("Shows Steal and Dump buttons in container screens.")
        .defaultValue(true)
        .build());

    public final FlagValue stealDrop = sgStealDump.add(new FlagValue.Builder()
        .name("steal-drop")
        .description("Drops stolen items on the ground instead of taking them into inventory.")
        .defaultValue(false)
        .build());

    public final FlagValue dropBackwards = sgStealDump.add(new FlagValue.Builder()
        .name("drop-backwards")
        .description("Throws items behind player.")
        .defaultValue(false)
        .visible(stealDrop::get)
        .build());

    public final ChoiceValue<FilterMode> dumpFilter = sgStealDump.add(new ChoiceValue.Builder<FilterMode>()
        .name("dump-filter")
        .description("Filter mode for container dump.")
        .defaultValue(FilterMode.None)
        .build());

    public final ItemListValue dumpItems = sgStealDump.add(new ItemListValue.Builder()
        .name("dump-items")
        .description("Item list for dump filter.")
        .defaultValue(new ArrayList<>())
        .build());

    public final ChoiceValue<FilterMode> stealFilter = sgStealDump.add(new ChoiceValue.Builder<FilterMode>()
        .name("steal-filter")
        .description("Filter mode for container steal.")
        .defaultValue(FilterMode.None)
        .build());

    public final ItemListValue stealItems = sgStealDump.add(new ItemListValue.Builder()
        .name("steal-items")
        .description("Item list for steal filter.")
        .defaultValue(new ArrayList<>())
        .build());

    public final FlagValue autoSteal = sgAutoSteal.add(new FlagValue.Builder()
        .name("auto-steal")
        .description("Automatically steals from opened containers.")
        .defaultValue(false)
        .build());

    public final FlagValue autoDump = sgAutoSteal.add(new FlagValue.Builder()
        .name("auto-dump")
        .description("Automatically dumps into opened containers.")
        .defaultValue(false)
        .build());

    public final IntValue delay = sgAutoSteal.add(new IntValue.Builder()
        .name("delay")
        .description("Ticks between slot operations.")
        .defaultValue(20)
        .min(1)
        .max(100)
        .build());

    public final IntValue initialDelay = sgAutoSteal.add(new IntValue.Builder()
        .name("initial-delay")
        .description("Ticks to wait after opening container.")
        .defaultValue(50)
        .min(0)
        .max(200)
        .build());

    public final IntValue random = sgAutoSteal.add(new IntValue.Builder()
        .name("random")
        .description("Random variance in ticks.")
        .defaultValue(50)
        .min(0)
        .max(100)
        .build());

    private record SlotAction(int containerId, int slotId, int button, ContainerInput input) {}
    private final Deque<SlotAction> actionQueue = new ArrayDeque<>();
    private int tickTimer = 0;

    public Sort() {
        super(Group.Misc, "sort", "Inventory sorting, auto-drop, container steal and dump.");
    }

    @Override
    public void onDeactivate() {
        actionQueue.clear();
    }

    @Listen
    private void onTick(ClientTick event) {
        var player = Pancreas.mc.player;
        if (player == null) return;

        if (!autoDropItems.get().isEmpty()) {
            var inv = player.getInventory();
            for (int i = 0; i < 36; i++) {
                if (excludeHotbar.get() && i < 9) continue;
                ItemStack stack = inv.getItem(i);
                if (stack.isEmpty()) continue;
                if (onlyFullStacks.get() && stack.getCount() < stack.getMaxStackSize()) continue;
                if (autoDropItems.get().contains(stack.getItem())) {
                    actionQueue.add(new SlotAction(0, i < 9 ? i + 36 : i, 1, ContainerInput.THROW));
                }
            }
        }

        if (tickTimer > 0) {
            tickTimer--;
            return;
        }

        if (!actionQueue.isEmpty()) {
            SlotAction act = actionQueue.poll();
            var gameMode = Pancreas.mc.gameMode;
            if (act != null && gameMode != null && act.input != null) {
                gameMode.handleContainerInput(act.containerId, act.slotId, act.button, act.input, player);
            }
            tickTimer = sortingDelay.get();
        }
    }

    @Listen
    private void onOpenScreen(ScreenOpen event) {
        if (event.screen instanceof AbstractContainerScreen<?> containerScreen) {
            if (autoSteal.get()) {
                tickTimer = initialDelay.get() + (random.get() > 0 ? (int) (Math.random() * random.get()) : 0);
                triggerSteal(containerScreen);
            } else if (autoDump.get()) {
                tickTimer = initialDelay.get() + (random.get() > 0 ? (int) (Math.random() * random.get()) : 0);
                triggerDump(containerScreen);
            }
        }
    }

    public void triggerSteal(AbstractContainerScreen<?> screen) {
        if (screen == null) return;
        var menu = screen.getMenu();
        if (menu == null || menu.slots == null) return;
        int containerSlots = menu.slots.size() - 36;
        for (int i = 0; i < containerSlots; i++) {
            Slot slot = menu.getSlot(i);
            if (slot == null || !slot.hasItem()) continue;
            ItemStack stack = slot.getItem();
            if (stack == null || stack.isEmpty()) continue;

            if (stealFilter.get() == FilterMode.Whitelist && !stealItems.get().contains(stack.getItem())) continue;
            if (stealFilter.get() == FilterMode.Blacklist && stealItems.get().contains(stack.getItem())) continue;

            actionQueue.add(new SlotAction(menu.containerId, i, 0, ContainerInput.QUICK_MOVE));
        }
    }

    public void triggerDump(AbstractContainerScreen<?> screen) {
        if (screen == null) return;
        var menu = screen.getMenu();
        if (menu == null || menu.slots == null) return;
        int totalSlots = menu.slots.size();
        for (int i = totalSlots - 36; i < totalSlots; i++) {
            Slot slot = menu.getSlot(i);
            if (slot == null || !slot.hasItem()) continue;
            ItemStack stack = slot.getItem();
            if (stack == null || stack.isEmpty()) continue;

            if (dumpFilter.get() == FilterMode.Whitelist && !dumpItems.get().contains(stack.getItem())) continue;
            if (dumpFilter.get() == FilterMode.Blacklist && dumpItems.get().contains(stack.getItem())) continue;

            actionQueue.add(new SlotAction(menu.containerId, i, 0, ContainerInput.QUICK_MOVE));
        }
    }
}