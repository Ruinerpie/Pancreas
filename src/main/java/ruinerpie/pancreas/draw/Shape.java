package ruinerpie.pancreas.draw;

import ruinerpie.pancreas.*;
import ruinerpie.pancreas.mixin.*;
import ruinerpie.pancreas.mixin.accessor.*;
import ruinerpie.pancreas.values.*;
import ruinerpie.pancreas.signals.*;
import ruinerpie.pancreas.kit.*;
import ruinerpie.pancreas.draw.*;

public enum Shape {
    Lines(true, false),
    Sides(false, true),
    Both(true, true);

    public final boolean lines;
    public final boolean sides;

    Shape(boolean lines, boolean sides) {
        this.lines = lines;
        this.sides = sides;
    }

    public boolean lines() { return lines; }
    public boolean sides() { return sides; }
}
