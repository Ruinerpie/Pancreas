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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TeamScreen extends Panel {
    private boolean sortAlphabetical = true;
    private int scrollOffset = 0;
    private String addInputText = "";
    private boolean addInputFocused = false;

    public TeamScreen(Screen parent) {
        super(Component.literal("Pancreas — Team"), parent, NavTab.SYSTEMS);
    }

    public TeamScreen() {
        this(null);
    }

    @Override
    protected String getSearchPlaceholder() {
        return "Search crew...";
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
        graphics.fill(panelX + 4, panelY + 4, panelX + 8, panelY + 4 + headerH, Theme.accentEmerald.getPacked());

        boolean backHov = isHovered(mouseX, mouseY, panelX + 12, panelY + 6, 50, 22);
        renderSlotButton(graphics, panelX + 12, panelY + 6, 50, 22, "< Back", backHov, false, Theme.accentEmerald.getPacked());

        graphics.text(font, "CREW", panelX + 70, panelY + 13, Theme.textAccent.getPacked(), false);
        graphics.text(font, "Known allies and combat-immune players (" + Team.get().all().size() + ")", panelX + 115, panelY + 13, Theme.textMuted.getPacked(), false);

        int sortW = 90;
        int sortX = panelX + panelW - sortW - 12;
        boolean sortHov = isHovered(mouseX, mouseY, sortX, panelY + 6, sortW, 22);
        String sortLabel = sortAlphabetical ? "SORT: A-Z" : "SORT: RECENT";
        renderSlotButton(graphics, sortX, panelY + 6, sortW, 22, sortLabel, sortHov, false, Theme.accentEmerald.getPacked());

        int addY = panelY + panelH - 34;
        renderBeveledPanel(graphics, panelX + 6, addY, panelW - 12, 28, true, Theme.surfaceAlt.getPacked(), 0);

        graphics.text(font, "Add Member:", panelX + 14, addY + 10, Theme.textAccent.getPacked(), false);
        int inputX = panelX + 85;
        int inputW = panelW - 195;
        int inputFocus = addInputFocused ? Theme.focusRing.getPacked() : 0;
        renderBeveledPanel(graphics, inputX, addY + 4, inputW, 20, true, Theme.surface.getPacked(), inputFocus);

        if (addInputText.isEmpty()) {
            graphics.text(font, "Enter player username...", inputX + 6, addY + 10, Theme.textDisabled.getPacked(), false);
        } else {
            String cursor = addInputFocused && (System.currentTimeMillis() / 450 % 2 == 0) ? "_" : "";
            graphics.text(font, addInputText + cursor, inputX + 6, addY + 10, Theme.textAccent.getPacked(), false);
        }

        int addBtnW = 80;
        int addBtnX = inputX + inputW + 8;
        boolean addHov = isHovered(mouseX, mouseY, addBtnX, addY + 4, addBtnW, 20);
        renderSlotButton(graphics, addBtnX, addY + 4, addBtnW, 20, "+ ADD", addHov, false, Theme.accentEmerald.getPacked());

        int listY = panelY + headerH + 8;
        int listH = addY - listY - 6;
        int startY = listY - scrollOffset;

        List<Teammate> allMembers = sortAlphabetical ? Team.get().sortedAlphabetically() : Team.get().sortedByAdded();
        List<Teammate> filtered = new ArrayList<>();
        for (Teammate m : allMembers) {
            if (searchQuery.isEmpty() || m.name.toLowerCase().contains(searchQuery.toLowerCase()) || m.uuid.toLowerCase().contains(searchQuery.toLowerCase())) {
                filtered.add(m);
            }
        }

        if (filtered.isEmpty()) {
            String emptyMsg = allMembers.isEmpty() ? "No crew members yet. Add players below to prevent targeting." : "No crew members matching '" + searchQuery + "'.";
            graphics.centeredText(font, emptyMsg, panelX + panelW / 2, listY + 30, Theme.textDisabled.getPacked());
        } else {
            int curY = startY;
            int rowH = 22;

            for (Teammate m : filtered) {
                if (curY + rowH >= listY && curY <= listY + listH - rowH) {
                    boolean hovered = isHovered(mouseX, mouseY, panelX + 8, curY, panelW - 16, rowH);
                    int fill = hovered ? Theme.surfaceRaised.getPacked() : Theme.surfaceAlt.getPacked();

                    renderBeveledPanel(graphics, panelX + 8, curY, panelW - 16, rowH, false, fill, hovered ? Theme.accentEmerald.getPacked() : 0);

                    graphics.fill(panelX + 14, curY + 6, panelX + 20, curY + 12, Theme.accentEmerald.getPacked());
                    graphics.text(font, m.name, panelX + 26, curY + 7, hovered ? Theme.textAccent.getPacked() : Theme.text.getPacked(), false);

                    String uuidShort = m.uuid.length() > 18 ? m.uuid.substring(0, 18) + "..." : m.uuid;
                    graphics.text(font, uuidShort, panelX + 160, curY + 7, Theme.textDisabled.getPacked(), false);

                    int removeW = 20;
                    int removeX = panelX + panelW - removeW - 16;
                    boolean rHov = isHovered(mouseX, mouseY, removeX, curY + 2, removeW, rowH - 4);
                    renderSlotButton(graphics, removeX, curY + 2, removeW, rowH - 4, "X", rHov, false, Theme.accentRed.getPacked());
                }
                curY += rowH + 2;
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
                addInputFocused = false;
                return true;
            }

            int panelX = (width - panelW) / 2;
            int panelY = 60;
            int panelH = height - 68;
            int headerH = 26;

            if (isHovered(mouseX, mouseY, panelX + 12, panelY + 6, 50, 22)) {
                playUiSound();
                if (minecraft != null) {
                    minecraft.setScreen(parent != null ? parent : new SystemHub());
                }
                return true;
            }

            int sortW = 90;
            int sortX = panelX + panelW - sortW - 12;
            if (isHovered(mouseX, mouseY, sortX, panelY + 6, sortW, 22)) {
                playUiSound();
                sortAlphabetical = !sortAlphabetical;
                return true;
            }

            int addY = panelY + panelH - 34;
            int inputX = panelX + 85;
            int inputW = panelW - 195;
            if (isHovered(mouseX, mouseY, inputX, addY + 4, inputW, 20)) {
                playUiSound();
                addInputFocused = true;
                searchFocused = false;
                return true;
            } else {
                addInputFocused = false;
            }

            int addBtnW = 80;
            int addBtnX = inputX + inputW + 8;
            if (isHovered(mouseX, mouseY, addBtnX, addY + 4, addBtnW, 20)) {
                submitAddMember();
                return true;
            }

            int listY = panelY + headerH + 8;
            int listH = addY - listY - 6;
            int startY = listY - scrollOffset;

            List<Teammate> allMembers = sortAlphabetical ? Team.get().sortedAlphabetically() : Team.get().sortedByAdded();
            List<Teammate> filtered = new ArrayList<>();
            for (Teammate m : allMembers) {
                if (searchQuery.isEmpty() || m.name.toLowerCase().contains(searchQuery.toLowerCase()) || m.uuid.toLowerCase().contains(searchQuery.toLowerCase())) {
                    filtered.add(m);
                }
            }

            int curY = startY;
            int rowH = 22;
            for (Teammate m : filtered) {
                if (curY + rowH >= listY && curY <= listY + listH - rowH) {
                    int removeW = 20;
                    int removeX = panelX + panelW - removeW - 16;
                    if (isHovered(mouseX, mouseY, removeX, curY + 2, removeW, rowH - 4)) {
                        playUiSound();
                        Team.get().remove(m.uuid);
                        return true;
                    }
                }
                curY += rowH + 2;
            }
        }
        return super.mouseClicked(event, isFocused);
    }

    private void submitAddMember() {
        if (!addInputText.trim().isEmpty()) {
            playUiSound();
            String name = addInputText.trim();
            UUID uuid = UUID.nameUUIDFromBytes(name.getBytes());
            if (minecraft != null && minecraft.level != null) {
                for (var p : minecraft.level.players()) {
                    if (p.getName().getString().equalsIgnoreCase(name)) {
                        uuid = p.getUUID();
                        name = p.getName().getString();
                        break;
                    }
                }
            }
            Team.get().add(uuid, name);
            addInputText = "";
            addInputFocused = false;
        }
    }

    @Override
    public boolean charTyped(CharacterEvent event) {
        if (addInputFocused && event.isAllowedChatCharacter()) {
            addInputText += event.codepointAsString();
            return true;
        }
        return super.charTyped(event);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        int key = event.key();

        if (addInputFocused) {
            if (key == GLFW.GLFW_KEY_BACKSPACE) {
                if (!addInputText.isEmpty()) {
                    addInputText = addInputText.substring(0, addInputText.length() - 1);
                }
                return true;
            }
            if (key == GLFW.GLFW_KEY_ENTER || key == GLFW.GLFW_KEY_KP_ENTER) {
                submitAddMember();
                return true;
            }
            if (key == GLFW.GLFW_KEY_ESCAPE) {
                addInputFocused = false;
                return true;
            }
        }

        return super.keyPressed(event);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        scrollOffset = Math.max(0, scrollOffset - (int) (verticalAmount * 22));
        return true;
    }
}