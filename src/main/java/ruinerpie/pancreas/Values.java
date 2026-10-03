package ruinerpie.pancreas;

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
import java.util.Collections;
import java.util.List;

public class Values {
    private final List<ValueGroup> groups = new ArrayList<>();
    private final ValueGroup defaultGroup = createGroup("General");

    public ValueGroup getDefaultGroup() {
        return defaultGroup;
    }

    public ValueGroup createGroup(String name) {
        ValueGroup group = new ValueGroup(name);
        groups.add(group);
        return group;
    }

    public List<ValueGroup> groups() {
        return Collections.unmodifiableList(groups);
    }
}