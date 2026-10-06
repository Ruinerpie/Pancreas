package ruinerpie.pancreas.signals;

import ruinerpie.pancreas.Signal;

public class Render3D extends Signal {
    public final float tickDelta;

    public Render3D(float tickDelta) {
        this.tickDelta = tickDelta;
    }

    public Render3D() {
        this(0.0f);
    }
}
