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

import org.lwjgl.glfw.GLFW;

public class Glimpse extends Feature {
    public enum DisplayWhen {
        Key,
        Always
    }

    public enum ByteSizeFormat {
        Bytes,
        Kilobytes,
        Megabytes,
        Dynamic
    }

    private final ValueGroup sgGeneral = settings.getDefaultGroup();
    private final ValueGroup sgPreviews = settings.createGroup("Previews");
    private final ValueGroup sgOther = settings.createGroup("Other");
    private final ValueGroup sgHideFlags = settings.createGroup("Hide Flags");

    public final Value<DisplayWhen> displayWhen = sgGeneral.add(new ChoiceValue.Builder<DisplayWhen>()
        .name("display-when")
        .description("When enhanced tooltip details appear.")
        .defaultValue(DisplayWhen.Key)
        .build()
    );

    public final Value<Integer> keybind = sgGeneral.add(new KeyValue.Builder()
        .name("keybind")
        .description("Key to reveal advanced tooltip components.")
        .defaultValue(GLFW.GLFW_KEY_LEFT_ALT)
        .visible(() -> displayWhen.get() == DisplayWhen.Key)
        .build()
    );

    public final Value<Boolean> openContents = sgGeneral.add(new FlagValue.Builder()
        .name("open-contents")
        .description("Allows opening container previews into interactive screens.")
        .defaultValue(true)
        .build()
    );

    public final Value<Integer> openContentsKeybind = sgGeneral.add(new KeyValue.Builder()
        .name("open-contents-keybind")
        .description("Key/mouse button to inspect container contents.")
        .defaultValue(GLFW.GLFW_MOUSE_BUTTON_MIDDLE)
        .visible(openContents::get)
        .build()
    );

    public final Value<Boolean> pauseInCreative = sgGeneral.add(new FlagValue.Builder()
        .name("pause-in-creative")
        .description("Pauses preview interactions in Creative mode.")
        .defaultValue(true)
        .visible(openContents::get)
        .build()
    );

    public final Value<Boolean> containers = sgPreviews.add(new FlagValue.Builder()
        .name("containers")
        .description("Shows grid preview of shulkers and storage.")
        .defaultValue(true)
        .build()
    );

    public final Value<Boolean> compactShulkerTooltip = sgPreviews.add(new FlagValue.Builder()
        .name("compact-shulker-tooltip")
        .description("Compact 3x9 grid view for shulker boxes.")
        .defaultValue(true)
        .visible(containers::get)
        .build()
    );

    public final Value<Boolean> echests = sgPreviews.add(new FlagValue.Builder()
        .name("echests")
        .description("Shows Ender Chest contents preview.")
        .defaultValue(true)
        .build()
    );

    public final Value<Boolean> maps = sgPreviews.add(new FlagValue.Builder()
        .name("maps")
        .description("Renders miniature map preview in tooltips.")
        .defaultValue(true)
        .build()
    );

    public final Value<Double> mapScale = sgPreviews.add(new DoubleValue.Builder()
        .name("map-scale")
        .description("Scale of rendered map tooltips.")
        .defaultValue(1.0)
        .min(0.5)
        .max(3.0)
        .visible(maps::get)
        .build()
    );

    public final Value<Boolean> books = sgPreviews.add(new FlagValue.Builder()
        .name("books")
        .description("Previews written book pages.")
        .defaultValue(true)
        .build()
    );

    public final Value<Boolean> banners = sgPreviews.add(new FlagValue.Builder()
        .name("banners")
        .description("Previews banner patterns.")
        .defaultValue(true)
        .build()
    );

    public final Value<Boolean> entitiesInBuckets = sgPreviews.add(new FlagValue.Builder()
        .name("entities-in-buckets")
        .description("Shows entity model for fish and axolotl buckets.")
        .defaultValue(true)
        .build()
    );

    public final Value<Boolean> bundles = sgPreviews.add(new FlagValue.Builder()
        .name("bundles")
        .description("Enhanced bundle item grid preview.")
        .defaultValue(true)
        .build()
    );

    public final Value<Boolean> foodInfo = sgPreviews.add(new FlagValue.Builder()
        .name("food-info")
        .description("Displays hunger and saturation values.")
        .defaultValue(true)
        .build()
    );

    public final Value<Boolean> byteSize = sgOther.add(new FlagValue.Builder()
        .name("byte-size")
        .description("Displays NBT packet byte size of items.")
        .defaultValue(true)
        .build()
    );

    public final Value<ByteSizeFormat> byteSizeFormat = sgOther.add(new ChoiceValue.Builder<ByteSizeFormat>()
        .name("byte-size-format")
        .description("Format unit for data byte size.")
        .defaultValue(ByteSizeFormat.Dynamic)
        .visible(byteSize::get)
        .build()
    );

    public final Value<Boolean> statusEffects = sgOther.add(new FlagValue.Builder()
        .name("status-effects")
        .description("Displays potion effect duration and potency stats.")
        .defaultValue(true)
        .build()
    );

    public final Value<Boolean> hideTooltip = sgHideFlags.add(new FlagValue.Builder()
        .name("tooltip")
        .description("Hides standard tooltips completely.")
        .defaultValue(false)
        .build()
    );

    public final Value<Boolean> hideTooltipComponents = sgHideFlags.add(new FlagValue.Builder()
        .name("tooltip-components")
        .description("Hides vanilla tooltip components.")
        .defaultValue(false)
        .build()
    );

    public Glimpse() {
        super(Group.Utilities, "glimpse", "Comprehensive tooltip expansions and visual item previews.");
    }

    public boolean shouldDisplay() {
        if (!isActive()) return false;
        if (displayWhen.get() == DisplayWhen.Always) return true;
        return GLFW.glfwGetKey(mc.getWindow().handle(), keybind.get()) == GLFW.GLFW_PRESS;
    }
}