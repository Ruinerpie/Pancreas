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

public class StylePrefs extends Panel {
    private final String initialPreset;
    private final String initialAccent;
    private final int initialOpacity;
    private final String initialDensity;
    private final float initialFontScale;
    private final boolean initialMotion;
    private final float initialMotionSpeed;
    private final boolean initialSound;
    private final boolean initialIcons;

    private int scrollOffset = 0;
    private String statusMsg = "";
    private long statusTime = 0;

    public StylePrefs(Screen parent) {
        super(Component.literal("Pancreas — GUI Customization"), parent, NavTab.GUI);

        Prefs cfg = Prefs.get();
        this.initialPreset = cfg.theme.preset;
        this.initialAccent = cfg.theme.accent;
        this.initialOpacity = cfg.theme.backgroundOpacity;
        this.initialDensity = cfg.layout.density;
        this.initialFontScale = cfg.typography.scale;
        this.initialMotion = cfg.motion.enabled;
        this.initialMotionSpeed = cfg.motion.speed;
        this.initialSound = cfg.motion.soundEnabled;
        this.initialIcons = cfg.layout.showIcons;
    }

    @Override
    protected String getSearchPlaceholder() {
        return "Search GUI preferences...";
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
        graphics.text(font, "GUI CUSTOMIZATION", panelX + 14, panelY + 13, Theme.textAccent.getPacked(), false);
        graphics.text(font, "Configure theme presets, palette, density, and motion with live preview", panelX + 130, panelY + 13, Theme.textMuted.getPacked(), false);

        int listY = panelY + headerH + 8;
        int listH = panelH - headerH - 42;
        int startY = listY - scrollOffset;
        int curY = startY;

        Prefs cfg = Prefs.get();
        String q = searchQuery.toLowerCase();

        if (q.isEmpty() || "theme preset stone deepslate forest nether end custom".contains(q)) {
            if (curY + 36 >= listY && curY <= listY + listH) {
                renderBeveledPanel(graphics, panelX + 8, curY, panelW - 16, 36, true, Theme.surfaceAlt.getPacked(), 0);
                graphics.text(font, "Theme Preset", panelX + 16, curY + 14, Theme.textAccent.getPacked(), false);

                String[] presets = {"stone", "deepslate", "forest", "nether", "end", "custom"};
                int btnX = panelX + 105;
                for (String p : presets) {
                    boolean sel = p.equalsIgnoreCase(cfg.theme.preset);
                    boolean hov = isHovered(mouseX, mouseY, btnX, curY + 8, 54, 20);
                    renderSlotButton(graphics, btnX, curY + 8, 54, 20, p.toUpperCase(), hov, sel, sel ? Theme.focusRing.getPacked() : 0);
                    btnX += 58;
                }
            }
            curY += 40;
        }

        if (q.isEmpty() || "accent color red purple green gold diamond lapis palette".contains(q)) {
            if (curY + 36 >= listY && curY <= listY + listH) {
                renderBeveledPanel(graphics, panelX + 8, curY, panelW - 16, 36, true, Theme.surfaceAlt.getPacked(), 0);
                graphics.text(font, "Accent Color", panelX + 16, curY + 14, Theme.textAccent.getPacked(), false);

                String[][] accents = {
                    {"#E53935", "Red"},
                    {"#6500FF", "Purple"},
                    {"#5D9B3C", "Green"},
                    {"#F2B233", "Gold"},
                    {"#4AEDD9", "Diamond"},
                    {"#3B6FCC", "Lapis"}
                };
                int aX = panelX + 105;
                for (String[] acc : accents) {
                    boolean sel = acc[0].equalsIgnoreCase(cfg.theme.accent);
                    boolean hov = isHovered(mouseX, mouseY, aX, curY + 8, 54, 20);
                    int parsed = Prefs.parseHexColor(acc[0]);
                    renderSlotButton(graphics, aX, curY + 8, 54, 20, acc[1], hov, sel, parsed);
                    aX += 58;
                }
            }
            curY += 40;
        }

        if (q.isEmpty() || "background opacity transparency blur".contains(q)) {
            if (curY + 32 >= listY && curY <= listY + listH) {
                renderBeveledPanel(graphics, panelX + 8, curY, panelW - 16, 32, true, Theme.surfaceAlt.getPacked(), 0);
                graphics.text(font, "Background Opacity", panelX + 16, curY + 12, Theme.textAccent.getPacked(), false);

                int minusX = panelX + panelW - 120;
                boolean minHov = isHovered(mouseX, mouseY, minusX, curY + 6, 20, 20);
                renderSlotButton(graphics, minusX, curY + 6, 20, 20, "-", minHov, false, 0);

                renderBeveledPanel(graphics, minusX + 24, curY + 6, 46, 20, true, Theme.surface.getPacked(), 0);
                graphics.centeredText(font, String.valueOf(cfg.theme.backgroundOpacity), minusX + 47, curY + 12, Theme.text.getPacked());

                int plusX = minusX + 74;
                boolean plusHov = isHovered(mouseX, mouseY, plusX, curY + 6, 20, 20);
                renderSlotButton(graphics, plusX, curY + 6, 20, 20, "+", plusHov, false, 0);
            }
            curY += 36;
        }

        if (q.isEmpty() || "density comfortable compact layout rows".contains(q)) {
            if (curY + 32 >= listY && curY <= listY + listH) {
                renderBeveledPanel(graphics, panelX + 8, curY, panelW - 16, 32, true, Theme.surfaceAlt.getPacked(), 0);
                graphics.text(font, "Density Layout", panelX + 16, curY + 12, Theme.textAccent.getPacked(), false);

                int dX = panelX + panelW - 170;
                boolean comfSel = "comfortable".equalsIgnoreCase(cfg.layout.density);
                boolean comfHov = isHovered(mouseX, mouseY, dX, curY + 6, 74, 20);
                renderSlotButton(graphics, dX, curY + 6, 74, 20, "COMFORT", comfHov, comfSel, comfSel ? Theme.focusRing.getPacked() : 0);

                boolean compSel = "compact".equalsIgnoreCase(cfg.layout.density);
                boolean compHov = isHovered(mouseX, mouseY, dX + 78, curY + 6, 68, 20);
                renderSlotButton(graphics, dX + 78, curY + 6, 68, 20, "COMPACT", compHov, compSel, compSel ? Theme.focusRing.getPacked() : 0);
            }
            curY += 36;
        }

        if (q.isEmpty() || "font scale typography size text".contains(q)) {
            if (curY + 32 >= listY && curY <= listY + listH) {
                renderBeveledPanel(graphics, panelX + 8, curY, panelW - 16, 32, true, Theme.surfaceAlt.getPacked(), 0);
                graphics.text(font, "Font Scale", panelX + 16, curY + 12, Theme.textAccent.getPacked(), false);

                int minusX = panelX + panelW - 120;
                boolean minHov = isHovered(mouseX, mouseY, minusX, curY + 6, 20, 20);
                renderSlotButton(graphics, minusX, curY + 6, 20, 20, "-", minHov, false, 0);

                renderBeveledPanel(graphics, minusX + 24, curY + 6, 46, 20, true, Theme.surface.getPacked(), 0);
                graphics.centeredText(font, String.format("%.2f", cfg.typography.scale), minusX + 47, curY + 12, Theme.text.getPacked());

                int plusX = minusX + 74;
                boolean plusHov = isHovered(mouseX, mouseY, plusX, curY + 6, 20, 20);
                renderSlotButton(graphics, plusX, curY + 6, 20, 20, "+", plusHov, false, 0);
            }
            curY += 36;
        }

        if (q.isEmpty() || "motion animation speed speed transitions".contains(q)) {
            if (curY + 32 >= listY && curY <= listY + listH) {
                renderBeveledPanel(graphics, panelX + 8, curY, panelW - 16, 32, true, Theme.surfaceAlt.getPacked(), 0);
                graphics.text(font, "Motion (Speed: " + String.format("%.2f", cfg.motion.speed) + "x)", panelX + 16, curY + 12, Theme.textAccent.getPacked(), false);

                int motX = panelX + panelW - 120;
                boolean motHov = isHovered(mouseX, mouseY, motX, curY + 6, 44, 20);
                renderSlotButton(graphics, motX, curY + 6, 44, 20, cfg.motion.enabled ? "ON" : "OFF", motHov, cfg.motion.enabled, cfg.motion.enabled ? Theme.moduleOn.getPacked() : 0);

                int speedX = motX + 48;
                boolean spdHov = isHovered(mouseX, mouseY, speedX, curY + 6, 46, 20);
                renderSlotButton(graphics, speedX, curY + 6, 46, 20, "+SPD", spdHov, false, 0);
            }
            curY += 36;
        }

        if (q.isEmpty() || "sound feedback audio click".contains(q)) {
            if (curY + 32 >= listY && curY <= listY + listH) {
                renderBeveledPanel(graphics, panelX + 8, curY, panelW - 16, 32, true, Theme.surfaceAlt.getPacked(), 0);
                graphics.text(font, "Interface Sound Feedback", panelX + 16, curY + 12, Theme.textAccent.getPacked(), false);

                int sndX = panelX + panelW - 68;
                boolean sndHov = isHovered(mouseX, mouseY, sndX, curY + 6, 44, 20);
                renderSlotButton(graphics, sndX, curY + 6, 44, 20, cfg.motion.soundEnabled ? "ON" : "OFF", sndHov, cfg.motion.soundEnabled, cfg.motion.soundEnabled ? Theme.moduleOn.getPacked() : 0);
            }
            curY += 36;
        }

        if (q.isEmpty() || "icons vanilla items display textures".contains(q)) {
            if (curY + 32 >= listY && curY <= listY + listH) {
                renderBeveledPanel(graphics, panelX + 8, curY, panelW - 16, 32, true, Theme.surfaceAlt.getPacked(), 0);
                graphics.text(font, "Vanilla Item Icons", panelX + 16, curY + 12, Theme.textAccent.getPacked(), false);

                int icnX = panelX + panelW - 68;
                boolean icnHov = isHovered(mouseX, mouseY, icnX, curY + 6, 44, 20);
                renderSlotButton(graphics, icnX, curY + 6, 44, 20, cfg.layout.showIcons ? "ON" : "OFF", icnHov, cfg.layout.showIcons, cfg.layout.showIcons ? Theme.moduleOn.getPacked() : 0);
            }
            curY += 36;
        }

        if (q.isEmpty() || "accessibility reduce motion high contrast".contains(q)) {
            if (curY + 32 >= listY && curY <= listY + listH) {
                renderBeveledPanel(graphics, panelX + 8, curY, panelW - 16, 32, true, Theme.surfaceAlt.getPacked(), 0);
                graphics.text(font, "Accessibility: Reduce Motion", panelX + 16, curY + 12, Theme.textAccent.getPacked(), false);

                int rmX = panelX + panelW - 68;
                boolean rmHov = isHovered(mouseX, mouseY, rmX, curY + 6, 44, 20);
                renderSlotButton(graphics, rmX, curY + 6, 44, 20, cfg.accessibility.reduceMotion ? "ON" : "OFF", rmHov, cfg.accessibility.reduceMotion, cfg.accessibility.reduceMotion ? Theme.moduleOn.getPacked() : 0);
            }
            curY += 36;

            if (curY + 32 >= listY && curY <= listY + listH) {
                renderBeveledPanel(graphics, panelX + 8, curY, panelW - 16, 32, true, Theme.surfaceAlt.getPacked(), 0);
                graphics.text(font, "Accessibility: High Contrast", panelX + 16, curY + 12, Theme.textAccent.getPacked(), false);

                int hcX = panelX + panelW - 68;
                boolean hcHov = isHovered(mouseX, mouseY, hcX, curY + 6, 44, 20);
                renderSlotButton(graphics, hcX, curY + 6, 44, 20, cfg.accessibility.highContrast ? "ON" : "OFF", hcHov, cfg.accessibility.highContrast, cfg.accessibility.highContrast ? Theme.moduleOn.getPacked() : 0);
            }
            curY += 36;
        }

        if (q.isEmpty() || "hud scale opacity debug grid".contains(q)) {
            
            if (curY + 32 >= listY && curY <= listY + listH) {
                renderBeveledPanel(graphics, panelX + 8, curY, panelW - 16, 32, true, Theme.surfaceAlt.getPacked(), 0);
                graphics.text(font, "HUD Global Scale", panelX + 16, curY + 12, Theme.textAccent.getPacked(), false);

                int minusX = panelX + panelW - 120;
                boolean minHov = isHovered(mouseX, mouseY, minusX, curY + 6, 20, 20);
                renderSlotButton(graphics, minusX, curY + 6, 20, 20, "-", minHov, false, 0);

                renderBeveledPanel(graphics, minusX + 24, curY + 6, 46, 20, true, Theme.surface.getPacked(), 0);
                graphics.centeredText(font, String.format("%.1fx", cfg.hud.globalScale), minusX + 47, curY + 12, Theme.text.getPacked());

                int plusX = minusX + 74;
                boolean plusHov = isHovered(mouseX, mouseY, plusX, curY + 6, 20, 20);
                renderSlotButton(graphics, plusX, curY + 6, 20, 20, "+", plusHov, false, 0);
            }
            curY += 36;

            if (curY + 32 >= listY && curY <= listY + listH) {
                renderBeveledPanel(graphics, panelX + 8, curY, panelW - 16, 32, true, Theme.surfaceAlt.getPacked(), 0);
                graphics.text(font, "HUD Background Opacity", panelX + 16, curY + 12, Theme.textAccent.getPacked(), false);

                int minusX = panelX + panelW - 120;
                boolean minHov = isHovered(mouseX, mouseY, minusX, curY + 6, 20, 20);
                renderSlotButton(graphics, minusX, curY + 6, 20, 20, "-", minHov, false, 0);

                renderBeveledPanel(graphics, minusX + 24, curY + 6, 46, 20, true, Theme.surface.getPacked(), 0);
                graphics.centeredText(font, String.valueOf(cfg.hud.bgOpacity), minusX + 47, curY + 12, Theme.text.getPacked());

                int plusX = minusX + 74;
                boolean plusHov = isHovered(mouseX, mouseY, plusX, curY + 6, 20, 20);
                renderSlotButton(graphics, plusX, curY + 6, 20, 20, "+", plusHov, false, 0);
            }
            curY += 36;

            if (curY + 32 >= listY && curY <= listY + listH) {
                renderBeveledPanel(graphics, panelX + 8, curY, panelW - 16, 32, true, Theme.surfaceAlt.getPacked(), 0);
                graphics.text(font, "Hide HUD When F3 Is Open", panelX + 16, curY + 12, Theme.textAccent.getPacked(), false);

                int hdX = panelX + panelW - 68;
                boolean hdHov = isHovered(mouseX, mouseY, hdX, curY + 6, 44, 20);
                renderSlotButton(graphics, hdX, curY + 6, 44, 20, cfg.hud.hideInDebug ? "ON" : "OFF", hdHov, cfg.hud.hideInDebug, cfg.hud.hideInDebug ? Theme.moduleOn.getPacked() : 0);
            }
            curY += 36;

            if (curY + 32 >= listY && curY <= listY + listH) {
                renderBeveledPanel(graphics, panelX + 8, curY, panelW - 16, 32, true, Theme.surfaceAlt.getPacked(), 0);
                graphics.text(font, "Show Editor Grid On Open", panelX + 16, curY + 12, Theme.textAccent.getPacked(), false);

                int grdX = panelX + panelW - 68;
                boolean grdHov = isHovered(mouseX, mouseY, grdX, curY + 6, 44, 20);
                renderSlotButton(graphics, grdX, curY + 6, 44, 20, cfg.hud.showGrid ? "ON" : "OFF", grdHov, cfg.hud.showGrid, cfg.hud.showGrid ? Theme.moduleOn.getPacked() : 0);
            }
            curY += 36;
        }

        int botY = panelY + panelH - 32;
        renderBeveledPanel(graphics, panelX + 4, botY, panelW - 8, 28, true, Theme.surfaceAlt.getPacked(), 0);

        if (!statusMsg.isEmpty() && System.currentTimeMillis() - statusTime < 3000) {
            graphics.text(font, statusMsg, panelX + 14, botY + 10, Theme.success.getPacked(), false);
        }

        int btnW = 90;
        int btnH = 20;
        int actionY = botY + 4;

        int resetX = panelX + panelW - btnW * 3 - 22;
        boolean rHover = isHovered(mouseX, mouseY, resetX, actionY, btnW, btnH);
        renderSlotButton(graphics, resetX, actionY, btnW, btnH, "RESET DEFAULTS", rHover, false, Theme.accentGold.getPacked());

        int cancelX = panelX + panelW - btnW * 2 - 14;
        boolean cHover = isHovered(mouseX, mouseY, cancelX, actionY, btnW, btnH);
        renderSlotButton(graphics, cancelX, actionY, btnW, btnH, "CANCEL", cHover, false, 0);

        int applyX = panelX + panelW - btnW - 6;
        boolean aHover = isHovered(mouseX, mouseY, applyX, actionY, btnW, btnH);
        renderSlotButton(graphics, applyX, actionY, btnW, btnH, "APPLY", aHover, false, Theme.success.getPacked());

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

            int panelW = Math.min(width - 32, 680);
            if (handleSearchClick(mouseX, mouseY, panelW)) {
                return true;
            }

            int panelX = (width - panelW) / 2;
            int panelY = 60;
            int panelH = height - 68;
            int headerH = 26;
            int listY = panelY + headerH + 8;
            int startY = listY - scrollOffset;
            int curY = startY;

            Prefs cfg = Prefs.get();
            String q = searchQuery.toLowerCase();

            if (q.isEmpty() || "theme preset stone deepslate forest nether end custom".contains(q)) {
                String[] presets = {"stone", "deepslate", "forest", "nether", "end", "custom"};
                int btnX = panelX + 105;
                for (String p : presets) {
                    if (isHovered(mouseX, mouseY, btnX, curY + 8, 54, 20)) {
                        playUiSound();
                        cfg.theme.preset = p;
                        cfg.apply();
                        return true;
                    }
                    btnX += 58;
                }
                curY += 40;
            }

            if (q.isEmpty() || "accent color red purple green gold diamond lapis palette".contains(q)) {
                String[][] accents = {
                    {"#E53935", "Red"},
                    {"#6500FF", "Purple"},
                    {"#5D9B3C", "Green"},
                    {"#F2B233", "Gold"},
                    {"#4AEDD9", "Diamond"},
                    {"#3B6FCC", "Lapis"}
                };
                int aX = panelX + 105;
                for (String[] acc : accents) {
                    if (isHovered(mouseX, mouseY, aX, curY + 8, 54, 20)) {
                        playUiSound();
                        cfg.theme.accent = acc[0];
                        cfg.apply();
                        return true;
                    }
                    aX += 58;
                }
                curY += 40;
            }

            if (q.isEmpty() || "background opacity transparency blur".contains(q)) {
                int minusX = panelX + panelW - 120;
                if (isHovered(mouseX, mouseY, minusX, curY + 6, 20, 20)) {
                    playUiSound();
                    cfg.theme.backgroundOpacity = Math.max(50, cfg.theme.backgroundOpacity - 15);
                    cfg.apply();
                    return true;
                }
                int plusX = minusX + 74;
                if (isHovered(mouseX, mouseY, plusX, curY + 6, 20, 20)) {
                    playUiSound();
                    cfg.theme.backgroundOpacity = Math.min(255, cfg.theme.backgroundOpacity + 15);
                    cfg.apply();
                    return true;
                }
                curY += 36;
            }

            if (q.isEmpty() || "density comfortable compact layout rows".contains(q)) {
                int dX = panelX + panelW - 170;
                if (isHovered(mouseX, mouseY, dX, curY + 6, 74, 20)) {
                    playUiSound();
                    cfg.layout.density = "comfortable";
                    return true;
                }
                if (isHovered(mouseX, mouseY, dX + 78, curY + 6, 68, 20)) {
                    playUiSound();
                    cfg.layout.density = "compact";
                    return true;
                }
                curY += 36;
            }

            if (q.isEmpty() || "font scale typography size text".contains(q)) {
                int fMinusX = panelX + panelW - 120;
                if (isHovered(mouseX, mouseY, fMinusX, curY + 6, 20, 20)) {
                    playUiSound();
                    cfg.typography.scale = Math.max(0.8f, (float) Math.round((cfg.typography.scale - 0.05f) * 100) / 100);
                    return true;
                }
                int fPlusX = fMinusX + 74;
                if (isHovered(mouseX, mouseY, fPlusX, curY + 6, 20, 20)) {
                    playUiSound();
                    cfg.typography.scale = Math.min(1.2f, (float) Math.round((cfg.typography.scale + 0.05f) * 100) / 100);
                    return true;
                }
                curY += 36;
            }

            if (q.isEmpty() || "motion animation speed speed transitions".contains(q)) {
                int motX = panelX + panelW - 120;
                if (isHovered(mouseX, mouseY, motX, curY + 6, 44, 20)) {
                    playUiSound();
                    cfg.motion.enabled = !cfg.motion.enabled;
                    return true;
                }
                int speedX = motX + 48;
                if (isHovered(mouseX, mouseY, speedX, curY + 6, 46, 20)) {
                    playUiSound();
                    cfg.motion.speed = cfg.motion.speed >= 2.0f ? 0.5f : cfg.motion.speed + 0.25f;
                    return true;
                }
                curY += 36;
            }

            if (q.isEmpty() || "sound feedback audio click".contains(q)) {
                int sndX = panelX + panelW - 68;
                if (isHovered(mouseX, mouseY, sndX, curY + 6, 44, 20)) {
                    playUiSound();
                    cfg.motion.soundEnabled = !cfg.motion.soundEnabled;
                    return true;
                }
                curY += 36;
            }

            if (q.isEmpty() || "icons vanilla items display textures".contains(q)) {
                int icnX = panelX + panelW - 68;
                if (isHovered(mouseX, mouseY, icnX, curY + 6, 44, 20)) {
                    playUiSound();
                    cfg.layout.showIcons = !cfg.layout.showIcons;
                    return true;
                }
                curY += 36;
            }

            if (q.isEmpty() || "accessibility reduce motion high contrast".contains(q)) {
                int rmX = panelX + panelW - 68;
                if (isHovered(mouseX, mouseY, rmX, curY + 6, 44, 20)) {
                    playUiSound();
                    cfg.accessibility.reduceMotion = !cfg.accessibility.reduceMotion;
                    return true;
                }
                curY += 36;

                int hcX = panelX + panelW - 68;
                if (isHovered(mouseX, mouseY, hcX, curY + 6, 44, 20)) {
                    playUiSound();
                    cfg.accessibility.highContrast = !cfg.accessibility.highContrast;
                    cfg.apply();
                    return true;
                }
                curY += 36;
            }

            if (q.isEmpty() || "hud scale opacity debug grid".contains(q)) {
                
                int minusX = panelX + panelW - 120;
                if (isHovered(mouseX, mouseY, minusX, curY + 6, 20, 20)) {
                    playUiSound();
                    cfg.hud.globalScale = Math.max(0.5f, (float) Math.round((cfg.hud.globalScale - 0.1f) * 10) / 10);
                    return true;
                }
                int plusX = minusX + 74;
                if (isHovered(mouseX, mouseY, plusX, curY + 6, 20, 20)) {
                    playUiSound();
                    cfg.hud.globalScale = Math.min(2.0f, (float) Math.round((cfg.hud.globalScale + 0.1f) * 10) / 10);
                    return true;
                }
                curY += 36;

                if (isHovered(mouseX, mouseY, minusX, curY + 6, 20, 20)) {
                    playUiSound();
                    cfg.hud.bgOpacity = Math.max(0, cfg.hud.bgOpacity - 15);
                    return true;
                }
                if (isHovered(mouseX, mouseY, plusX, curY + 6, 20, 20)) {
                    playUiSound();
                    cfg.hud.bgOpacity = Math.min(255, cfg.hud.bgOpacity + 15);
                    return true;
                }
                curY += 36;

                int hdX = panelX + panelW - 68;
                if (isHovered(mouseX, mouseY, hdX, curY + 6, 44, 20)) {
                    playUiSound();
                    cfg.hud.hideInDebug = !cfg.hud.hideInDebug;
                    return true;
                }
                curY += 36;

                int grdX = panelX + panelW - 68;
                if (isHovered(mouseX, mouseY, grdX, curY + 6, 44, 20)) {
                    playUiSound();
                    cfg.hud.showGrid = !cfg.hud.showGrid;
                    return true;
                }
                curY += 36;
            }

            int botY = panelY + panelH - 32;
            int btnW = 90;
            int btnH = 20;
            int actionY = botY + 4;

            int resetX = panelX + panelW - btnW * 3 - 22;
            if (isHovered(mouseX, mouseY, resetX, actionY, btnW, btnH)) {
                playUiSound();
                cfg.resetToDefaults();
                statusMsg = "Restored default settings.";
                statusTime = System.currentTimeMillis();
                return true;
            }

            int cancelX = panelX + panelW - btnW * 2 - 14;
            if (isHovered(mouseX, mouseY, cancelX, actionY, btnW, btnH)) {
                playUiSound();
                cfg.theme.preset = initialPreset;
                cfg.theme.accent = initialAccent;
                cfg.theme.backgroundOpacity = initialOpacity;
                cfg.layout.density = initialDensity;
                cfg.typography.scale = initialFontScale;
                cfg.motion.enabled = initialMotion;
                cfg.motion.speed = initialMotionSpeed;
                cfg.motion.soundEnabled = initialSound;
                cfg.layout.showIcons = initialIcons;
                cfg.apply();

                if (minecraft != null && parent != null) minecraft.setScreen(parent);
                return true;
            }

            int applyX = panelX + panelW - btnW - 6;
            if (isHovered(mouseX, mouseY, applyX, actionY, btnW, btnH)) {
                playUiSound();
                cfg.save();
                statusMsg = "Saved settings to gui.json.";
                statusTime = System.currentTimeMillis();
                return true;
            }
        }
        return super.mouseClicked(event, isFocused);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        scrollOffset = Math.max(0, scrollOffset - (int) (verticalAmount * 24));
        return true;
    }
}