package ruinerpie.pancreas.screens;

import ruinerpie.pancreas.saved.Macro;
import ruinerpie.pancreas.saved.MacroStep;
import ruinerpie.pancreas.saved.Macros;
import ruinerpie.pancreas.theme.Theme;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class MacroScreen extends Panel {
    private Macro editingAction = null;
    private boolean listeningForKeybind = false;
    private int scrollOffset = 0;

    private String newActionName = "";
    private boolean createInputFocused = false;

    private MacroStep.Kind newStepKind = MacroStep.Kind.TOGGLE_TWEAK;
    private String newStepTarget = "";
    private boolean stepTargetFocused = false;

    public MacroScreen(Screen parent) {
        super(Component.literal("Pancreas — Macros"), parent, NavTab.SYSTEMS);
    }

    public MacroScreen() {
        this(null);
    }

    @Override
    protected String getSearchPlaceholder() {
        return "Search actions...";
    }

    private static String getKeyName(int key) {
        if (key <= 0) return "NONE";
        String glfwName = GLFW.glfwGetKeyName(key, 0);
        if (glfwName != null && !glfwName.isEmpty()) return glfwName.toUpperCase();
        return switch (key) {
            case GLFW.GLFW_KEY_SPACE -> "SPACE";
            case GLFW.GLFW_KEY_LEFT_SHIFT -> "LSHIFT";
            case GLFW.GLFW_KEY_RIGHT_SHIFT -> "RSHIFT";
            case GLFW.GLFW_KEY_LEFT_CONTROL -> "LCTRL";
            case GLFW.GLFW_KEY_RIGHT_CONTROL -> "RCTRL";
            case GLFW.GLFW_KEY_LEFT_ALT -> "LALT";
            case GLFW.GLFW_KEY_RIGHT_ALT -> "RALT";
            case GLFW.GLFW_KEY_TAB -> "TAB";
            default -> "KEY_" + key;
        };
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
        graphics.fill(panelX + 4, panelY + 4, panelX + 8, panelY + 4 + headerH, Theme.accentDiamond.getPacked());

        boolean backHov = isHovered(mouseX, mouseY, panelX + 12, panelY + 6, 50, 22);
        renderSlotButton(graphics, panelX + 12, panelY + 6, 50, 22, "< Back", backHov, false, Theme.accentDiamond.getPacked());

        if (editingAction == null) {
            renderListView(graphics, mouseX, mouseY, panelX, panelY, panelW, panelH, headerH);
        } else {
            renderEditorView(graphics, mouseX, mouseY, panelX, panelY, panelW, panelH, headerH);
        }
    }

    private void renderListView(GuiGraphicsExtractor graphics, int mouseX, int mouseY, int panelX, int panelY, int panelW, int panelH, int headerH) {
        graphics.text(font, "QUICKACTIONS", panelX + 70, panelY + 13, Theme.textAccent.getPacked(), false);
        graphics.text(font, "Key-bound macro sequences and command chains (" + Macros.get().all().size() + ")", panelX + 160, panelY + 13, Theme.textMuted.getPacked(), false);

        int botY = panelY + panelH - 34;
        renderBeveledPanel(graphics, panelX + 6, botY, panelW - 12, 28, true, Theme.surfaceAlt.getPacked(), 0);

        graphics.text(font, "New Action:", panelX + 14, botY + 10, Theme.textAccent.getPacked(), false);
        int inputX = panelX + 85;
        int inputW = panelW - 225;
        int inFocus = createInputFocused ? Theme.focusRing.getPacked() : 0;
        renderBeveledPanel(graphics, inputX, botY + 4, inputW, 20, true, Theme.surface.getPacked(), inFocus);

        if (newActionName.isEmpty()) {
            graphics.text(font, "Enter action name...", inputX + 6, botY + 10, Theme.textDisabled.getPacked(), false);
        } else {
            String cursor = createInputFocused && (System.currentTimeMillis() / 450 % 2 == 0) ? "_" : "";
            graphics.text(font, newActionName + cursor, inputX + 6, botY + 10, Theme.textAccent.getPacked(), false);
        }

        int createBtnW = 100;
        int createBtnX = inputX + inputW + 8;
        boolean cHov = isHovered(mouseX, mouseY, createBtnX, botY + 4, createBtnW, 20);
        renderSlotButton(graphics, createBtnX, botY + 4, createBtnW, 20, "+ CREATE", cHov, false, Theme.accentDiamond.getPacked());

        int listY = panelY + headerH + 8;
        int startY = listY - scrollOffset;

        List<Macro> allActions = Macros.get().all();
        List<Macro> filtered = new ArrayList<>();
        for (Macro a : allActions) {
            if (searchQuery.isEmpty() || a.name.toLowerCase().contains(searchQuery.toLowerCase()) || a.id.toLowerCase().contains(searchQuery.toLowerCase())) {
                filtered.add(a);
            }
        }

        if (filtered.isEmpty()) {
            String emptyMsg = allActions.isEmpty() ? "No actions created yet. Create one below to chain commands and tweaks." : "No actions matching '" + searchQuery + "'.";
            graphics.centeredText(font, emptyMsg, panelX + panelW / 2, listY + 30, Theme.textDisabled.getPacked());
        } else {
            int curY = startY;
            int rowH = 24;

            for (Macro a : filtered) {
                if (curY + rowH >= listY && curY <= botY - 6 - rowH) {
                    boolean hovered = isHovered(mouseX, mouseY, panelX + 8, curY, panelW - 16, rowH);
                    int fill = hovered ? Theme.surfaceRaised.getPacked() : Theme.surfaceAlt.getPacked();

                    renderBeveledPanel(graphics, panelX + 8, curY, panelW - 16, rowH, false, fill, hovered ? Theme.accentDiamond.getPacked() : 0);

                    graphics.fill(panelX + 14, curY + 7, panelX + 20, curY + 13, Theme.accentDiamond.getPacked());
                    graphics.text(font, a.name, panelX + 26, curY + 8, hovered ? Theme.textAccent.getPacked() : Theme.text.getPacked(), false);

                    String keyStr = "[" + getKeyName(a.keybind) + "]";
                    graphics.text(font, keyStr, panelX + 160, curY + 8, a.keybind > 0 ? Theme.accentLapis.getPacked() : Theme.textDisabled.getPacked(), false);

                    graphics.text(font, a.steps.size() + " steps", panelX + 240, curY + 8, Theme.textDisabled.getPacked(), false);

                    int runW = 44;
                    int delW = 50;
                    int editW = 46;
                    int delX = panelX + panelW - delW - 14;
                    int editX = delX - editW - 6;
                    int runX = editX - runW - 6;

                    boolean runHov = isHovered(mouseX, mouseY, runX, curY + 3, runW, 18);
                    renderSlotButton(graphics, runX, curY + 3, runW, 18, "RUN", runHov, false, Theme.accentEmerald.getPacked());

                    boolean editHov = isHovered(mouseX, mouseY, editX, curY + 3, editW, 18);
                    renderSlotButton(graphics, editX, curY + 3, editW, 18, "EDIT", editHov, false, Theme.accentDiamond.getPacked());

                    boolean delHov = isHovered(mouseX, mouseY, delX, curY + 3, delW, 18);
                    renderSlotButton(graphics, delX, curY + 3, delW, 18, "DEL", delHov, false, Theme.accentRed.getPacked());
                }
                curY += rowH + 3;
            }
        }
    }

    private void renderEditorView(GuiGraphicsExtractor graphics, int mouseX, int mouseY, int panelX, int panelY, int panelW, int panelH, int headerH) {
        graphics.text(font, "EDIT ACTION: " + editingAction.name, panelX + 70, panelY + 13, Theme.textAccent.getPacked(), false);

        String keyText = listeningForKeybind ? "PRESS KEY..." : "KEY: " + getKeyName(editingAction.keybind);
        int keyW = font.width(keyText) + 16;
        int keyX = panelX + panelW - keyW - 14;
        boolean keyHov = isHovered(mouseX, mouseY, keyX, panelY + 6, keyW, 22);
        int keyAccent = listeningForKeybind ? Theme.accentGold.getPacked() : (editingAction.keybind > 0 ? Theme.accentLapis.getPacked() : 0);
        renderSlotButton(graphics, keyX, panelY + 6, keyW, 22, keyText, keyHov, listeningForKeybind, keyAccent);

        int addY = panelY + panelH - 34;
        renderBeveledPanel(graphics, panelX + 6, addY, panelW - 12, 28, true, Theme.surfaceAlt.getPacked(), 0);

        int kindW = 110;
        int kindX = panelX + 12;
        boolean kindHov = isHovered(mouseX, mouseY, kindX, addY + 4, kindW, 20);
        renderSlotButton(graphics, kindX, addY + 4, kindW, 20, newStepKind.name(), kindHov, false, Theme.accentDiamond.getPacked());

        int inX = kindX + kindW + 8;
        int inW = panelW - kindW - 120 - 40;
        int inFocus = stepTargetFocused ? Theme.focusRing.getPacked() : 0;
        renderBeveledPanel(graphics, inX, addY + 4, inW, 20, true, Theme.surface.getPacked(), inFocus);

        String placeholder = switch (newStepKind) {
            case TOGGLE_TWEAK, ENABLE_TWEAK, DISABLE_TWEAK -> "tweak id (e.g. sustenance)";
            case OPEN_SCREEN -> "screen (crew, shadow, loadouts, hud, etc.)";
            case RUN_COMMAND -> "command (e.g. /gamemode creative)";
            case WAIT_TICKS -> "ticks to wait (e.g. 20)";
        };

        if (newStepTarget.isEmpty()) {
            graphics.text(font, placeholder, inX + 6, addY + 10, Theme.textDisabled.getPacked(), false);
        } else {
            String cursor = stepTargetFocused && (System.currentTimeMillis() / 450 % 2 == 0) ? "_" : "";
            graphics.text(font, newStepTarget + cursor, inX + 6, addY + 10, Theme.textAccent.getPacked(), false);
        }

        int addBtnW = 80;
        int addBtnX = inX + inW + 8;
        boolean addHov = isHovered(mouseX, mouseY, addBtnX, addY + 4, addBtnW, 20);
        renderSlotButton(graphics, addBtnX, addY + 4, addBtnW, 20, "+ STEP", addHov, false, Theme.accentDiamond.getPacked());

        int listY = panelY + headerH + 8;
        int startY = listY - scrollOffset;

        if (editingAction.steps.isEmpty()) {
            graphics.centeredText(font, "No steps in this action yet. Add your first step below.", panelX + panelW / 2, listY + 30, Theme.textDisabled.getPacked());
        } else {
            int curY = startY;
            int rowH = 24;

            for (int i = 0; i < editingAction.steps.size(); i++) {
                MacroStep step = editingAction.steps.get(i);
                if (curY + rowH >= listY && curY <= addY - 6 - rowH) {
                    boolean hovered = isHovered(mouseX, mouseY, panelX + 8, curY, panelW - 16, rowH);
                    int fill = hovered ? Theme.surfaceRaised.getPacked() : Theme.surfaceAlt.getPacked();

                    renderBeveledPanel(graphics, panelX + 8, curY, panelW - 16, rowH, false, fill, hovered ? Theme.accentDiamond.getPacked() : 0);

                    graphics.text(font, (i + 1) + ". [" + step.kind.name() + "]", panelX + 16, curY + 8, Theme.accentDiamond.getPacked(), false);
                    graphics.text(font, step.target != null ? step.target : "", panelX + 170, curY + 8, Theme.text.getPacked(), false);

                    int btnSize = 18;
                    int delX = panelX + panelW - btnSize - 14;
                    int downX = delX - btnSize - 4;
                    int upX = downX - btnSize - 4;

                    boolean upHov = isHovered(mouseX, mouseY, upX, curY + 3, btnSize, btnSize);
                    boolean downHov = isHovered(mouseX, mouseY, downX, curY + 3, btnSize, btnSize);
                    boolean delHov = isHovered(mouseX, mouseY, delX, curY + 3, btnSize, btnSize);

                    renderSlotButton(graphics, upX, curY + 3, btnSize, btnSize, "^", upHov, false, 0);
                    renderSlotButton(graphics, downX, curY + 3, btnSize, btnSize, "v", downHov, false, 0);
                    renderSlotButton(graphics, delX, curY + 3, btnSize, btnSize, "X", delHov, false, Theme.accentRed.getPacked());
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
                createInputFocused = false;
                stepTargetFocused = false;
                return true;
            }

            int panelX = (width - panelW) / 2;
            int panelY = 60;
            int panelH = height - 68;
            int headerH = 26;

            if (isHovered(mouseX, mouseY, panelX + 12, panelY + 6, 50, 22)) {
                playUiSound();
                if (editingAction != null) {
                    editingAction = null;
                } else if (minecraft != null) {
                    minecraft.setScreen(parent != null ? parent : new SystemHub());
                }
                return true;
            }

            if (editingAction == null) {
                
                int botY = panelY + panelH - 34;
                int inputX = panelX + 85;
                int inputW = panelW - 225;
                if (isHovered(mouseX, mouseY, inputX, botY + 4, inputW, 20)) {
                    playUiSound();
                    createInputFocused = true;
                    searchFocused = false;
                    return true;
                } else {
                    createInputFocused = false;
                }

                int createBtnW = 100;
                int createBtnX = inputX + inputW + 8;
                if (isHovered(mouseX, mouseY, createBtnX, botY + 4, createBtnW, 20)) {
                    submitCreate();
                    return true;
                }

                int listY = panelY + headerH + 8;
                int startY = listY - scrollOffset;

                List<Macro> allActions = Macros.get().all();
                List<Macro> filtered = new ArrayList<>();
                for (Macro a : allActions) {
                    if (searchQuery.isEmpty() || a.name.toLowerCase().contains(searchQuery.toLowerCase()) || a.id.toLowerCase().contains(searchQuery.toLowerCase())) {
                        filtered.add(a);
                    }
                }

                int curY = startY;
                int rowH = 24;

                for (Macro a : filtered) {
                    if (curY + rowH >= listY && curY <= botY - 6 - rowH) {
                        int runW = 44;
                        int delW = 50;
                        int editW = 46;
                        int delX = panelX + panelW - delW - 14;
                        int editX = delX - editW - 6;
                        int runX = editX - runW - 6;

                        if (isHovered(mouseX, mouseY, runX, curY + 3, runW, 18)) {
                            playUiSound();
                            Macros.get().trigger(a.id);
                            return true;
                        }
                        if (isHovered(mouseX, mouseY, editX, curY + 3, editW, 18)) {
                            playUiSound();
                            editingAction = a;
                            scrollOffset = 0;
                            return true;
                        }
                        if (isHovered(mouseX, mouseY, delX, curY + 3, delW, 18)) {
                            playUiSound();
                            Macros.get().delete(a.id);
                            return true;
                        }
                    }
                    curY += rowH + 3;
                }
            } else {
                
                String keyText = listeningForKeybind ? "PRESS KEY..." : "KEY: " + getKeyName(editingAction.keybind);
                int keyW = font.width(keyText) + 16;
                int keyX = panelX + panelW - keyW - 14;
                if (isHovered(mouseX, mouseY, keyX, panelY + 6, keyW, 22)) {
                    playUiSound();
                    listeningForKeybind = !listeningForKeybind;
                    return true;
                }

                int addY = panelY + panelH - 34;
                int kindW = 110;
                int kindX = panelX + 12;
                if (isHovered(mouseX, mouseY, kindX, addY + 4, kindW, 20)) {
                    playUiSound();
                    int nextOrd = (newStepKind.ordinal() + 1) % MacroStep.Kind.values().length;
                    newStepKind = MacroStep.Kind.values()[nextOrd];
                    return true;
                }

                int inX = kindX + kindW + 8;
                int inW = panelW - kindW - 120 - 40;
                if (isHovered(mouseX, mouseY, inX, addY + 4, inW, 20)) {
                    playUiSound();
                    stepTargetFocused = true;
                    searchFocused = false;
                    return true;
                } else {
                    stepTargetFocused = false;
                }

                int addBtnW = 80;
                int addBtnX = inX + inW + 8;
                if (isHovered(mouseX, mouseY, addBtnX, addY + 4, addBtnW, 20)) {
                    submitAddStep();
                    return true;
                }

                int listY = panelY + headerH + 8;
                int startY = listY - scrollOffset;
                int curY = startY;
                int rowH = 24;

                for (int i = 0; i < editingAction.steps.size(); i++) {
                    if (curY + rowH >= listY && curY <= addY - 6 - rowH) {
                        int btnSize = 18;
                        int delX = panelX + panelW - btnSize - 14;
                        int downX = delX - btnSize - 4;
                        int upX = downX - btnSize - 4;

                        if (isHovered(mouseX, mouseY, upX, curY + 3, btnSize, btnSize) && i > 0) {
                            playUiSound();
                            MacroStep step = editingAction.steps.remove(i);
                            editingAction.steps.add(i - 1, step);
                            Macros.get().save();
                            return true;
                        }
                        if (isHovered(mouseX, mouseY, downX, curY + 3, btnSize, btnSize) && i < editingAction.steps.size() - 1) {
                            playUiSound();
                            MacroStep step = editingAction.steps.remove(i);
                            editingAction.steps.add(i + 1, step);
                            Macros.get().save();
                            return true;
                        }
                        if (isHovered(mouseX, mouseY, delX, curY + 3, btnSize, btnSize)) {
                            playUiSound();
                            Macros.get().removeStep(editingAction.id, i);
                            return true;
                        }
                    }
                    curY += rowH + 3;
                }
            }
        }
        return super.mouseClicked(event, isFocused);
    }

    private void submitCreate() {
        if (!newActionName.trim().isEmpty()) {
            playUiSound();
            Macros.get().create(newActionName.trim());
            newActionName = "";
            createInputFocused = false;
        }
    }

    private void submitAddStep() {
        if (!newStepTarget.trim().isEmpty() && editingAction != null) {
            playUiSound();
            MacroStep step = new MacroStep(newStepKind, newStepTarget.trim());
            Macros.get().addStep(editingAction.id, step);
            newStepTarget = "";
            stepTargetFocused = false;
        }
    }

    @Override
    public boolean charTyped(CharacterEvent event) {
        if (createInputFocused && event.isAllowedChatCharacter()) {
            newActionName += event.codepointAsString();
            return true;
        }
        if (stepTargetFocused && event.isAllowedChatCharacter()) {
            newStepTarget += event.codepointAsString();
            return true;
        }
        return super.charTyped(event);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        int key = event.key();

        if (listeningForKeybind && editingAction != null) {
            if (key == GLFW.GLFW_KEY_ESCAPE || key == GLFW.GLFW_KEY_BACKSPACE) {
                Macros.get().bindKey(editingAction.id, -1);
            } else {
                Macros.get().bindKey(editingAction.id, key);
            }
            listeningForKeybind = false;
            playUiSound();
            return true;
        }

        if (createInputFocused) {
            if (key == GLFW.GLFW_KEY_BACKSPACE) {
                if (!newActionName.isEmpty()) newActionName = newActionName.substring(0, newActionName.length() - 1);
                return true;
            }
            if (key == GLFW.GLFW_KEY_ENTER || key == GLFW.GLFW_KEY_KP_ENTER) {
                submitCreate();
                return true;
            }
            if (key == GLFW.GLFW_KEY_ESCAPE) {
                createInputFocused = false;
                return true;
            }
        }

        if (stepTargetFocused) {
            if (key == GLFW.GLFW_KEY_BACKSPACE) {
                if (!newStepTarget.isEmpty()) newStepTarget = newStepTarget.substring(0, newStepTarget.length() - 1);
                return true;
            }
            if (key == GLFW.GLFW_KEY_ENTER || key == GLFW.GLFW_KEY_KP_ENTER) {
                submitAddStep();
                return true;
            }
            if (key == GLFW.GLFW_KEY_ESCAPE) {
                stepTargetFocused = false;
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