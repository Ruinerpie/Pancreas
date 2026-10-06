package ruinerpie.pancreas;

import ruinerpie.pancreas.tweaks.utilities.FRC;
import ruinerpie.pancreas.tweaks.utilities.EdgeShift;

import java.util.*;

public final class Features {
    private static final Features INSTANCE = new Features();

    public static Features get() {
        return INSTANCE;
    }

    private final List<Feature> modules = new ArrayList<>();
    private final Map<Class<? extends Feature>, Feature> byClass = new HashMap<>();
    private final Map<String, Feature> byId = new HashMap<>();

    public void init() {
        if (!modules.isEmpty()) return;

        register(new FRC());
        register(new EdgeShift());
    }

    public void register(Feature m) {
        modules.add(m);
        byClass.put(m.getClass(), m);
        byId.put(m.id.toLowerCase(), m);
        byId.put(m.name.toLowerCase(), m);
        byId.put(m.getClass().getSimpleName().toLowerCase(), m);
        if (m instanceof FRC) {
            byId.put("quickcast", m);
            byId.put("fastuse", m);
            byId.put("fastrightclick", m);
        } else if (m instanceof EdgeShift) {
            byId.put("precipice", m);
            byId.put("safewalk", m);
            byId.put("safeedge", m);
            byId.put("edgeshift", m);
            byId.put("edge-shift", m);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Feature> T get(Class<T> type) {
        return (T) byClass.get(type);
    }

    public Feature get(String id) {
        if (id == null) return null;
        return byId.get(id.toLowerCase());
    }

    public List<Feature> all() {
        return Collections.unmodifiableList(modules);
    }

    public List<Feature> in(Group c) {
        return modules.stream().filter(m -> m.category == c).toList();
    }
}
