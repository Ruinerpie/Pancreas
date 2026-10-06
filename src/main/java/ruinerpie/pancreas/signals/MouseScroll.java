package ruinerpie.pancreas.signals;

import ruinerpie.pancreas.Signal;

public class MouseScroll extends Signal {
    public final double vertical;

    public MouseScroll(double vertical) {
        this.vertical = vertical;
    }
}
