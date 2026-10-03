package ruinerpie.pancreas.screens;

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

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public class Groups extends Panel {

    public Groups(Screen parent) {
        super(Component.literal("Pancreas — Group"), parent);
    }

    @Override
    protected String getSearchPlaceholder() {
        return "Search categories...";
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        renderBackground(graphics);
        renderTabs(graphics, mouseX, mouseY);

        int panelW = Math.min(width - 32, 680);
        int panelX = (width - panelW) / 2;

        renderSearchBox(graphics, mouseX, mouseY, panelW);

        int panelY = 60;
        int panelH = height - 68;

        renderBeveledPanel(graphics, panelX, panelY, panelW, panelH, false, Theme.surface.getPacked(), 0);

        int headerH = 26;
        renderBeveledPanel(graphics, panelX + 4, panelY + 4, panelW - 8, headerH, true, Theme.surfaceAlt.getPacked(), 0);
        graphics.fill(panelX + 4, panelY + 4, panelX + 8, panelY + 4 + headerH, Theme.accentPurple.getPacked());
        graphics.text(font, "CATEGORIES", panelX + 14, panelY + 13, Theme.textAccent.getPacked(), false);
        graphics.text(font, "Select a category or click a tab above to browse tweaks", panelX + 90, panelY + 13, Theme.textMuted.getPacked(), false);

        List<Group> matched = new ArrayList<>();
        for (Group cat : Group.values()) {
            if (searchQuery.isEmpty()
                || cat.displayName.toLowerCase().contains(searchQuery.toLowerCase())
                || (cat.description != null && cat.description.toLowerCase().contains(searchQuery.toLowerCase()))) {
                matched.add(cat);
            }
        }

        int cardY = panelY + headerH + 10;
        int cardH = (panelH - headerH - 32) / 4;
        boolean showIcons = Prefs.get().layout.showIcons;

        if (matched.isEmpty()) {
            graphics.centeredText(font, "No categories found matching '" + searchQuery + "'.", panelX + panelW / 2, cardY + 30, Theme.textDisabled.getPacked());
        } else {
            for (Group cat : matched) {
                boolean hovered = isHovered(mouseX, mouseY, panelX + 8, cardY, panelW - 16, cardH);
                int catColor = Theme.getCategoryColor(cat).getPacked();
                int fill = hovered ? Theme.surfaceRaised.getPacked() : Theme.surfaceAlt.getPacked();
                int focus = hovered ? catColor : 0;

                renderBeveledPanel(graphics, panelX + 8, cardY, panelW - 16, cardH, false, fill, focus);
                graphics.fill(panelX + 8, cardY, panelX + 12, cardY + cardH, catColor);

                int textLeft = panelX + 20;

                graphics.text(font, cat.displayName, textLeft, cardY + 10, catColor, false);
                if (cat.description != null) {
                    graphics.text(font, cat.description, textLeft, cardY + 24, Theme.textMuted.getPacked(), false);
                }

                List<Feature> mods = Features.get().in(cat);
                long activeCount = mods.stream().filter(Feature::isActive).count();
                String stats = mods.size() + " tweaks (" + activeCount + " on)  >";
                int statsW = font.width(stats);
                int statsColor = hovered ? Theme.textAccent.getPacked() : Theme.textDisabled.getPacked();
                graphics.text(font, stats, panelX + panelW - statsW - 24, cardY + (cardH - 8) / 2, statsColor, false);

                cardY += cardH + 4;
            }
        }
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean isFocused) {
        if (event.button() == 0) {
            double mouseX = event.x();
            double mouseY = event.y();

            if (handleTabClicks(mouseX, mouseY)) {
                return true;
            }

            int panelW = Math.min(width - 32, 680);
            if (handleSearchClick(mouseX, mouseY, panelW)) {
                return true;
            }

            int panelX = (width - panelW) / 2;
            int panelY = 60;
            int panelH = height - 68;
            int headerH = 26;

            int cardY = panelY + headerH + 10;
            int cardH = (panelH - headerH - 32) / 4;

            for (Group cat : Group.values()) {
                if (searchQuery.isEmpty()
                    || cat.displayName.toLowerCase().contains(searchQuery.toLowerCase())
                    || (cat.description != null && cat.description.toLowerCase().contains(searchQuery.toLowerCase()))) {
                    if (isHovered(mouseX, mouseY, panelX + 8, cardY, panelW - 16, cardH)) {
                        playUiSound();
                        if (minecraft != null) {
                            minecraft.setScreen(new TweakIndex(cat, this));
                        }
                        return true;
                    }
                    cardY += cardH + 4;
                }
            }
        }
        return super.mouseClicked(event, isFocused);
    }

    @Override
    protected void onSearchEnterPressed() {
        for (Group cat : Group.values()) {
            if (searchQuery.isEmpty()
                || cat.displayName.toLowerCase().contains(searchQuery.toLowerCase())
                || (cat.description != null && cat.description.toLowerCase().contains(searchQuery.toLowerCase()))) {
                if (minecraft != null) {
                    minecraft.setScreen(new TweakIndex(cat, this));
                }
                return;
            }
        }
    }
}