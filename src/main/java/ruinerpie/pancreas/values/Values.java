package ruinerpie.pancreas.values;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Values {
    private final ValueGroup defaultGroup = new ValueGroup("General");
    private final List<ValueGroup> groups = new ArrayList<>();

    public Values() {
        groups.add(defaultGroup);
    }

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
