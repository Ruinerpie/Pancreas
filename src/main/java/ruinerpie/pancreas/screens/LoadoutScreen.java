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
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LoadoutScreen extends Panel {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private String saveInput = "";
    private boolean saveInputFocused = false;
    private int scrollOffset = 0;
    private String statusMsg = "";
    private long statusTime = 0;

    public LoadoutScreen(Screen parent) {
        super(Component.literal("Pancreas — Loadouts"), parent, NavTab.SYSTEMS);
    }

    public LoadoutScreen() {
        this(null);
    }

    @Override
    protected String getSearchPlaceholder() {
        return "Search loadouts...";
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
        graphics.fill(panelX + 4, panelY + 4, panelX + 8, panelY + 4 + headerH, Theme.accentGold.getPacked());

        boolean backHov = isHovered(mouseX, mouseY, panelX + 12, panelY + 6, 50, 22);
        renderSlotButton(graphics, panelX + 12, panelY + 6, 50, 22, "< Back", backHov, false, Theme.accentGold.getPacked());

        graphics.text(font, "LOADOUTS", panelX + 70, panelY + 13, Theme.textAccent.getPacked(), false);
        graphics.text(font, "Save and load custom tweak sets and setting profiles (" + Loadouts.get().all().size() + ")", panelX + 135, panelY + 13, Theme.textMuted.getPacked(), false);

        if (!statusMsg.isEmpty() && System.currentTimeMillis() - statusTime < 3000) {
            int msgW = font.width(statusMsg);
            graphics.text(font, statusMsg, panelX + panelW - msgW - 14, panelY + 13, Theme.success.getPacked(), false);
        }

        int botY = panelY + panelH - 34;
        renderBeveledPanel(graphics, panelX + 6, botY, panelW - 12, 28, true, Theme.surfaceAlt.getPacked(), 0);

        graphics.text(font, "Save Current As:", panelX + 14, botY + 10, Theme.textAccent.getPacked(), false);
        int inputX = panelX + 110;
        int inputW = panelW - 250;
        int inFocus = saveInputFocused ? Theme.focusRing.getPacked() : 0;
        renderBeveledPanel(graphics, inputX, botY + 4, inputW, 20, true, Theme.surface.getPacked(), inFocus);

        if (saveInput.isEmpty()) {
            graphics.text(font, "Enter new loadout name...", inputX + 6, botY + 10, Theme.textDisabled.getPacked(), false);
        } else {
            String cursor = saveInputFocused && (System.currentTimeMillis() / 450 % 2 == 0) ? "_" : "";
            graphics.text(font, saveInput + cursor, inputX + 6, botY + 10, Theme.textAccent.getPacked(), false);
        }

        int saveBtnW = 110;
        int saveBtnX = inputX + inputW + 8;
        boolean sHov = isHovered(mouseX, mouseY, saveBtnX, botY + 4, saveBtnW, 20);
        renderSlotButton(graphics, saveBtnX, botY + 4, saveBtnW, 20, "SAVE CURRENT", sHov, false, Theme.accentGold.getPacked());

        int listY = panelY + headerH + 8;
        int listH = botY - listY - 6;
        int startY = listY - scrollOffset;

        List<Loadout> allLoadouts = Loadouts.get().all();
        List<Loadout> filtered = new ArrayList<>();
        for (Loadout l : allLoadouts) {
            if (searchQuery.isEmpty() || l.name.toLowerCase().contains(searchQuery.toLowerCase())) {
                filtered.add(l);
            }
        }

        if (filtered.isEmpty()) {
            String emptyMsg = allLoadouts.isEmpty() ? "No loadouts saved yet. Save current tweaks and settings below." : "No loadouts matching '" + searchQuery + "'.";
            graphics.centeredText(font, emptyMsg, panelX + panelW / 2, listY + 30, Theme.textDisabled.getPacked());
        } else {
            int curY = startY;
            int rowH = 24;

            for (Loadout l : filtered) {
                if (curY + rowH >= listY && curY <= listY + listH - rowH) {
                    boolean hovered = isHovered(mouseX, mouseY, panelX + 8, curY, panelW - 16, rowH);
                    int fill = hovered ? Theme.surfaceRaised.getPacked() : Theme.surfaceAlt.getPacked();

                    renderBeveledPanel(graphics, panelX + 8, curY, panelW - 16, rowH, false, fill, hovered ? Theme.accentGold.getPacked() : 0);

                    graphics.fill(panelX + 14, curY + 7, panelX + 20, curY + 13, Theme.accentGold.getPacked());
                    graphics.text(font, l.name, panelX + 26, curY + 8, hovered ? Theme.textAccent.getPacked() : Theme.text.getPacked(), false);

                    long activeCount = l.tweakStates != null ? l.tweakStates.values().stream().filter(b -> b).count() : 0;
                    String info = activeCount + " tweaks on  |  Saved: " + DATE_FORMAT.format(new Date(l.savedAt));
                    graphics.text(font, info, panelX + 160, curY + 8, Theme.textDisabled.getPacked(), false);

                    int delW = 54;
                    int delX = panelX + panelW - delW - 14;
                    boolean delHov = isHovered(mouseX, mouseY, delX, curY + 3, delW, 18);
                    renderSlotButton(graphics, delX, curY + 3, delW, 18, "DELETE", delHov, false, Theme.accentRed.getPacked());

                    int loadW = 50;
                    int loadX = delX - loadW - 6;
                    boolean loadHov = isHovered(mouseX, mouseY, loadX, curY + 3, loadW, 18);
                    renderSlotButton(graphics, loadX, curY + 3, loadW, 18, "LOAD", loadHov, false, Theme.success.getPacked());
                }
                curY += rowH + 3;
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
                saveInputFocused = false;
                return true;
            }

            int panelX = (width - panelW) / 2;
            int panelY = 60;
            int panelH = height - 68;

            if (isHovered(mouseX, mouseY, panelX + 12, panelY + 6, 50, 22)) {
                playUiSound();
                if (minecraft != null) {
                    minecraft.setScreen(parent != null ? parent : new SystemHub());
                }
                return true;
            }

            int botY = panelY + panelH - 34;
            int inputX = panelX + 110;
            int inputW = panelW - 250;
            if (isHovered(mouseX, mouseY, inputX, botY + 4, inputW, 20)) {
                playUiSound();
                saveInputFocused = true;
                searchFocused = false;
                return true;
            } else {
                saveInputFocused = false;
            }

            int saveBtnW = 110;
            int saveBtnX = inputX + inputW + 8;
            if (isHovered(mouseX, mouseY, saveBtnX, botY + 4, saveBtnW, 20)) {
                submitSave();
                return true;
            }

            int headerH = 26;
            int listY = panelY + headerH + 8;
            int startY = listY - scrollOffset;

            List<Loadout> allLoadouts = Loadouts.get().all();
            List<Loadout> filtered = new ArrayList<>();
            for (Loadout l : allLoadouts) {
                if (searchQuery.isEmpty() || l.name.toLowerCase().contains(searchQuery.toLowerCase())) {
                    filtered.add(l);
                }
            }

            int curY = startY;
            int rowH = 24;

            for (Loadout l : filtered) {
                if (curY + rowH >= listY && curY <= botY - 6 - rowH) {
                    int delW = 54;
                    int delX = panelX + panelW - delW - 14;
                    int loadW = 50;
                    int loadX = delX - loadW - 6;

                    if (isHovered(mouseX, mouseY, loadX, curY + 3, loadW, 18)) {
                        playUiSound();
                        Loadouts.get().load(l.name);
                        statusMsg = "Loaded profile '" + l.name + "'.";
                        statusTime = System.currentTimeMillis();
                        return true;
                    }

                    if (isHovered(mouseX, mouseY, delX, curY + 3, delW, 18)) {
                        playUiSound();
                        Loadouts.get().delete(l.name);
                        statusMsg = "Deleted profile '" + l.name + "'.";
                        statusTime = System.currentTimeMillis();
                        return true;
                    }
                }
                curY += rowH + 3;
            }
        }
        return super.mouseClicked(event, isFocused);
    }

    private void submitSave() {
        if (!saveInput.trim().isEmpty()) {
            playUiSound();
            String name = saveInput.trim();
            Loadouts.get().save(name);
            statusMsg = "Saved loadout '" + name + "'.";
            statusTime = System.currentTimeMillis();
            saveInput = "";
            saveInputFocused = false;
        }
    }

    @Override
    public boolean charTyped(CharacterEvent event) {
        if (saveInputFocused && event.isAllowedChatCharacter()) {
            saveInput += event.codepointAsString();
            return true;
        }
        return super.charTyped(event);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        int key = event.key();

        if (saveInputFocused) {
            if (key == GLFW.GLFW_KEY_BACKSPACE) {
                if (!saveInput.isEmpty()) {
                    saveInput = saveInput.substring(0, saveInput.length() - 1);
                }
                return true;
            }
            if (key == GLFW.GLFW_KEY_ENTER || key == GLFW.GLFW_KEY_KP_ENTER) {
                submitSave();
                return true;
            }
            if (key == GLFW.GLFW_KEY_ESCAPE) {
                saveInputFocused = false;
                return true;
            }
        }

        return super.keyPressed(event);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        scrollOffset = Math.max(0, scrollOffset - (int) (verticalAmount * 24));
        return true;
    }
}