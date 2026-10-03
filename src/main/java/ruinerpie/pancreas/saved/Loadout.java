package ruinerpie.pancreas.saved;

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

import java.util.Map;

public class Loadout {
    public String name;
    public long savedAt;
    public Map<String, Boolean> tweakStates;
    public Map<String, Map<String, Object>> settingValues;
    public Map<String, Integer> keybinds;

    public Loadout(String name, long savedAt, Map<String, Boolean> tweakStates,
                   Map<String, Map<String, Object>> settingValues, Map<String, Integer> keybinds) {
        this.name = name;
        this.savedAt = savedAt;
        this.tweakStates = tweakStates;
        this.settingValues = settingValues;
        this.keybinds = keybinds;
    }
}