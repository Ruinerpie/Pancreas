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

import java.util.LinkedHashMap;
import java.util.Map;

public class MacroStep {
    public enum Kind {
        TOGGLE_TWEAK,
        ENABLE_TWEAK,
        DISABLE_TWEAK,
        OPEN_SCREEN,
        RUN_COMMAND,
        WAIT_TICKS
    }

    public Kind kind;
    public String target;
    public Map<String, Object> args = new LinkedHashMap<>();

    public MacroStep() {}

    public MacroStep(Kind kind, String target) {
        this.kind = kind;
        this.target = target;
    }

    public MacroStep(Kind kind, String target, Map<String, Object> args) {
        this.kind = kind;
        this.target = target;
        this.args = args != null ? args : new LinkedHashMap<>();
    }
}