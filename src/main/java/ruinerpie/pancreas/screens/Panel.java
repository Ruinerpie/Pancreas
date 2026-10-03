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

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.lwjgl.glfw.GLFW;

public abstract class Panel extends Screen {
    protected final Screen parent;
    protected final long openTimestamp;
    protected static final long BASE_ANIMATION_DURATION_MS = 150; 
    protected long searchShakeTime = 0;

    protected String searchQuery = "";
    protected boolean searchFocused = false;
    protected int selectedResultIndex = 0;

    public enum NavTab {
        CHEATS("Cheats", Group.Cheats),
        EXTRAS("Extras", Group.Extras),
        MISC("Misc", Group.Misc),
        UTILITIES("Utilities", Group.Utilities),
        HUD("HUD", null),
        SYSTEMS("Systems", null),
        SETTINGS("Values", null),
        GUI("GUI", null);

        public final String label;
        public final Group category;
        public final ItemStack iconStack = null;

        NavTab(String label, Group category) {
            this.label = label;
            this.category = category;
        }
    }

    protected final NavTab currentTab;

    protected Panel(Component title, Screen parent, NavTab currentTab) {
        super(title);
        this.parent = parent;
        this.currentTab = currentTab;
        this.openTimestamp = System.currentTimeMillis();
    }

    protected Panel(Component title, Screen parent) {
        this(title, parent, null);
    }

    protected Panel(Component title) {
        this(title, null, null);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    protected float getAnimationProgress() {
        Prefs cfg = Prefs.get();
        if (cfg.accessibility.reduceMotion || !cfg.motion.enabled) return 1.0f;

        long duration = (long) (BASE_ANIMATION_DURATION_MS / Math.max(0.2f, cfg.motion.speed));
        long elapsed = System.currentTimeMillis() - openTimestamp;
        float linear = Math.min(1.0f, (float) elapsed / duration);
        return 1.0f - (1.0f - linear) * (1.0f - linear);
    }

    protected int getSlideOffset(int maxOffset) {
        return (int) (maxOffset * (1.0f - getAnimationProgress()));
    }

    protected void playUiSound() {
        if (Prefs.get().motion.soundEnabled && minecraft != null) {
            minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        }
    }

    public static boolean hasShiftDown() {
        if (Minecraft.getInstance() == null || Minecraft.getInstance().getWindow() == null) return false;
        long h = Minecraft.getInstance().getWindow().handle();
        return GLFW.glfwGetKey(h, GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS
            || GLFW.glfwGetKey(h, GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS;
    }

    public static boolean hasControlDown() {
        if (Minecraft.getInstance() == null || Minecraft.getInstance().getWindow() == null) return false;
        long h = Minecraft.getInstance().getWindow().handle();
        return GLFW.glfwGetKey(h, GLFW.GLFW_KEY_LEFT_CONTROL) == GLFW.GLFW_PRESS
            || GLFW.glfwGetKey(h, GLFW.GLFW_KEY_RIGHT_CONTROL) == GLFW.GLFW_PRESS;
    }

    protected void renderBackground(GuiGraphicsExtractor graphics) {
        float progress = getAnimationProgress();
        int alpha = (int) (Prefs.get().theme.backgroundOpacity * progress);
        int packedBg = (alpha << 24) | (Theme.background.getPacked() & 0x00FFFFFF);
        graphics.fill(0, 0, width, height, packedBg);
    }

    public static void renderBeveledPanel(GuiGraphicsExtractor graphics, int x, int y, int w, int h, boolean inset, int fillColor, int focusRingColor) {
        int t = Prefs.get().theme.borderThickness;
        int shadow = Theme.border.getPacked();
        int light = Theme.borderLight.getPacked();

        int tl = inset ? shadow : light;
        int br = inset ? light : shadow;

        if (focusRingColor != 0) {
            graphics.outline(x - 1, y - 1, w + 2, h + 2, focusRingColor);
        }

        graphics.fill(x, y, x + w, y + t, tl);
        graphics.fill(x, y, x + t, y + h, tl);
        graphics.fill(x, y + h - t, x + w, y + h, br);
        graphics.fill(x + w - t, y, x + w, y + h, br);
        graphics.fill(x + t, y + t, x + w - t, y + h - t, fillColor);
    }

    protected void renderSlotButton(GuiGraphicsExtractor graphics, int x, int y, int w, int h, String text, boolean hovered, boolean pressed, int accentColor) {
        int fill = pressed ? Theme.surface.getPacked() : (hovered ? Theme.surfaceRaised.getPacked() : Theme.surfaceAlt.getPacked());
        int focus = hovered ? (accentColor != 0 ? accentColor : Theme.focusRing.getPacked()) : 0;

        renderBeveledPanel(graphics, x, y, w, h, pressed, fill, focus);

        int textColor = hovered ? Theme.textAccent.getPacked() : Theme.text.getPacked();
        int textW = font.width(text);
        int textX = x + (w - textW) / 2;
        int textY = y + (h - 8) / 2 + (pressed ? 1 : 0);
        graphics.text(font, text, textX, textY, textColor, false);
    }

    protected void renderToggleSlot(GuiGraphicsExtractor graphics, int x, int y, int w, int h, boolean active, boolean hovered) {
        int fill = active ? Theme.surfaceRaised.getPacked() : Theme.surfaceAlt.getPacked();
        int focus = hovered ? (active ? Theme.moduleOn.getPacked() : Theme.focusRing.getPacked()) : 0;

        renderBeveledPanel(graphics, x, y, w, h, active, fill, focus);

        int indicatorColor = active ? Theme.moduleOn.getPacked() : Theme.moduleOff.getPacked();
        graphics.fill(x + 4, y + 4, x + h - 4, y + h - 4, indicatorColor);

        String label = active ? "ON" : "OFF";
        int labelColor = active ? Theme.textAccent.getPacked() : Theme.textDisabled.getPacked();
        int textX = x + h + 2;
        int textY = y + (h - 8) / 2;
        graphics.text(font, label, textX, textY, labelColor, false);
    }

    protected void renderTabs(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        NavTab[] tabs = NavTab.values();
        int availableW = width - 40;
        int tabW = Math.max(68, availableW / tabs.length);
        int tabH = 26;
        int totalW = tabs.length * tabW;
        int startX = (width - totalW) / 2;
        int tabY = 8;

        boolean showIcons = Prefs.get().layout.showIcons;

        for (int i = 0; i < tabs.length; i++) {
            NavTab tab = tabs[i];
            int x = startX + i * tabW;
            boolean isSelected = tab == currentTab;
            boolean isHovered = isHovered(mouseX, mouseY, x, tabY, tabW, tabH);

            int fill = isSelected ? Theme.surface.getPacked() : (isHovered ? Theme.surfaceAlt.getPacked() : Theme.surfaceRaised.getPacked());
            int accent = tab.category != null ? Theme.getCategoryColor(tab.category).getPacked() : Theme.accentPurple.getPacked();
            int focus = isSelected ? accent : (isHovered ? Theme.focusRing.getPacked() : 0);

            renderBeveledPanel(graphics, x, tabY, tabW, tabH, false, fill, focus);

            if (isSelected) {
                graphics.fill(x + 2, tabY + tabH - 3, x + tabW - 2, tabY + tabH - 1, accent);
            }

            int contentX = x + 6;
            if (showIcons && tab.iconStack != null) {
                graphics.fakeItem(tab.iconStack, contentX - 1, tabY + 5);
                contentX += 18;
            }

            int textColor = isSelected ? Theme.textAccent.getPacked() : (isHovered ? Theme.text.getPacked() : Theme.textMuted.getPacked());
            graphics.text(font, tab.label, contentX, tabY + 9, textColor, false);
        }
    }

    protected void renderStatusBar(GuiGraphicsExtractor graphics) {
        int barH = 14;
        int barY = height - barH;
        graphics.fill(0, barY, width, height, Theme.surface.getPacked());
        graphics.fill(0, barY, width, barY + 1, Theme.border.getPacked());

        String screenName = (title != null) ? title.getString() : "Pancreas";
        graphics.text(font, screenName, 8, barY + 3, Theme.textMuted.getPacked(), false);

        long tweaksOn = ruinerpie.pancreas.Features.get().all().stream().filter(ruinerpie.pancreas.Feature::isActive).count();
        long hudOn = ruinerpie.pancreas.hud.Rig.get().all().stream().filter(ruinerpie.pancreas.hud.Part::isVisible).count();
        String centerStr = tweaksOn + " tweaks on | " + hudOn + " HUD elements";
        graphics.centeredText(font, centerStr, width / 2, barY + 3, Theme.textMuted.getPacked());

        int fps = minecraft != null ? minecraft.getFps() : 0;
        String rightStr = fps + " FPS | v" + ruinerpie.pancreas.Pancreas.VERSION;
        int rW = font.width(rightStr);
        graphics.text(font, rightStr, width - rW - 8, barY + 3, Theme.textMuted.getPacked(), false);
    }

    public static void drawHighlightedText(GuiGraphicsExtractor graphics, net.minecraft.client.gui.Font font, String fullText, String query, int x, int y, int defaultColor, int highlightColor) {
        if (query == null || query.isEmpty()) {
            graphics.text(font, fullText, x, y, defaultColor, false);
            return;
        }
        int idx = fullText.toLowerCase().indexOf(query.toLowerCase());
        if (idx == -1) {
            graphics.text(font, fullText, x, y, defaultColor, false);
            return;
        }
        String before = fullText.substring(0, idx);
        String match = fullText.substring(idx, idx + query.length());
        String after = fullText.substring(idx + query.length());

        int curX = x;
        if (!before.isEmpty()) {
            graphics.text(font, before, curX, y, defaultColor, false);
            curX += font.width(before);
        }
        graphics.text(font, match, curX, y, highlightColor, false);
        curX += font.width(match);
        if (!after.isEmpty()) {
            graphics.text(font, after, curX, y, defaultColor, false);
        }
    }

    public void triggerSearchShake() {
        this.searchShakeTime = System.currentTimeMillis();
        playUiSound();
    }

    protected void renderSearchBox(GuiGraphicsExtractor graphics, int mouseX, int mouseY, int boxW) {
        renderSearchBox(graphics, mouseX, mouseY, boxW, 36);
    }

    protected void renderSearchBox(GuiGraphicsExtractor graphics, int mouseX, int mouseY, int boxW, int searchY) {
        int searchW = boxW;
        int searchX = (width - searchW) / 2;
        int searchH = 20;

        if (searchShakeTime > 0) {
            long elapsed = System.currentTimeMillis() - searchShakeTime;
            if (elapsed < 200) {
                int shakeOffset = (int) (Math.sin(elapsed * 0.08) * 5 * (1.0f - elapsed / 200f));
                searchX += shakeOffset;
            } else {
                searchShakeTime = 0;
            }
        }

        int focusRing = searchFocused ? Theme.focusRing.getPacked() : 0;
        renderBeveledPanel(graphics, searchX, searchY, searchW, searchH, true, Theme.surface.getPacked(), focusRing);

        graphics.text(font, ">", searchX + 6, searchY + 6, Theme.accentMinecraft.getPacked(), false);

        int textX = searchX + 18;
        if (searchQuery.isEmpty()) {
            graphics.text(font, getSearchPlaceholder(), textX, searchY + 6, Theme.textDisabled.getPacked(), false);
        } else {
            String display = searchQuery + (searchFocused && (System.currentTimeMillis() / 450 % 2 == 0) ? "_" : "");
            graphics.text(font, display, textX, searchY + 6, Theme.textAccent.getPacked(), false);

            int xBtnX = searchX + searchW - 18;
            int xBtnY = searchY + 3;
            boolean xHover = isHovered(mouseX, mouseY, xBtnX, xBtnY, 14, 14);
            renderBeveledPanel(graphics, xBtnX, xBtnY, 14, 14, false, xHover ? Theme.surfaceRaised.getPacked() : Theme.surfaceAlt.getPacked(), xHover ? Theme.accentRed.getPacked() : 0);
            graphics.centeredText(font, "x", xBtnX + 7, xBtnY + 3, xHover ? Theme.accentRed.getPacked() : Theme.textMuted.getPacked());
        }
    }

    protected String getSearchPlaceholder() {
        return "Search...";
    }

    protected void onSearchQueryChanged(String query) {}

    protected void onSearchEnterPressed() {}

    protected boolean handleSearchClick(double mouseX, double mouseY, int boxW) {
        int searchW = boxW;
        int searchX = (width - searchW) / 2;
        int searchY = 36;
        int searchH = 20;

        if (!searchQuery.isEmpty()) {
            int xBtnX = searchX + searchW - 18;
            int xBtnY = searchY + 3;
            if (isHovered(mouseX, mouseY, xBtnX, xBtnY, 14, 14)) {
                searchQuery = "";
                selectedResultIndex = 0;
                onSearchQueryChanged("");
                playUiSound();
                return true;
            }
        }

        if (isHovered(mouseX, mouseY, searchX, searchY, searchW, searchH)) {
            searchFocused = true;
            playUiSound();
            return true;
        } else {
            searchFocused = false;
        }
        return false;
    }

    protected boolean handleTabClicks(double mouseX, double mouseY) {
        NavTab[] tabs = NavTab.values();
        int tabW = 68;
        int tabH = 26;
        int totalW = tabs.length * tabW;
        int startX = (width - totalW) / 2;
        int tabY = 8;

        for (int i = 0; i < tabs.length; i++) {
            NavTab tab = tabs[i];
            int x = startX + i * tabW;
            if (isHovered(mouseX, mouseY, x, tabY, tabW, tabH)) {
                if (tab == currentTab) return true;

                playUiSound();
                switch (tab) {
                    case CHEATS, EXTRAS, MISC, UTILITIES -> {
                        if (minecraft != null) minecraft.setScreen(new TweakIndex(tab.category, this));
                    }
                    case HUD -> {
                        if (minecraft != null) {
                            minecraft.setScreen(new ruinerpie.pancreas.hud.Studio(this));
                        }
                    }
                    case SYSTEMS -> {
                        if (minecraft != null) {
                            minecraft.setScreen(new ruinerpie.pancreas.screens.SystemHub(this));
                        }
                    }
                    case SETTINGS -> {
                        if (minecraft != null) minecraft.setScreen(new GlobalPrefs(this));
                    }
                    case GUI -> {
                        if (minecraft != null) minecraft.setScreen(new StylePrefs(this));
                    }
                }
                return true;
            }
        }
        return false;
    }

    public static boolean isHovered(double mouseX, double mouseY, int x, int y, int w, int h) {
        return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
    }

    @Override
    public boolean charTyped(CharacterEvent event) {
        if (searchFocused && event.isAllowedChatCharacter()) {
            searchQuery += event.codepointAsString();
            selectedResultIndex = 0;
            onSearchQueryChanged(searchQuery);
            return true;
        }
        return super.charTyped(event);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        int key = event.key();

        if (searchFocused) {
            if (key == GLFW.GLFW_KEY_BACKSPACE) {
                if (!searchQuery.isEmpty()) {
                    searchQuery = searchQuery.substring(0, searchQuery.length() - 1);
                    selectedResultIndex = 0;
                    onSearchQueryChanged(searchQuery);
                }
                return true;
            }
            if (key == GLFW.GLFW_KEY_TAB) {
                searchFocused = false;
                return true;
            }
            if (key == GLFW.GLFW_KEY_ENTER || key == GLFW.GLFW_KEY_KP_ENTER) {
                onSearchEnterPressed();
                return true;
            }
            if (key == GLFW.GLFW_KEY_DOWN) {
                selectedResultIndex++;
                return true;
            }
            if (key == GLFW.GLFW_KEY_UP) {
                selectedResultIndex = Math.max(0, selectedResultIndex - 1);
                return true;
            }
        }

        if (key == GLFW.GLFW_KEY_ESCAPE || key == ruinerpie.pancreas.Keys.get().getGuiKey()) {
            if (searchFocused && !searchQuery.isEmpty()) {
                searchQuery = "";
                selectedResultIndex = 0;
                onSearchQueryChanged("");
                return true;
            }
            if (minecraft != null) {
                minecraft.setScreen(null);
            }
            return true;
        }

        return super.keyPressed(event);
    }
}