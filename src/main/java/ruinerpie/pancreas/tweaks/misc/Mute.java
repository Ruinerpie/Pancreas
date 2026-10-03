package ruinerpie.pancreas.tweaks.misc;

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

import net.minecraft.sounds.SoundEvent;

import java.util.ArrayList;
import java.util.List;

public class Mute extends Feature {
    private final ValueGroup sgGeneral = settings.getDefaultGroup();

    public final Value<List<SoundEvent>> blockedSounds = sgGeneral.add(new SoundListValue.Builder()
        .name("blocked-sounds")
        .description("Mute selected sound effects.")
        .defaultValue(new ArrayList<>())
        .build()
    );

    public Mute() {
        super(Group.Misc, "mute", "Cancels and mutes selected sound effects.");
    }

    @Listen
    private void onPlaySound(PlaySound event) {
        if (event.sound == null || event.sound.getIdentifier() == null) return;
        List<SoundEvent> blocked = blockedSounds.get();
        if (blocked == null || blocked.isEmpty()) return;

        for (SoundEvent sound : blocked) {
            if (sound != null && sound.location().equals(event.sound.getIdentifier())) {
                event.cancel();
                break;
            }
        }
    }
}