package ruinerpie.pancreas.signals;

import ruinerpie.pancreas.Signal;
import ruinerpie.pancreas.kit.ActionKind;

public class KeyPress extends Signal {
    public final int key;
    public final ActionKind action;

    public KeyPress(int key, ActionKind action) {
        this.key = key;
        this.action = action;
    }
}
