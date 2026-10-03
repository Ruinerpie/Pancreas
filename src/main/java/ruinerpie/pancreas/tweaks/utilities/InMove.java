package ruinerpie.pancreas.tweaks.utilities;

import ruinerpie.pancreas.mixin.*;
import ruinerpie.pancreas.*;
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

import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.util.Mth;
import net.minecraft.world.item.CreativeModeTabs;

import static org.lwjgl.glfw.GLFW.*;

public class InMove extends Feature {
    public enum Screens {
        GUI,
        Inventory,
        Both
    }

    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    private final Value<Screens> screens = sgGeneral.add(new ChoiceValue.Builder<Screens>()
        .name("guis")
        .description("Which GUIs to move in.")
        .defaultValue(Screens.Inventory)
        .build()
    );

    public final Value<Boolean> jump = sgGeneral.add(new FlagValue.Builder()
        .name("jump")
        .description("Allows you to jump while in GUIs.")
        .defaultValue(true)
        .onChanged(aBoolean -> {
            if (isActive() && !aBoolean) mc.options.keyJump.setDown(false);
        })
        .build()
    );

    public final Value<Boolean> sneak = sgGeneral.add(new FlagValue.Builder()
        .name("sneak")
        .description("Allows you to sneak while in GUIs.")
        .defaultValue(true)
        .onChanged(aBoolean -> {
            if (isActive() && !aBoolean) mc.options.keyShift.setDown(false);
        })
        .build()
    );

    public final Value<Boolean> sprint = sgGeneral.add(new FlagValue.Builder()
        .name("sprint")
        .description("Allows you to sprint while in GUIs.")
        .defaultValue(true)
        .onChanged(aBoolean -> {
            if (isActive() && !aBoolean) mc.options.keySprint.setDown(false);
        })
        .build()
    );

    private final Value<Boolean> arrowsRotate = sgGeneral.add(new FlagValue.Builder()
        .name("arrows-rotate")
        .description("Allows you to use your arrow keys to rotate while in GUIs.")
        .defaultValue(true)
        .build()
    );

    private final Value<Double> rotateSpeed = sgGeneral.add(new DoubleValue.Builder()
        .name("rotate-speed")
        .description("Rotation speed while in GUIs.")
        .defaultValue(4.0)
        .min(0.0)
        .build()
    );

    public InMove() {
        super(Group.Utilities, "roam", "Allows you to perform various actions while in GUIs.");
    }

    @Override
    public void onDeactivate() {
        mc.options.keyUp.setDown(false);
        mc.options.keyDown.setDown(false);
        mc.options.keyLeft.setDown(false);
        mc.options.keyRight.setDown(false);

        if (jump.get()) mc.options.keyJump.setDown(false);
        if (sneak.get()) mc.options.keyShift.setDown(false);
        if (sprint.get()) mc.options.keySprint.setDown(false);
    }

    public boolean disableSpace() {
        return isActive() && jump.get() && mc.options.keyJump.isDefault();
    }

    public boolean disableArrows() {
        return isActive() && arrowsRotate.get();
    }

    @Listen
    private void onKey(KeyPress event) {
        onInput(event.key(), event.action);
    }

    @Listen
    private void onButton(MouseClick event) {
        onInput(event.button(), event.action);
    }

    private void onInput(int key, ActionKind action) {
        if (skip()) return;

        pass(mc.options.keyUp, key, action);
        pass(mc.options.keyDown, key, action);
        pass(mc.options.keyLeft, key, action);
        pass(mc.options.keyRight, key, action);

        if (jump.get()) pass(mc.options.keyJump, key, action);
        if (sneak.get()) pass(mc.options.keyShift, key, action);
        if (sprint.get()) pass(mc.options.keySprint, key, action);
    }

    @Listen
    private void onRender3D(Render3D event) {
        if (skip() || mc.player == null) return;

        float rotationDelta = Math.min((float) (rotateSpeed.get() * event.frameTime * 20f), 100);

        Spectre freecam = Features.get().get(Spectre.class);

        if (arrowsRotate.get()) {
            if (freecam == null || !freecam.isActive()) {
                float yaw = mc.player.getYRot();
                float pitch = mc.player.getXRot();

                if (InputKit.isKeyPressed(GLFW_KEY_LEFT)) yaw -= rotationDelta;
                if (InputKit.isKeyPressed(GLFW_KEY_RIGHT)) yaw += rotationDelta;
                if (InputKit.isKeyPressed(GLFW_KEY_UP)) pitch -= rotationDelta;
                if (InputKit.isKeyPressed(GLFW_KEY_DOWN)) pitch += rotationDelta;

                pitch = Mth.clamp(pitch, -90, 90);

                mc.player.setYRot(yaw);
                mc.player.setXRot(pitch);
            } else {
                double dy = 0, dx = 0;

                if (InputKit.isKeyPressed(GLFW_KEY_LEFT)) dy = -rotationDelta;
                if (InputKit.isKeyPressed(GLFW_KEY_RIGHT)) dy = rotationDelta;
                if (InputKit.isKeyPressed(GLFW_KEY_UP)) dx = -rotationDelta;
                if (InputKit.isKeyPressed(GLFW_KEY_DOWN)) dx = rotationDelta;

                freecam.changeLookDirection(dy, dx);
            }
        }
    }

    private void pass(KeyMapping bind, int key, ActionKind action) {
        if (InputKit.getKey(bind) != key) return;
        if (action == ActionKind.Press) bind.setDown(true);
        if (action == ActionKind.Release) bind.setDown(false);
    }

    public boolean skip() {
        if (mc.screen == null ||
            (mc.screen instanceof CreativeModeInventoryScreen && CreativeScreenMixin.pancreas$getSelectedTab() == CreativeModeTabs.searchTab())
            || mc.screen instanceof ChatScreen
            || mc.screen instanceof SignEditScreen
            || mc.screen instanceof AnvilScreen
            || mc.screen instanceof AbstractCommandBlockEditScreen
            || mc.screen instanceof StructureBlockEditScreen) return true;
        if (screens.get() == Screens.GUI && !(mc.screen instanceof Panel)) return true;
        return screens.get() == Screens.Inventory && mc.screen instanceof Panel;
    }
}