package ruinerpie.pancreas.screens;

import ruinerpie.pancreas.Feature;
import ruinerpie.pancreas.Features;
import ruinerpie.pancreas.Group;
import ruinerpie.pancreas.draw.Color;
import ruinerpie.pancreas.draw.Flat;
import ruinerpie.pancreas.values.*;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.navigation.*;
import net.minecraft.client.gui.components.events.*;
import net.minecraft.client.input.*;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class Home extends Screen {
    public static final Color BG_MAIN = new Color(0, 0, 0, 255);
    public static final Color BG_HEAD = new Color(15, 0, 32, 255);
    public static final Color BORDER_MAIN = new Color(37, 0, 46, 255);
    public static final Color SEARCH_BG = new Color(15, 0, 32, 255);
    public static final Color SEARCH_BORDER = new Color(37, 0, 46, 255);

    public static final Color BORDER_TABS = new Color(37, 0, 46, 255);
    public static final Color BORDER_BUTTONS = new Color(37, 0, 46, 255);


    public static final Color TEXT_SELECTED = new Color(255, 0, 0, 255);
    public static final Color TEXT_HOVERED = new Color(255, 255, 255, 255);
    public static final Color TEXT_NORMAL = new Color(160, 160, 160, 255);
    public static final Color TEXT_OTHER = new Color(194, 144, 144, 255);

    public static final Color BG_TAB_SELECTED = new Color(37, 0, 0, 255);
    public static final Color BG_TAB_HOVERED = new Color(55, 10, 10, 255);
    public static final Color BG_TAB_NORMAL = new Color(37, 0, 0, 255);

    public static final Color BG_ELEMENT_SELECTED = new Color(27, 0, 32, 255);
    public static final Color BG_ELEMENT_HOVERED = new Color(42, 0, 50, 255);
    public static final Color BG_ELEMENT_NORMAL = new Color(27, 0, 32, 255);

    public static final Color BG_BUTTON_SELECTED = new Color(35, 0, 42, 255);
    public static final Color BG_BUTTON_HOVERED = new Color(30, 0, 36, 255);
    public static final Color BG_BUTTON_NORMAL = new Color(27, 0, 32, 255);
    public static final Color BG_BUTTON_OTHER = new Color(15, 0, 32, 255);

    public static final Color PANEL_BG = BG_MAIN;
    public static final Color HEADER_BG = BG_HEAD;
    public static final Color TEXT_PRIMARY = TEXT_NORMAL;
    public static final Color TEXT_MUTED = TEXT_OTHER;
    public static final Color ACCENT_BLUE = TEXT_SELECTED;
    public static final Color TOGGLE_ON = new Color(101, 0, 255, 255);
    public static final Color TOGGLE_OFF = new Color(27, 0, 32, 255);
    public static final Color HOVER_ROW = BG_ELEMENT_HOVERED;
    public static final Color TAB_ACTIVE_BG = BG_TAB_SELECTED;
    public static final Color TAB_INACTIVE_BG = BG_TAB_NORMAL;

    public static double bgTransparency = 30.0;

    public enum FontChoice { Default, Qraftys }
    public enum ColorChoice { Text, Background, Accent, Border }
    public enum TextTarget { Selected, Hovered, Normal, Other }
    public enum TabTarget { Selected, Hovered, Normal }
    public enum ElementTarget { Selected, Hovered, Normal }
    public enum ButtonTarget { Selected, Hovered, Normal, Other }
    public enum BorderTarget { Main, Tabs, Buttons }
    public enum PickerMode { HEX, RGB }

    public static final ChoiceValue<FontChoice> guiFontChoice = new ChoiceValue<>("Font", "", FontChoice.Default, () -> true);
    public static final ChoiceValue<ColorChoice> guiColorChoice = new ChoiceValue<>("Color", "", ColorChoice.Text, () -> true);
    public static final ChoiceValue<TextTarget> guiTextChoice = new ChoiceValue<>("Text Target", "", TextTarget.Selected, () -> true);
    public static final ChoiceValue<TabTarget> guiTabChoice = new ChoiceValue<>("Tab Target", "", TabTarget.Selected, () -> true);
    public static final ChoiceValue<ElementTarget> guiElementChoice = new ChoiceValue<>("Element Target", "", ElementTarget.Selected, () -> true);
    public static final ChoiceValue<ButtonTarget> guiButtonChoice = new ChoiceValue<>("Button Target", "", ButtonTarget.Normal, () -> true);
    public static final ChoiceValue<PickerMode> guiPickerMode = new ChoiceValue<>("Format", "", PickerMode.HEX, () -> true);
    public static final ChoiceValue<BorderTarget> guiBorderChoice = new ChoiceValue<>("Border Target", "", BorderTarget.Main, () -> true);

    public static final List<String> savedFavorites = new ArrayList<>();
    public static final List<String> savedFavoriteSettings = new ArrayList<>();

    public static boolean isColorPickerOpen = false;
    private int pickerWindowX = -1;
    private int pickerWindowY = -1;
    private final int pickerWindowW = 270;
    private final int pickerWindowH = 205;
    private boolean isDraggingPickerWindow = false;
    private int dragOffsetWindowX = 0;
    private int dragOffsetWindowY = 0;

    private Color activePickerColor = null;
    private int originalPickerR = 0;
    private int originalPickerG = 0;
    private int originalPickerB = 0;
    private String activePickerTitle = "";
    private float pickerHue = 0.0f;
    private float pickerSat = 1.0f;
    private float pickerBri = 1.0f;

    private boolean isDraggingSettingsTrans = false;
    private boolean isDraggingHue = false;
    private boolean isDraggingSB = false;

    private boolean buttonsGroupExpanded = false;

    public enum TextField { NONE, HEX, R, G, B }
    private TextField focusedField = TextField.NONE;
    private String hexInput = "";
    private String rInput = "";
    private String gInput = "";
    private String bInput = "";
    private boolean isTextSelected = false;

    private String searchQuery = "";
    private boolean searchFocused = false;
    private int selectedTopTab = 0;
    private int selectedFolderIndex = 3;
    private int selectedFavTab = 0;
    private Feature selectedFeature = null;

    private ChoiceValue<?> openDropdown = null;
    private int openDropdownX = 0;
    private int openDropdownY = 0;
    private int openDropdownW = 84;
    private int openDropdownH = 18;

    private final List<Feature> favoriteFeatures = new ArrayList<>();
    private final Screen parent;

    public Home(Screen parent) {
        super(Component.literal("Pancreas Config"));
        this.parent = parent;
        favoriteFeatures.clear();
        for (String fid : savedFavorites) {
            Feature f = Features.get().get(fid);
            if (f != null && !favoriteFeatures.contains(f)) {
                favoriteFeatures.add(f);
            }
        }
        List<Feature> all = Features.get().all();
        if (!all.isEmpty()) {
            selectedFeature = all.get(0);
        }
    }

    public Home() {
        this(null);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);

        int pad = 8;
        int configWidth = Math.min(this.width - 32, 820);
        int configHeight = Math.min(this.height - 32, 500);
        int startX = (this.width - configWidth) / 2;
        int startY = (this.height - configHeight) / 2;
        int innerW = configWidth - pad * 2;
        int mainX = startX + pad;







        int alpha = (int) ((bgTransparency / 100.0) * 255);
        BG_MAIN.a(alpha);

        Flat.COLOR.fill(graphics, startX, startY, startX + configWidth, startY + configHeight, BG_MAIN);
        Flat.COLOR.outline(graphics, startX, startY, configWidth, configHeight, BORDER_MAIN);

        int topBarH = 28;
        int botH = 92;
        int topY = startY + pad;
        int searchW = (innerW - pad) / 2;
        int tabsW = (innerW - pad) / 2;
        int tabsX = startX + pad + searchW + pad;

        Flat.COLOR.fill(graphics, startX + pad, topY, startX + pad + searchW, topY + topBarH, SEARCH_BG);
        Flat.COLOR.outline(graphics, startX + pad, topY, searchW, topBarH, searchFocused ? TEXT_SELECTED : SEARCH_BORDER);
        
        Font font = this.font;
        String displayText = searchQuery.isEmpty() ? (searchFocused ? "" : "Search tweaks or settings...") : searchQuery;
        
        boolean blink = (System.currentTimeMillis() % 1000) < 500;
        if (searchFocused && isTextSelected) {
            Flat.COLOR.fill(graphics, startX + pad + 8, topY + 8, startX + pad + 8 + Flat.COLOR.width(font, displayText), topY + topBarH - 8, new Color(179, 0, 34, 255));
        } else if (searchFocused && blink) {
            displayText += "|";
        }

        Color textColor = searchQuery.isEmpty() ? TEXT_OTHER : TEXT_NORMAL;
        if (searchFocused && isTextSelected) {
            textColor = new Color(255, 255, 0, 255);
        }
        Flat.COLOR.text(graphics, font, displayText, startX + pad + 8, topY + (topBarH - 8) / 2, textColor, false);

        int searchBtnW = 20;
        int searchBtnX = startX + pad + searchW - searchBtnW;
        boolean searchBtnHov = mouseX >= searchBtnX && mouseX <= searchBtnX + searchBtnW && mouseY >= topY && mouseY <= topY + topBarH;
        String searchBtnIcon = searchQuery.isEmpty() ? "🔍" : "✕";
        Flat.COLOR.text(graphics, font, searchBtnIcon, searchBtnX + (searchBtnW - Flat.COLOR.width(font, searchBtnIcon)) / 2, topY + (topBarH - 8) / 2, searchBtnHov ? TEXT_HOVERED : TEXT_OTHER, false);

        int tabSpacing = 4;
        int singleTabW = (tabsW - tabSpacing * 3) / 4;
        String[] topTabs = {"Home", "Tweaks", "Settings", "GUI"};
        for (int i = 0; i < 4; i++) {
            int tx = tabsX + i * (singleTabW + tabSpacing);
            boolean active = (selectedTopTab == i);
            boolean hovered = (mouseX >= tx && mouseX <= tx + singleTabW && mouseY >= topY && mouseY <= topY + topBarH);
            Color tabBg = active ? BG_TAB_SELECTED : (hovered ? BG_TAB_HOVERED : BG_TAB_NORMAL);
            Color tabTextColor = active ? TEXT_SELECTED : (hovered ? TEXT_HOVERED : TEXT_NORMAL);
            Flat.COLOR.fill(graphics, tx, topY, tx + singleTabW, topY + topBarH, tabBg);
            Flat.COLOR.outline(graphics, tx, topY, singleTabW, topBarH, active ? TEXT_SELECTED : BORDER_TABS);
            Flat.COLOR.text(graphics, font, topTabs[i], tx + (singleTabW - Flat.COLOR.width(font, topTabs[i])) / 2, topY + (topBarH - 8) / 2, tabTextColor, false);
        }

        int botY = startY + configHeight - pad - botH;
        int panelW = (innerW - pad) / 2;

        if (selectedTopTab == 0) {
            int midY = topY + topBarH + pad;
            int midH = botY - midY - pad;
            renderHomeContent(graphics, font, startX, pad, midY, midH, panelW, mouseX, mouseY);
            renderFavoritesPanel(graphics, font, startX, pad, innerW, botY, botH);
        } else if (selectedTopTab == 1) {
            int subTopY = topY + topBarH + pad;
            int subTopH = 24;
            renderTweaksSubTabs(graphics, font, startX, pad, innerW, subTopY, subTopH, mouseX, mouseY);
            int midY = subTopY + subTopH + pad;
            int midH = botY - midY - pad;
            renderTweaksContent(graphics, font, startX, pad, midY, midH, panelW, mouseX, mouseY);
            renderFavoritesPanel(graphics, font, startX, pad, innerW, botY, botH);
        } else if (selectedTopTab == 2) {
            int midY = topY + topBarH + pad;
            int midH = botY - midY - pad;
            renderSettingsContent(graphics, font, startX, pad, innerW, midY, midH, mouseX, mouseY);
            renderFavoritesPanel(graphics, font, startX, pad, innerW, botY, botH);
        } else if (selectedTopTab == 3) {
            int midY = topY + topBarH + pad;
            int midH = (startY + configHeight - pad) - midY;
            renderGuiContent(graphics, font, startX, pad, innerW, midY, midH, mouseX, mouseY);
        }

        if (isColorPickerOpen) {
            renderColorPickerWindow(graphics, font, mouseX, mouseY);
        }

        if (openDropdown != null) {
            renderDropdownOverlay(graphics, font, mouseX, mouseY);
        }
    }

    private void updatePickerColorFromHSB() {
        if (activePickerColor == null) return;
        int rgb = java.awt.Color.HSBtoRGB(pickerHue, pickerSat, pickerBri);
        activePickerColor.r = (rgb >> 16) & 0xFF;
        activePickerColor.g = (rgb >> 8) & 0xFF;
        activePickerColor.b = rgb & 0xFF;
        hexInput = String.format("#%02X%02X%02X", activePickerColor.r, activePickerColor.g, activePickerColor.b);
        rInput = String.valueOf(activePickerColor.r);
        gInput = String.valueOf(activePickerColor.g);
        bInput = String.valueOf(activePickerColor.b);
    }

    private void renderTweaksSubTabs(GuiGraphicsExtractor graphics, Font font, int startX, int pad, int innerW, int subTopY, int subTopH, int mouseX, int mouseY) {
        Group[] groups = Group.values();
        int subTabSpacing = 4;
        int folderW = (innerW - subTabSpacing * 3) / 4;
        for (int i = 0; i < groups.length; i++) {
            int fx = startX + pad + i * (folderW + subTabSpacing);
            boolean active = (selectedFolderIndex == i);
            boolean hasFeatures = !Features.get().in(groups[i]).isEmpty();
            boolean hovered = hasFeatures && (mouseX >= fx && mouseX <= fx + folderW && mouseY >= subTopY && mouseY <= subTopY + subTopH);
            Color tabBg = active ? BG_TAB_SELECTED : (hovered ? BG_TAB_HOVERED : BG_TAB_NORMAL);
            Color tabTextColor = active ? TEXT_SELECTED : (hovered ? TEXT_HOVERED : TEXT_NORMAL);
            Flat.COLOR.fill(graphics, fx, subTopY, fx + folderW, subTopY + subTopH, tabBg);
            Flat.COLOR.outline(graphics, fx, subTopY, folderW, subTopH, active ? TEXT_SELECTED : BORDER_TABS);
            if (hasFeatures) {
                Flat.COLOR.text(graphics, font, groups[i].title, fx + (folderW - Flat.COLOR.width(font, groups[i].title)) / 2, subTopY + (subTopH - 8) / 2, tabTextColor, false);
            }
        }
    }

    private boolean matchesSearch(Feature f, String query) {
        if (query == null || query.isEmpty()) return true;
        String q = query.toLowerCase();
        if (f.name != null && f.name.toLowerCase().contains(q)) return true;
        if (f.id != null && f.id.toLowerCase().contains(q)) return true;
        if (f.description != null && f.description.toLowerCase().contains(q)) return true;
        if (f.category != null && f.category.title != null && f.category.title.toLowerCase().contains(q)) return true;
        for (ruinerpie.pancreas.values.ValueGroup vg : f.settings.groups()) {
            if (vg.getName() != null && vg.getName().toLowerCase().contains(q)) return true;
            for (ruinerpie.pancreas.values.Value<?> v : vg.getSettings()) {
                if (v.name != null && v.name.toLowerCase().contains(q)) return true;
                if (v.description != null && v.description.toLowerCase().contains(q)) return true;
            }
        }
        return false;
    }



    private void renderHomeContent(GuiGraphicsExtractor graphics, Font font, int startX, int pad, int midY, int midH, int panelW, int mouseX, int mouseY) {
        int leftX = startX + pad;
        int rightX = leftX + panelW + pad;

        Flat.COLOR.fill(graphics, rightX, midY, rightX + panelW, midY + midH, BG_MAIN);
        Flat.COLOR.fill(graphics, rightX, midY, rightX + panelW, midY + 22, BG_HEAD);
        Flat.COLOR.outline(graphics, rightX, midY, panelW, midH, BORDER_MAIN);
        String title = searchQuery.isEmpty() ? "Active Tweaks" : "Search Results";
        Flat.COLOR.text(graphics, font, title, rightX + 8, midY + 7, TEXT_SELECTED, false);

        int itemY = midY + 28;
        int rowH = 26;

        java.util.List<Object> results = new java.util.ArrayList<>();
        for (Feature f : Features.get().all()) {
            if (matchesSearch(f, searchQuery)) {
                results.add(f);
            }
        }
        if (!searchQuery.isEmpty()) {
            String q = searchQuery.toLowerCase();
            if ("gui opacity".contains(q) || "global settings".contains(q) || "transparent".contains(q)) results.add("PSEUDO:2:GUI Opacity (Global Settings)");
            if ("interface font".contains(q) || "font".contains(q) || "visual".contains(q) || "gui settings".contains(q)) results.add("PSEUDO:3:Interface Font (GUI Settings)");
            if ("text".contains(q) || "colour".contains(q) || "color".contains(q) || "target".contains(q)) results.add("PSEUDO:3:Text GUI Setting");
            if ("background".contains(q) || "main background".contains(q) || "head background".contains(q)) results.add("PSEUDO:3:Background GUI Setting");
            if ("tab".contains(q) || "tabs".contains(q)) results.add("PSEUDO:3:Tab GUI Setting");
            if ("button".contains(q) || "buttons".contains(q)) results.add("PSEUDO:3:Button GUI Setting");
        }

        for (Object item : results) {
            if (item instanceof Feature) {
                Feature f = (Feature) item;
                boolean isSelected = (selectedFeature == f);
                boolean isHovered = mouseX >= rightX + 4 && mouseX <= rightX + panelW - 4 && mouseY >= itemY && mouseY <= itemY + rowH;
    
                Color elemBg = isSelected ? BG_ELEMENT_SELECTED : (isHovered ? BG_ELEMENT_HOVERED : BG_ELEMENT_NORMAL);
                Color elemTextColor = isSelected ? TEXT_SELECTED : (isHovered ? TEXT_HOVERED : TEXT_NORMAL);
    
                Flat.COLOR.fill(graphics, rightX + 4, itemY, rightX + panelW - 4, itemY + rowH, elemBg);
                if (isSelected) {
                    Flat.COLOR.outline(graphics, rightX + 4, itemY, panelW - 8, rowH, TEXT_SELECTED);
                }
    
                int toggleX = rightX + 8;
                int toggleY = itemY + (rowH - 12) / 2;
                boolean active = f.isActive();
                Flat.COLOR.fill(graphics, toggleX, toggleY, toggleX + 22, toggleY + 12, active ? TOGGLE_ON : TOGGLE_OFF);
                int knobX = active ? toggleX + 12 : toggleX + 2;
                Flat.COLOR.fill(graphics, knobX, toggleY + 2, knobX + 8, toggleY + 10, TEXT_NORMAL);

                Flat.COLOR.text(graphics, font, f.name, toggleX + 28, itemY + (rowH - 8) / 2, elemTextColor, false);
    
                boolean isFav = favoriteFeatures.contains(f);
                String star = isFav ? "★" : "☆";
                Color starColor = isFav ? TEXT_SELECTED : TEXT_OTHER;
                Flat.COLOR.text(graphics, font, star, rightX + panelW - 20, itemY + (rowH - 8) / 2, starColor, false);
            } else if (item instanceof String) {
                String s = (String) item;
                String[] parts = s.split(":", 3);
                String displayStr = parts.length == 3 ? parts[2] : s;
                boolean isHovered = mouseX >= rightX + 4 && mouseX <= rightX + panelW - 4 && mouseY >= itemY && mouseY <= itemY + rowH;
                Color elemBg = isHovered ? BG_ELEMENT_HOVERED : BG_ELEMENT_NORMAL;
                Color elemTextColor = isHovered ? TEXT_HOVERED : TEXT_NORMAL;
                Flat.COLOR.fill(graphics, rightX + 4, itemY, rightX + panelW - 4, itemY + rowH, elemBg);
                Flat.COLOR.text(graphics, font, displayStr, rightX + 12, itemY + (rowH - 8) / 2, elemTextColor, false);
                Flat.COLOR.text(graphics, font, "→", rightX + panelW - 20, itemY + (rowH - 8) / 2, TEXT_OTHER, false);
            }

            itemY += rowH + 4;
        }

        renderTweakSettingsPane(graphics, font, leftX, midY, panelW, midH);
    }

    private void renderTweaksContent(GuiGraphicsExtractor graphics, Font font, int startX, int pad, int midY, int midH, int panelW, int mouseX, int mouseY) {
        int leftX = startX + pad;
        int rightX = leftX + panelW + pad;

        Group currentGroup = Group.values()[Math.min(selectedFolderIndex, Group.values().length - 1)];

        Flat.COLOR.fill(graphics, rightX, midY, rightX + panelW, midY + midH, BG_MAIN);
        Flat.COLOR.fill(graphics, rightX, midY, rightX + panelW, midY + 22, BG_HEAD);
        Flat.COLOR.outline(graphics, rightX, midY, panelW, midH, BORDER_MAIN);
        Flat.COLOR.text(graphics, font, currentGroup.title, rightX + 8, midY + 7, TEXT_SELECTED, false);

        int itemY = midY + 28;
        int rowH = 26;
        List<Feature> categoryFeatures = Features.get().in(currentGroup);
        if (categoryFeatures.isEmpty()) {
            Flat.COLOR.text(graphics, font, "No tweaks in " + currentGroup.title, rightX + 12, itemY + 8, TEXT_OTHER, false);
        } else {
            for (Feature f : categoryFeatures) {
                if (!matchesSearch(f, searchQuery)) {
                    continue;
                }
                boolean isSelected = (selectedFeature == f);
                boolean isHovered = mouseX >= rightX + 4 && mouseX <= rightX + panelW - 4 && mouseY >= itemY && mouseY <= itemY + rowH;

                Color elemBg = isSelected ? BG_ELEMENT_SELECTED : (isHovered ? BG_ELEMENT_HOVERED : BG_ELEMENT_NORMAL);
                Color elemTextColor = isSelected ? TEXT_SELECTED : (isHovered ? TEXT_HOVERED : TEXT_NORMAL);

                Flat.COLOR.fill(graphics, rightX + 4, itemY, rightX + panelW - 4, itemY + rowH, elemBg);
                if (isSelected) {
                    Flat.COLOR.outline(graphics, rightX + 4, itemY, panelW - 8, rowH, TEXT_SELECTED);
                }

                int toggleX = rightX + 8;
                int toggleY = itemY + (rowH - 12) / 2;
                boolean active = f.isActive();
                Flat.COLOR.fill(graphics, toggleX, toggleY, toggleX + 22, toggleY + 12, active ? TOGGLE_ON : TOGGLE_OFF);
                int knobX = active ? toggleX + 12 : toggleX + 2;
                Flat.COLOR.fill(graphics, knobX, toggleY + 2, knobX + 8, toggleY + 10, TEXT_NORMAL);

                Flat.COLOR.text(graphics, font, f.name, toggleX + 28, itemY + (rowH - 8) / 2, elemTextColor, false);

                boolean isFav = favoriteFeatures.contains(f);
                String star = isFav ? "★" : "☆";
                Color starColor = isFav ? TEXT_SELECTED : TEXT_OTHER;
                Flat.COLOR.text(graphics, font, star, rightX + panelW - 20, itemY + (rowH - 8) / 2, starColor, false);

                itemY += rowH + 4;
            }
        }

        renderTweakSettingsPane(graphics, font, leftX, midY, panelW, midH);
    }

    private void renderTweakSettingsPane(GuiGraphicsExtractor graphics, Font font, int leftX, int midY, int leftW, int midH) {
        Flat.COLOR.fill(graphics, leftX, midY, leftX + leftW, midY + midH, BG_MAIN);
        Flat.COLOR.fill(graphics, leftX, midY, leftX + leftW, midY + 22, BG_HEAD);
        Flat.COLOR.outline(graphics, leftX, midY, leftW, midH, BORDER_MAIN);

        String title = (selectedFeature != null) ? selectedFeature.name + " Settings" : "Tweak Settings";
        Flat.COLOR.text(graphics, font, title, leftX + 8, midY + 7, TEXT_SELECTED, false);

        if (selectedFeature != null) {
            boolean active = selectedFeature.isActive();
            int btnW = 58;
            int btnH = 16;
            int btnX = leftX + leftW - btnW - 6;
            int btnY = midY + 3;
            Flat.COLOR.fill(graphics, btnX, btnY, btnX + btnW, btnY + btnH, active ? TOGGLE_ON : TOGGLE_OFF);
            Flat.COLOR.outline(graphics, btnX, btnY, btnW, btnH, active ? TOGGLE_ON : BORDER_BUTTONS);
            String btnText = active ? "ACTIVE" : "INACTIVE";
            Flat.COLOR.text(graphics, font, btnText, btnX + (btnW - Flat.COLOR.width(font, btnText)) / 2, btnY + 4, TEXT_NORMAL, false);

            int settingY = midY + 30;
            Flat.COLOR.text(graphics, font, selectedFeature.description, leftX + 10, settingY, TEXT_OTHER, false);
            settingY += 18;

            int rowH = 26;
            for (ValueGroup group : selectedFeature.settings.groups()) {
                Flat.COLOR.text(graphics, font, "[" + group.getName() + "]", leftX + 10, settingY + 4, TEXT_SELECTED, false);
                settingY += 16;

                for (Value<?> val : group.getSettings()) {
                    if (!val.isVisible()) continue;

                    Flat.COLOR.text(graphics, font, val.getName(), leftX + 14, settingY + (rowH - 8) / 2, TEXT_NORMAL, false);
                    
                    if (val instanceof ChoiceValue<?> choice) {
                        String optStr = val.get().toString();
                        int boxW = 84;
                        int boxH = 18;
                        int boxX = leftX + leftW - boxW - 8;
                        int boxY = settingY + (rowH - boxH) / 2;
                        boolean isOpen = (openDropdown == choice);

                        Flat.COLOR.fill(graphics, boxX, boxY, boxX + boxW, boxY + boxH, SEARCH_BG);
                        Flat.COLOR.outline(graphics, boxX, boxY, boxW, boxH, isOpen ? TEXT_SELECTED : SEARCH_BORDER);
                        Flat.COLOR.text(graphics, font, optStr, boxX + 6, boxY + (boxH - 8) / 2, TEXT_NORMAL, false);
                        Flat.COLOR.text(graphics, font, isOpen ? "▲" : "▼", boxX + boxW - 14, boxY + (boxH - 8) / 2, TEXT_SELECTED, false);

                        if (isOpen) {
                            openDropdownX = boxX;
                            openDropdownY = boxY;
                            openDropdownW = boxW;
                            openDropdownH = boxH;
                        }
                    } else if (val instanceof FlagValue flag) {
                        int boxW = 22;
                        int boxH = 12;
                        int boxX = leftX + leftW - boxW - 12;
                        int boxY = settingY + (rowH - boxH) / 2;
                        boolean flagActive = flag.get();
                        Flat.COLOR.fill(graphics, boxX, boxY, boxX + boxW, boxY + boxH, flagActive ? TOGGLE_ON : TOGGLE_OFF);
                        int knobX = flagActive ? boxX + 12 : boxX + 2;
                        Flat.COLOR.fill(graphics, knobX, boxY + 2, knobX + 8, boxY + 10, TEXT_NORMAL);
                    }
                    settingY += rowH;
                }
            }
        } else {
            Flat.COLOR.text(graphics, font, "Select any tweak to inspect settings", leftX + 12, midY + 40, TEXT_OTHER, false);
        }
    }

    private void renderSettingsContent(GuiGraphicsExtractor graphics, Font font, int startX, int pad, int innerW, int midY, int midH, int mouseX, int mouseY) {
        int mainX = startX + pad;

        Flat.COLOR.fill(graphics, mainX, midY, mainX + innerW, midY + midH, BG_MAIN);
        Flat.COLOR.fill(graphics, mainX, midY, mainX + innerW, midY + 22, BG_HEAD);
        Flat.COLOR.outline(graphics, mainX, midY, innerW, midH, BORDER_MAIN);

        Flat.COLOR.text(graphics, font, "Global Mod Settings", mainX + 10, midY + 7, TEXT_SELECTED, false);
        
        Flat.COLOR.text(graphics, font, "GUI Opacity", mainX + 14, midY + 38, TEXT_NORMAL, false);
        
        boolean isTransFav = savedFavoriteSettings.contains("GUI Opacity") || savedFavoriteSettings.contains("Transparent GUI");
        String star = isTransFav ? "★" : "☆";
        Color starColor = isTransFav ? TEXT_SELECTED : TEXT_OTHER;
        Flat.COLOR.text(graphics, font, star, mainX + innerW - 175, midY + 38, starColor, false);

        Flat.COLOR.text(graphics, font, (int)bgTransparency + "%", mainX + innerW - 150, midY + 38, TEXT_OTHER, false);
        
        int sliderX = mainX + innerW - 120;
        int sliderY = midY + 36;
        int sliderW = 100;
        int sliderH = 12;
        Flat.COLOR.fill(graphics, sliderX, sliderY, sliderX + sliderW, sliderY + sliderH, SEARCH_BG);
        Flat.COLOR.outline(graphics, sliderX, sliderY, sliderW, sliderH, SEARCH_BORDER);
        
        int knobX = sliderX + (int) ((bgTransparency / 100.0) * (sliderW - 8));
        Flat.COLOR.fill(graphics, knobX, sliderY + 1, knobX + 8, sliderY + sliderH - 1, TEXT_SELECTED);

        int btnY = midY + 70;
        int btnW = 110;
        int btnH = 20;

        boolean saveHov = mouseX >= mainX + 14 && mouseX <= mainX + 14 + btnW && mouseY >= btnY && mouseY <= btnY + btnH;
        Flat.COLOR.fill(graphics, mainX + 14, btnY, mainX + 14 + btnW, btnY + btnH, saveHov ? BG_BUTTON_HOVERED : BG_BUTTON_NORMAL);
        Flat.COLOR.outline(graphics, mainX + 14, btnY, btnW, btnH, saveHov ? TEXT_SELECTED : BORDER_BUTTONS);
        String saveText = "SAVE CONFIG";
        Flat.COLOR.text(graphics, font, saveText, mainX + 14 + (btnW - Flat.COLOR.width(font, saveText)) / 2, btnY + 6, saveHov ? TEXT_HOVERED : TEXT_NORMAL, false);

        boolean resetHov = mouseX >= mainX + 134 && mouseX <= mainX + 134 + btnW && mouseY >= btnY && mouseY <= btnY + btnH;
        Flat.COLOR.fill(graphics, mainX + 134, btnY, mainX + 134 + btnW, btnY + btnH, resetHov ? BG_BUTTON_HOVERED : BG_BUTTON_NORMAL);
        Flat.COLOR.outline(graphics, mainX + 134, btnY, btnW, btnH, resetHov ? TEXT_SELECTED : BORDER_BUTTONS);
        String resetText = "RESET ALL";
        Flat.COLOR.text(graphics, font, resetText, mainX + 134 + (btnW - Flat.COLOR.width(font, resetText)) / 2, btnY + 6, resetHov ? TEXT_HOVERED : TEXT_OTHER, false);
    }

    private void renderGuiContent(GuiGraphicsExtractor graphics, Font font, int startX, int pad, int innerW, int midY, int midH, int mouseX, int mouseY) {
        int boxX = startX + pad;
        int boxW = innerW;
        int leftMargin = boxX + 16;

        Flat.COLOR.fill(graphics, boxX, midY, boxX + boxW, midY + midH, BG_MAIN);
        Flat.COLOR.fill(graphics, boxX, midY, boxX + boxW, midY + 22, BG_HEAD);
        Flat.COLOR.outline(graphics, boxX, midY, boxW, midH, BORDER_MAIN);
        Flat.COLOR.text(graphics, font, "GUI & Visual Customization", boxX + 10, midY + 7, TEXT_SELECTED, false);

        int curY = midY + 30;

        Flat.COLOR.text(graphics, font, "Interface Font:", leftMargin, curY + 4, TEXT_NORMAL, false);
        int fontBtnX = leftMargin + 115;
        int fontBtnW = 150;
        int fontBtnH = 18;
        boolean fontOpen = (openDropdown == guiFontChoice);
        Flat.COLOR.fill(graphics, fontBtnX, curY, fontBtnX + fontBtnW, curY + fontBtnH, SEARCH_BG);
        Flat.COLOR.outline(graphics, fontBtnX, curY, fontBtnW, fontBtnH, fontOpen ? TEXT_SELECTED : SEARCH_BORDER);
        String fontDisplay = (guiFontChoice.get() == FontChoice.Default) ? "Default Minecraft" : "Qrafty's Capitalized";
        Flat.COLOR.text(graphics, font, fontDisplay, fontBtnX + 6, curY + (fontBtnH - 8) / 2, TEXT_NORMAL, false);
        Flat.COLOR.text(graphics, font, fontOpen ? "▲" : "▼", fontBtnX + fontBtnW - 14, curY + (fontBtnH - 8) / 2, TEXT_SELECTED, false);

        curY += 26;
        Flat.COLOR.text(graphics, font, "TEXT", leftMargin, curY + 4, TEXT_SELECTED, false);
        Flat.COLOR.fill(graphics, leftMargin + 42, curY + 8, boxX + boxW - 16, curY + 9, BORDER_MAIN);

        curY += 18;
        Flat.COLOR.text(graphics, font, "Text Target:", leftMargin, curY + 4, TEXT_NORMAL, false);

        int textDropX = leftMargin + 115;
        int textDropW = 100;
        int textDropH = 18;
        boolean textDropOpen = (openDropdown == guiTextChoice);
        Flat.COLOR.fill(graphics, textDropX, curY, textDropX + textDropW, curY + textDropH, SEARCH_BG);
        Flat.COLOR.outline(graphics, textDropX, curY, textDropW, textDropH, textDropOpen ? TEXT_SELECTED : SEARCH_BORDER);
        Flat.COLOR.text(graphics, font, guiTextChoice.get().name(), textDropX + 6, curY + (textDropH - 8) / 2, TEXT_NORMAL, false);
        Flat.COLOR.text(graphics, font, textDropOpen ? "▲" : "▼", textDropX + textDropW - 14, curY + (textDropH - 8) / 2, TEXT_SELECTED, false);

        Color curTextColor = getTargetTextColor(guiTextChoice.get());
        int swatchW = 28;
        int swatchH = 18;
        int textSwatchX = textDropX + textDropW + 10;
        Flat.COLOR.fill(graphics, textSwatchX, curY, textSwatchX + swatchW, curY + swatchH, curTextColor);
        Flat.COLOR.outline(graphics, textSwatchX, curY, swatchW, swatchH, BORDER_MAIN);

        int changeBtnW = 95;
        int changeBtnH = 18;
        int textChangeX = textSwatchX + swatchW + 10;
        boolean textChangeHov = mouseX >= textChangeX && mouseX <= textChangeX + changeBtnW && mouseY >= curY && mouseY <= curY + changeBtnH;
        Flat.COLOR.fill(graphics, textChangeX, curY, textChangeX + changeBtnW, curY + changeBtnH, textChangeHov ? BG_BUTTON_HOVERED : BG_BUTTON_NORMAL);
        Flat.COLOR.outline(graphics, textChangeX, curY, changeBtnW, changeBtnH, textChangeHov ? TEXT_SELECTED : BORDER_BUTTONS);
        Flat.COLOR.text(graphics, font, "Change Colour", textChangeX + (changeBtnW - Flat.COLOR.width(font, "Change Colour")) / 2, curY + 5, textChangeHov ? TEXT_HOVERED : TEXT_NORMAL, false);

        int resetBtnW = 55;
        int resetBtnH = 18;
        int textResetX = textChangeX + changeBtnW + 8;
        boolean textResetHov = mouseX >= textResetX && mouseX <= textResetX + resetBtnW && mouseY >= curY && mouseY <= curY + resetBtnH;
        Flat.COLOR.fill(graphics, textResetX, curY, textResetX + resetBtnW, curY + resetBtnH, textResetHov ? BG_BUTTON_HOVERED : BG_BUTTON_NORMAL);
        Flat.COLOR.outline(graphics, textResetX, curY, resetBtnW, resetBtnH, textResetHov ? TEXT_SELECTED : BORDER_BUTTONS);
        Flat.COLOR.text(graphics, font, "Reset", textResetX + (resetBtnW - Flat.COLOR.width(font, "Reset")) / 2, curY + 5, textResetHov ? TEXT_HOVERED : TEXT_OTHER, false);

        curY += 26;
        Flat.COLOR.text(graphics, font, "BACKGROUND", leftMargin, curY + 4, TEXT_SELECTED, false);
        Flat.COLOR.fill(graphics, leftMargin + 85, curY + 8, boxX + boxW - 16, curY + 9, BORDER_MAIN);

        curY += 18;
        Flat.COLOR.text(graphics, font, "Main Background:", leftMargin, curY + 4, TEXT_NORMAL, false);

        int mbSwatchX = leftMargin + 115;
        Flat.COLOR.fill(graphics, mbSwatchX, curY, mbSwatchX + swatchW, curY + swatchH, BG_MAIN);
        Flat.COLOR.outline(graphics, mbSwatchX, curY, swatchW, swatchH, BORDER_MAIN);

        int mbChangeX = mbSwatchX + swatchW + 10;
        boolean mbChangeHov = mouseX >= mbChangeX && mouseX <= mbChangeX + changeBtnW && mouseY >= curY && mouseY <= curY + changeBtnH;
        Flat.COLOR.fill(graphics, mbChangeX, curY, mbChangeX + changeBtnW, curY + changeBtnH, mbChangeHov ? BG_BUTTON_HOVERED : BG_BUTTON_NORMAL);
        Flat.COLOR.outline(graphics, mbChangeX, curY, changeBtnW, changeBtnH, mbChangeHov ? TEXT_SELECTED : BORDER_BUTTONS);
        Flat.COLOR.text(graphics, font, "Change Colour", mbChangeX + (changeBtnW - Flat.COLOR.width(font, "Change Colour")) / 2, curY + 5, mbChangeHov ? TEXT_HOVERED : TEXT_NORMAL, false);

        int mbResetX = mbChangeX + changeBtnW + 8;
        boolean mbResetHov = mouseX >= mbResetX && mouseX <= mbResetX + resetBtnW && mouseY >= curY && mouseY <= curY + resetBtnH;
        Flat.COLOR.fill(graphics, mbResetX, curY, mbResetX + resetBtnW, curY + resetBtnH, mbResetHov ? BG_BUTTON_HOVERED : BG_BUTTON_NORMAL);
        Flat.COLOR.outline(graphics, mbResetX, curY, resetBtnW, resetBtnH, mbResetHov ? TEXT_SELECTED : BORDER_BUTTONS);
        Flat.COLOR.text(graphics, font, "Reset", mbResetX + (resetBtnW - Flat.COLOR.width(font, "Reset")) / 2, curY + 5, mbResetHov ? TEXT_HOVERED : TEXT_OTHER, false);

        curY += 22;
        Flat.COLOR.text(graphics, font, "Head Background:", leftMargin, curY + 4, TEXT_NORMAL, false);

        int hbSwatchX = leftMargin + 115;
        Flat.COLOR.fill(graphics, hbSwatchX, curY, hbSwatchX + swatchW, curY + swatchH, BG_HEAD);
        Flat.COLOR.outline(graphics, hbSwatchX, curY, swatchW, swatchH, BORDER_MAIN);

        int hbChangeX = hbSwatchX + swatchW + 10;
        boolean hbChangeHov = mouseX >= hbChangeX && mouseX <= hbChangeX + changeBtnW && mouseY >= curY && mouseY <= curY + changeBtnH;
        Flat.COLOR.fill(graphics, hbChangeX, curY, hbChangeX + changeBtnW, curY + changeBtnH, hbChangeHov ? BG_BUTTON_HOVERED : BG_BUTTON_NORMAL);
        Flat.COLOR.outline(graphics, hbChangeX, curY, changeBtnW, changeBtnH, hbChangeHov ? TEXT_SELECTED : BORDER_BUTTONS);
        Flat.COLOR.text(graphics, font, "Change Colour", hbChangeX + (changeBtnW - Flat.COLOR.width(font, "Change Colour")) / 2, curY + 5, hbChangeHov ? TEXT_HOVERED : TEXT_NORMAL, false);

        int hbResetX = hbChangeX + changeBtnW + 8;
        boolean hbResetHov = mouseX >= hbResetX && mouseX <= hbResetX + resetBtnW && mouseY >= curY && mouseY <= curY + resetBtnH;
        Flat.COLOR.fill(graphics, hbResetX, curY, hbResetX + resetBtnW, curY + resetBtnH, hbResetHov ? BG_BUTTON_HOVERED : BG_BUTTON_NORMAL);
        Flat.COLOR.outline(graphics, hbResetX, curY, resetBtnW, resetBtnH, hbResetHov ? TEXT_SELECTED : BORDER_BUTTONS);
        Flat.COLOR.text(graphics, font, "Reset", hbResetX + (resetBtnW - Flat.COLOR.width(font, "Reset")) / 2, curY + 5, hbResetHov ? TEXT_HOVERED : TEXT_OTHER, false);

        curY += 22;
        Flat.COLOR.text(graphics, font, "Tab Background:", leftMargin, curY + 4, TEXT_NORMAL, false);

        int tabDropX = leftMargin + 115;
        int tabDropW = 100;
        int tabDropH = 18;
        boolean tabDropOpen = (openDropdown == guiTabChoice);
        Flat.COLOR.fill(graphics, tabDropX, curY, tabDropX + tabDropW, curY + tabDropH, SEARCH_BG);
        Flat.COLOR.outline(graphics, tabDropX, curY, tabDropW, tabDropH, tabDropOpen ? TEXT_SELECTED : SEARCH_BORDER);
        Flat.COLOR.text(graphics, font, guiTabChoice.get().name(), tabDropX + 6, curY + (tabDropH - 8) / 2, TEXT_NORMAL, false);
        Flat.COLOR.text(graphics, font, tabDropOpen ? "▲" : "▼", tabDropX + tabDropW - 14, curY + (tabDropH - 8) / 2, TEXT_SELECTED, false);

        Color curTabCol = getTargetTabColor(guiTabChoice.get());
        int tabSwatchX = tabDropX + tabDropW + 10;
        Flat.COLOR.fill(graphics, tabSwatchX, curY, tabSwatchX + swatchW, curY + swatchH, curTabCol);
        Flat.COLOR.outline(graphics, tabSwatchX, curY, swatchW, swatchH, BORDER_MAIN);

        int tabChangeX = tabSwatchX + swatchW + 10;
        boolean tabChangeHov = mouseX >= tabChangeX && mouseX <= tabChangeX + changeBtnW && mouseY >= curY && mouseY <= curY + changeBtnH;
        Flat.COLOR.fill(graphics, tabChangeX, curY, tabChangeX + changeBtnW, curY + changeBtnH, tabChangeHov ? BG_BUTTON_HOVERED : BG_BUTTON_NORMAL);
        Flat.COLOR.outline(graphics, tabChangeX, curY, changeBtnW, changeBtnH, tabChangeHov ? TEXT_SELECTED : BORDER_BUTTONS);
        Flat.COLOR.text(graphics, font, "Change Colour", tabChangeX + (changeBtnW - Flat.COLOR.width(font, "Change Colour")) / 2, curY + 5, tabChangeHov ? TEXT_HOVERED : TEXT_NORMAL, false);

        int tabResetX = tabChangeX + changeBtnW + 8;
        boolean tabResetHov = mouseX >= tabResetX && mouseX <= tabResetX + resetBtnW && mouseY >= curY && mouseY <= curY + resetBtnH;
        Flat.COLOR.fill(graphics, tabResetX, curY, tabResetX + resetBtnW, curY + resetBtnH, tabResetHov ? BG_BUTTON_HOVERED : BG_BUTTON_NORMAL);
        Flat.COLOR.outline(graphics, tabResetX, curY, resetBtnW, resetBtnH, tabResetHov ? TEXT_SELECTED : BORDER_BUTTONS);
        Flat.COLOR.text(graphics, font, "Reset", tabResetX + (resetBtnW - Flat.COLOR.width(font, "Reset")) / 2, curY + 5, tabResetHov ? TEXT_HOVERED : TEXT_OTHER, false);


        curY += 22;
        int btnToggleX = leftMargin;
        int btnToggleW = 150;
        int btnToggleH = 18;
        boolean btnHov = mouseX >= btnToggleX && mouseX <= btnToggleX + btnToggleW && mouseY >= curY && mouseY <= curY + btnToggleH;
        Flat.COLOR.fill(graphics, btnToggleX, curY, btnToggleX + btnToggleW, curY + btnToggleH, btnHov ? BG_BUTTON_HOVERED : BG_BUTTON_NORMAL);
        Flat.COLOR.outline(graphics, btnToggleX, curY, btnToggleW, btnToggleH, buttonsGroupExpanded ? TEXT_SELECTED : BORDER_BUTTONS);
        String btnToggleText = buttonsGroupExpanded ? "▼ Buttons" : "▶ Buttons";
        Flat.COLOR.text(graphics, font, btnToggleText, btnToggleX + 8, curY + 5, buttonsGroupExpanded ? TEXT_SELECTED : TEXT_NORMAL, false);

        if (buttonsGroupExpanded) {
            curY += 22;
            Flat.COLOR.text(graphics, font, "  Buttons State:", leftMargin, curY + 4, TEXT_NORMAL, false);

            int bDropX = leftMargin + 115;
            int bDropW = 100;
            int bDropH = 18;
            boolean bDropOpen = (openDropdown == guiButtonChoice);
            Flat.COLOR.fill(graphics, bDropX, curY, bDropX + bDropW, curY + bDropH, SEARCH_BG);
            Flat.COLOR.outline(graphics, bDropX, curY, bDropW, bDropH, bDropOpen ? TEXT_SELECTED : SEARCH_BORDER);
            Flat.COLOR.text(graphics, font, guiButtonChoice.get().name(), bDropX + 6, curY + (bDropH - 8) / 2, TEXT_NORMAL, false);
            Flat.COLOR.text(graphics, font, bDropOpen ? "▲" : "▼", bDropX + bDropW - 14, curY + (bDropH - 8) / 2, TEXT_SELECTED, false);

            Color curBCol = getTargetButtonColor(guiButtonChoice.get());
            int bSwatchX = bDropX + bDropW + 10;
            Flat.COLOR.fill(graphics, bSwatchX, curY, bSwatchX + swatchW, curY + swatchH, curBCol);
            Flat.COLOR.outline(graphics, bSwatchX, curY, swatchW, swatchH, BORDER_MAIN);

            int bChangeX = bSwatchX + swatchW + 10;
            boolean bChangeHov = mouseX >= bChangeX && mouseX <= bChangeX + changeBtnW && mouseY >= curY && mouseY <= curY + changeBtnH;
            Flat.COLOR.fill(graphics, bChangeX, curY, bChangeX + changeBtnW, curY + changeBtnH, bChangeHov ? BG_BUTTON_HOVERED : BG_BUTTON_NORMAL);
            Flat.COLOR.outline(graphics, bChangeX, curY, changeBtnW, changeBtnH, bChangeHov ? TEXT_SELECTED : BORDER_BUTTONS);
            Flat.COLOR.text(graphics, font, "Change Colour", bChangeX + (changeBtnW - Flat.COLOR.width(font, "Change Colour")) / 2, curY + 5, bChangeHov ? TEXT_HOVERED : TEXT_NORMAL, false);

            int bResetX = bChangeX + changeBtnW + 8;
            boolean bResetHov = mouseX >= bResetX && mouseX <= bResetX + resetBtnW && mouseY >= curY && mouseY <= curY + resetBtnH;
            Flat.COLOR.fill(graphics, bResetX, curY, bResetX + resetBtnW, curY + resetBtnH, bResetHov ? BG_BUTTON_HOVERED : BG_BUTTON_NORMAL);
            Flat.COLOR.outline(graphics, bResetX, curY, resetBtnW, resetBtnH, bResetHov ? TEXT_SELECTED : BORDER_BUTTONS);
            Flat.COLOR.text(graphics, font, "Reset", bResetX + (resetBtnW - Flat.COLOR.width(font, "Reset")) / 2, curY + 5, bResetHov ? TEXT_HOVERED : TEXT_OTHER, false);
        }

        curY += 26;
        Flat.COLOR.text(graphics, font, "BORDERS", leftMargin, curY + 4, TEXT_SELECTED, false);
        Flat.COLOR.fill(graphics, leftMargin + 65, curY + 8, boxX + boxW - 16, curY + 9, BORDER_MAIN);

        curY += 18;
        Flat.COLOR.text(graphics, font, "Border Target:", leftMargin, curY + 4, TEXT_NORMAL, false);

        int borderDropX = leftMargin + 115;
        int borderDropW = 100;
        int borderDropH = 18;
        boolean borderDropOpen = (openDropdown == guiBorderChoice);
        Flat.COLOR.fill(graphics, borderDropX, curY, borderDropX + borderDropW, curY + borderDropH, SEARCH_BG);
        Flat.COLOR.outline(graphics, borderDropX, curY, borderDropW, borderDropH, borderDropOpen ? TEXT_SELECTED : SEARCH_BORDER);
        Flat.COLOR.text(graphics, font, guiBorderChoice.get().name(), borderDropX + 6, curY + (borderDropH - 8) / 2, TEXT_NORMAL, false);
        Flat.COLOR.text(graphics, font, borderDropOpen ? "▲" : "▼", borderDropX + borderDropW - 14, curY + (borderDropH - 8) / 2, TEXT_SELECTED, false);

        Color curBorderCol = BORDER_MAIN;
        if (guiBorderChoice.get() == BorderTarget.Tabs) curBorderCol = BORDER_TABS;
        else if (guiBorderChoice.get() == BorderTarget.Buttons) curBorderCol = BORDER_BUTTONS;

        int borderSwatchX = borderDropX + borderDropW + 10;
        Flat.COLOR.fill(graphics, borderSwatchX, curY, borderSwatchX + swatchW, curY + swatchH, curBorderCol);
        Flat.COLOR.outline(graphics, borderSwatchX, curY, swatchW, swatchH, BORDER_MAIN);

        int borderChangeX = borderSwatchX + swatchW + 10;
        boolean borderChangeHov = mouseX >= borderChangeX && mouseX <= borderChangeX + changeBtnW && mouseY >= curY && mouseY <= curY + changeBtnH;
        Flat.COLOR.fill(graphics, borderChangeX, curY, borderChangeX + changeBtnW, curY + changeBtnH, borderChangeHov ? BG_BUTTON_HOVERED : BG_BUTTON_NORMAL);
        Flat.COLOR.outline(graphics, borderChangeX, curY, changeBtnW, changeBtnH, borderChangeHov ? TEXT_SELECTED : BORDER_BUTTONS);
        Flat.COLOR.text(graphics, font, "Change Colour", borderChangeX + (changeBtnW - Flat.COLOR.width(font, "Change Colour")) / 2, curY + 5, borderChangeHov ? TEXT_HOVERED : TEXT_NORMAL, false);

        int borderResetX = borderChangeX + changeBtnW + 8;
        boolean borderResetHov = mouseX >= borderResetX && mouseX <= borderResetX + resetBtnW && mouseY >= curY && mouseY <= curY + resetBtnH;
        Flat.COLOR.fill(graphics, borderResetX, curY, borderResetX + resetBtnW, curY + resetBtnH, borderResetHov ? BG_BUTTON_HOVERED : BG_BUTTON_NORMAL);
        Flat.COLOR.outline(graphics, borderResetX, curY, resetBtnW, resetBtnH, borderResetHov ? TEXT_SELECTED : BORDER_BUTTONS);
        Flat.COLOR.text(graphics, font, "Reset", borderResetX + (resetBtnW - Flat.COLOR.width(font, "Reset")) / 2, curY + 5, borderResetHov ? TEXT_HOVERED : TEXT_OTHER, false);

        curY += 26;
        Flat.COLOR.text(graphics, font, "ELEMENTS", leftMargin, curY + 4, TEXT_SELECTED, false);
        Flat.COLOR.fill(graphics, leftMargin + 65, curY + 8, boxX + boxW - 16, curY + 9, BORDER_MAIN);

        curY += 18;
        Flat.COLOR.text(graphics, font, "Elements:", leftMargin, curY + 4, TEXT_NORMAL, false);

        int elemDropX = leftMargin + 115;
        int elemDropW = 100;
        int elemDropH = 18;
        boolean elemDropOpen = (openDropdown == guiElementChoice);
        Flat.COLOR.fill(graphics, elemDropX, curY, elemDropX + elemDropW, curY + elemDropH, SEARCH_BG);
        Flat.COLOR.outline(graphics, elemDropX, curY, elemDropW, elemDropH, elemDropOpen ? TEXT_SELECTED : SEARCH_BORDER);
        Flat.COLOR.text(graphics, font, guiElementChoice.get().name(), elemDropX + 6, curY + (elemDropH - 8) / 2, TEXT_NORMAL, false);
        Flat.COLOR.text(graphics, font, elemDropOpen ? "▲" : "▼", elemDropX + elemDropW - 14, curY + (elemDropH - 8) / 2, TEXT_SELECTED, false);

        Color curElemCol = getTargetElementColor(guiElementChoice.get());
        int elemSwatchX = elemDropX + elemDropW + 10;
        Flat.COLOR.fill(graphics, elemSwatchX, curY, elemSwatchX + swatchW, curY + swatchH, curElemCol);
        Flat.COLOR.outline(graphics, elemSwatchX, curY, swatchW, swatchH, BORDER_MAIN);

        int elemChangeX = elemSwatchX + swatchW + 10;
        boolean elemChangeHov = mouseX >= elemChangeX && mouseX <= elemChangeX + changeBtnW && mouseY >= curY && mouseY <= curY + changeBtnH;
        Flat.COLOR.fill(graphics, elemChangeX, curY, elemChangeX + changeBtnW, curY + changeBtnH, elemChangeHov ? BG_BUTTON_HOVERED : BG_BUTTON_NORMAL);
        Flat.COLOR.outline(graphics, elemChangeX, curY, changeBtnW, changeBtnH, elemChangeHov ? TEXT_SELECTED : BORDER_BUTTONS);
        Flat.COLOR.text(graphics, font, "Change Colour", elemChangeX + (changeBtnW - Flat.COLOR.width(font, "Change Colour")) / 2, curY + 5, elemChangeHov ? TEXT_HOVERED : TEXT_NORMAL, false);

        int elemResetX = elemChangeX + changeBtnW + 8;
        boolean elemResetHov = mouseX >= elemResetX && mouseX <= elemResetX + resetBtnW && mouseY >= curY && mouseY <= curY + resetBtnH;
        Flat.COLOR.fill(graphics, elemResetX, curY, elemResetX + resetBtnW, curY + resetBtnH, elemResetHov ? BG_BUTTON_HOVERED : BG_BUTTON_NORMAL);
        Flat.COLOR.outline(graphics, elemResetX, curY, resetBtnW, resetBtnH, elemResetHov ? TEXT_SELECTED : BORDER_BUTTONS);
        Flat.COLOR.text(graphics, font, "Reset", elemResetX + (resetBtnW - Flat.COLOR.width(font, "Reset")) / 2, curY + 5, elemResetHov ? TEXT_HOVERED : TEXT_OTHER, false);


        int rstAllY = midY + midH - 26;
        int rstAllW = 160;
        int rstAllX = leftMargin;
        boolean rstAllHov = mouseX >= rstAllX && mouseX <= rstAllX + rstAllW && mouseY >= rstAllY && mouseY <= rstAllY + 18;
        Flat.COLOR.fill(graphics, rstAllX, rstAllY, rstAllX + rstAllW, rstAllY + 18, rstAllHov ? BG_BUTTON_HOVERED : BG_BUTTON_NORMAL);
        Flat.COLOR.outline(graphics, rstAllX, rstAllY, rstAllW, 18, rstAllHov ? TEXT_SELECTED : BORDER_BUTTONS);
        String rstAllTxt = "Reset All GUI Colors";
        Flat.COLOR.text(graphics, font, rstAllTxt, rstAllX + (rstAllW - Flat.COLOR.width(font, rstAllTxt)) / 2, rstAllY + 5, rstAllHov ? TEXT_HOVERED : TEXT_OTHER, false);
    }

    private void renderColorPickerWindow(GuiGraphicsExtractor graphics, Font font, int mouseX, int mouseY) {
        if (!isColorPickerOpen || activePickerColor == null) return;

        Flat.COLOR.fill(graphics, pickerWindowX + 4, pickerWindowY + 4, pickerWindowX + pickerWindowW + 4, pickerWindowY + pickerWindowH + 4, new Color(0, 0, 0, 160));

        Flat.COLOR.fill(graphics, pickerWindowX, pickerWindowY, pickerWindowX + pickerWindowW, pickerWindowY + pickerWindowH, new Color(0, 0, 0, 255));
        Flat.COLOR.outline(graphics, pickerWindowX, pickerWindowY, pickerWindowW, pickerWindowH, TEXT_SELECTED);

        Flat.COLOR.fill(graphics, pickerWindowX, pickerWindowY, pickerWindowX + pickerWindowW, pickerWindowY + 22, BG_HEAD);
        Flat.COLOR.outline(graphics, pickerWindowX, pickerWindowY, pickerWindowW, 22, BORDER_MAIN);
        String title = "Colour: " + activePickerTitle;
        Flat.COLOR.text(graphics, font, title, pickerWindowX + 8, pickerWindowY + 7, TEXT_NORMAL, false);

        int closeX = pickerWindowX + pickerWindowW - 18;
        int closeY = pickerWindowY + 3;
        int closeS = 16;
        boolean closeHov = mouseX >= closeX && mouseX <= closeX + closeS && mouseY >= closeY && mouseY <= closeY + closeS;
        if (closeHov) {
            Flat.COLOR.fill(graphics, closeX, closeY, closeX + closeS, closeY + closeS, new Color(200, 40, 40, 255));
        }
        Flat.COLOR.text(graphics, font, "✕", closeX + 4, closeY + 4, closeHov ? Color.WHITE : TEXT_OTHER, false);

        int sbX = pickerWindowX + 12;
        int sbY = pickerWindowY + 28;
        int sbSize = 110;
        int steps = 22;
        float stepSize = sbSize / (float) steps;
        for (int i = 0; i < steps; i++) {
            float s = (i + 0.5f) / steps;
            int rgbTop = java.awt.Color.HSBtoRGB(pickerHue, s, 1.0f);
            int rgbBot = java.awt.Color.HSBtoRGB(pickerHue, s, 0.0f);
            Color cTop = new Color(rgbTop);
            Color cBot = new Color(rgbBot);
            Flat.COLOR.fillGradient(graphics, sbX + i * stepSize, sbY, sbX + (i + 1) * stepSize, sbY + sbSize, cTop, cBot);
        }
        Flat.COLOR.outline(graphics, sbX, sbY, sbSize, sbSize, SEARCH_BORDER);

        int kx = sbX + (int) (pickerSat * sbSize);
        int ky = sbY + (int) ((1.0f - pickerBri) * sbSize);
        Flat.COLOR.fill(graphics, kx - 3, ky - 3, kx + 3, ky + 3, Color.WHITE);
        Flat.COLOR.outline(graphics, kx - 3, ky - 3, 6, 6, Color.BLACK);

        int hueX = pickerWindowX + 12;
        int hueY = pickerWindowY + 146;
        int hueW = 110;
        int hueH = 12;
        for (int i = 0; i < steps; i++) {
            float h = (i + 0.5f) / steps;
            int rgb = java.awt.Color.HSBtoRGB(h, 1.0f, 1.0f);
            Flat.COLOR.fill(graphics, hueX + i * stepSize, hueY, hueX + (i + 1) * stepSize, hueY + hueH, new Color(rgb));
        }
        Flat.COLOR.outline(graphics, hueX, hueY, hueW, hueH, SEARCH_BORDER);

        int hx = hueX + (int) (pickerHue * hueW);
        Flat.COLOR.fill(graphics, hx - 2, hueY - 2, hx + 2, hueY + hueH + 2, Color.WHITE);
        Flat.COLOR.outline(graphics, hx - 2, hueY - 2, 4, hueH + 4, Color.BLACK);

        int rightX = pickerWindowX + 132;
        int ctrlW = pickerWindowW - 144;

        Flat.COLOR.fill(graphics, rightX, pickerWindowY + 28, rightX + ctrlW, pickerWindowY + 52, activePickerColor);
        Flat.COLOR.outline(graphics, rightX, pickerWindowY + 28, ctrlW, 24, BORDER_MAIN);
        String hexStr = String.format("#%02X%02X%02X", activePickerColor.r, activePickerColor.g, activePickerColor.b);
        Flat.COLOR.text(graphics, font, hexStr, rightX + (ctrlW - Flat.COLOR.width(font, hexStr)) / 2, pickerWindowY + 36, (pickerBri > 0.5f && pickerSat < 0.5f) ? Color.BLACK : Color.WHITE, true);

        Flat.COLOR.text(graphics, font, "Mode:", rightX, pickerWindowY + 62, TEXT_OTHER, false);
        int modeBtnX = rightX + 38;
        int modeBtnW = ctrlW - 38;
        int modeBtnH = 16;
        boolean modeOpen = (openDropdown == guiPickerMode);
        Flat.COLOR.fill(graphics, modeBtnX, pickerWindowY + 60, modeBtnX + modeBtnW, pickerWindowY + 60 + modeBtnH, SEARCH_BG);
        Flat.COLOR.outline(graphics, modeBtnX, pickerWindowY + 60, modeBtnW, modeBtnH, modeOpen ? TEXT_SELECTED : SEARCH_BORDER);
        Flat.COLOR.text(graphics, font, guiPickerMode.get().name(), modeBtnX + 6, pickerWindowY + 64, TEXT_SELECTED, false);
        Flat.COLOR.text(graphics, font, modeOpen ? "▲" : "▼", modeBtnX + modeBtnW - 12, pickerWindowY + 64, TEXT_SELECTED, false);

        if (guiPickerMode.get() == PickerMode.HEX) {
            int hexBoxY = pickerWindowY + 88;
            Flat.COLOR.text(graphics, font, "HEX:", rightX, hexBoxY + 4, TEXT_NORMAL, false);
            int boxX = rightX + 32;
            int boxW = ctrlW - 32;
            boolean focused = (focusedField == TextField.HEX);
            Flat.COLOR.fill(graphics, boxX, hexBoxY, boxX + boxW, hexBoxY + 18, SEARCH_BG);
            Flat.COLOR.outline(graphics, boxX, hexBoxY, boxW, 18, focused ? TEXT_SELECTED : SEARCH_BORDER);
            boolean blink = (System.currentTimeMillis() % 1000) < 500;
            String display = focused ? (hexInput + (blink && !isTextSelected ? "|" : "")) : hexStr;
            if (focused && isTextSelected) {
                Flat.COLOR.fill(graphics, boxX + 6, hexBoxY + 5, boxX + 6 + Flat.COLOR.width(font, display), hexBoxY + 5 + 8, new Color(179, 0, 34, 255));
            }
            Color txtCol = (focused && isTextSelected) ? new Color(255, 255, 0, 255) : (focused ? TEXT_NORMAL : TEXT_OTHER);
            Flat.COLOR.text(graphics, font, display, boxX + 6, hexBoxY + 5, txtCol, false);
        } else {
            int rgbBoxY = pickerWindowY + 84;
            drawRgbField(graphics, font, "R", rightX, rgbBoxY, TextField.R, focusedField == TextField.R ? rInput : String.valueOf(activePickerColor.r), ctrlW);
            drawRgbField(graphics, font, "G", rightX, rgbBoxY + 22, TextField.G, focusedField == TextField.G ? gInput : String.valueOf(activePickerColor.g), ctrlW);
            drawRgbField(graphics, font, "B", rightX, rgbBoxY + 44, TextField.B, focusedField == TextField.B ? bInput : String.valueOf(activePickerColor.b), ctrlW);
        }

        int applyY = pickerWindowY + 168;
        int applyH = 20;
        boolean applyHov = mouseX >= rightX && mouseX <= rightX + ctrlW && mouseY >= applyY && mouseY <= applyY + applyH;
        Flat.COLOR.fill(graphics, rightX, applyY, rightX + ctrlW, applyY + applyH, applyHov ? BG_BUTTON_HOVERED : BG_BUTTON_NORMAL);
        Flat.COLOR.outline(graphics, rightX, applyY, ctrlW, applyH, applyHov ? TEXT_SELECTED : BORDER_BUTTONS);
        String applyTxt = "APPLY AND CLOSE";
        Flat.COLOR.text(graphics, font, applyTxt, rightX + (ctrlW - Flat.COLOR.width(font, applyTxt)) / 2, applyY + 6, applyHov ? TEXT_HOVERED : TEXT_NORMAL, false);
    }

    private void drawRgbField(GuiGraphicsExtractor graphics, Font font, String label, int x, int y, TextField field, String text, int totalW) {
        Flat.COLOR.text(graphics, font, label + ":", x, y + 4, TEXT_NORMAL, false);
        int boxX = x + 24;
        int boxW = totalW - 24;
        boolean focused = (focusedField == field);
        Flat.COLOR.fill(graphics, boxX, y, boxX + boxW, y + 16, SEARCH_BG);
        Flat.COLOR.outline(graphics, boxX, y, boxW, 16, focused ? TEXT_SELECTED : SEARCH_BORDER);
        boolean blink = (System.currentTimeMillis() % 1000) < 500;
        String display = text + (focused && blink && !isTextSelected ? "|" : "");
        if (focused && isTextSelected) {
            Flat.COLOR.fill(graphics, boxX + 6, y + 4, boxX + 6 + Flat.COLOR.width(font, display), y + 4 + 8, new Color(179, 0, 34, 255));
        }
        Color txtCol = (focused && isTextSelected) ? new Color(255, 255, 0, 255) : (focused ? TEXT_NORMAL : TEXT_OTHER);
        Flat.COLOR.text(graphics, font, display, boxX + 6, y + 4, txtCol, false);
    }

    private void renderFavoritesPanel(GuiGraphicsExtractor graphics, Font font, int startX, int pad, int innerW, int botY, int botH) {
        Flat.COLOR.fill(graphics, startX + pad, botY, startX + pad + innerW, botY + botH, BG_MAIN);
        Flat.COLOR.fill(graphics, startX + pad, botY, startX + pad + innerW, botY + 22, BG_HEAD);
        Flat.COLOR.outline(graphics, startX + pad, botY, innerW, botH, BORDER_MAIN);

        Flat.COLOR.text(graphics, font, "Favorites", startX + pad + 10, botY + 7, TEXT_SELECTED, false);

        int favTabsX = startX + pad + 80;
        String[] favTabs = {"ALL", "Tweaks", "Settings"};
        for (int i = 0; i < 3; i++) {
            int ftx = favTabsX + i * 58;
            boolean active = (selectedFavTab == i);
            Flat.COLOR.fill(graphics, ftx, botY + 3, ftx + 54, botY + 19, active ? BG_TAB_SELECTED : BG_TAB_NORMAL);
            Flat.COLOR.outline(graphics, ftx, botY + 3, 54, 16, active ? TEXT_SELECTED : BORDER_BUTTONS);
            Flat.COLOR.text(graphics, font, favTabs[i], ftx + (54 - Flat.COLOR.width(font, favTabs[i])) / 2, botY + 7, active ? TEXT_SELECTED : TEXT_NORMAL, false);
        }

        int favItemX = startX + pad + 10;
        int favItemY = botY + 32;
        
        List<Object> displayFavs = new ArrayList<>();
        if (selectedFavTab == 0 || selectedFavTab == 1) {
            displayFavs.addAll(favoriteFeatures);
        }
        if (selectedFavTab == 0 || selectedFavTab == 2) {
            displayFavs.addAll(savedFavoriteSettings);
        }

        if (displayFavs.isEmpty()) {
            if (favoriteFeatures.isEmpty() && savedFavoriteSettings.isEmpty() && selectedFavTab == 0) {
                Flat.COLOR.text(graphics, font, "No favorites added yet. Click ☆ next to any tweak or setting to bookmark it!", favItemX, favItemY + 6, TEXT_OTHER, false);
            } else {
                Flat.COLOR.text(graphics, font, "No favorites found in this category.", favItemX, favItemY + 6, TEXT_OTHER, false);
            }
        } else {
            for (Object fav : displayFavs) {
                String title = (fav instanceof Feature f) ? "★ " + f.name : "★ " + fav.toString();
                int boxW = Math.max(110, Flat.COLOR.width(font, title) + 16);
                Flat.COLOR.fill(graphics, favItemX, favItemY, favItemX + boxW, favItemY + 22, SEARCH_BG);
                Flat.COLOR.outline(graphics, favItemX, favItemY, boxW, 22, TEXT_SELECTED);
                Flat.COLOR.text(graphics, font, title, favItemX + 8, favItemY + 7, TEXT_NORMAL, false);
                favItemX += boxW + 8;
            }
        }
    }

    private void renderDropdownOverlay(GuiGraphicsExtractor graphics, Font font, int mouseX, int mouseY) {
        if (openDropdown == null) return;

        Enum<?> current = (Enum<?>) openDropdown.get();
        Enum<?>[] constants = current.getDeclaringClass().getEnumConstants();

        int itemH = 18;
        int popupY = openDropdownY + openDropdownH + 1;
        int popupH = constants.length * itemH;

        Flat.COLOR.fill(graphics, openDropdownX, popupY, openDropdownX + openDropdownW, popupY + popupH, BG_HEAD);
        Flat.COLOR.outline(graphics, openDropdownX, popupY, openDropdownW, popupH, TEXT_SELECTED);

        for (int i = 0; i < constants.length; i++) {
            Enum<?> val = constants[i];
            int itemY = popupY + i * itemH;
            boolean isHovered = (mouseX >= openDropdownX && mouseX <= openDropdownX + openDropdownW && mouseY >= itemY && mouseY < itemY + itemH);
            boolean isSelected = (val == current);

            if (isHovered) {
                Flat.COLOR.fill(graphics, openDropdownX + 1, itemY, openDropdownX + openDropdownW - 1, itemY + itemH, BG_ELEMENT_HOVERED);
            }

            Color textColor = isSelected ? TEXT_SELECTED : (isHovered ? TEXT_HOVERED : TEXT_OTHER);
            String displayName = val.toString();
            if (val == FontChoice.Default) displayName = "Default Minecraft";
            if (val == FontChoice.Qraftys) displayName = "Qrafty's Capitalized";
            Flat.COLOR.text(graphics, font, displayName, openDropdownX + 6, itemY + (itemH - 8) / 2, textColor, false);

            if (isSelected) {
                Flat.COLOR.text(graphics, font, "✓", openDropdownX + openDropdownW - 14, itemY + (itemH - 8) / 2, TEXT_SELECTED, false);
            }
        }
    }

    private void openColorPicker(Color target, String title) {
        this.activePickerColor = target;
        this.originalPickerR = target.r;
        this.originalPickerG = target.g;
        this.originalPickerB = target.b;
        this.activePickerTitle = title;
        float[] hsb = java.awt.Color.RGBtoHSB(target.r, target.g, target.b, null);
        this.pickerHue = hsb[0];
        this.pickerSat = hsb[1];
        this.pickerBri = hsb[2];
        this.hexInput = String.format("#%02X%02X%02X", target.r, target.g, target.b);
        this.rInput = String.valueOf(target.r);
        this.gInput = String.valueOf(target.g);
        this.bInput = String.valueOf(target.b);
        this.focusedField = TextField.NONE;
        this.openDropdown = null;

        if (this.pickerWindowX < 0 || this.pickerWindowY < 0) {
            this.pickerWindowX = (this.width - pickerWindowW) / 2;
            this.pickerWindowY = (this.height - pickerWindowH) / 2;
        }
        this.pickerWindowX = Math.max(10, Math.min(this.width - pickerWindowW - 10, this.pickerWindowX));
        this.pickerWindowY = Math.max(10, Math.min(this.height - pickerWindowH - 10, this.pickerWindowY));
        isColorPickerOpen = true;
    }

    private Color getTargetTextColor(TextTarget target) {
        return switch (target) {
            case Selected -> TEXT_SELECTED;
            case Hovered -> TEXT_HOVERED;
            case Normal -> TEXT_NORMAL;
            case Other -> TEXT_OTHER;
        };
    }

    private Color getTargetTabColor(TabTarget target) {
        return switch (target) {
            case Selected -> BG_TAB_SELECTED;
            case Hovered -> BG_TAB_HOVERED;
            case Normal -> BG_TAB_NORMAL;
        };
    }

    private Color getTargetElementColor(ElementTarget target) {
        return switch (target) {
            case Selected -> BG_ELEMENT_SELECTED;
            case Hovered -> BG_ELEMENT_HOVERED;
            case Normal -> BG_ELEMENT_NORMAL;
        };
    }


    private Color getTargetBorderColor(BorderTarget target) {
        if (target == BorderTarget.Tabs) return BORDER_TABS;
        if (target == BorderTarget.Buttons) return BORDER_BUTTONS;
        return BORDER_MAIN;
    }

    private Color getTargetButtonColor(ButtonTarget target) {
        return switch (target) {
            case Selected -> BG_BUTTON_SELECTED;
            case Hovered -> BG_BUTTON_HOVERED;
            case Normal -> BG_BUTTON_NORMAL;
            case Other -> BG_BUTTON_OTHER;
        };
    }

    private void resetTextColor(TextTarget target) {
        switch (target) {
            case Selected -> TEXT_SELECTED.set(new Color(255, 0, 0, 255));
            case Hovered -> TEXT_HOVERED.set(new Color(255, 255, 255, 255));
            case Normal -> TEXT_NORMAL.set(new Color(160, 160, 160, 255));
            case Other -> TEXT_OTHER.set(new Color(194, 144, 144, 255));
        }
        ruinerpie.pancreas.Config.save();
        ruinerpie.pancreas.Toasts.get().info("Pancreas", "Reset " + target.name() + " text color.");
    }

    private void resetMainBg() {
        BG_MAIN.set(new Color(0, 0, 0, 255));
        ruinerpie.pancreas.Config.save();
        ruinerpie.pancreas.Toasts.get().info("Pancreas", "Reset Main Background to Black.");
    }

    private void resetHeadBg() {
        BG_HEAD.set(new Color(15, 0, 32, 255));
        ruinerpie.pancreas.Config.save();
        ruinerpie.pancreas.Toasts.get().info("Pancreas", "Reset Head Background to #0f0020.");
    }

    private void resetTabBg(TabTarget target) {
        switch (target) {
            case Selected -> BG_TAB_SELECTED.set(new Color(37, 0, 0, 255));
            case Hovered -> BG_TAB_HOVERED.set(new Color(55, 10, 10, 255));
            case Normal -> BG_TAB_NORMAL.set(new Color(37, 0, 0, 255));
        }
        ruinerpie.pancreas.Config.save();
        ruinerpie.pancreas.Toasts.get().info("Pancreas", "Reset " + target.name() + " Tab Background.");
    }

    private void resetElementBg(ElementTarget target) {
        switch (target) {
            case Selected -> BG_ELEMENT_SELECTED.set(new Color(27, 0, 32, 255));
            case Hovered -> BG_ELEMENT_HOVERED.set(new Color(42, 0, 50, 255));
            case Normal -> BG_ELEMENT_NORMAL.set(new Color(27, 0, 32, 255));
        }
        ruinerpie.pancreas.Config.save();
        ruinerpie.pancreas.Toasts.get().info("Pancreas", "Reset " + target.name() + " Element Background.");
    }

    private void resetButtonBg(ButtonTarget target) {
        switch (target) {
            case Selected -> BG_BUTTON_SELECTED.set(new Color(35, 0, 42, 255));
            case Hovered -> BG_BUTTON_HOVERED.set(new Color(30, 0, 36, 255));
            case Normal -> BG_BUTTON_NORMAL.set(new Color(27, 0, 32, 255));
            case Other -> BG_BUTTON_OTHER.set(new Color(15, 0, 32, 255));
        }
        ruinerpie.pancreas.Config.save();
        ruinerpie.pancreas.Toasts.get().info("Pancreas", "Reset " + target.name() + " Button Background.");
    }


    private void resetBorderColor(BorderTarget target) {
        if (target == BorderTarget.Tabs) BORDER_TABS.set(new Color(37, 0, 46, 255));
        else if (target == BorderTarget.Buttons) BORDER_BUTTONS.set(new Color(37, 0, 46, 255));
        else BORDER_MAIN.set(new Color(37, 0, 46, 255));
        ruinerpie.pancreas.Config.save();
        ruinerpie.pancreas.Toasts.get().info("Pancreas", "Reset " + target.name() + " Border Color.");
    }

    private void resetAllGuiColors() {
        guiFontChoice.reset();
        guiTextChoice.reset();
        guiTabChoice.reset();
        guiElementChoice.reset();
        guiButtonChoice.reset();
        guiBorderChoice.reset();
        guiPickerMode.reset();

        BG_MAIN.set(new Color(0, 0, 0, 255));
        BG_HEAD.set(new Color(15, 0, 32, 255));
        BORDER_MAIN.set(new Color(37, 0, 46, 255));
        BORDER_TABS.set(new Color(37, 0, 46, 255));
        BORDER_BUTTONS.set(new Color(37, 0, 46, 255));
        SEARCH_BG.set(new Color(15, 0, 32, 255));
        SEARCH_BORDER.set(new Color(37, 0, 46, 255));

        BG_TAB_SELECTED.set(new Color(37, 0, 0, 255));
        BG_TAB_HOVERED.set(new Color(55, 10, 10, 255));
        BG_TAB_NORMAL.set(new Color(37, 0, 0, 255));

        BG_ELEMENT_SELECTED.set(new Color(27, 0, 32, 255));
        BG_ELEMENT_HOVERED.set(new Color(42, 0, 50, 255));
        BG_ELEMENT_NORMAL.set(new Color(27, 0, 32, 255));

        BG_BUTTON_SELECTED.set(new Color(35, 0, 42, 255));
        BG_BUTTON_HOVERED.set(new Color(30, 0, 36, 255));
        BG_BUTTON_NORMAL.set(new Color(27, 0, 32, 255));
        BG_BUTTON_OTHER.set(new Color(15, 0, 32, 255));

        TEXT_SELECTED.set(new Color(255, 0, 0, 255));
        TEXT_HOVERED.set(new Color(255, 255, 255, 255));
        TEXT_NORMAL.set(new Color(160, 160, 160, 255));
        TEXT_OTHER.set(new Color(194, 144, 144, 255));

        TOGGLE_OFF.set(new Color(27, 0, 32, 255));
        TOGGLE_ON.set(new Color(101, 0, 255, 255));

        ruinerpie.pancreas.Config.save();
        ruinerpie.pancreas.Toasts.get().info("Pancreas", "All GUI Settings & Colors Reset!");
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        double mouseX = event.x();
        double mouseY = event.y();

        if (openDropdown != null) {
            Enum<?> current = (Enum<?>) openDropdown.get();
            Enum<?>[] constants = current.getDeclaringClass().getEnumConstants();
            int itemH = 18;
            int popupY = openDropdownY + openDropdownH + 1;
            int popupH = constants.length * itemH;

            if (mouseX >= openDropdownX && mouseX <= openDropdownX + openDropdownW && mouseY >= popupY && mouseY <= popupY + popupH) {
                int clickedIndex = (int) ((mouseY - popupY) / itemH);
                if (clickedIndex >= 0 && clickedIndex < constants.length) {
                    setChoice(openDropdown, constants[clickedIndex]);
                }
                openDropdown = null;
                return true;
            }

            if (mouseX >= openDropdownX && mouseX <= openDropdownX + openDropdownW && mouseY >= openDropdownY && mouseY <= openDropdownY + openDropdownH) {
                openDropdown = null;
                return true;
            }

            openDropdown = null;
        }

        if (isColorPickerOpen) {
            if (mouseX >= pickerWindowX && mouseX <= pickerWindowX + pickerWindowW && mouseY >= pickerWindowY && mouseY <= pickerWindowY + pickerWindowH) {
                if (mouseX >= pickerWindowX + pickerWindowW - 18 && mouseX <= pickerWindowX + pickerWindowW - 2 && mouseY >= pickerWindowY + 2 && mouseY <= pickerWindowY + 18) {
                    isColorPickerOpen = false;
                    focusedField = TextField.NONE;
                    if (activePickerColor != null) {
                        activePickerColor.r = originalPickerR;
                        activePickerColor.g = originalPickerG;
                        activePickerColor.b = originalPickerB;
                    }
                    ruinerpie.pancreas.Config.save();
                    return true;
                }

                if (mouseY >= pickerWindowY && mouseY <= pickerWindowY + 22) {
                    isDraggingPickerWindow = true;
                    dragOffsetWindowX = (int) mouseX - pickerWindowX;
                    dragOffsetWindowY = (int) mouseY - pickerWindowY;
                    return true;
                }

                int sbX = pickerWindowX + 12;
                int sbY = pickerWindowY + 28;
                int sbSize = 110;
                if (mouseX >= sbX && mouseX <= sbX + sbSize && mouseY >= sbY && mouseY <= sbY + sbSize) {
                    isDraggingSB = true;
                    float s = (float) (mouseX - sbX) / sbSize;
                    float b = 1.0f - (float) (mouseY - sbY) / sbSize;
                    if (s < 0) s = 0; if (s > 1) s = 1;
                    if (b < 0) b = 0; if (b > 1) b = 1;
                    pickerSat = s;
                    pickerBri = b;
                    updatePickerColorFromHSB();
                    return true;
                }

                int hueX = pickerWindowX + 12;
                int hueY = pickerWindowY + 146;
                int hueW = 110;
                int hueH = 12;
                if (mouseX >= hueX && mouseX <= hueX + hueW && mouseY >= hueY && mouseY <= hueY + hueH) {
                    isDraggingHue = true;
                    float h = (float) (mouseX - hueX) / hueW;
                    if (h < 0) h = 0; if (h > 1) h = 1;
                    pickerHue = h;
                    updatePickerColorFromHSB();
                    return true;
                }

                int rightX = pickerWindowX + 132;
                int ctrlW = pickerWindowW - 144;
                int modeBtnX = rightX + 38;
                int modeBtnW = ctrlW - 38;
                int modeBtnH = 16;
                if (mouseX >= modeBtnX && mouseX <= modeBtnX + modeBtnW && mouseY >= pickerWindowY + 60 && mouseY <= pickerWindowY + 60 + modeBtnH) {
                    openDropdown = (openDropdown == guiPickerMode) ? null : guiPickerMode;
                    openDropdownX = modeBtnX;
                    openDropdownY = pickerWindowY + 60;
                    openDropdownW = modeBtnW;
                    openDropdownH = modeBtnH;
                    return true;
                }

                if (guiPickerMode.get() == PickerMode.HEX) {
                    int boxX = rightX + 32;
                    int boxW = ctrlW - 32;
                    int hexBoxY = pickerWindowY + 88;
                    if (mouseX >= boxX && mouseX <= boxX + boxW && mouseY >= hexBoxY && mouseY <= hexBoxY + 18) {
                        focusedField = TextField.HEX;
                        hexInput = String.format("#%02X%02X%02X", activePickerColor.r, activePickerColor.g, activePickerColor.b);
                        return true;
                    }
                } else {
                    int boxX = rightX + 24;
                    int boxW = ctrlW - 24;
                    int rgbBoxY = pickerWindowY + 84;
                    if (mouseX >= boxX && mouseX <= boxX + boxW) {
                        if (mouseY >= rgbBoxY && mouseY <= rgbBoxY + 16) {
                            focusedField = TextField.R;
                            rInput = String.valueOf(activePickerColor.r);
                            return true;
                        }
                        if (mouseY >= rgbBoxY + 22 && mouseY <= rgbBoxY + 38) {
                            focusedField = TextField.G;
                            gInput = String.valueOf(activePickerColor.g);
                            return true;
                        }
                        if (mouseY >= rgbBoxY + 44 && mouseY <= rgbBoxY + 60) {
                            focusedField = TextField.B;
                            bInput = String.valueOf(activePickerColor.b);
                            return true;
                        }
                    }
                }

                int applyY = pickerWindowY + 168;
                int applyH = 20;
                if (mouseX >= rightX && mouseX <= rightX + ctrlW && mouseY >= applyY && mouseY <= applyY + applyH) {
                    isColorPickerOpen = false;
                    focusedField = TextField.NONE;
                    if (activePickerColor != null) {
                        originalPickerR = activePickerColor.r;
                        originalPickerG = activePickerColor.g;
                        originalPickerB = activePickerColor.b;
                    }
                    ruinerpie.pancreas.Config.save();
                    return true;
                }

                return true;
            }
        }

        int pad = 8;
        int configWidth = Math.min(this.width - 32, 820);
        int configHeight = Math.min(this.height - 32, 500);
        int startX = (this.width - configWidth) / 2;
        int startY = (this.height - configHeight) / 2;

        int topBarH = 28;
        int botH = 92;
        int topY = startY + pad;
        int innerW = configWidth - pad * 2;
        int searchW = (innerW - pad) / 2;
        int tabsW = (innerW - pad) / 2;
        int tabsX = startX + pad + searchW + pad;

        if (mouseX >= startX + pad && mouseX <= startX + pad + searchW && mouseY >= topY && mouseY <= topY + topBarH) {
            int searchBtnW = 20;
            int searchBtnX = startX + pad + searchW - searchBtnW;
            if (mouseX >= searchBtnX && mouseX <= searchBtnX + searchBtnW) {
                searchQuery = "";
            }
            searchFocused = true;
            focusedField = TextField.NONE;
        } else {
            searchFocused = false;
        }

        int tabSpacing = 4;
        int singleTabW = (tabsW - tabSpacing * 3) / 4;
        if (mouseY >= topY && mouseY <= topY + topBarH) {
            for (int i = 0; i < 4; i++) {
                int tx = tabsX + i * (singleTabW + tabSpacing);
                if (mouseX >= tx && mouseX <= tx + singleTabW) {
                    selectedTopTab = i;
                    openDropdown = null;
                    if (selectedTopTab == 1) {
                        Group currentGroup = Group.values()[Math.min(selectedFolderIndex, Group.values().length - 1)];
                        List<Feature> categoryFeatures = Features.get().in(currentGroup);
                        if (!categoryFeatures.isEmpty() && (selectedFeature == null || !categoryFeatures.contains(selectedFeature))) {
                            selectedFeature = categoryFeatures.get(0);
                        }
                    }
                    return true;
                }
            }
        }

        if (selectedTopTab != 3) {
            int botY = startY + configHeight - pad - botH;
            int favTabsX = startX + pad + 80;
            if (mouseY >= botY + 3 && mouseY <= botY + 19) {
                for (int i = 0; i < 3; i++) {
                    int ftx = favTabsX + i * 58;
                    if (mouseX >= ftx && mouseX <= ftx + 54) {
                        selectedFavTab = i;
                        return true;
                    }
                }
            }

            int favItemY = botY + 32;
            if (mouseY >= favItemY && mouseY <= favItemY + 22) {
                int favItemX = startX + pad + 10;
                List<Object> displayFavs = new ArrayList<>();
                if (selectedFavTab == 0 || selectedFavTab == 1) {
                    displayFavs.addAll(favoriteFeatures);
                }
                if (selectedFavTab == 0 || selectedFavTab == 2) {
                    displayFavs.addAll(savedFavoriteSettings);
                }
                Font font = this.font;
                for (Object fav : displayFavs) {
                    String title = (fav instanceof Feature f) ? "★ " + f.name : "★ " + fav.toString();
                    int boxW = Math.max(110, Flat.COLOR.width(font, title) + 16);
                    if (mouseX >= favItemX && mouseX <= favItemX + boxW) {
                        if (fav instanceof Feature f) {
                            selectedTopTab = 0;
                            selectedFeature = f;
                            openDropdown = null;
                        } else {
                            selectedTopTab = 2;
                            openDropdown = null;
                        }
                        return true;
                    }
                    favItemX += boxW + 8;
                }
            }
        }

        int panelW = (innerW - pad) / 2;
        int leftX = startX + pad;
        int rightX = leftX + panelW + pad;

        if (selectedTopTab == 0) {
            int midY = topY + topBarH + pad;
            int itemY = midY + 28;
            int rowH = 26;

            java.util.List<Object> results = new java.util.ArrayList<>();
            for (Feature f : Features.get().all()) {
                if (matchesSearch(f, searchQuery)) {
                    results.add(f);
                }
            }
            if (!searchQuery.isEmpty()) {
                String q = searchQuery.toLowerCase();
                if ("gui opacity".contains(q) || "global settings".contains(q) || "transparent".contains(q)) results.add("PSEUDO:2:GUI Opacity (Global Settings)");
                if ("interface font".contains(q) || "font".contains(q) || "visual".contains(q) || "gui settings".contains(q)) results.add("PSEUDO:3:Interface Font (GUI Settings)");
                if ("text".contains(q) || "colour".contains(q) || "color".contains(q) || "target".contains(q)) results.add("PSEUDO:3:Text GUI Setting");
                if ("background".contains(q) || "main background".contains(q) || "head background".contains(q)) results.add("PSEUDO:3:Background GUI Setting");
                if ("tab".contains(q) || "tabs".contains(q)) results.add("PSEUDO:3:Tab GUI Setting");
                if ("button".contains(q) || "buttons".contains(q)) results.add("PSEUDO:3:Button GUI Setting");
            }
            
            for (Object item : results) {
                if (mouseX >= rightX + 4 && mouseX <= rightX + panelW - 4 && mouseY >= itemY && mouseY <= itemY + rowH) {
                    if (item instanceof Feature) {
                        Feature f = (Feature) item;
                        int toggleX = rightX + 8;
                        int toggleY = itemY + (rowH - 12) / 2;
                        if (mouseX >= toggleX && mouseX <= toggleX + 22 && mouseY >= toggleY && mouseY <= toggleY + 12) {
                            f.toggle();
                            ruinerpie.pancreas.Config.save();
                        } else if (mouseX >= rightX + panelW - 25 && mouseX <= rightX + panelW - 5) {
                            if (favoriteFeatures.contains(f)) {
                                favoriteFeatures.remove(f);
                                savedFavorites.remove(f.id);
                            } else {
                                favoriteFeatures.add(f);
                                if (!savedFavorites.contains(f.id)) {
                                    savedFavorites.add(f.id);
                                }
                            }
                            ruinerpie.pancreas.Config.save();
                        } else {
                            selectedFeature = f;
                            openDropdown = null;
                        }
                    } else if (item instanceof String) {
                        String s = (String) item;
                        if (s.startsWith("PSEUDO:")) {
                            String[] parts = s.split(":", 3);
                            if (parts.length >= 2) {
                                try {
                                    selectedTopTab = Integer.parseInt(parts[1]);
                                    openDropdown = null;
                                } catch (Exception ignored) {}
                            }
                        }
                    }
                    return true;
                }
                itemY += rowH + 4;
            }
            if (selectedFeature != null) {
                int btnW = 58;
                int btnH = 16;
                int btnX = leftX + panelW - btnW - 6;
                int btnY = midY + 3;
                if (mouseX >= btnX && mouseX <= btnX + btnW && mouseY >= btnY && mouseY <= btnY + btnH) {
                    selectedFeature.toggle();
                    ruinerpie.pancreas.Config.save();
                    return true;
                }

                int settingY = midY + 48;
                for (ruinerpie.pancreas.values.ValueGroup group : selectedFeature.settings.groups()) {
                    settingY += 16;
                    for (ruinerpie.pancreas.values.Value<?> val : group.getSettings()) {
                        if (!val.isVisible()) continue;

                        if (val instanceof ruinerpie.pancreas.values.ChoiceValue<?> choice) {
                            int boxW = 84;
                            int boxH = 18;
                            int boxX = leftX + panelW - boxW - 8;
                            int boxY = settingY + (rowH - boxH) / 2;
                            if (mouseX >= boxX && mouseX <= boxX + boxW && mouseY >= boxY && mouseY <= boxY + boxH) {
                                openDropdown = (openDropdown == choice) ? null : choice;
                                openDropdownX = boxX;
                                openDropdownY = boxY;
                                openDropdownW = boxW;
                                openDropdownH = boxH;
                                return true;
                            }
                        } else if (val instanceof ruinerpie.pancreas.values.FlagValue flag) {
                            int boxW = 22;
                            int boxH = 12;
                            int boxX = leftX + panelW - boxW - 12;
                            int boxY = settingY + (rowH - boxH) / 2;
                            if (mouseX >= boxX && mouseX <= boxX + boxW && mouseY >= boxY && mouseY <= boxY + boxH) {
                                flag.set(!flag.get());
                                ruinerpie.pancreas.Config.save();
                                return true;
                            }
                        }
                        settingY += rowH;
                    }
                }
            }
        }

        if (selectedTopTab == 1) {
            int subTopY = topY + topBarH + pad;
            int subTopH = 24;
            int subTabSpacing = 4;
            int folderW = (innerW - subTabSpacing * 3) / 4;

            if (mouseY >= subTopY && mouseY <= subTopY + subTopH) {
                for (int i = 0; i < 4; i++) {
                    int fx = startX + pad + i * (folderW + subTabSpacing);
                    if (mouseX >= fx && mouseX <= fx + folderW) {
                        Group g = Group.values()[i];
                        List<Feature> categoryFeatures = Features.get().in(g);
                        if (categoryFeatures.isEmpty()) {
                            return true;
                        }
                        selectedFolderIndex = i;
                        openDropdown = null;
                        selectedFeature = categoryFeatures.get(0);
                        return true;
                    }
                }
            }

            int midY = subTopY + subTopH + pad;
            int itemY = midY + 28;
            int rowH = 26;

            Group currentGroup = Group.values()[Math.min(selectedFolderIndex, Group.values().length - 1)];
            List<Feature> categoryFeatures = Features.get().in(currentGroup);
            for (Feature f : categoryFeatures) {
                if (!matchesSearch(f, searchQuery)) {
                    continue;
                }
                int toggleX = rightX + 8;
                int toggleY = itemY + (rowH - 12) / 2;
                if (mouseX >= toggleX && mouseX <= toggleX + 22 && mouseY >= toggleY && mouseY <= toggleY + 12) {
                    f.toggle();
                    ruinerpie.pancreas.Config.save();
                    return true;
                }

                if (mouseX >= rightX + panelW - 25 && mouseX <= rightX + panelW - 5 && mouseY >= itemY && mouseY <= itemY + rowH) {
                    if (favoriteFeatures.contains(f)) {
                        favoriteFeatures.remove(f);
                        savedFavorites.remove(f.id);
                    } else {
                        favoriteFeatures.add(f);
                        if (!savedFavorites.contains(f.id)) {
                            savedFavorites.add(f.id);
                        }
                    }
                    ruinerpie.pancreas.Config.save();
                    return true;
                }

                if (mouseX >= rightX + 4 && mouseX <= rightX + panelW - 4 && mouseY >= itemY && mouseY <= itemY + rowH) {
                    selectedFeature = f;
                    openDropdown = null;
                    return true;
                }

                itemY += rowH + 4;
            }

            if (selectedFeature != null) {
                int btnW = 58;
                int btnH = 16;
                int btnX = leftX + panelW - btnW - 6;
                int btnY = midY + 3;
                if (mouseX >= btnX && mouseX <= btnX + btnW && mouseY >= btnY && mouseY <= btnY + btnH) {
                    selectedFeature.toggle();
                    ruinerpie.pancreas.Config.save();
                    return true;
                }

                int settingY = midY + 48;
                for (ValueGroup group : selectedFeature.settings.groups()) {
                    settingY += 16;
                    for (Value<?> val : group.getSettings()) {
                        if (!val.isVisible()) continue;

                        if (val instanceof ChoiceValue<?> choice) {
                            int boxW = 84;
                            int boxH = 18;
                            int boxX = leftX + panelW - boxW - 8;
                            int boxY = settingY + (rowH - boxH) / 2;
                            if (mouseX >= boxX && mouseX <= boxX + boxW && mouseY >= boxY && mouseY <= boxY + boxH) {
                                openDropdown = (openDropdown == choice) ? null : choice;
                                openDropdownX = boxX;
                                openDropdownY = boxY;
                                openDropdownW = boxW;
                                openDropdownH = boxH;
                                return true;
                            }
                        } else if (val instanceof FlagValue flag) {
                            int boxW = 22;
                            int boxH = 12;
                            int boxX = leftX + panelW - boxW - 12;
                            int boxY = settingY + (rowH - boxH) / 2;
                            if (mouseX >= boxX && mouseX <= boxX + boxW && mouseY >= boxY && mouseY <= boxY + boxH) {
                                flag.set(!flag.get());
                                ruinerpie.pancreas.Config.save();
                                return true;
                            }
                        }
                        settingY += rowH;
                    }
                }
            }
        } else if (selectedTopTab == 2) {
            int midY = topY + topBarH + pad;
            int mainX = startX + pad;
            int sliderX = mainX + innerW - 120;
            int sliderY = midY + 36;
            int sliderW = 100;
            int sliderH = 12;

            if (mouseX >= mainX + innerW - 175 && mouseX <= mainX + innerW - 155 && mouseY >= midY + 30 && mouseY <= midY + 50) {
                if (savedFavoriteSettings.contains("GUI Opacity") || savedFavoriteSettings.contains("Transparent GUI")) {
                    savedFavoriteSettings.remove("GUI Opacity");
                    savedFavoriteSettings.remove("Transparent GUI");
                } else {
                    savedFavoriteSettings.add("GUI Opacity");
                }
                ruinerpie.pancreas.Config.save();
                return true;
            }
            
            if (mouseX >= sliderX && mouseX <= sliderX + sliderW && mouseY >= sliderY && mouseY <= sliderY + sliderH) {
                isDraggingSettingsTrans = true;
                double ratio = Math.max(0.0, Math.min(1.0, (mouseX - sliderX) / (double) sliderW));
                bgTransparency = Math.round(ratio * 100.0);
                ruinerpie.pancreas.Config.save();
                return true;
            }

            int btnY = midY + 70;
            int btnW = 110;
            int btnH = 20;
            if (mouseY >= btnY && mouseY <= btnY + btnH) {
                if (mouseX >= mainX + 14 && mouseX <= mainX + 14 + btnW) {
                    ruinerpie.pancreas.Config.save();
                    ruinerpie.pancreas.Toasts.get().success("Pancreas", "Configuration saved!");
                    return true;
                } else if (mouseX >= mainX + 134 && mouseX <= mainX + 134 + btnW) {
                    for (Feature f : Features.get().all()) {
                        f.resetSettings();
                    }
                    bgTransparency = 30.0;
                    resetAllGuiColors();
                    ruinerpie.pancreas.Config.save();
                    ruinerpie.pancreas.Toasts.get().info("Pancreas", "Reset to default settings.");
                    return true;
                }
            }
        } else if (selectedTopTab == 3) {
            int midY = topY + topBarH + pad;
            int midH = (startY + configHeight - pad) - midY;
            int boxX = startX + pad;
            int leftMargin = boxX + 16;
            int curY = midY + 30;

            int fontBtnX = leftMargin + 115;
            int fontBtnW = 150;
            int fontBtnH = 18;
            if (mouseX >= fontBtnX && mouseX <= fontBtnX + fontBtnW && mouseY >= curY && mouseY <= curY + fontBtnH) {
                openDropdown = (openDropdown == guiFontChoice) ? null : guiFontChoice;
                openDropdownX = fontBtnX;
                openDropdownY = curY;
                openDropdownW = fontBtnW;
                openDropdownH = fontBtnH;
                return true;
            }

            curY += 26 + 18;
            int textDropX = leftMargin + 115;
            int textDropW = 100;
            int textDropH = 18;
            if (mouseX >= textDropX && mouseX <= textDropX + textDropW && mouseY >= curY && mouseY <= curY + textDropH) {
                openDropdown = (openDropdown == guiTextChoice) ? null : guiTextChoice;
                openDropdownX = textDropX;
                openDropdownY = curY;
                openDropdownW = textDropW;
                openDropdownH = textDropH;
                return true;
            }

            int swatchW = 28;
            int changeBtnW = 95;
            int changeBtnH = 18;
            int textChangeX = textDropX + textDropW + 10 + swatchW + 10;
            if (mouseX >= textChangeX && mouseX <= textChangeX + changeBtnW && mouseY >= curY && mouseY <= curY + changeBtnH) {
                TextTarget tt = guiTextChoice.get();
                openColorPicker(getTargetTextColor(tt), tt.name() + " Text");
                return true;
            }

            int resetBtnW = 55;
            int resetBtnH = 18;
            int textResetX = textChangeX + changeBtnW + 8;
            if (mouseX >= textResetX && mouseX <= textResetX + resetBtnW && mouseY >= curY && mouseY <= curY + resetBtnH) {
                resetTextColor(guiTextChoice.get());
                return true;
            }

            curY += 26 + 18;
            int mbChangeX = leftMargin + 115 + swatchW + 10;
            if (mouseX >= mbChangeX && mouseX <= mbChangeX + changeBtnW && mouseY >= curY && mouseY <= curY + changeBtnH) {
                openColorPicker(BG_MAIN, "Main Background");
                return true;
            }
            int mbResetX = mbChangeX + changeBtnW + 8;
            if (mouseX >= mbResetX && mouseX <= mbResetX + resetBtnW && mouseY >= curY && mouseY <= curY + resetBtnH) {
                resetMainBg();
                return true;
            }

            curY += 22;
            int hbChangeX = leftMargin + 115 + swatchW + 10;
            if (mouseX >= hbChangeX && mouseX <= hbChangeX + changeBtnW && mouseY >= curY && mouseY <= curY + changeBtnH) {
                openColorPicker(BG_HEAD, "Head Background");
                return true;
            }
            int hbResetX = hbChangeX + changeBtnW + 8;
            if (mouseX >= hbResetX && mouseX <= hbResetX + resetBtnW && mouseY >= curY && mouseY <= curY + resetBtnH) {
                resetHeadBg();
                return true;
            }

            curY += 22;
            int tabDropX = leftMargin + 115;
            int tabDropW = 100;
            int tabDropH = 18;
            if (mouseX >= tabDropX && mouseX <= tabDropX + tabDropW && mouseY >= curY && mouseY <= curY + tabDropH) {
                openDropdown = (openDropdown == guiTabChoice) ? null : guiTabChoice;
                openDropdownX = tabDropX;
                openDropdownY = curY;
                openDropdownW = tabDropW;
                openDropdownH = tabDropH;
                return true;
            }
            int tabChangeX = tabDropX + tabDropW + 10 + swatchW + 10;
            if (mouseX >= tabChangeX && mouseX <= tabChangeX + changeBtnW && mouseY >= curY && mouseY <= curY + changeBtnH) {
                TabTarget tt = guiTabChoice.get();
                openColorPicker(getTargetTabColor(tt), "Tab (" + tt.name() + ") Background");
                return true;
            }
            int tabResetX = tabChangeX + changeBtnW + 8;
            if (mouseX >= tabResetX && mouseX <= tabResetX + resetBtnW && mouseY >= curY && mouseY <= curY + resetBtnH) {
                resetTabBg(guiTabChoice.get());
                return true;
            }


            curY += 22;
            int btnToggleX = leftMargin;
            int btnToggleW = 150;
            int btnToggleH = 18;
            if (mouseX >= btnToggleX && mouseX <= btnToggleX + btnToggleW && mouseY >= curY && mouseY <= curY + btnToggleH) {
                buttonsGroupExpanded = !buttonsGroupExpanded;
                return true;
            }

            if (buttonsGroupExpanded) {
                curY += 22;
                int bDropX = leftMargin + 115;
                int bDropW = 100;
                int bDropH = 18;
                if (mouseX >= bDropX && mouseX <= bDropX + bDropW && mouseY >= curY && mouseY <= curY + bDropH) {
                    openDropdown = (openDropdown == guiButtonChoice) ? null : guiButtonChoice;
                    openDropdownX = bDropX;
                    openDropdownY = curY;
                    openDropdownW = bDropW;
                    openDropdownH = bDropH;
                    return true;
                }
                int bChangeX = bDropX + bDropW + 10 + swatchW + 10;
                if (mouseX >= bChangeX && mouseX <= bChangeX + changeBtnW && mouseY >= curY && mouseY <= curY + changeBtnH) {
                    ButtonTarget bt = guiButtonChoice.get();
                    openColorPicker(getTargetButtonColor(bt), "Button (" + bt.name() + ")");
                    return true;
                }
                int bResetX = bChangeX + changeBtnW + 8;
                if (mouseX >= bResetX && mouseX <= bResetX + resetBtnW && mouseY >= curY && mouseY <= curY + resetBtnH) {
                    resetButtonBg(guiButtonChoice.get());
                    return true;
                }
            }

            curY += 26 + 18;
            int borderDropX = leftMargin + 115;
            int borderDropW = 100;
            int borderDropH = 18;
            if (mouseX >= borderDropX && mouseX <= borderDropX + borderDropW && mouseY >= curY && mouseY <= curY + borderDropH) {
                openDropdown = (openDropdown == guiBorderChoice) ? null : guiBorderChoice;
                openDropdownX = borderDropX;
                openDropdownY = curY;
                openDropdownW = borderDropW;
                openDropdownH = borderDropH;
                return true;
            }
            int borderChangeX = borderDropX + borderDropW + 10 + swatchW + 10;
            if (mouseX >= borderChangeX && mouseX <= borderChangeX + changeBtnW && mouseY >= curY && mouseY <= curY + changeBtnH) {
                BorderTarget bt = guiBorderChoice.get();
                openColorPicker(getTargetBorderColor(bt), "Border (" + bt.name() + ")");
                return true;
            }
            int borderResetX = borderChangeX + changeBtnW + 8;
            if (mouseX >= borderResetX && mouseX <= borderResetX + resetBtnW && mouseY >= curY && mouseY <= curY + resetBtnH) {
                resetBorderColor(guiBorderChoice.get());
                return true;
            }

            curY += 26 + 18;
            int elemDropX = leftMargin + 115;
            int elemDropW = 100;
            int elemDropH = 18;
            if (mouseX >= elemDropX && mouseX <= elemDropX + elemDropW && mouseY >= curY && mouseY <= curY + elemDropH) {
                openDropdown = (openDropdown == guiElementChoice) ? null : guiElementChoice;
                openDropdownX = elemDropX;
                openDropdownY = curY;
                openDropdownW = elemDropW;
                openDropdownH = elemDropH;
                return true;
            }
            int elemChangeX = elemDropX + elemDropW + 10 + swatchW + 10;
            if (mouseX >= elemChangeX && mouseX <= elemChangeX + changeBtnW && mouseY >= curY && mouseY <= curY + changeBtnH) {
                ElementTarget et = guiElementChoice.get();
                openColorPicker(getTargetElementColor(et), "Element (" + et.name() + ")");
                return true;
            }
            int elemResetX = elemChangeX + changeBtnW + 8;
            if (mouseX >= elemResetX && mouseX <= elemResetX + resetBtnW && mouseY >= curY && mouseY <= curY + resetBtnH) {
                resetElementBg(guiElementChoice.get());
                return true;
            }


            int rstAllY = midY + midH - 26;
            int rstAllW = 160;
            int rstAllX = leftMargin;
            if (mouseX >= rstAllX && mouseX <= rstAllX + rstAllW && mouseY >= rstAllY && mouseY <= rstAllY + 18) {
                resetAllGuiColors();
                return true;
            }

        }

        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dragX, double dragY) {
        double mouseX = event.x();
        double mouseY = event.y();

        if (isDraggingSettingsTrans) {
            int pad = 8;
            int configWidth = Math.min(this.width - 32, 820);
            int startX = (this.width - configWidth) / 2;
            int innerW = configWidth - pad * 2;
            int mainX = startX + pad;
            int sliderX = mainX + innerW - 120;
            int sliderW = 100;
            double ratio = Math.max(0.0, Math.min(1.0, (mouseX - sliderX) / (double) sliderW));
            bgTransparency = Math.round(ratio * 100.0);
            return true;
        }

        if (isColorPickerOpen && activePickerColor != null) {
            if (isDraggingPickerWindow) {
                pickerWindowX = (int) mouseX - dragOffsetWindowX;
                pickerWindowY = (int) mouseY - dragOffsetWindowY;
                pickerWindowX = Math.max(0, Math.min(this.width - pickerWindowW, pickerWindowX));
                pickerWindowY = Math.max(0, Math.min(this.height - pickerWindowH, pickerWindowY));
                return true;
            }

            if (isDraggingSB) {
                int sbX = pickerWindowX + 12;
                int sbY = pickerWindowY + 28;
                int sbSize = 110;
                float s = (float) (mouseX - sbX) / sbSize;
                float b = 1.0f - (float) (mouseY - sbY) / sbSize;
                if (s < 0.0f) s = 0.0f; if (s > 1.0f) s = 1.0f;
                if (b < 0.0f) b = 0.0f; if (b > 1.0f) b = 1.0f;
                pickerSat = s;
                pickerBri = b;
                updatePickerColorFromHSB();
                return true;
            }

            if (isDraggingHue) {
                int hueX = pickerWindowX + 12;
                int hueW = 110;
                float h = (float) (mouseX - hueX) / hueW;
                if (h < 0.0f) h = 0.0f; if (h > 1.0f) h = 1.0f;
                pickerHue = h;
                updatePickerColorFromHSB();
                return true;
            }
        }

        return super.mouseDragged(event, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        boolean wasDraggingTrans = isDraggingSettingsTrans;
        isDraggingSettingsTrans = false;
        isDraggingPickerWindow = false;
        isDraggingHue = false;
        isDraggingSB = false;
        if (wasDraggingTrans) {
            ruinerpie.pancreas.Config.save();
            return true;
        }
        return super.mouseReleased(event);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void setChoice(ChoiceValue<?> choice, Enum<?> val) {
        ((ChoiceValue) choice).set(val);
        ruinerpie.pancreas.Config.save();
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        int keyCode = event.key();
        
        boolean ctrl = com.mojang.blaze3d.platform.InputConstants.isKeyDown(this.minecraft.getWindow(), org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_CONTROL) ||
                       com.mojang.blaze3d.platform.InputConstants.isKeyDown(this.minecraft.getWindow(), org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT_CONTROL);
        if (ctrl && (searchFocused || focusedField != TextField.NONE)) {
            if (keyCode == GLFW.GLFW_KEY_A) {
                isTextSelected = true;
                return true;
            } else if (keyCode == GLFW.GLFW_KEY_C) {
                if (isTextSelected) {
                    if (searchFocused) this.minecraft.keyboardHandler.setClipboard(searchQuery);
                    else if (focusedField == TextField.HEX) this.minecraft.keyboardHandler.setClipboard(hexInput);
                    else if (focusedField == TextField.R) this.minecraft.keyboardHandler.setClipboard(rInput);
                    else if (focusedField == TextField.G) this.minecraft.keyboardHandler.setClipboard(gInput);
                    else if (focusedField == TextField.B) this.minecraft.keyboardHandler.setClipboard(bInput);
                }
                return true;
            } else if (keyCode == GLFW.GLFW_KEY_V) {
                String paste = this.minecraft.keyboardHandler.getClipboard();
                if (paste != null) {
                    if (searchFocused) { searchQuery = paste; isTextSelected = false; }
                    else if (focusedField == TextField.HEX) { hexInput = paste; applyTextInput(); isTextSelected = false; }
                    else if (focusedField == TextField.R) { rInput = paste; applyTextInput(); isTextSelected = false; }
                    else if (focusedField == TextField.G) { gInput = paste; applyTextInput(); isTextSelected = false; }
                    else if (focusedField == TextField.B) { bInput = paste; applyTextInput(); isTextSelected = false; }
                }
                return true;
            } else if (keyCode == GLFW.GLFW_KEY_X) {
                if (isTextSelected) {
                    if (searchFocused) { this.minecraft.keyboardHandler.setClipboard(searchQuery); searchQuery = ""; isTextSelected = false; }
                    else if (focusedField == TextField.HEX) { this.minecraft.keyboardHandler.setClipboard(hexInput); hexInput = ""; applyTextInput(); isTextSelected = false; }
                    else if (focusedField == TextField.R) { this.minecraft.keyboardHandler.setClipboard(rInput); rInput = ""; applyTextInput(); isTextSelected = false; }
                    else if (focusedField == TextField.G) { this.minecraft.keyboardHandler.setClipboard(gInput); gInput = ""; applyTextInput(); isTextSelected = false; }
                    else if (focusedField == TextField.B) { this.minecraft.keyboardHandler.setClipboard(bInput); bInput = ""; applyTextInput(); isTextSelected = false; }
                }
                return true;
            }
        }

        if (isColorPickerOpen) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
                isColorPickerOpen = false;
                openDropdown = null;
                focusedField = TextField.NONE;
                if (activePickerColor != null) {
                    activePickerColor.r = originalPickerR;
                    activePickerColor.g = originalPickerG;
                    activePickerColor.b = originalPickerB;
                }
                ruinerpie.pancreas.Config.save();
                return true;
            }
        }

        if (openDropdown != null) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
                openDropdown = null;
                return true;
            }
        }

        if (searchFocused) {
            if (keyCode == GLFW.GLFW_KEY_BACKSPACE && !searchQuery.isEmpty()) {
                if (isTextSelected) {
                    searchQuery = "";
                    isTextSelected = false;
                } else {
                    searchQuery = searchQuery.substring(0, searchQuery.length() - 1);
                }
                return true;
            }
            if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
                searchFocused = false;
                return true;
            }
        }

        if (focusedField != TextField.NONE) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
                focusedField = TextField.NONE;
                return true;
            }
            if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
                if (isTextSelected) {
                    if (focusedField == TextField.HEX) hexInput = "";
                    else if (focusedField == TextField.R) rInput = "";
                    else if (focusedField == TextField.G) gInput = "";
                    else if (focusedField == TextField.B) bInput = "";
                    applyTextInput();
                    isTextSelected = false;
                    return true;
                }
                if (focusedField == TextField.HEX && !hexInput.isEmpty()) {
                    hexInput = hexInput.substring(0, hexInput.length() - 1);
                    applyTextInput();
                } else if (focusedField == TextField.R && !rInput.isEmpty()) {
                    rInput = rInput.substring(0, rInput.length() - 1);
                    applyTextInput();
                } else if (focusedField == TextField.G && !gInput.isEmpty()) {
                    gInput = gInput.substring(0, gInput.length() - 1);
                    applyTextInput();
                } else if (focusedField == TextField.B && !bInput.isEmpty()) {
                    bInput = bInput.substring(0, bInput.length() - 1);
                    applyTextInput();
                }
                return true;
            }
        }

        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            if (selectedTopTab != 0) {
                selectedTopTab = 0;
                return true;
            }
            this.onClose();
            return true;
        }

        if (!searchFocused && focusedField == TextField.NONE) {
            if (keyCode == GLFW.GLFW_KEY_P || (ruinerpie.pancreas.Pancreas.KEY_CONFIG != null && ruinerpie.pancreas.Pancreas.KEY_CONFIG.matches(event))) {
                this.onClose();
                return true;
            }
        }

        return super.keyPressed(event);
    }

    private void applyTextInput() {
        if (activePickerColor == null) return;
        try {
            if (focusedField == TextField.HEX) {
                String hex = hexInput.replace("#", "").trim();
                if (hex.length() == 6) {
                    int rgb = (int) Long.parseLong(hex, 16);
                    activePickerColor.r = (rgb >> 16) & 0xFF;
                    activePickerColor.g = (rgb >> 8) & 0xFF;
                    activePickerColor.b = rgb & 0xFF;
                    float[] hsb = java.awt.Color.RGBtoHSB(activePickerColor.r, activePickerColor.g, activePickerColor.b, null);
                    pickerHue = hsb[0];
                    pickerSat = hsb[1];
                    pickerBri = hsb[2];
                    rInput = String.valueOf(activePickerColor.r);
                    gInput = String.valueOf(activePickerColor.g);
                    bInput = String.valueOf(activePickerColor.b);
                }
            } else {
                int r = Integer.parseInt(rInput.isEmpty() ? "0" : rInput);
                int g = Integer.parseInt(gInput.isEmpty() ? "0" : gInput);
                int b = Integer.parseInt(bInput.isEmpty() ? "0" : bInput);
                if (r < 0) r = 0; if (r > 255) r = 255;
                if (g < 0) g = 0; if (g > 255) g = 255;
                if (b < 0) b = 0; if (b > 255) b = 255;
                activePickerColor.r = r;
                activePickerColor.g = g;
                activePickerColor.b = b;
                float[] hsb = java.awt.Color.RGBtoHSB(r, g, b, null);
                pickerHue = hsb[0];
                pickerSat = hsb[1];
                pickerBri = hsb[2];
                hexInput = String.format("#%02X%02X%02X", r, g, b);
            }
        } catch (Exception ignored) {}
    }

    @Override
    public boolean charTyped(CharacterEvent event) {
        if (searchFocused) {
            if (isTextSelected) {
                searchQuery = "";
                isTextSelected = false;
            }
            searchQuery += (char) event.codepoint();
            return true;
        }
        if (focusedField != TextField.NONE) {
            if (isTextSelected) {
                if (focusedField == TextField.HEX) hexInput = "";
                else if (focusedField == TextField.R) rInput = "";
                else if (focusedField == TextField.G) gInput = "";
                else if (focusedField == TextField.B) bInput = "";
                isTextSelected = false;
            }
            char c = (char) event.codepoint();
            if (focusedField == TextField.HEX) {
                if (String.valueOf(c).matches("[0-9a-fA-F#]")) {
                    hexInput += c;
                    if (hexInput.length() > 7) hexInput = hexInput.substring(0, 7);
                    applyTextInput();
                }
            } else {
                if (Character.isDigit(c)) {
                    if (focusedField == TextField.R) { rInput += c; if (rInput.length() > 3) rInput = rInput.substring(0, 3); }
                    if (focusedField == TextField.G) { gInput += c; if (gInput.length() > 3) gInput = gInput.substring(0, 3); }
                    if (focusedField == TextField.B) { bInput += c; if (bInput.length() > 3) bInput = bInput.substring(0, 3); }
                    applyTextInput();
                }
            }
            return true;
        }
        return super.charTyped(event);
    }

    @Override
    public void onClose() {
        if (isColorPickerOpen && activePickerColor != null) {
            activePickerColor.r = originalPickerR;
            activePickerColor.g = originalPickerG;
            activePickerColor.b = originalPickerB;
            isColorPickerOpen = false;
        }
        ruinerpie.pancreas.Config.save();
        if (this.minecraft != null) {
            this.minecraft.setScreen(this.parent);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean isInGameUi() {
        return true;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
    }
}


