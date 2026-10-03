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

import java.util.ArrayList;
import java.util.List;

public class Macro {
    public String id;
    public String name;
    public int keybind = -1;
    public List<MacroStep> steps = new ArrayList<>();

    public Macro(String id, String name, int keybind, List<MacroStep> steps) {
        this.id = id;
        this.name = name;
        this.keybind = keybind;
        this.steps = steps != null ? steps : new ArrayList<>();
    }

    public Macro(String id, String name) {
        this(id, name, -1, new ArrayList<>());
    }
}