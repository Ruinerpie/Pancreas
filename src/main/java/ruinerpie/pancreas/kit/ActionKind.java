package ruinerpie.pancreas.kit;

import ruinerpie.pancreas.*;
import ruinerpie.pancreas.mixin.*;
import ruinerpie.pancreas.mixin.accessor.*;
import ruinerpie.pancreas.values.*;
import ruinerpie.pancreas.signals.*;
import ruinerpie.pancreas.kit.*;
import ruinerpie.pancreas.draw.*;

public enum ActionKind {
    Press,
    Repeat,
    Release;

    public static ActionKind get(int action) {
        return switch (action) {
            case 1 -> Press;
            case 2 -> Repeat;
            default -> Release;
        };
    }
}
