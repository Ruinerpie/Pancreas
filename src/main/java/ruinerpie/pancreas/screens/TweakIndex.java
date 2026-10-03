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

import java.util.ArrayList;
import java.util.List;

public class TweakIndex extends Panel {
    private final Group category;
    private int scrollOffset = 0;

    private Feature contextMenuModule = null;
    private int contextMenuX = 0;
    private int contextMenuY = 0;

    public TweakIndex(Group category, Screen parent) {
        super(Component.literal("Pancreas — " + category.displayName), parent, getTabForCategory(category));
        this.category = category;
    }

    private static NavTab getTabForCategory(Group cat) {
        return switch (cat) {
            case Cheats -> NavTab.CHEATS;
            case Extras -> NavTab.EXTRAS;
            case Misc -> NavTab.MISC;
            case Utilities -> NavTab.UTILITIES;
        };
    }

    @Override
    protected String getSearchPlaceholder() {
        return "Search tweaks in " + category.displayName + "...";
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        renderBackground(graphics);
        renderTabs(graphics, mouseX, mouseY);

        boolean isWide = width >= 700;
        int rowH = isWide ? 26 : 22;
        int spacing = 3;

        int panelW = Math.min(width - 32, 680);
        int panelX = (width - panelW) / 2;

        renderSearchBox(graphics, mouseX, mouseY, panelW);

        int panelY = 60;
        int panelH = height - 76; 
        int catAccent = Theme.getCategoryColor(category).getPacked();

        renderBeveledPanel(graphics, panelX, panelY, panelW, panelH, false, Theme.surface.getPacked(), 0);

        int headerH = 26;
        renderBeveledPanel(graphics, panelX + 4, panelY + 4, panelW - 8, headerH, true, Theme.surfaceAlt.getPacked(), 0);
        graphics.fill(panelX + 4, panelY + 4, panelX + 8, panelY + 4 + headerH, catAccent);

        graphics.text(font, category.displayName.toUpperCase(), panelX + 14, panelY + 12, catAccent, false);
        graphics.text(font, category.description != null ? category.description : "", panelX + 90, panelY + 13, Theme.textMuted.getPacked(), false);

        List<Feature> allModules = Features.get().in(category);
        long activeCount = allModules.stream().filter(Feature::isActive).count();
        String stats = activeCount + " on / " + allModules.size() + " total";
        int statsW = font.width(stats);
        graphics.text(font, stats, panelX + panelW - statsW - 14, panelY + 13, Theme.textAccent.getPacked(), false);

        List<Feature> filtered = new ArrayList<>();
        for (Feature m : allModules) {
            if (searchQuery.isEmpty()
                || m.name.toLowerCase().contains(searchQuery.toLowerCase())
                || (m.description != null && m.description.toLowerCase().contains(searchQuery.toLowerCase()))
                || m.id.toLowerCase().contains(searchQuery.toLowerCase())) {
                filtered.add(m);
            }
        }

        int listY = panelY + headerH + 8;
        int listH = panelH - headerH - 14;
        int startY = listY - scrollOffset;
        boolean showIcons = Prefs.get().layout.showIcons;

        if (filtered.isEmpty()) {
            graphics.centeredText(font, "No tweaks found matching '" + searchQuery + "'.", panelX + panelW / 2, listY + 30, Theme.textDisabled.getPacked());
        } else {
            int curY = startY;
            for (Feature m : filtered) {
                if (curY + rowH >= listY && curY <= listY + listH - rowH) {
                    boolean active = m.isActive();
                    boolean hovered = isHovered(mouseX, mouseY, panelX + 6, curY, panelW - 12, rowH);

                    int rowFill = active ? Theme.surfaceRaised.getPacked() : (hovered ? Theme.surfaceAlt.getPacked() : Theme.surface.getPacked());
                    int focusRing = hovered ? catAccent : (active ? Theme.borderLight.getPacked() : 0);

                    renderBeveledPanel(graphics, panelX + 6, curY, panelW - 12, rowH, active, rowFill, focusRing);

                    int textLeft = panelX + 12;

                    int nameColor = active ? Theme.textAccent.getPacked() : (hovered ? Theme.text.getPacked() : Theme.textMuted.getPacked());

                    if (isWide && m.description != null && !m.description.isEmpty()) {
                        
                        drawHighlightedText(graphics, font, m.name, searchQuery, textLeft, curY + 4, nameColor, Theme.accentGold.getPacked());
                        graphics.text(font, m.description, textLeft, curY + 14, Theme.textDisabled.getPacked(), false);
                    } else {
                        drawHighlightedText(graphics, font, m.name, searchQuery, textLeft, curY + (rowH - 8) / 2, nameColor, Theme.accentGold.getPacked());
                        if (m.description != null && !m.description.isEmpty()) {
                            int descLeft = textLeft + font.width(m.name) + 12;
                            int maxDescW = panelW - 240;
                            if (maxDescW > 50) {
                                graphics.text(font, m.description, descLeft, curY + (rowH - 8) / 2, Theme.textDisabled.getPacked(), false);
                            }
                        }
                    }

                    if (m.getKeybind() > 0) {
                        String bindStr = m.getKeybindName();
                        int kbdW = font.width(bindStr) + 8;
                        int kbdX = panelX + panelW - 74 - kbdW;
                        int kbdY = curY + (rowH - 14) / 2;
                        renderBeveledPanel(graphics, kbdX, kbdY, kbdW, 14, true, Theme.surface.getPacked(), 0);
                        graphics.centeredText(font, bindStr, kbdX + kbdW / 2, kbdY + 3, Theme.accentLapis.getPacked());
                    }

                    int toggleW = 54;
                    int toggleH = rowH - 4;
                    int toggleX = panelX + panelW - 68;
                    int toggleY = curY + 2;
                    boolean toggleHover = isHovered(mouseX, mouseY, toggleX, toggleY, toggleW, toggleH);
                    renderToggleSlot(graphics, toggleX, toggleY, toggleW, toggleH, active, toggleHover);
                }

                curY += rowH + spacing;
            }
        }

        if (contextMenuModule != null) {
            int cmW = 120;
            int cmH = 76;
            int cmX = Math.min(contextMenuX, width - cmW - 4);
            int cmY = Math.min(contextMenuY, height - cmH - 18);

            renderBeveledPanel(graphics, cmX, cmY, cmW, cmH, false, Theme.surfaceRaised.getPacked(), Theme.focusRing.getPacked());

            String[] items = {
                contextMenuModule.isActive() ? "Disable" : "Enable",
                "Reset Defaults",
                "Configure",
                "Copy ID"
            };

            for (int i = 0; i < items.length; i++) {
                int itemY = cmY + 4 + i * 17;
                boolean hov = isHovered(mouseX, mouseY, cmX + 3, itemY, cmW - 6, 16);
                if (hov) {
                    graphics.fill(cmX + 3, itemY, cmX + cmW - 3, itemY + 16, Theme.surfaceAlt.getPacked());
                }
                graphics.text(font, items[i], cmX + 8, itemY + 4, hov ? Theme.textAccent.getPacked() : Theme.text.getPacked(), false);
            }
        }

        renderStatusBar(graphics);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean isFocused) {
        double mouseX = event.x();
        double mouseY = event.y();

        if (contextMenuModule != null) {
            int cmW = 120;
            int cmH = 76;
            int cmX = Math.min(contextMenuX, width - cmW - 4);
            int cmY = Math.min(contextMenuY, height - cmH - 18);

            if (event.button() == 0 && isHovered(mouseX, mouseY, cmX, cmY, cmW, cmH)) {
                int clickedIndex = (int) (mouseY - cmY - 4) / 17;
                playUiSound();
                switch (clickedIndex) {
                    case 0 -> contextMenuModule.toggle();
                    case 1 -> contextMenuModule.resetSettings();
                    case 2 -> {
                        if (minecraft != null) minecraft.setScreen(new TweakSettings(contextMenuModule, this));
                    }
                    case 3 -> {
                        if (minecraft != null) minecraft.keyboardHandler.setClipboard(contextMenuModule.id);
                    }
                }
                contextMenuModule = null;
                return true;
            } else {
                contextMenuModule = null;
                if (event.button() != 1) return true;
            }
        }

        int panelW = Math.min(width - 32, 680);
        int panelX = (width - panelW) / 2;
        int panelY = 60;
        int panelH = height - 76;
        int headerH = 26;
        boolean isWide = width >= 700;
        int rowH = isWide ? 26 : 22;
        int spacing = 3;

        int listY = panelY + headerH + 8;
        int listH = panelH - headerH - 14;
        int startY = listY - scrollOffset;

        List<Feature> allModules = Features.get().in(category);
        List<Feature> filtered = new ArrayList<>();
        for (Feature m : allModules) {
            if (searchQuery.isEmpty()
                || m.name.toLowerCase().contains(searchQuery.toLowerCase())
                || (m.description != null && m.description.toLowerCase().contains(searchQuery.toLowerCase()))
                || m.id.toLowerCase().contains(searchQuery.toLowerCase())) {
                filtered.add(m);
            }
        }

        if (event.button() == 0) {
            if (handleTabClicks(mouseX, mouseY)) return true;
            if (handleSearchClick(mouseX, mouseY, panelW)) return true;

            int curY = startY;
            for (Feature m : filtered) {
                if (curY + rowH >= listY && curY <= listY + listH - rowH) {
                    if (isHovered(mouseX, mouseY, panelX + 6, curY, panelW - 12, rowH)) {
                        int toggleW = 54;
                        int toggleH = rowH - 4;
                        int toggleX = panelX + panelW - 68;
                        int toggleY = curY + 2;

                        playUiSound();
                        if (isHovered(mouseX, mouseY, toggleX, toggleY, toggleW, toggleH)) {
                            m.toggle();
                        } else {
                            if (minecraft != null) {
                                minecraft.setScreen(new TweakSettings(m, this));
                            }
                        }
                        return true;
                    }
                }
                curY += rowH + spacing;
            }
        } else if (event.button() == 1) {
            
            int curY = startY;
            for (Feature m : filtered) {
                if (curY + rowH >= listY && curY <= listY + listH - rowH) {
                    if (isHovered(mouseX, mouseY, panelX + 6, curY, panelW - 12, rowH)) {
                        playUiSound();
                        contextMenuModule = m;
                        contextMenuX = (int) mouseX;
                        contextMenuY = (int) mouseY;
                        return true;
                    }
                }
                curY += rowH + spacing;
            }
        }

        return super.mouseClicked(event, isFocused);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        scrollOffset = Math.max(0, scrollOffset - (int) (verticalAmount * 24));
        return true;
    }

    @Override
    protected void onSearchEnterPressed() {
        for (Feature m : Features.get().in(category)) {
            if (searchQuery.isEmpty()
                || m.name.toLowerCase().contains(searchQuery.toLowerCase())
                || (m.description != null && m.description.toLowerCase().contains(searchQuery.toLowerCase()))
                || m.id.toLowerCase().contains(searchQuery.toLowerCase())) {
                if (minecraft != null) {
                    minecraft.setScreen(new TweakSettings(m, this));
                }
                return;
            }
        }
        triggerSearchShake();
    }
}