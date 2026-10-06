package ruinerpie.pancreas.values;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ValueGroup {
    private final String name;
    private final List<Value<?>> settings = new ArrayList<>();

    public ValueGroup(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    public <V extends Value<?>> V add(V setting) {
        settings.add(setting);
        return setting;
    }

    public List<Value<?>> getSettings() {
        return Collections.unmodifiableList(settings);
    }
}
