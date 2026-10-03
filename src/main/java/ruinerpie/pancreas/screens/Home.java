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
import java.util.Comparator;
import java.util.List;

public class Home extends Panel {

    private int activeScrollOffset = 0;
    private int searchScrollOffset = 0;

    public Home() {
        super(Component.literal("Pancreas"), null, null);
    }

    public Home(Screen parent) {
        super(Component.literal("Pancreas"), parent, null);
    }

    @Override
    protected String getSearchPlaceholder() {
        return "Search everything (tweaks, HUD, systems, settings)...";
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        renderBackground(graphics);
        renderTabs(graphics, mouseX, mouseY);

        int panelW = Math.min(width - 32, 740);
        int panelX = (width - panelW) / 2;

        renderSearchBox(graphics, mouseX, mouseY, panelW);

        int panelY = 60;
        int panelH = height - 76; 

        int rightW = 230;
        int leftW = panelW - rightW - 8;
        int rightX = panelX + leftW + 8;

        renderBeveledPanel(graphics, panelX, panelY, leftW, panelH, false, Theme.surface.getPacked(), 0);

        int headerH = 24;
        renderBeveledPanel(graphics, panelX + 4, panelY + 4, leftW - 8, headerH, true, Theme.surfaceAlt.getPacked(), 0);

        long totalActive = Features.get().all().stream().filter(Feature::isActive).count();

        if (searchQuery.isEmpty()) {
            
            graphics.fill(panelX + 4, panelY + 4, panelX + 8, panelY + 4 + headerH, Theme.accentRed.getPacked());
            graphics.text(font, "CATEGORIES", panelX + 14, panelY + 12, Theme.textAccent.getPacked(), false);

            String statsStr = Features.get().all().size() + " Tweaks | " + totalActive + " On";
            int statsW = font.width(statsStr);
            graphics.text(font, statsStr, panelX + leftW - statsW - 12, panelY + 12, Theme.textMuted.getPacked(), false);

            int contentStartY = panelY + headerH + 8;
            int availH = panelH - headerH - 14;

            int emptyBannerH = 0;
            if (totalActive == 0) {
                emptyBannerH = 34;
                renderBeveledPanel(graphics, panelX + 8, contentStartY, leftW - 16, emptyBannerH, true, Theme.surfaceAlt.getPacked(), 0);
                graphics.text(font, "Nothing enabled yet. Pick a tweak to get started.", panelX + 16, contentStartY + 8, Theme.textMuted.getPacked(), false);
                boolean btnHov = isHovered(mouseX, mouseY, panelX + leftW - 116, contentStartY + 7, 98, 20);
                renderSlotButton(graphics, panelX + leftW - 116, contentStartY + 7, 98, 20, "Browse Tweaks", btnHov, false, Theme.accentRed.getPacked());
                contentStartY += emptyBannerH + 6;
                availH -= (emptyBannerH + 6);
            }

            int quickTileH = 32;
            int quickTilesY = panelY + panelH - quickTileH - 6;
            int gridH = quickTilesY - contentStartY - 6;

            Group[] cats = Group.values();
            int cardW = (leftW - 22) / 2;
            int cardH = (gridH - 6) / 2;

            for (int i = 0; i < cats.length; i++) {
                Group cat = cats[i];
                int col = i % 2;
                int row = i / 2;
                int cx = panelX + 8 + col * (cardW + 6);
                int cy = contentStartY + row * (cardH + 6);

                boolean hovered = isHovered(mouseX, mouseY, cx, cy, cardW, cardH);
                int catColor = Theme.getCategoryColor(cat).getPacked();
                int fill = hovered ? Theme.surfaceRaised.getPacked() : Theme.surfaceAlt.getPacked();
                int focus = 0;

                renderBeveledPanel(graphics, cx, cy, cardW, cardH, false, fill, focus);
                graphics.fill(cx, cy, cx + 4, cy + cardH, catColor);

                int textLeft = cx + 34;
                graphics.text(font, cat.displayName, textLeft, cy + 6, hovered ? Theme.textAccent.getPacked() : catColor, false);

                if (cat.description != null && cardH >= 36) {
                    int descWidth = font.width(cat.description);
                    int availDescW = cardW - 40;
                    if (descWidth > availDescW && hovered) {
                        int scroll = (int) (((System.currentTimeMillis() - openTimestamp) / 20) % (descWidth + 20));
                        graphics.enableScissor(textLeft, cy + 18, textLeft + availDescW, cy + 18 + 10);
                        graphics.text(font, cat.description, textLeft - scroll, cy + 18, Theme.textMuted.getPacked(), false);
                        graphics.disableScissor();
                    } else if (descWidth > availDescW) {
                        graphics.text(font, font.plainSubstrByWidth(cat.description, availDescW - 8) + "...", textLeft, cy + 18, Theme.textMuted.getPacked(), false);
                    } else {
                        graphics.text(font, cat.description, textLeft, cy + 18, Theme.textMuted.getPacked(), false);
                    }
                }

                List<Feature> mods = Features.get().in(cat);
                long activeCount = mods.stream().filter(Feature::isActive).count();
                String footerStr = mods.size() + " tweaks, " + activeCount + " on";
                graphics.text(font, footerStr, textLeft, cy + cardH - 11, Theme.textDisabled.getPacked(), false);
            }

            int tileW = (leftW - 22) / 2;
            int t1X = panelX + 8;
            int t2X = t1X + tileW + 6;

            boolean t1Hov = isHovered(mouseX, mouseY, t1X, quickTilesY, tileW, quickTileH);
            renderBeveledPanel(graphics, t1X, quickTilesY, tileW, quickTileH, false, t1Hov ? Theme.surfaceRaised.getPacked() : Theme.surfaceAlt.getPacked(), t1Hov ? Theme.accentDiamond.getPacked() : 0);
            graphics.fill(t1X, quickTilesY, t1X + 3, quickTilesY + quickTileH, Theme.accentDiamond.getPacked());
            graphics.text(font, "HUD Editor", t1X + 28, quickTilesY + 6, t1Hov ? Theme.textAccent.getPacked() : Theme.accentDiamond.getPacked(), false);
            graphics.text(font, "Customize overlays & layout", t1X + 28, quickTilesY + 18, Theme.textDisabled.getPacked(), false);

            boolean t2Hov = isHovered(mouseX, mouseY, t2X, quickTilesY, tileW, quickTileH);
            renderBeveledPanel(graphics, t2X, quickTilesY, tileW, quickTileH, false, t2Hov ? Theme.surfaceRaised.getPacked() : Theme.surfaceAlt.getPacked(), 0);
            graphics.fill(t2X, quickTilesY, t2X + 3, quickTilesY + quickTileH, Theme.accentGold.getPacked());
            graphics.text(font, "Loadouts", t2X + 28, quickTilesY + 6, t2Hov ? Theme.textAccent.getPacked() : Theme.accentGold.getPacked(), false);
            graphics.text(font, "Switch profiles & configs", t2X + 28, quickTilesY + 18, Theme.textDisabled.getPacked(), false);

        } else {
            
            renderSearchBox(graphics, mouseX, mouseY, leftW, panelY + 4);

            graphics.fill(panelX + 4, panelY + 36, panelX + 8, panelY + 36 + headerH, Theme.accentDiamond.getPacked());
            graphics.text(font, "SEARCH RESULTS", panelX + 14, panelY + 44, Theme.textAccent.getPacked(), false);

            List<Feature> matchedModules = new ArrayList<>();
            for (Feature m : Features.get().all()) {
                if (m.name.toLowerCase().contains(searchQuery.toLowerCase())
                    || (m.description != null && m.description.toLowerCase().contains(searchQuery.toLowerCase()))
                    || m.id.toLowerCase().contains(searchQuery.toLowerCase())) {
                    matchedModules.add(m);
                }
            }

            List<ruinerpie.pancreas.hud.Part> matchedHud = new ArrayList<>();
            for (ruinerpie.pancreas.hud.Part el : ruinerpie.pancreas.hud.Rig.get().all()) {
                if (el.name.toLowerCase().contains(searchQuery.toLowerCase())
                    || (el.description != null && el.description.toLowerCase().contains(searchQuery.toLowerCase()))
                    || el.id.toLowerCase().contains(searchQuery.toLowerCase())) {
                    matchedHud.add(el);
                }
            }

            int totalFound = matchedModules.size() + matchedHud.size();
            int countW = font.width(totalFound + " found");
            graphics.text(font, totalFound + " found", panelX + leftW - countW - 12, panelY + 12, Theme.accentDiamond.getPacked(), false);

            int listY = panelY + headerH + 8;
            int listH = panelH - headerH - 14;
            int curY = listY - searchScrollOffset;

            if (totalFound == 0) {
                graphics.centeredText(font, "No results found matching '" + searchQuery + "'.", panelX + leftW / 2, listY + 30, Theme.textDisabled.getPacked());
            } else {
                int rowH = 22;
                int resultIdx = 0;

                for (Feature m : matchedModules) {
                    if (curY + rowH >= listY && curY <= listY + listH - rowH) {
                        boolean hovered = isHovered(mouseX, mouseY, panelX + 8, curY, leftW - 16, rowH);
                        boolean isSelected = resultIdx == selectedResultIndex;
                        int catColor = Theme.getCategoryColor(m.category).getPacked();

                        int fill = m.isActive() ? Theme.surfaceRaised.getPacked() : (hovered ? Theme.surfaceAlt.getPacked() : Theme.surface.getPacked());
                        int focus = isSelected ? Theme.focusRing.getPacked() : (hovered ? catColor : 0);

                        renderBeveledPanel(graphics, panelX + 8, curY, leftW - 16, rowH, m.isActive(), fill, focus);
                        graphics.fill(panelX + 8, curY, panelX + 11, curY + rowH, catColor);

                        drawHighlightedText(graphics, font, m.name, searchQuery, panelX + 16, curY + 7, m.isActive() ? Theme.textAccent.getPacked() : Theme.text.getPacked(), Theme.accentGold.getPacked());
                        graphics.text(font, "[" + m.category.displayName + "]", panelX + 140, curY + 7, catColor, false);

                        int toggleW = 48;
                        int toggleX = panelX + leftW - 64;
                        renderToggleSlot(graphics, toggleX, curY + 2, toggleW, rowH - 4, m.isActive(), hovered);
                    }
                    curY += rowH + 2;
                    resultIdx++;
                }

                for (ruinerpie.pancreas.hud.Part el : matchedHud) {
                    if (curY + rowH >= listY && curY <= listY + listH - rowH) {
                        boolean hovered = isHovered(mouseX, mouseY, panelX + 8, curY, leftW - 16, rowH);
                        boolean isSelected = resultIdx == selectedResultIndex;

                        int fill = el.isVisible() ? Theme.surfaceRaised.getPacked() : (hovered ? Theme.surfaceAlt.getPacked() : Theme.surface.getPacked());
                        int focus = isSelected ? Theme.focusRing.getPacked() : (hovered ? Theme.accentDiamond.getPacked() : 0);

                        renderBeveledPanel(graphics, panelX + 8, curY, leftW - 16, rowH, el.isVisible(), fill, focus);
                        graphics.fill(panelX + 8, curY, panelX + 11, curY + rowH, Theme.accentDiamond.getPacked());

                        drawHighlightedText(graphics, font, el.name, searchQuery, panelX + 16, curY + 7, el.isVisible() ? Theme.textAccent.getPacked() : Theme.text.getPacked(), Theme.accentGold.getPacked());
                        graphics.text(font, "[HUD Element]", panelX + 140, curY + 7, Theme.accentDiamond.getPacked(), false);

                        int toggleW = 48;
                        int toggleX = panelX + leftW - 64;
                        renderToggleSlot(graphics, toggleX, curY + 2, toggleW, rowH - 4, el.isVisible(), hovered);
                    }
                    curY += rowH + 2;
                    resultIdx++;
                }
            }
        }

        renderBeveledPanel(graphics, rightX, panelY, rightW, panelH, false, Theme.surface.getPacked(), 0);

        renderBeveledPanel(graphics, rightX + 4, panelY + 4, rightW - 8, headerH, true, Theme.surfaceAlt.getPacked(), 0);
        graphics.fill(rightX + 4, panelY + 4, rightX + 8, panelY + 4 + headerH, Theme.accentEmerald.getPacked());
        graphics.text(font, "ACTIVE TWEAKS", rightX + 14, panelY + 12, Theme.textMuted.getPacked(), false);

        List<Feature> activeModules = Features.get().all().stream()
            .filter(Feature::isActive)
            .sorted(Comparator.comparing(m -> m.name))
            .toList();

        List<ruinerpie.pancreas.hud.Part> activeHud = ruinerpie.pancreas.hud.Rig.get().all().stream()
            .filter(ruinerpie.pancreas.hud.Part::isVisible)
            .sorted(Comparator.comparing(el -> el.name))
            .toList();

        int aListY = panelY + headerH + 8;
        int aListH = panelH - headerH - 14;
        int curAY = aListY - activeScrollOffset;

        int totalActiveContentHeight = 0;
        if (!activeModules.isEmpty()) totalActiveContentHeight += 18 + activeModules.size() * 20;
        if (!activeHud.isEmpty()) totalActiveContentHeight += 18 + activeHud.size() * 20;
        totalActiveContentHeight += 18 + 4 * 20;

        if (activeModules.isEmpty() && activeHud.isEmpty()) {
            graphics.text(font, "Nothing enabled yet.", rightX + 14, aListY + 20, Theme.textDisabled.getPacked(), false);
            graphics.text(font, "Pick a tweak to start.", rightX + 14, aListY + 34, Theme.textDisabled.getPacked(), false);
            boolean bHov = isHovered(mouseX, mouseY, rightX + 14, aListY + 54, rightW - 28, 20);
            renderSlotButton(graphics, rightX + 14, aListY + 54, rightW - 28, 20, "Browse Tweaks", bHov, false, Theme.accentRed.getPacked());
        } else {
            
            if (!activeModules.isEmpty()) {
                if (curAY + 14 >= aListY && curAY <= aListY + aListH) {
                    graphics.text(font, "TWEAKS ON (" + activeModules.size() + ")", rightX + 12, curAY, Theme.textMuted.getPacked(), false);
                    graphics.fill(rightX + 12, curAY + 10, rightX + 110, curAY + 11, Theme.accentEmerald.getPacked());
                }
                curAY += 14;

                int rowH = 18;
                for (Feature m : activeModules) {
                    if (curAY + rowH >= aListY && curAY <= aListY + aListH - rowH) {
                        boolean hovered = isHovered(mouseX, mouseY, rightX + 6, curAY, rightW - 14, rowH);
                        int fill = hovered ? Theme.surfaceRaised.getPacked() : Theme.surfaceAlt.getPacked();
                        int catColor = Theme.getCategoryColor(m.category).getPacked();

                        renderBeveledPanel(graphics, rightX + 6, curAY, rightW - 14, rowH, false, fill, hovered ? catColor : 0);

                        graphics.fill(rightX + 11, curAY + 6, rightX + 16, curAY + 11, catColor);

                        String displayName = m.name;
                        if (font.width(displayName) > 105) {
                            displayName = font.plainSubstrByWidth(displayName, 95) + "...";
                        }
                        graphics.text(font, displayName, rightX + 20, curAY + 5, Theme.text.getPacked(), false);

                        int rightOffset = rightX + rightW - 20;
                        if (m.getKeybind() > 0) {
                            String kbd = "[" + m.getKeybindName() + "]";
                            int kW = font.width(kbd);
                            graphics.text(font, kbd, rightOffset - kW - 22, curAY + 5, Theme.accentLapis.getPacked(), false);
                        }
                        graphics.text(font, "ON", rightOffset - 14, curAY + 5, Theme.moduleOn.getPacked(), false);
                    }
                    curAY += rowH + 2;
                }
                curAY += 8;
            }

            if (!activeHud.isEmpty()) {
                if (curAY + 14 >= aListY && curAY <= aListY + aListH) {
                    graphics.text(font, "HUD ON (" + activeHud.size() + ")", rightX + 12, curAY, Theme.textMuted.getPacked(), false);
                    graphics.fill(rightX + 12, curAY + 10, rightX + 90, curAY + 11, Theme.accentDiamond.getPacked());
                }
                curAY += 14;

                int rowH = 18;
                for (ruinerpie.pancreas.hud.Part el : activeHud) {
                    if (curAY + rowH >= aListY && curAY <= aListY + aListH - rowH) {
                        boolean hovered = isHovered(mouseX, mouseY, rightX + 6, curAY, rightW - 14, rowH);
                        int fill = hovered ? Theme.surfaceRaised.getPacked() : Theme.surfaceAlt.getPacked();

                        renderBeveledPanel(graphics, rightX + 6, curAY, rightW - 14, rowH, false, fill, hovered ? Theme.accentDiamond.getPacked() : 0);

                        String displayName = el.name;
                        if (font.width(displayName) > 120) {
                            displayName = font.plainSubstrByWidth(displayName, 110) + "...";
                        }
                        graphics.text(font, displayName, rightX + 20, curAY + 5, Theme.text.getPacked(), false);
                        graphics.text(font, "ON", rightX + rightW - 34, curAY + 5, Theme.moduleOn.getPacked(), false);
                    }
                    curAY += rowH + 2;
                }
                curAY += 8;
            }
        }

        if (curAY + 14 >= aListY && curAY <= aListY + aListH) {
            graphics.text(font, "SYSTEMS", rightX + 12, curAY, Theme.textMuted.getPacked(), false);
            graphics.fill(rightX + 12, curAY + 10, rightX + 70, curAY + 11, Theme.accentPurple.getPacked());
        }
        curAY += 14;

        int rowH = 18;
        String crewStat = ruinerpie.pancreas.saved.Team.get().all().isEmpty() ? "OFF" : "ON (" + ruinerpie.pancreas.saved.Team.get().all().size() + ")";
        String shadowStat = ruinerpie.pancreas.stalk.Stalk.get().isActive() ? "ON" : "OFF";
        String loadoutStat = ruinerpie.pancreas.saved.Loadouts.get().all().size() + " saved";
        long boundCount = ruinerpie.pancreas.saved.Macros.get().all().stream().filter(a -> a.keybind > 0).count();
        String qaStat = boundCount + " bound";

        String[][] sysData = {
            {"Team", crewStat, String.valueOf(Theme.accentEmerald.getPacked())},
            {"Stalk", shadowStat, String.valueOf(Theme.accentPurple.getPacked())},
            {"Loadouts", loadoutStat, String.valueOf(Theme.accentGold.getPacked())},
            {"Macros", qaStat, String.valueOf(Theme.accentDiamond.getPacked())}
        };

        for (String[] sys : sysData) {
            if (curAY + rowH >= aListY && curAY <= aListY + aListH - rowH) {
                boolean hovered = isHovered(mouseX, mouseY, rightX + 6, curAY, rightW - 14, rowH);
                int fill = hovered ? Theme.surfaceRaised.getPacked() : Theme.surfaceAlt.getPacked();
                int accent = Integer.parseInt(sys[2]);

                renderBeveledPanel(graphics, rightX + 6, curAY, rightW - 14, rowH, false, fill, hovered ? accent : 0);
                graphics.fill(rightX + 11, curAY + 6, rightX + 16, curAY + 11, accent);
                graphics.text(font, sys[0], rightX + 20, curAY + 5, Theme.text.getPacked(), false);

                int statW = font.width(sys[1]);
                graphics.text(font, sys[1], rightX + rightW - statW - 16, curAY + 5, accent, false);
            }
            curAY += rowH + 2;
        }

        if (totalActiveContentHeight > aListH) {
            int trackX = rightX + rightW - 5;
            int trackY = aListY;
            int trackH = aListH;
            graphics.fill(trackX, trackY, trackX + 3, trackY + trackH, Theme.border.getPacked());

            float viewRatio = (float) aListH / totalActiveContentHeight;
            int thumbH = Math.max(16, (int) (trackH * viewRatio));
            float maxScroll = totalActiveContentHeight - aListH;
            int thumbY = trackY + (int) ((trackH - thumbH) * Math.min(1.0f, (float) activeScrollOffset / maxScroll));
            graphics.fill(trackX, thumbY, trackX + 3, thumbY + thumbH, Theme.accentLapis.getPacked());
        }

        renderStatusBar(graphics);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean isFocused) {
        if (event.button() == 0) {
            double mouseX = event.x();
            double mouseY = event.y();

            if (handleTabClicks(mouseX, mouseY)) {
                return true;
            }

            int panelW = Math.min(width - 32, 740);
            if (handleSearchClick(mouseX, mouseY, panelW)) {
                return true;
            }

            int panelX = (width - panelW) / 2;
            int panelY = 60;
            int panelH = height - 76;
            int headerH = 24;

        int rightW = 230;
            int leftW = panelW - rightW - 8;
            int rightX = panelX + leftW + 8;

            long totalActive = Features.get().all().stream().filter(Feature::isActive).count();

            if (searchQuery.isEmpty()) {
                int contentStartY = panelY + headerH + 8;
                int availH = panelH - headerH - 14;

                if (totalActive == 0) {
                    int emptyBannerH = 34;
                    if (isHovered(mouseX, mouseY, panelX + leftW - 116, contentStartY + 7, 98, 20)) {
                        playUiSound();
                        if (minecraft != null) minecraft.setScreen(new TweakIndex(Group.Cheats, this));
                        return true;
                    }
                    contentStartY += emptyBannerH + 6;
                    availH -= (emptyBannerH + 6);
                }

                int quickTileH = 32;
                int quickTilesY = panelY + panelH - quickTileH - 6;
                int gridH = quickTilesY - contentStartY - 6;
                Group[] cats = Group.values();
                int cardW = (leftW - 22) / 2;
                int cardH = (gridH - 6) / 2;

                for (int i = 0; i < cats.length; i++) {
                    int col = i % 2;
                    int row = i / 2;
                    int cx = panelX + 8 + col * (cardW + 6);
                    int cy = contentStartY + row * (cardH + 6);

                    if (isHovered(mouseX, mouseY, cx, cy, cardW, cardH)) {
                        playUiSound();
                        if (minecraft != null) {
                            minecraft.setScreen(new TweakIndex(cats[i], this));
                        }
                        return true;
                    }
                }

                int tileW = (leftW - 22) / 2;
                int t1X = panelX + 8;
                int t2X = t1X + tileW + 6;

                if (isHovered(mouseX, mouseY, t1X, quickTilesY, tileW, quickTileH)) {
                    playUiSound();
                    if (minecraft != null) minecraft.setScreen(new ruinerpie.pancreas.hud.Studio(this));
                    return true;
                }

                if (isHovered(mouseX, mouseY, t2X, quickTilesY, tileW, quickTileH)) {
                    playUiSound();
                    if (minecraft != null) minecraft.setScreen(new ruinerpie.pancreas.screens.LoadoutScreen(this));
                    return true;
                }

            } else {
                
                List<Feature> matchedModules = new ArrayList<>();
                for (Feature m : Features.get().all()) {
                    if (m.name.toLowerCase().contains(searchQuery.toLowerCase())
                        || (m.description != null && m.description.toLowerCase().contains(searchQuery.toLowerCase()))
                        || m.id.toLowerCase().contains(searchQuery.toLowerCase())) {
                        matchedModules.add(m);
                    }
                }

                List<ruinerpie.pancreas.hud.Part> matchedHud = new ArrayList<>();
                for (ruinerpie.pancreas.hud.Part el : ruinerpie.pancreas.hud.Rig.get().all()) {
                    if (el.name.toLowerCase().contains(searchQuery.toLowerCase())
                        || (el.description != null && el.description.toLowerCase().contains(searchQuery.toLowerCase()))
                        || el.id.toLowerCase().contains(searchQuery.toLowerCase())) {
                        matchedHud.add(el);
                    }
                }

                int listY = panelY + headerH + 8;
                int listH = panelH - headerH - 14;
                int curY = listY - searchScrollOffset;
                int rowH = 22;

                for (Feature m : matchedModules) {
                    if (curY + rowH >= listY && curY <= listY + listH - rowH) {
                        if (isHovered(mouseX, mouseY, panelX + 8, curY, leftW - 16, rowH)) {
                            int toggleW = 48;
                            int toggleX = panelX + leftW - 64;

                            playUiSound();
                            if (isHovered(mouseX, mouseY, toggleX, curY + 2, toggleW, rowH - 4)) {
                                m.toggle();
                            } else {
                                if (minecraft != null) minecraft.setScreen(new TweakSettings(m, this));
                            }
                            return true;
                        }
                    }
                    curY += rowH + 2;
                }

                for (ruinerpie.pancreas.hud.Part el : matchedHud) {
                    if (curY + rowH >= listY && curY <= listY + listH - rowH) {
                        if (isHovered(mouseX, mouseY, panelX + 8, curY, leftW - 16, rowH)) {
                            int toggleW = 48;
                            int toggleX = panelX + leftW - 64;

                            playUiSound();
                            if (isHovered(mouseX, mouseY, toggleX, curY + 2, toggleW, rowH - 4)) {
                                el.toggle();
                            } else {
                                if (minecraft != null) minecraft.setScreen(new ruinerpie.pancreas.hud.Studio(this, el));
                            }
                            return true;
                        }
                    }
                    curY += rowH + 2;
                }
            }

            int aListY = panelY + headerH + 8;
            int aListH = panelH - headerH - 14;
            int curAY = aListY - activeScrollOffset;

            List<Feature> activeModules = Features.get().all().stream().filter(Feature::isActive).toList();
            List<ruinerpie.pancreas.hud.Part> activeHud = ruinerpie.pancreas.hud.Rig.get().all().stream().filter(ruinerpie.pancreas.hud.Part::isVisible).toList();

            if (activeModules.isEmpty() && activeHud.isEmpty()) {
                if (isHovered(mouseX, mouseY, rightX + 14, aListY + 54, rightW - 28, 20)) {
                    playUiSound();
                    if (minecraft != null) minecraft.setScreen(new TweakIndex(Group.Cheats, this));
                    return true;
                }
            } else {
                if (!activeModules.isEmpty()) {
                    curAY += 14;
                    int rowH = 18;
                    for (Feature m : activeModules) {
                        if (curAY + rowH >= aListY && curAY <= aListY + aListH - rowH) {
                            if (isHovered(mouseX, mouseY, rightX + 6, curAY, rightW - 14, rowH)) {
                                playUiSound();
                                if (minecraft != null) minecraft.setScreen(new TweakSettings(m, this));
                                return true;
                            }
                        }
                        curAY += rowH + 2;
                    }
                    curAY += 8;
                }

                if (!activeHud.isEmpty()) {
                    curAY += 14;
                    int rowH = 18;
                    for (ruinerpie.pancreas.hud.Part el : activeHud) {
                        if (curAY + rowH >= aListY && curAY <= aListY + aListH - rowH) {
                            if (isHovered(mouseX, mouseY, rightX + 6, curAY, rightW - 14, rowH)) {
                                playUiSound();
                                if (minecraft != null) minecraft.setScreen(new ruinerpie.pancreas.hud.Studio(this, el));
                                return true;
                            }
                        }
                        curAY += rowH + 2;
                    }
                    curAY += 8;
                }
            }

            curAY += 14;
            int rowH = 18;
            String[] sysNames = {"Team", "Stalk", "Loadouts", "Macros"};
            for (String sys : sysNames) {
                if (curAY + rowH >= aListY && curAY <= aListY + aListH - rowH) {
                    if (isHovered(mouseX, mouseY, rightX + 6, curAY, rightW - 14, rowH)) {
                        playUiSound();
                        if (minecraft != null) {
                            switch (sys) {
                                case "Team" -> minecraft.setScreen(new TeamScreen(this));
                                case "Stalk" -> minecraft.setScreen(new StalkScreen(this));
                                case "Loadouts" -> minecraft.setScreen(new LoadoutScreen(this));
                                case "Macros" -> minecraft.setScreen(new MacroScreen(this));
                            }
                        }
                        return true;
                    }
                }
                curAY += rowH + 2;
            }
        }
        return super.mouseClicked(event, isFocused);
    }

    @Override
    protected void onSearchEnterPressed() {
        if (!searchQuery.isEmpty()) {
            for (Feature m : Features.get().all()) {
                if (m.name.toLowerCase().contains(searchQuery.toLowerCase())
                    || (m.description != null && m.description.toLowerCase().contains(searchQuery.toLowerCase()))
                    || m.id.toLowerCase().contains(searchQuery.toLowerCase())) {
                    if (minecraft != null) minecraft.setScreen(new TweakSettings(m, this));
                    return;
                }
            }
            for (ruinerpie.pancreas.hud.Part el : ruinerpie.pancreas.hud.Rig.get().all()) {
                if (el.name.toLowerCase().contains(searchQuery.toLowerCase())
                    || (el.description != null && el.description.toLowerCase().contains(searchQuery.toLowerCase()))
                    || el.id.toLowerCase().contains(searchQuery.toLowerCase())) {
                    if (minecraft != null) minecraft.setScreen(new ruinerpie.pancreas.hud.Studio(this, el));
                    return;
                }
            }
            
            triggerSearchShake();
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        int panelW = Math.min(width - 32, 740);
        int panelX = (width - panelW) / 2;
        int rightW = 230;
        int leftW = panelW - rightW - 8;

        if (mouseX > panelX + leftW) {
            activeScrollOffset = Math.max(0, activeScrollOffset - (int) (verticalAmount * 18));
        } else {
            searchScrollOffset = Math.max(0, searchScrollOffset - (int) (verticalAmount * 22));
        }
        return true;
    }
}