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

public class GlobalPrefs extends Panel {
    private String statusMessage = "";
    private long statusTimestamp = 0;

    public GlobalPrefs(Screen parent) {
        super(Component.literal("Pancreas — Values"), parent, NavTab.SETTINGS);
    }

    @Override
    protected String getSearchPlaceholder() {
        return "Search global settings...";
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
        graphics.fill(panelX + 4, panelY + 4, panelX + 8, panelY + 4 + headerH, Theme.accentRed.getPacked());
        graphics.text(font, "SETTINGS", panelX + 14, panelY + 13, Theme.textAccent.getPacked(), false);
        graphics.text(font, "Global client configuration and persistence", panelX + 80, panelY + 13, Theme.textMuted.getPacked(), false);

        int curY = panelY + headerH + 12;
        String q = searchQuery.toLowerCase();

        boolean showConfig = q.isEmpty() || "config engine".contains(q) || "schema".contains(q) || "storage".contains(q) || "save".contains(q) || "reload".contains(q);
        boolean showProfile = q.isEmpty() || "active profile".contains(q) || "loadouts".contains(q) || "default".contains(q);

        if (!showConfig && !showProfile) {
            graphics.centeredText(font, "No configuration options found matching '" + searchQuery + "'.", panelX + panelW / 2, curY + 30, Theme.textDisabled.getPacked());
        }

        if (showConfig) {
            int secH1 = 70;
            renderBeveledPanel(graphics, panelX + 8, curY, panelW - 16, secH1, true, Theme.surfaceAlt.getPacked(), 0);
            graphics.fill(panelX + 8, curY, panelX + 12, curY + secH1, Theme.accentPurple.getPacked());

            graphics.text(font, "Config Engine", panelX + 18, curY + 10, Theme.textAccent.getPacked(), false);
            graphics.text(font, "Schema Version: v" + Vault.CONFIG_VERSION + "  |  Storage: config/pancreas/", panelX + 18, curY + 24, Theme.textMuted.getPacked(), false);

            if (!statusMessage.isEmpty() && System.currentTimeMillis() - statusTimestamp < 3000) {
                graphics.text(font, statusMessage, panelX + 18, curY + 42, Theme.success.getPacked(), false);
            }

            int btnW = 90;
            int btnH = 22;
            int btnY = curY + 12;

            int saveX = panelX + panelW - btnW * 2 - 24;
            boolean saveHover = isHovered(mouseX, mouseY, saveX, btnY, btnW, btnH);
            renderSlotButton(graphics, saveX, btnY, btnW, btnH, "SAVE CONFIG", saveHover, false, Theme.accentPurple.getPacked());

            int reloadX = panelX + panelW - btnW - 16;
            boolean reloadHover = isHovered(mouseX, mouseY, reloadX, btnY, btnW, btnH);
            renderSlotButton(graphics, reloadX, btnY, btnW, btnH, "RELOAD CONFIG", reloadHover, false, Theme.focusRing.getPacked());

            curY += secH1 + 10;
        }

        if (showProfile) {
            int secH2 = 56;
            renderBeveledPanel(graphics, panelX + 8, curY, panelW - 16, secH2, true, Theme.surfaceAlt.getPacked(), 0);
            graphics.fill(panelX + 8, curY, panelX + 12, curY + secH2, Theme.accentMinecraft.getPacked());

            graphics.text(font, "Active Profile", panelX + 18, curY + 10, Theme.textAccent.getPacked(), false);
            graphics.text(font, "Default (Loadouts profile manager)", panelX + 18, curY + 24, Theme.textMuted.getPacked(), false);
            graphics.text(font, "Status: Synchronized with filesystem", panelX + 18, curY + 38, Theme.textDisabled.getPacked(), false);
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
            int headerH = 26;
            int curY = panelY + headerH + 12;

            int btnW = 90;
            int btnH = 22;
            int btnY = curY + 12;

            int saveX = panelX + panelW - btnW * 2 - 24;
            if (isHovered(mouseX, mouseY, saveX, btnY, btnW, btnH)) {
                playUiSound();
                Vault.get().save();
                statusMessage = "Configuration saved successfully.";
                statusTimestamp = System.currentTimeMillis();
                return true;
            }

            int reloadX = panelX + panelW - btnW - 16;
            if (isHovered(mouseX, mouseY, reloadX, btnY, btnW, btnH)) {
                playUiSound();
                Vault.get().load();
                statusMessage = "Configuration reloaded from disk.";
                statusTimestamp = System.currentTimeMillis();
                return true;
            }
        }
        return super.mouseClicked(event, isFocused);
    }
}