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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StalkScreen extends Panel {
    private String followInput = "";
    private boolean inputFocused = false;
    private int scrollOffset = 0;

    public StalkScreen(Screen parent) {
        super(Component.literal("Pancreas — Stalk"), parent, NavTab.SYSTEMS);
    }

    public StalkScreen() {
        this(null);
    }

    @Override
    protected String getSearchPlaceholder() {
        return "Search targets...";
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

        boolean backHov = isHovered(mouseX, mouseY, panelX + 12, panelY + 6, 50, 22);
        renderSlotButton(graphics, panelX + 12, panelY + 6, 50, 22, "< Back", backHov, false, Theme.accentPurple.getPacked());

        graphics.text(font, "SHADOW", panelX + 70, panelY + 13, Theme.textAccent.getPacked(), false);
        graphics.text(font, "Entity follow state provider and target tracking", panelX + 126, panelY + 13, Theme.textMuted.getPacked(), false);

        int curY = panelY + headerH + 8;

        int bannerH = 46;
        renderBeveledPanel(graphics, panelX + 8, curY, panelW - 16, bannerH, true, Theme.surfaceAlt.getPacked(), 0);

        StalkTarget target = Stalk.get().current();
        if (target != null) {
            graphics.fill(panelX + 8, curY, panelX + 12, curY + bannerH, Theme.accentPurple.getPacked());
            graphics.text(font, "CURRENT TARGET: " + target.name, panelX + 18, curY + 8, Theme.textAccent.getPacked(), false);

            double dist = 0;
            if (minecraft != null && minecraft.player != null && target.lastKnownPos != null) {
                dist = Math.sqrt(minecraft.player.distanceToSqr(target.lastKnownPos));
            }
            long secAgo = (System.currentTimeMillis() - target.lastSeen) / 1000;
            String posStr = target.lastKnownPos != null ? String.format("Pos: %.1f, %.1f, %.1f", target.lastKnownPos.x, target.lastKnownPos.y, target.lastKnownPos.z) : "Pos: Unknown";
            String info = String.format("Dist: %.1fm  |  Last seen: %ds ago  |  %s", dist, secAgo, posStr);
            graphics.text(font, info, panelX + 18, curY + 24, Theme.textMuted.getPacked(), false);

            int stopW = 70;
            int stopX = panelX + panelW - stopW - 18;
            boolean stopHov = isHovered(mouseX, mouseY, stopX, curY + 12, stopW, 22);
            renderSlotButton(graphics, stopX, curY + 12, stopW, 22, "STOP", stopHov, false, Theme.accentRed.getPacked());
        } else {
            graphics.fill(panelX + 8, curY, panelX + 12, curY + bannerH, Theme.border.getPacked());
            graphics.text(font, "STATUS: IDLE", panelX + 18, curY + 12, Theme.textDisabled.getPacked(), false);
            graphics.text(font, "Select an entity below, follow combat target, or enter a username to start tracking.", panelX + 18, curY + 26, Theme.textMuted.getPacked(), false);
        }

        curY += bannerH + 8;

        int ctrlH = 30;
        renderBeveledPanel(graphics, panelX + 8, curY, panelW - 16, ctrlH, true, Theme.surfaceAlt.getPacked(), 0);

        int inputX = panelX + 16;
        int inputW = 180;
        int inFocus = inputFocused ? Theme.focusRing.getPacked() : 0;
        renderBeveledPanel(graphics, inputX, curY + 5, inputW, 20, true, Theme.surface.getPacked(), inFocus);

        if (followInput.isEmpty()) {
            graphics.text(font, "Enter player username...", inputX + 6, curY + 11, Theme.textDisabled.getPacked(), false);
        } else {
            String cursor = inputFocused && (System.currentTimeMillis() / 450 % 2 == 0) ? "_" : "";
            graphics.text(font, followInput + cursor, inputX + 6, curY + 11, Theme.textAccent.getPacked(), false);
        }

        int followBtnW = 70;
        int followBtnX = inputX + inputW + 6;
        boolean fHov = isHovered(mouseX, mouseY, followBtnX, curY + 5, followBtnW, 20);
        renderSlotButton(graphics, followBtnX, curY + 5, followBtnW, 20, "FOLLOW", fHov, false, Theme.accentPurple.getPacked());

        boolean hasCombatTarget = minecraft != null && minecraft.crosshairPickEntity instanceof LivingEntity;
        int combatBtnW = 160;
        int combatBtnX = panelX + panelW - combatBtnW - 16;
        boolean cpHov = isHovered(mouseX, mouseY, combatBtnX, curY + 5, combatBtnW, 20);
        int cbColor = hasCombatTarget ? Theme.accentRed.getPacked() : Theme.textDisabled.getPacked();
        renderSlotButton(graphics, combatBtnX, curY + 5, combatBtnW, 20, "TARGET IN CROSSHAIR", cpHov && hasCombatTarget, false, cbColor);

        curY += ctrlH + 8;

        int listY = curY;
        int listH = panelH - (listY - panelY) - 8;
        int startY = listY - scrollOffset;

        List<Entity> nearby = new ArrayList<>();
        if (minecraft != null && minecraft.level != null && minecraft.player != null) {
            for (Entity e : minecraft.level.entitiesForRendering()) {
                if (e == minecraft.player) continue;
                if (searchQuery.isEmpty() || e.getName().getString().toLowerCase().contains(searchQuery.toLowerCase())) {
                    nearby.add(e);
                }
            }
        }

        if (nearby.isEmpty()) {
            graphics.centeredText(font, "No entities nearby in range.", panelX + panelW / 2, listY + 30, Theme.textDisabled.getPacked());
        } else {
            int rowY = startY;
            int rowH = 22;

            for (Entity e : nearby) {
                if (rowY + rowH >= listY && rowY <= listY + listH - rowH) {
                    boolean hovered = isHovered(mouseX, mouseY, panelX + 8, rowY, panelW - 16, rowH);
                    int fill = hovered ? Theme.surfaceRaised.getPacked() : Theme.surfaceAlt.getPacked();
                    boolean isTarget = target != null && target.uuid.equals(e.getUUID());

                    renderBeveledPanel(graphics, panelX + 8, rowY, panelW - 16, rowH, isTarget, fill, isTarget ? Theme.accentPurple.getPacked() : 0);

                    graphics.text(font, e.getName().getString(), panelX + 16, rowY + 7, isTarget ? Theme.accentPurple.getPacked() : (hovered ? Theme.textAccent.getPacked() : Theme.text.getPacked()), false);

                    double d = minecraft != null && minecraft.player != null ? minecraft.player.distanceTo(e) : 0;
                    graphics.text(font, String.format("%.1fm", d), panelX + 160, rowY + 7, Theme.textDisabled.getPacked(), false);

                    int trackW = 54;
                    int trackX = panelX + panelW - trackW - 16;
                    boolean tHov = isHovered(mouseX, mouseY, trackX, rowY + 2, trackW, rowH - 4);
                    renderSlotButton(graphics, trackX, rowY + 2, trackW, rowH - 4, isTarget ? "TRACKING" : "TRACK", tHov, isTarget, isTarget ? Theme.accentPurple.getPacked() : 0);
                }
                rowY += rowH + 2;
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
                inputFocused = false;
                return true;
            }

            int panelX = (width - panelW) / 2;
            int panelY = 60;

            if (isHovered(mouseX, mouseY, panelX + 12, panelY + 6, 50, 22)) {
                playUiSound();
                if (minecraft != null) {
                    minecraft.setScreen(parent != null ? parent : new SystemHub());
                }
                return true;
            }

            StalkTarget target = Stalk.get().current();
            int headerH = 26;
            int curY = panelY + headerH + 8;
            int bannerH = 46;
            if (target != null) {
                int stopW = 70;
                int stopX = panelX + panelW - stopW - 18;
                if (isHovered(mouseX, mouseY, stopX, curY + 12, stopW, 22)) {
                    playUiSound();
                    Stalk.get().stop();
                    return true;
                }
            }

            curY += bannerH + 8;

            int inputX = panelX + 16;
            int inputW = 180;
            if (isHovered(mouseX, mouseY, inputX, curY + 5, inputW, 20)) {
                playUiSound();
                inputFocused = true;
                searchFocused = false;
                return true;
            } else {
                inputFocused = false;
            }

            int followBtnW = 70;
            int followBtnX = inputX + inputW + 6;
            if (isHovered(mouseX, mouseY, followBtnX, curY + 5, followBtnW, 20)) {
                submitFollow();
                return true;
            }

            int combatBtnW = 160;
            int combatBtnX = panelX + panelW - combatBtnW - 16;
            if (isHovered(mouseX, mouseY, combatBtnX, curY + 5, combatBtnW, 20)) {
                if (minecraft != null && minecraft.crosshairPickEntity instanceof LivingEntity) {
                    playUiSound();
                    Stalk.get().followCombatTarget();
                    return true;
                }
            }

            curY += 38;

            int listY = curY;
            int listH = height - 68 - (listY - panelY) - 8;
            int startY = listY - scrollOffset;

            List<Entity> nearby = new ArrayList<>();
            if (minecraft != null && minecraft.level != null && minecraft.player != null) {
                for (Entity e : minecraft.level.entitiesForRendering()) {
                    if (e == minecraft.player) continue;
                    if (searchQuery.isEmpty() || e.getName().getString().toLowerCase().contains(searchQuery.toLowerCase())) {
                        nearby.add(e);
                    }
                }
            }

            int rowY = startY;
            int rowH = 22;
            for (Entity e : nearby) {
                if (rowY + rowH >= listY && rowY <= listY + listH - rowH) {
                    if (isHovered(mouseX, mouseY, panelX + 8, rowY, panelW - 16, rowH)) {
                        playUiSound();
                        Stalk.get().follow(e.getUUID(), e.getName().getString());
                        return true;
                    }
                }
                rowY += rowH + 2;
            }
        }
        return super.mouseClicked(event, isFocused);
    }

    private void submitFollow() {
        if (!followInput.trim().isEmpty()) {
            playUiSound();
            String name = followInput.trim();
            UUID uuid = UUID.nameUUIDFromBytes(name.getBytes());
            if (minecraft != null && minecraft.level != null) {
                for (Player p : minecraft.level.players()) {
                    if (p.getName().getString().equalsIgnoreCase(name)) {
                        uuid = p.getUUID();
                        name = p.getName().getString();
                        break;
                    }
                }
            }
            Stalk.get().follow(uuid, name);
            followInput = "";
            inputFocused = false;
        }
    }

    @Override
    public boolean charTyped(CharacterEvent event) {
        if (inputFocused && event.isAllowedChatCharacter()) {
            followInput += event.codepointAsString();
            return true;
        }
        return super.charTyped(event);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        int key = event.key();

        if (inputFocused) {
            if (key == GLFW.GLFW_KEY_BACKSPACE) {
                if (!followInput.isEmpty()) {
                    followInput = followInput.substring(0, followInput.length() - 1);
                }
                return true;
            }
            if (key == GLFW.GLFW_KEY_ENTER || key == GLFW.GLFW_KEY_KP_ENTER) {
                submitFollow();
                return true;
            }
            if (key == GLFW.GLFW_KEY_ESCAPE) {
                inputFocused = false;
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