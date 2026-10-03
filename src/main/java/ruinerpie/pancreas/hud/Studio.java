package ruinerpie.pancreas.hud;

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
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Studio extends Panel {
    private Part selectedElement = null;
    private Part draggingElement = null;
    private int dragOffsetX = 0;
    private int dragOffsetY = 0;
    private int sidebarScroll = 0;

    private int snapLineX = -1;
    private int snapLineY = -1;

    private record UndoSnapshot(Part element, int x, int y, float scale, Part.Anchor anchor) {}
    private final Deque<UndoSnapshot> undoStack = new ArrayDeque<>();

    public Studio(Screen parent) {
        this(parent, null);
    }

    public Studio(Screen parent, Part initialSelection) {
        super(Component.literal("Pancreas — HUD Editor"), parent, NavTab.HUD);
        this.selectedElement = initialSelection;
    }

    public Studio() {
        this(null, null);
    }

    private void pushUndo(Part el) {
        if (el != null) {
            if (undoStack.size() >= 25) undoStack.removeLast();
            undoStack.push(new UndoSnapshot(el, el.x, el.y, el.scale, el.anchor));
        }
    }

    @Override
    protected String getSearchPlaceholder() {
        return "Search HUD elements...";
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        renderBackground(graphics);
        renderTabs(graphics, mouseX, mouseY);

        int sidebarW = 200;
        int sidebarX = 12;

        renderSearchBox(graphics, mouseX, mouseY, width - 24);

        int panelY = 60;
        int panelH = height - 80;

        if (Prefs.get().hud.showGrid || hasShiftDown()) {
            int gridStep = 16;
            int dotColor = 0x22FFFFFF;
            for (int gx = 0; gx < width; gx += gridStep) {
                for (int gy = 40; gy < height - 16; gy += gridStep) {
                    graphics.fill(gx, gy, gx + 1, gy + 1, dotColor);
                }
            }
        }

        Context ctx = new Context(graphics, width, height, minecraft, delta);
        for (Part el : Rig.get().all()) {
            if (el.isVisible()) {
                el.render(ctx);

                int rx = el.getRenderX(width);
                int ry = el.getRenderY(height);
                int rw = (int) (el.getWidth() * el.scale);
                int rh = (int) (el.getHeight() * el.scale);

                boolean isSel = el == selectedElement;
                boolean isHov = isHovered(mouseX, mouseY, rx, ry, rw, rh);

                int outlineColor = isSel ? Theme.accentPurple.getPacked() : (isHov ? Theme.focusRing.getPacked() : 0x444A4A52);
                graphics.outline(rx - 1, ry - 1, rw + 2, rh + 2, outlineColor);
                if (isSel) {
                    graphics.outline(rx - 2, ry - 2, rw + 4, rh + 4, 0x446500FF);
                }
            }
        }

        if (snapLineX >= 0) {
            graphics.fill(snapLineX, 0, snapLineX + 1, height, 0x994AEDD9);
        }
        if (snapLineY >= 0) {
            graphics.fill(0, snapLineY, width, snapLineY + 1, 0x994AEDD9);
        }

        renderBeveledPanel(graphics, sidebarX, panelY, sidebarW, panelH, false, Theme.surface.getPacked(), 0);

        int headerH = 24;
        renderBeveledPanel(graphics, sidebarX + 4, panelY + 4, sidebarW - 8, headerH, true, Theme.surfaceAlt.getPacked(), 0);
        graphics.fill(sidebarX + 4, panelY + 4, sidebarX + 7, panelY + 4 + headerH, Theme.accentDiamond.getPacked());
        graphics.text(font, "HUD ELEMENTS", sidebarX + 12, panelY + 12, Theme.textAccent.getPacked(), false);

        int rstAllW = 54;
        int rstAllX = sidebarX + sidebarW - rstAllW - 6;
        boolean rstAllHov = isHovered(mouseX, mouseY, rstAllX, panelY + 6, rstAllW, 20);
        renderSlotButton(graphics, rstAllX, panelY + 6, rstAllW, 20, "RESET", rstAllHov, false, Theme.accentGold.getPacked());

        List<Part> filtered = new ArrayList<>();
        for (Part el : Rig.get().all()) {
            if (searchQuery.isEmpty() || el.name.toLowerCase().contains(searchQuery.toLowerCase()) || el.id.toLowerCase().contains(searchQuery.toLowerCase())) {
                filtered.add(el);
            }
        }

        int listY = panelY + headerH + 8;
        int listH = 140;
        int curY = listY - sidebarScroll;
        int rowH = 20;

        for (Part el : filtered) {
            if (curY + rowH >= listY && curY <= listY + listH) {
                boolean isSel = el == selectedElement;
                boolean hov = isHovered(mouseX, mouseY, sidebarX + 6, curY, sidebarW - 12, rowH);

                int fill = isSel ? Theme.surfaceRaised.getPacked() : (hov ? Theme.surfaceAlt.getPacked() : Theme.surface.getPacked());
                int focus = isSel ? Theme.accentPurple.getPacked() : (hov ? Theme.focusRing.getPacked() : 0);

                renderBeveledPanel(graphics, sidebarX + 6, curY, sidebarW - 12, rowH, isSel, fill, focus);
                drawHighlightedText(graphics, font, el.name, searchQuery, sidebarX + 12, curY + 6, isSel ? Theme.textAccent.getPacked() : Theme.text.getPacked(), Theme.accentGold.getPacked());

                int togW = 34;
                int togX = sidebarX + sidebarW - 44;
                renderSlotButton(graphics, togX, curY + 2, togW, 16, el.enabled ? "ON" : "OFF", false, el.enabled, el.enabled ? Theme.moduleOn.getPacked() : 0);
            }
            curY += rowH + 2;
        }

        int inspY = listY + listH + 10;
        int inspH = panelH - (inspY - panelY) - 6;
        renderBeveledPanel(graphics, sidebarX + 4, inspY, sidebarW - 8, inspH, true, Theme.surfaceAlt.getPacked(), 0);

        if (selectedElement != null) {
            graphics.text(font, selectedElement.name, sidebarX + 12, inspY + 8, Theme.textAccent.getPacked(), false);
            graphics.text(font, "ID: " + selectedElement.id, sidebarX + 12, inspY + 20, Theme.textDisabled.getPacked(), false);

            int ctrlY = inspY + 34;
            graphics.text(font, "Status:", sidebarX + 12, ctrlY + 4, Theme.text.getPacked(), false);
            boolean togHov = isHovered(mouseX, mouseY, sidebarX + 60, ctrlY, 50, 18);
            renderSlotButton(graphics, sidebarX + 60, ctrlY, 50, 18, selectedElement.enabled ? "ENABLED" : "DISABLED", togHov, selectedElement.enabled, selectedElement.enabled ? Theme.moduleOn.getPacked() : 0);

            ctrlY += 24;
            graphics.text(font, "Scale: " + String.format("%.1f", selectedElement.scale), sidebarX + 12, ctrlY + 4, Theme.text.getPacked(), false);
            boolean minusHov = isHovered(mouseX, mouseY, sidebarX + 80, ctrlY, 18, 18);
            boolean plusHov = isHovered(mouseX, mouseY, sidebarX + 102, ctrlY, 18, 18);
            renderSlotButton(graphics, sidebarX + 80, ctrlY, 18, 18, "-", minusHov, false, 0);
            renderSlotButton(graphics, sidebarX + 102, ctrlY, 18, 18, "+", plusHov, false, 0);

            ctrlY += 24;
            graphics.text(font, "Anchor: " + selectedElement.anchor.name(), sidebarX + 12, ctrlY + 4, Theme.text.getPacked(), false);
            String[] anchors = {"TL", "TR", "BL", "BR", "C"};
            int ancX = sidebarX + 12;
            int ancY = ctrlY + 16;
            for (int i = 0; i < anchors.length; i++) {
                Part.Anchor a = Part.Anchor.values()[i];
                boolean aSel = selectedElement.anchor == a;
                boolean aHov = isHovered(mouseX, mouseY, ancX, ancY, 32, 16);
                renderSlotButton(graphics, ancX, ancY, 32, 16, anchors[i], aHov, aSel, aSel ? Theme.accentPurple.getPacked() : 0);
                ancX += 36;
            }

            ctrlY += 38;
            boolean rstHov = isHovered(mouseX, mouseY, sidebarX + 12, ctrlY, sidebarW - 24, 20);
            renderSlotButton(graphics, sidebarX + 12, ctrlY, sidebarW - 24, 20, "RESET ELEMENT", rstHov, false, Theme.accentGold.getPacked());
        } else {
            graphics.centeredText(font, "Select an element to edit", sidebarX + sidebarW / 2, inspY + inspH / 2 - 4, Theme.textDisabled.getPacked());
        }

        int barY = height - 16;
        graphics.fill(0, barY, width, height, Theme.surface.getPacked());
        graphics.fill(0, barY, width, barY + 1, Theme.border.getPacked());

        if (selectedElement != null) {
            String liveStr = selectedElement.name + " | Pos: (" + selectedElement.x + ", " + selectedElement.y + ") | Scale: " + String.format("%.1fx", selectedElement.scale) + " | Anchor: " + selectedElement.anchor.name();
            graphics.text(font, liveStr, 12, barY + 4, Theme.textAccent.getPacked(), false);
        } else {
            graphics.text(font, "Click element to select | Drag to move | Hold Shift for 8px snap | Ctrl+Z to undo", 12, barY + 4, Theme.textMuted.getPacked(), false);
        }
        graphics.text(font, "HUD Editor", width - 80, barY + 4, Theme.accentDiamond.getPacked(), false);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean isFocused) {
        double mouseX = event.x();
        double mouseY = event.y();

        if (event.button() == 0) {
            if (handleTabClicks(mouseX, mouseY)) return true;
            if (handleSearchClick(mouseX, mouseY, width - 24)) return true;

            int sidebarW = 200;
            int sidebarX = 12;
            int panelY = 60;

            int rstAllW = 54;
            int rstAllX = sidebarX + sidebarW - rstAllW - 6;
            if (isHovered(mouseX, mouseY, rstAllX, panelY + 6, rstAllW, 20)) {
                playUiSound();
                resetAllLayouts();
                return true;
            }

            int headerH = 24;
            int listY = panelY + headerH + 8;
            int listH = 140;

            List<Part> filtered = new ArrayList<>();
            for (Part el : Rig.get().all()) {
                if (searchQuery.isEmpty() || el.name.toLowerCase().contains(searchQuery.toLowerCase()) || el.id.toLowerCase().contains(searchQuery.toLowerCase())) {
                    filtered.add(el);
                }
            }

            int curY = listY - sidebarScroll;
            int rowH = 20;

            for (Part el : filtered) {
                if (curY + rowH >= listY && curY <= listY + listH) {
                    if (isHovered(mouseX, mouseY, sidebarX + 6, curY, sidebarW - 12, rowH)) {
                        playUiSound();
                        int togX = sidebarX + sidebarW - 44;
                        if (isHovered(mouseX, mouseY, togX, curY + 2, 34, 16)) {
                            el.toggle();
                        } else {
                            selectedElement = el;
                        }
                        return true;
                    }
                }
                curY += rowH + 2;
            }

            if (selectedElement != null) {
                int inspY = listY + listH + 10;
                int ctrlY = inspY + 34;

                if (isHovered(mouseX, mouseY, sidebarX + 60, ctrlY, 50, 18)) {
                    playUiSound();
                    selectedElement.toggle();
                    return true;
                }

                ctrlY += 24;
                if (isHovered(mouseX, mouseY, sidebarX + 80, ctrlY, 18, 18)) {
                    pushUndo(selectedElement);
                    playUiSound();
                    selectedElement.scale = Math.max(0.5f, (float) Math.round((selectedElement.scale - 0.1f) * 10) / 10);
                    return true;
                }
                if (isHovered(mouseX, mouseY, sidebarX + 102, ctrlY, 18, 18)) {
                    pushUndo(selectedElement);
                    playUiSound();
                    selectedElement.scale = Math.min(2.0f, (float) Math.round((selectedElement.scale + 0.1f) * 10) / 10);
                    return true;
                }

                ctrlY += 24;
                int ancX = sidebarX + 12;
                int ancY = ctrlY + 16;
                for (int i = 0; i < 5; i++) {
                    if (isHovered(mouseX, mouseY, ancX, ancY, 32, 16)) {
                        pushUndo(selectedElement);
                        playUiSound();
                        selectedElement.anchor = Part.Anchor.values()[i];
                        return true;
                    }
                    ancX += 36;
                }

                ctrlY += 38;
                if (isHovered(mouseX, mouseY, sidebarX + 12, ctrlY, sidebarW - 24, 20)) {
                    pushUndo(selectedElement);
                    playUiSound();
                    selectedElement.x = 4;
                    selectedElement.y = 4;
                    return true;
                }
            }

            for (Part el : Rig.get().all()) {
                if (el.isVisible()) {
                    int rx = el.getRenderX(width);
                    int ry = el.getRenderY(height);
                    int rw = (int) (el.getWidth() * el.scale);
                    int rh = (int) (el.getHeight() * el.scale);

                    if (isHovered(mouseX, mouseY, rx, ry, rw, rh)) {
                        pushUndo(el);
                        playUiSound();
                        selectedElement = el;
                        draggingElement = el;
                        dragOffsetX = (int) mouseX - rx;
                        dragOffsetY = (int) mouseY - ry;
                        return true;
                    }
                }
            }
        } else if (event.button() == 1) {
            
            for (Part el : Rig.get().all()) {
                if (el.isVisible()) {
                    int rx = el.getRenderX(width);
                    int ry = el.getRenderY(height);
                    int rw = (int) (el.getWidth() * el.scale);
                    int rh = (int) (el.getHeight() * el.scale);

                    if (isHovered(mouseX, mouseY, rx, ry, rw, rh)) {
                        playUiSound();
                        if (el instanceof ClientMark cm) {
                            cm.cycleLayout();
                        } else if (el instanceof Coords c) {
                            c.toggleShowOnlyXZ();
                        }
                        return true;
                    }
                }
            }
        }
        return super.mouseClicked(event, isFocused);
    }

    private void resetAllLayouts() {
        for (Part el : Rig.get().all()) {
            pushUndo(el);
            el.scale = 1.0f;
            switch (el.id) {
                case "client-mark" -> { el.x = 4; el.y = 4; el.anchor = Part.Anchor.TOP_LEFT; }
                case "coords" -> { el.x = 4; el.y = 26; el.anchor = Part.Anchor.TOP_LEFT; }
                case "array-list" -> { el.x = 4; el.y = 4; el.anchor = Part.Anchor.TOP_RIGHT; }
                case "armor-view" -> { el.x = 4; el.y = 60; el.anchor = Part.Anchor.BOTTOM_LEFT; }
                case "inv-peek" -> { el.x = 4; el.y = 4; el.anchor = Part.Anchor.BOTTOM_RIGHT; }
                case "combat-view" -> { el.x = 4; el.y = 80; el.anchor = Part.Anchor.TOP_RIGHT; }
                case "radar-text" -> { el.x = 4; el.y = 45; el.anchor = Part.Anchor.TOP_LEFT; }
                case "notif-stack" -> { el.x = 4; el.y = 50; el.anchor = Part.Anchor.BOTTOM_RIGHT; }
                case "keybind-list" -> { el.x = 4; el.y = 140; el.anchor = Part.Anchor.TOP_RIGHT; }
                case "potion-list" -> { el.x = 4; el.y = 180; el.anchor = Part.Anchor.TOP_RIGHT; }
                default -> { el.x = 4; el.y = 4; el.anchor = Part.Anchor.TOP_LEFT; }
            }
        }
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        if (event.button() == 0) {
            draggingElement = null;
            snapLineX = -1;
            snapLineY = -1;
        }
        return super.mouseReleased(event);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double deltaX, double deltaY) {
        if (draggingElement != null) {
            int newRenderX = (int) event.x() - dragOffsetX;
            int newRenderY = (int) event.y() - dragOffsetY;

            int rw = (int) (draggingElement.getWidth() * draggingElement.scale);
            int rh = (int) (draggingElement.getHeight() * draggingElement.scale);

            if (hasShiftDown()) {
                newRenderX = (newRenderX / 8) * 8;
                newRenderY = (newRenderY / 8) * 8;
            }

            snapLineX = -1;
            snapLineY = -1;

            if (Math.abs(newRenderX) < 10) {
                newRenderX = 4;
                snapLineX = 4;
            } else if (Math.abs(newRenderX + rw - width) < 10) {
                newRenderX = width - rw - 4;
                snapLineX = width - 4;
            }

            if (Math.abs(newRenderY) < 10) {
                newRenderY = 4;
                snapLineY = 4;
            } else if (Math.abs(newRenderY + rh - height) < 10) {
                newRenderY = height - rh - 4;
                snapLineY = height - 4;
            }

            switch (draggingElement.anchor) {
                case TOP_LEFT -> {
                    draggingElement.x = Math.max(0, newRenderX);
                    draggingElement.y = Math.max(0, newRenderY);
                }
                case TOP_RIGHT -> {
                    draggingElement.x = Math.max(0, width - rw - newRenderX);
                    draggingElement.y = Math.max(0, newRenderY);
                }
                case BOTTOM_LEFT -> {
                    draggingElement.x = Math.max(0, newRenderX);
                    draggingElement.y = Math.max(0, height - rh - newRenderY);
                }
                case BOTTOM_RIGHT -> {
                    draggingElement.x = Math.max(0, width - rw - newRenderX);
                    draggingElement.y = Math.max(0, height - rh - newRenderY);
                }
                case CENTER -> {
                    draggingElement.x = newRenderX - (width - rw) / 2;
                    draggingElement.y = newRenderY - (height - rh) / 2;
                }
            }
            return true;
        }
        return super.mouseDragged(event, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        sidebarScroll = Math.max(0, sidebarScroll - (int) (verticalAmount * 20));
        return true;
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        
        if (event.key() == GLFW.GLFW_KEY_Z && hasControlDown()) {
            if (!undoStack.isEmpty()) {
                UndoSnapshot snap = undoStack.pop();
                snap.element.x = snap.x;
                snap.element.y = snap.y;
                snap.element.scale = snap.scale;
                snap.element.anchor = snap.anchor;
                selectedElement = snap.element;
                playUiSound();
                return true;
            }
        }

        return super.keyPressed(event);
    }

    @Override
    public void onClose() {
        Rig.get().save();
        super.onClose();
    }

    @Override
    protected void onSearchEnterPressed() {
        for (Part el : Rig.get().all()) {
            if (searchQuery.isEmpty() || el.name.toLowerCase().contains(searchQuery.toLowerCase()) || el.id.toLowerCase().contains(searchQuery.toLowerCase())) {
                selectedElement = el;
                return;
            }
        }
        triggerSearchShake();
    }
}