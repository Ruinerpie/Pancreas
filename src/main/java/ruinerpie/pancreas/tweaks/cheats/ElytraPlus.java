package ruinerpie.pancreas.tweaks.cheats;

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

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Items;

public class ElytraPlus extends Feature {
    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    public final FlagValue antiConsume = sgGeneral.add(new FlagValue.Builder()
        .name("anti-consume")
        .description("Prevents firework rocket consumption when boosting.")
        .defaultValue(true)
        .build());

    public final IntValue fireworkDuration = sgGeneral.add(new IntValue.Builder()
        .name("firework-duration")
        .description("Duration of firework boost ticks.")
        .defaultValue(0)
        .min(0)
        .max(255)
        .build());

    public final FlagValue playSound = sgGeneral.add(new FlagValue.Builder()
        .name("play-sound")
        .description("Plays firework rocket launch sound.")
        .defaultValue(true)
        .build());

    public final KeyValue keybind = sgGeneral.add(new KeyValue.Builder()
        .name("keybind")
        .description("Key to trigger manual elytra boost.")
        .defaultValue(-1)
        .build());

    public ElytraPlus() {
        super(Group.Cheats, "elytraplus", "Provides unlimited firework boost acceleration while gliding with elytra.");
    }

    @Listen
    private void onInteractItem(InteractItem event) {
        if (Pancreas.mc.player == null) return;
        if (!Pancreas.mc.player.isFallFlying()) return;

        InteractionHand hand = event.hand;
        if (Pancreas.mc.player.getItemInHand(hand).getItem() == Items.FIREWORK_ROCKET) {
            if (antiConsume.get()) {
                event.cancel();
                applyBoost();
            }
        }
    }

    @Listen
    private void onTick(WorldTick event) {
        if (Pancreas.mc.player == null || !Pancreas.mc.player.isFallFlying()) return;

        if (keybind.get() > 0 && org.lwjgl.glfw.GLFW.glfwGetKey(Pancreas.mc.getWindow().handle(), keybind.get()) == org.lwjgl.glfw.GLFW.GLFW_PRESS) {
            applyBoost();
        }
    }

    private void applyBoost() {
        if (Pancreas.mc.player == null) return;
        var look = Pancreas.mc.player.getLookAngle();
        var delta = Pancreas.mc.player.getDeltaMovement();
        Pancreas.mc.player.setDeltaMovement(delta.add(look.x * 0.1 + (look.x * 1.5 - delta.x) * 0.5,
            look.y * 0.1 + (look.y * 1.5 - delta.y) * 0.5,
            look.z * 0.1 + (look.z * 1.5 - delta.z) * 0.5));

        if (playSound.get() && Pancreas.mc.level != null) {
            Pancreas.mc.level.playLocalSound(Pancreas.mc.player.getX(), Pancreas.mc.player.getY(), Pancreas.mc.player.getZ(),
                SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.PLAYERS, 3.0F, 1.0F, false);
        }
    }
}