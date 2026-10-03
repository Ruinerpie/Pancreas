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

public class SystemHub extends Panel {

    public SystemHub(Screen parent) {
        super(Component.literal("Pancreas — Systems"), parent, NavTab.SYSTEMS);
    }

    public SystemHub() {
        this(null);
    }

    @Override
    protected String getSearchPlaceholder() {
        return "Search systems (Team, Stalk, Loadouts, Macros)...";
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        renderBackground(graphics);
        renderTabs(graphics, mouseX, mouseY);

        int panelW = Math.min(width - 32, 680);
        int panelX = (width - panelW) / 2;

        int panelY = 40;
        int panelH = height - 48;

        renderBeveledPanel(graphics, panelX, panelY, panelW, panelH, false, Theme.surface.getPacked(), 0);

        renderSearchBox(graphics, mouseX, mouseY, panelW - 16, panelY + 8);

        int headerH = 26;
        renderBeveledPanel(graphics, panelX + 4, panelY + 36, panelW - 8, headerH, true, Theme.surfaceAlt.getPacked(), 0);
        graphics.fill(panelX + 4, panelY + 36, panelX + 8, panelY + 36 + headerH, Theme.accentPurple.getPacked());
        graphics.text(font, "SYSTEMS", panelX + 14, panelY + 45, Theme.textAccent.getPacked(), false);
        graphics.text(font, "Peer client subsystems — standalone tools and automation", panelX + 80, panelY + 45, Theme.textMuted.getPacked(), false);

        int cardY = panelY + 36 + headerH + 12;
        int cardH = (panelH - 36 - headerH - 48) / 4;
        String q = searchQuery.toLowerCase();

        if (q.isEmpty() || "crew friends allies player combat".contains(q)) {
            boolean hov = isHovered(mouseX, mouseY, panelX + 8, cardY, panelW - 16, cardH);
            int fill = hov ? Theme.surfaceRaised.getPacked() : Theme.surfaceAlt.getPacked();
            int focus = hov ? Theme.accentEmerald.getPacked() : 0;
            renderBeveledPanel(graphics, panelX + 8, cardY, panelW - 16, cardH, false, fill, focus);
            graphics.fill(panelX + 8, cardY, panelX + 12, cardY + cardH, Theme.accentEmerald.getPacked());

            graphics.text(font, "Team", panelX + 20, cardY + 8, Theme.accentEmerald.getPacked(), false);
            graphics.text(font, "Allies and friendly-fire immunity list (" + Team.get().all().size() + " members)", panelX + 20, cardY + 22, Theme.textMuted.getPacked(), false);
            String stat = Team.get().all().size() + " members  >";
            int statW = font.width(stat);
            graphics.text(font, stat, panelX + panelW - statW - 28, cardY + (cardH - 8) / 2, hov ? Theme.textAccent.getPacked() : Theme.textDisabled.getPacked(), false);
        }
        cardY += cardH + 12;

        if (q.isEmpty() || "shadow follow target tracking tracking entity".contains(q)) {
            boolean hov = isHovered(mouseX, mouseY, panelX + 8, cardY, panelW - 16, cardH);
            int fill = hov ? Theme.surfaceRaised.getPacked() : Theme.surfaceAlt.getPacked();
            int focus = hov ? Theme.accentPurple.getPacked() : 0;
            renderBeveledPanel(graphics, panelX + 8, cardY, panelW - 16, cardH, false, fill, focus);
            graphics.fill(panelX + 8, cardY, panelX + 12, cardY + cardH, Theme.accentPurple.getPacked());

            graphics.text(font, "Stalk", panelX + 20, cardY + 8, Theme.accentPurple.getPacked(), false);
            String status = Stalk.get().isActive() ? "Following " + Stalk.get().current().name : "Idle (no target)";
            graphics.text(font, "Entity and player follow state provider (" + status + ")", panelX + 20, cardY + 22, Theme.textMuted.getPacked(), false);
            String stat = (Stalk.get().isActive() ? "TRACKING" : "IDLE") + "  >";
            int statW = font.width(stat);
            graphics.text(font, stat, panelX + panelW - statW - 28, cardY + (cardH - 8) / 2, hov ? Theme.textAccent.getPacked() : Theme.textDisabled.getPacked(), false);
        }
        cardY += cardH + 12;

        if (q.isEmpty() || "loadouts profiles presets configs save".contains(q)) {
            boolean hov = isHovered(mouseX, mouseY, panelX + 8, cardY, panelW - 16, cardH);
            int fill = hov ? Theme.surfaceRaised.getPacked() : Theme.surfaceAlt.getPacked();
            int focus = hov ? Theme.accentGold.getPacked() : 0;
            renderBeveledPanel(graphics, panelX + 8, cardY, panelW - 16, cardH, false, fill, focus);
            graphics.fill(panelX + 8, cardY, panelX + 12, cardY + cardH, Theme.accentGold.getPacked());

            graphics.text(font, "Loadouts", panelX + 20, cardY + 8, Theme.accentGold.getPacked(), false);
            graphics.text(font, "Saved profiles of tweaks, settings, and keybinds (" + Loadouts.get().all().size() + " saved)", panelX + 20, cardY + 22, Theme.textMuted.getPacked(), false);
            String stat = Loadouts.get().all().size() + " loadouts  >";
            int statW = font.width(stat);
            graphics.text(font, stat, panelX + panelW - statW - 28, cardY + (cardH - 8) / 2, hov ? Theme.textAccent.getPacked() : Theme.textDisabled.getPacked(), false);
        }
        cardY += cardH + 12;

        if (q.isEmpty() || "quickactions macros actions bind keys chain".contains(q)) {
            boolean hov = isHovered(mouseX, mouseY, panelX + 8, cardY, panelW - 16, cardH);
            int fill = hov ? Theme.surfaceRaised.getPacked() : Theme.surfaceAlt.getPacked();
            int focus = hov ? Theme.accentDiamond.getPacked() : 0;
            renderBeveledPanel(graphics, panelX + 8, cardY, panelW - 16, cardH, false, fill, focus);
            graphics.fill(panelX + 8, cardY, panelX + 12, cardY + cardH, Theme.accentDiamond.getPacked());

            long boundCount = Macros.get().all().stream().filter(a -> a.keybind > 0).count();
            graphics.text(font, "Macros", panelX + 20, cardY + 8, Theme.accentDiamond.getPacked(), false);
            graphics.text(font, "Multi-step macro sequences and command chains (" + boundCount + " bound)", panelX + 20, cardY + 22, Theme.textMuted.getPacked(), false);
            String stat = Macros.get().all().size() + " actions  >";
            int statW = font.width(stat);
            graphics.text(font, stat, panelX + panelW - statW - 28, cardY + (cardH - 8) / 2, hov ? Theme.textAccent.getPacked() : Theme.textDisabled.getPacked(), false);
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
            String q = searchQuery.toLowerCase();

            if (q.isEmpty() || "crew friends allies player combat".contains(q)) {
                if (isHovered(mouseX, mouseY, panelX + 8, cardY, panelW - 16, cardH)) {
                    playUiSound();
                    if (minecraft != null) minecraft.setScreen(new TeamScreen(this));
                    return true;
                }
            }
            cardY += cardH + 4;

            if (q.isEmpty() || "shadow follow target tracking tracking entity".contains(q)) {
                if (isHovered(mouseX, mouseY, panelX + 8, cardY, panelW - 16, cardH)) {
                    playUiSound();
                    if (minecraft != null) minecraft.setScreen(new StalkScreen(this));
                    return true;
                }
            }
            cardY += cardH + 4;

            if (q.isEmpty() || "loadouts profiles presets configs save".contains(q)) {
                if (isHovered(mouseX, mouseY, panelX + 8, cardY, panelW - 16, cardH)) {
                    playUiSound();
                    if (minecraft != null) minecraft.setScreen(new LoadoutScreen(this));
                    return true;
                }
            }
            cardY += cardH + 4;

            if (q.isEmpty() || "quickactions macros actions bind keys chain".contains(q)) {
                if (isHovered(mouseX, mouseY, panelX + 8, cardY, panelW - 16, cardH)) {
                    playUiSound();
                    if (minecraft != null) minecraft.setScreen(new MacroScreen(this));
                    return true;
                }
            }
        }
        return super.mouseClicked(event, isFocused);
    }
}