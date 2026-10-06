package ruinerpie.pancreas.signals;

import ruinerpie.pancreas.Signal;

public class ClipAtLedge extends Signal {
    private boolean clip;

    public boolean isClip() { return clip; }
    public void setClip(boolean clip) { this.clip = clip; }
}
