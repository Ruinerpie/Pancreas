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

import java.util.List;

public class Gleam extends Feature {
    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    private final Value<List<Item>> items = sgGeneral.add(new ItemListValue.Builder()
        .name("items")
        .description("Items to highlight.")
        .build()
    );

    private final Value<Tint> color = sgGeneral.add(new TintValue.Builder()
        .name("color")
        .description("The color to highlight the items with.")
        .defaultValue(new Tint(225, 25, 255, 50))
        .build()
    );

    public Gleam() {
        super(Group.Extras, "gleam", "Highlights selected items when in guis");
    }

    public int getColor(ItemStack stack) {
        if (stack != null && items.get().contains(stack.getItem()) && isActive()) return color.get().getPacked();
        return -1;
    }
}