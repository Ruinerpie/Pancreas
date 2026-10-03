package ruinerpie.pancreas.tweaks.utilities;

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

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class Sun extends Feature {
    public enum Mode {
        Gamma,
        Potion,
        Luminance
    }

    public enum LightType {
        Block,
        Sky
    }

    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    public final Value<Mode> mode = sgGeneral.add(new ChoiceValue.Builder<Mode>()
        .name("mode")
        .description("Illumination method.")
        .defaultValue(Mode.Gamma)
        .onChanged(m -> reloadRenderer())
        .build()
    );

    public final Value<LightType> lightType = sgGeneral.add(new ChoiceValue.Builder<LightType>()
        .name("light-type")
        .description("Target light layer in Luminance mode.")
        .defaultValue(LightType.Block)
        .visible(() -> mode.get() == Mode.Luminance)
        .onChanged(l -> reloadRenderer())
        .build()
    );

    public final Value<Integer> minimumLightLevel = sgGeneral.add(new IntValue.Builder()
        .name("minimum-light-level")
        .description("Forced floor light level.")
        .defaultValue(8)
        .min(0)
        .max(15)
        .sliderRange(0, 15)
        .visible(() -> mode.get() == Mode.Luminance)
        .onChanged(v -> reloadRenderer())
        .build()
    );

    public Sun() {
        super(Group.Utilities, "sun", "Illuminates dark environments with full brightness.");
    }

    @Override
    public void onActivate() {
        reloadRenderer();
    }

    @Override
    public void onDeactivate() {
        if (mc.player != null && mode.get() == Mode.Potion) {
            mc.player.removeEffect(MobEffects.NIGHT_VISION);
        }
        reloadRenderer();
    }

    private void reloadRenderer() {
        if (mc.levelRenderer != null) {
            mc.levelRenderer.allChanged();
        }
    }

    @Listen
    private void onTick(WorldTick event) {
        if (mc.player == null) return;

        if (mode.get() == Mode.Potion) {
            mc.player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 260, 0, false, false, false));
        }
    }
}