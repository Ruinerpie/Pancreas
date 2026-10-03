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
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.HashSet;
import java.util.Set;

public class TweakSettings extends Panel {
    private final Feature module;
    private int scrollOffset = 0;
    private boolean listeningForKeybind = false;
    private long lastModifiedTime = 0;

    private final Set<String> collapsedGroups = new HashSet<>();

    private TintValue activeColorSetting = null;

    public TweakSettings(Feature module, Screen parent) {
        super(Component.literal("Pancreas — " + module.name), parent, getTabForCategory(module.category));
        this.module = module;
    }

    private static NavTab getTabForCategory(Group cat) {
        return switch (cat) {
            case Cheats -> NavTab.CHEATS;
            case Extras -> NavTab.EXTRAS;
            case Misc -> NavTab.MISC;
            case Utilities -> NavTab.UTILITIES;
        };
    }

    private void markModified() {
        this.lastModifiedTime = System.currentTimeMillis();
    }

    @Override
    protected String getSearchPlaceholder() {
        return "Search " + module.name + " settings...";
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        renderBackground(graphics);
        renderTabs(graphics, mouseX, mouseY);

        int panelW = Math.min(width - 32, 680);
        int panelX = (width - panelW) / 2;

        renderSearchBox(graphics, mouseX, mouseY, panelW);

        int panelY = 60;
        int panelH = height - 76;
        int catAccent = Theme.getCategoryColor(module.category).getPacked();

        renderBeveledPanel(graphics, panelX, panelY, panelW, panelH, false, Theme.surface.getPacked(), 0);

        int bannerH = 50;
        renderBeveledPanel(graphics, panelX + 4, panelY + 4, panelW - 8, bannerH, true, Theme.surfaceAlt.getPacked(), 0);
        graphics.fill(panelX + 4, panelY + 4, panelX + 8, panelY + 4 + bannerH, catAccent);

        boolean backHover = isHovered(mouseX, mouseY, panelX + 12, panelY + 14, 52, 22);
        renderSlotButton(graphics, panelX + 12, panelY + 14, 52, 22, "< Back", backHover, false, catAccent);

        int contentX = panelX + 72;
        if (Prefs.get().layout.showIcons) {
            graphics.fakeItem(net.minecraft.world.item.ItemStack.EMPTY, contentX, panelY + 17);
            contentX += 20;
        }

        graphics.text(font, module.name, contentX, panelY + 12, Theme.textAccent.getPacked(), false);

        int chipX = contentX + font.width(module.name) + 8;
        String catChip = "[" + module.category.displayName + "]";
        graphics.text(font, catChip, chipX, panelY + 12, catAccent, false);

        chipX += font.width(catChip) + 6;
        String stateChip = module.isActive() ? "[ENABLED]" : "[DISABLED]";
        int stateColor = module.isActive() ? Theme.moduleOn.getPacked() : Theme.textDisabled.getPacked();
        graphics.text(font, stateChip, chipX, panelY + 12, stateColor, false);

        if (module.description != null && !module.description.isEmpty()) {
            graphics.text(font, module.description, contentX, panelY + 28, Theme.textMuted.getPacked(), false);
        }

        int btnY = panelY + 14;
        int rightCur = panelX + panelW - 14;

        int resetW = 68;
        int resetX = rightCur - resetW;
        boolean resetHover = isHovered(mouseX, mouseY, resetX, btnY, resetW, 22);
        renderSlotButton(graphics, resetX, btnY, resetW, 22, "RESET ALL", resetHover, false, Theme.accentGold.getPacked());

        String keyText = listeningForKeybind ? "PRESS KEY..." : "KEY: " + module.getKeybindName();
        int keyW = font.width(keyText) + 16;
        int keyX = resetX - keyW - 6;
        boolean keyHover = isHovered(mouseX, mouseY, keyX, btnY, keyW, 22);
        int keyAccent = listeningForKeybind ? Theme.accentGold.getPacked() : 0;
        renderSlotButton(graphics, keyX, btnY, keyW, 22, keyText, keyHover, listeningForKeybind, keyAccent);

        int toggleW = 54;
        int toggleX = keyX - toggleW - 6;
        boolean toggleHover = isHovered(mouseX, mouseY, toggleX, btnY, toggleW, 22);
        renderToggleSlot(graphics, toggleX, btnY, toggleW, 22, module.isActive(), toggleHover);

        int listY = panelY + bannerH + 8;
        int listH = panelH - bannerH - 36; 
        int startY = listY - scrollOffset;

        int curY = startY;
        boolean anyMatched = false;

        for (ValueGroup group : module.settings.groups()) {
            boolean groupHasMatch = false;
            for (Value<?> s : group.getSettings()) {
                if (matchesSearch(s)) {
                    groupHasMatch = true;
                    anyMatched = true;
                    break;
                }
            }
            if (!groupHasMatch) continue;

            boolean isCollapsed = collapsedGroups.contains(group.name);
            if (curY + 20 >= listY && curY <= listY + listH - 20) {
                boolean gHov = isHovered(mouseX, mouseY, panelX + 6, curY, panelW - 12, 20);
                renderBeveledPanel(graphics, panelX + 6, curY, panelW - 12, 20, false, gHov ? Theme.surfaceAlt.getPacked() : Theme.surfaceRaised.getPacked(), gHov ? Theme.accentPurple.getPacked() : 0);
                String arrow = isCollapsed ? "► " : "▼ ";
                graphics.text(font, arrow + group.name.toUpperCase(), panelX + 14, curY + 6, Theme.accentPurple.getPacked(), false);
                graphics.text(font, "(" + group.getSettings().size() + " settings)", panelX + panelW - 90, curY + 6, Theme.textDisabled.getPacked(), false);
            }
            curY += 24;

            if (isCollapsed) {
                continue;
            }

            for (Value<?> setting : group.getSettings()) {
                if (!setting.isVisible()) continue;
                if (!matchesSearch(setting)) continue;

                int rowH = 30;
                if (curY + rowH >= listY && curY <= listY + listH - rowH) {
                    boolean rowHover = isHovered(mouseX, mouseY, panelX + 6, curY, panelW - 12, rowH);
                    int focus = rowHover ? catAccent : 0;

                    renderBeveledPanel(graphics, panelX + 6, curY, panelW - 12, rowH, true, Theme.surfaceAlt.getPacked(), focus);

                    drawHighlightedText(graphics, font, setting.name, searchQuery, panelX + 14, curY + 5, Theme.text.getPacked(), Theme.accentGold.getPacked());

                    if (setting.description != null && !setting.description.isEmpty()) {
                        graphics.text(font, setting.description, panelX + 14, curY + 17, Theme.textDisabled.getPacked(), false);
                    }

                    int rX = panelX + panelW - 28;
                    boolean rHover = isHovered(mouseX, mouseY, rX, curY + 4, 18, 18);
                    renderSlotButton(graphics, rX, curY + 4, 18, 18, "R", rHover, false, Theme.accentGold.getPacked());

                    int ctrlRight = rX - 6;
                    renderSettingControl(graphics, setting, ctrlRight, curY + 4, mouseX, mouseY);
                }

                curY += rowH + 3;
            }
            curY += 6;
        }

        if (!anyMatched && !searchQuery.isEmpty()) {
            graphics.centeredText(font, "No settings found matching '" + searchQuery + "'.", panelX + panelW / 2, listY + 30, Theme.textDisabled.getPacked());
        }

        int footerY = panelY + panelH - 24;
        renderBeveledPanel(graphics, panelX + 4, footerY, panelW - 8, 20, true, Theme.surfaceAlt.getPacked(), 0);
        if (lastModifiedTime > 0) {
            long sec = (System.currentTimeMillis() - lastModifiedTime) / 1000;
            String modStr = "Last modified " + (sec == 0 ? "just now" : sec + "s ago") + " (saved)";
            graphics.text(font, modStr, panelX + 12, footerY + 6, Theme.accentEmerald.getPacked(), false);
        } else {
            graphics.text(font, "Values automatically persist to config/pancreas/", panelX + 12, footerY + 6, Theme.textDisabled.getPacked(), false);
        }

        if (activeColorSetting != null) {
            renderColorPickerPopup(graphics, mouseX, mouseY);
        }

        renderStatusBar(graphics);
    }

    private void renderColorPickerPopup(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        int pw = 180;
        int ph = 120;
        int px = (width - pw) / 2;
        int py = (height - ph) / 2;

        renderBeveledPanel(graphics, px, py, pw, ph, false, Theme.surfaceRaised.getPacked(), Theme.focusRing.getPacked());
        graphics.text(font, "COLOR: " + activeColorSetting.name, px + 10, py + 8, Theme.textAccent.getPacked(), false);

        Tint col = activeColorSetting.get();
        
        renderBeveledPanel(graphics, px + 10, py + 22, 40, 40, true, col.getPacked(), 0);
        graphics.text(font, String.format("#%02X%02X%02X", col.r, col.g, col.b), px + 58, py + 26, Theme.text.getPacked(), false);
        graphics.text(font, "Alpha: " + col.a, px + 58, py + 42, Theme.textMuted.getPacked(), false);

        int btnY = py + 70;
        graphics.text(font, "Adjust:", px + 10, btnY + 4, Theme.textMuted.getPacked(), false);
        boolean redHov = isHovered(mouseX, mouseY, px + 54, btnY, 26, 18);
        renderSlotButton(graphics, px + 54, btnY, 26, 18, "+R", redHov, false, Theme.accentRed.getPacked());

        boolean grnHov = isHovered(mouseX, mouseY, px + 84, btnY, 26, 18);
        renderSlotButton(graphics, px + 84, btnY, 26, 18, "+G", grnHov, false, Theme.accentEmerald.getPacked());

        boolean bluHov = isHovered(mouseX, mouseY, px + 114, btnY, 26, 18);
        renderSlotButton(graphics, px + 114, btnY, 26, 18, "+B", bluHov, false, Theme.accentLapis.getPacked());

        boolean closeHov = isHovered(mouseX, mouseY, px + pw - 60, py + ph - 24, 50, 18);
        renderSlotButton(graphics, px + pw - 60, py + ph - 24, 50, 18, "DONE", closeHov, false, Theme.accentGold.getPacked());
    }

    private boolean matchesSearch(Value<?> setting) {
        if (searchQuery.isEmpty()) return true;
        String q = searchQuery.toLowerCase();
        return setting.name.toLowerCase().contains(q)
            || (setting.description != null && setting.description.toLowerCase().contains(q));
    }

    private void renderSettingControl(GuiGraphicsExtractor graphics, Value<?> setting, int rightX, int y, int mouseX, int mouseY) {
        if (setting instanceof FlagValue bs) {
            boolean val = bs.get();
            int w = 46;
            int x = rightX - w;
            boolean hov = isHovered(mouseX, mouseY, x, y, w, 18);
            renderSlotButton(graphics, x, y, w, 18, val ? "ON" : "OFF", hov, val, val ? Theme.moduleOn.getPacked() : 0);
        } else if (setting instanceof IntValue is) {
            String valStr = String.valueOf(is.get());
            int valW = font.width(valStr);
            int boxW = Math.max(34, valW + 10);
            int minusX = rightX - boxW - 42;
            int plusX = rightX - 18;

            boolean minusHov = isHovered(mouseX, mouseY, minusX, y, 18, 18);
            boolean plusHov = isHovered(mouseX, mouseY, plusX, y, 18, 18);

            renderSlotButton(graphics, minusX, y, 18, 18, "-", minusHov, false, 0);
            renderBeveledPanel(graphics, minusX + 21, y, boxW, 18, true, Theme.surface.getPacked(), 0);
            graphics.centeredText(font, valStr, minusX + 21 + boxW / 2, y + 5, Theme.accentPurple.getPacked());
            renderSlotButton(graphics, plusX, y, 18, 18, "+", plusHov, false, 0);
        } else if (setting instanceof DoubleValue ds) {
            String valStr = String.format("%.1f", ds.get());
            int valW = font.width(valStr);
            int boxW = Math.max(36, valW + 10);
            int minusX = rightX - boxW - 42;
            int plusX = rightX - 18;

            boolean minusHov = isHovered(mouseX, mouseY, minusX, y, 18, 18);
            boolean plusHov = isHovered(mouseX, mouseY, plusX, y, 18, 18);

            renderSlotButton(graphics, minusX, y, 18, 18, "-", minusHov, false, 0);
            renderBeveledPanel(graphics, minusX + 21, y, boxW, 18, true, Theme.surface.getPacked(), 0);
            graphics.centeredText(font, valStr, minusX + 21 + boxW / 2, y + 5, Theme.accentPurple.getPacked());
            renderSlotButton(graphics, plusX, y, 18, 18, "+", plusHov, false, 0);
        } else if (setting instanceof ChoiceValue<?> es) {
            String valStr = es.get().toString();
            int w = font.width(valStr) + 14;
            int x = rightX - w;
            boolean hov = isHovered(mouseX, mouseY, x, y, w, 18);
            renderSlotButton(graphics, x, y, w, 18, valStr, hov, false, Theme.accentPurple.getPacked());
        } else if (setting instanceof TintValue cs) {
            Tint col = cs.get();
            int w = 58;
            int x = rightX - w;
            boolean hov = isHovered(mouseX, mouseY, x, y, w, 18);
            renderBeveledPanel(graphics, x, y, w, 18, true, col.getPacked(), hov ? Theme.focusRing.getPacked() : 0);
            String hex = String.format("#%02X%02X%02X", col.r, col.g, col.b);
            graphics.centeredText(font, hex, x + w / 2, y + 5, 0xFFFFFFFF);
        } else if (setting instanceof ItemListValue ils) {
            String valStr = ils.get().size() + " items";
            int w = font.width(valStr) + 14;
            int x = rightX - w;
            boolean hov = isHovered(mouseX, mouseY, x, y, w, 18);
            renderSlotButton(graphics, x, y, w, 18, valStr, hov, false, Theme.accentDiamond.getPacked());
        } else {
            String valStr = String.valueOf(setting.get());
            int w = font.width(valStr) + 14;
            int x = rightX - w;
            renderBeveledPanel(graphics, x, y, w, 18, true, Theme.surface.getPacked(), 0);
            graphics.centeredText(font, valStr, x + w / 2, y + 5, Theme.text.getPacked());
        }
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean isFocused) {
        if (event.button() == 0) {
            double mouseX = event.x();
            double mouseY = event.y();

            if (activeColorSetting != null) {
                int pw = 180;
                int ph = 120;
                int px = (width - pw) / 2;
                int py = (height - ph) / 2;

                int btnY = py + 70;
                Tint col = activeColorSetting.get();

                if (isHovered(mouseX, mouseY, px + 54, btnY, 26, 18)) {
                    playUiSound();
                    col.r = (col.r + 32) % 256;
                    markModified();
                    return true;
                }
                if (isHovered(mouseX, mouseY, px + 84, btnY, 26, 18)) {
                    playUiSound();
                    col.g = (col.g + 32) % 256;
                    markModified();
                    return true;
                }
                if (isHovered(mouseX, mouseY, px + 114, btnY, 26, 18)) {
                    playUiSound();
                    col.b = (col.b + 32) % 256;
                    markModified();
                    return true;
                }
                if (isHovered(mouseX, mouseY, px + pw - 60, py + ph - 24, 50, 18)) {
                    playUiSound();
                    activeColorSetting = null;
                    return true;
                }

                if (!isHovered(mouseX, mouseY, px, py, pw, ph)) {
                    activeColorSetting = null;
                    return true;
                }
                return true;
            }

            if (handleTabClicks(mouseX, mouseY)) return true;

            int panelW = Math.min(width - 32, 680);
            if (handleSearchClick(mouseX, mouseY, panelW)) return true;

            int panelX = (width - panelW) / 2;
            int panelY = 60;
            int panelH = height - 76;
            int bannerH = 50;

            if (isHovered(mouseX, mouseY, panelX + 12, panelY + 14, 52, 22)) {
                playUiSound();
                if (minecraft != null && parent != null) minecraft.setScreen(parent);
                return true;
            }

            int btnY = panelY + 14;
            int rightCur = panelX + panelW - 14;

            int resetW = 68;
            int resetX = rightCur - resetW;
            if (isHovered(mouseX, mouseY, resetX, btnY, resetW, 22)) {
                playUiSound();
                module.resetSettings();
                markModified();
                return true;
            }

            String keyText = listeningForKeybind ? "PRESS KEY..." : "KEY: " + module.getKeybindName();
            int keyW = font.width(keyText) + 16;
            int keyX = resetX - keyW - 6;
            if (isHovered(mouseX, mouseY, keyX, btnY, keyW, 22)) {
                playUiSound();
                listeningForKeybind = !listeningForKeybind;
                return true;
            }

            int toggleW = 54;
            int toggleX = keyX - toggleW - 6;
            if (isHovered(mouseX, mouseY, toggleX, btnY, toggleW, 22)) {
                playUiSound();
                module.toggle();
                markModified();
                return true;
            }

            int listY = panelY + bannerH + 8;
            int listH = panelH - bannerH - 36;
            int startY = listY - scrollOffset;
            int curY = startY;

            for (ValueGroup group : module.settings.groups()) {
                boolean groupHasMatch = false;
                for (Value<?> s : group.getSettings()) {
                    if (matchesSearch(s)) { groupHasMatch = true; break; }
                }
                if (!groupHasMatch) continue;

                if (curY + 20 >= listY && curY <= listY + listH - 20) {
                    if (isHovered(mouseX, mouseY, panelX + 6, curY, panelW - 12, 20)) {
                        playUiSound();
                        if (collapsedGroups.contains(group.name)) {
                            collapsedGroups.remove(group.name);
                        } else {
                            collapsedGroups.add(group.name);
                        }
                        return true;
                    }
                }
                curY += 24;

                if (collapsedGroups.contains(group.name)) continue;

                for (Value<?> setting : group.getSettings()) {
                    if (!setting.isVisible()) continue;
                    if (!matchesSearch(setting)) continue;

                    int rowH = 30;
                    int rX = panelX + panelW - 28;

                    if (isHovered(mouseX, mouseY, rX, curY + 4, 18, 18)) {
                        playUiSound();
                        setting.reset();
                        markModified();
                        return true;
                    }

                    int ctrlRight = rX - 6;
                    if (handleSettingClick(setting, ctrlRight, curY + 4, mouseX, mouseY)) {
                        playUiSound();
                        markModified();
                        return true;
                    }

                    curY += rowH + 3;
                }
                curY += 6;
            }
        }
        return super.mouseClicked(event, isFocused);
    }

    private boolean handleSettingClick(Value<?> setting, int rightX, int y, double mouseX, double mouseY) {
        if (setting instanceof FlagValue bs) {
            int w = 46;
            int x = rightX - w;
            if (isHovered(mouseX, mouseY, x, y, w, 18)) {
                bs.set(!bs.get());
                return true;
            }
        } else if (setting instanceof IntValue is) {
            String valStr = String.valueOf(is.get());
            int boxW = Math.max(34, font.width(valStr) + 10);
            int minusX = rightX - boxW - 42;
            int plusX = rightX - 18;

            if (isHovered(mouseX, mouseY, minusX, y, 18, 18)) {
                is.set(Math.max(is.min, is.get() - 1));
                return true;
            }
            if (isHovered(mouseX, mouseY, plusX, y, 18, 18)) {
                is.set(Math.min(is.max, is.get() + 1));
                return true;
            }
        } else if (setting instanceof DoubleValue ds) {
            String valStr = String.format("%.1f", ds.get());
            int boxW = Math.max(36, font.width(valStr) + 10);
            int minusX = rightX - boxW - 42;
            int plusX = rightX - 18;

            if (isHovered(mouseX, mouseY, minusX, y, 18, 18)) {
                ds.set(Math.max(ds.min, ds.get() - 0.5));
                return true;
            }
            if (isHovered(mouseX, mouseY, plusX, y, 18, 18)) {
                ds.set(Math.min(ds.max, ds.get() + 0.5));
                return true;
            }
        } else if (setting instanceof ChoiceValue<?> es) {
            String valStr = es.get().toString();
            int w = font.width(valStr) + 14;
            int x = rightX - w;
            if (isHovered(mouseX, mouseY, x, y, w, 18)) {
                es.next();
                return true;
            }
        } else if (setting instanceof TintValue cs) {
            int w = 58;
            int x = rightX - w;
            if (isHovered(mouseX, mouseY, x, y, w, 18)) {
                activeColorSetting = cs;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        scrollOffset = Math.max(0, scrollOffset - (int) (verticalAmount * 24));
        return true;
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (listeningForKeybind) {
            int key = event.key();
            if (key == GLFW.GLFW_KEY_ESCAPE || key == GLFW.GLFW_KEY_BACKSPACE) {
                module.setKeybind(-1);
            } else {
                module.setKeybind(key);
            }
            listeningForKeybind = false;
            markModified();
            playUiSound();
            return true;
        }

        return super.keyPressed(event);
    }
}