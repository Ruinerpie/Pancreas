package ruinerpie.pancreas.signals;

import ruinerpie.pancreas.Signal;
import ruinerpie.pancreas.kit.ActionKind;

public class MouseClick extends Signal {
    public final int button;
    public final ActionKind action;

    public MouseClick(int button, ActionKind action) {
        this.button = button;
        this.action = action;
    }
}
