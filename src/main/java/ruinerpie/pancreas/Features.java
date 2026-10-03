package ruinerpie.pancreas;

import ruinerpie.pancreas.tweaks.cheats.*;
import ruinerpie.pancreas.tweaks.extras.*;
import ruinerpie.pancreas.tweaks.misc.*;
import ruinerpie.pancreas.tweaks.utilities.*;

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

        register(new Sustenance());
        register(new Silhouette());
        register(new ElytraPlus());
        register(new Companion());
        register(new pESP());
        register(new Delve());
        register(new EchoBody());
        register(new Ascend());
        register(new Reload());
        register(new LastSeen());
        register(new Parallel());
        register(new Truestone());
        register(new Fluidity());
        register(new Nudge());
        register(new Slick());
        register(new Trove());
        register(new Chrono());
        register(new Vein());
        register(new Oreline());

        register(new Ambient());
        register(new Rapid());
        register(new Toss());
        register(new Feast());
        register(new Lifeline());
        register(new Mender());
        register(new Stockpile());
        register(new Revive());
        register(new Forge());
        register(new Arsenal());
        register(new Reaper());
        register(new Glide());
        register(new Stare());
        register(new Flask());
        register(new Spectre());
        register(new Peek());
        register(new Handwork());
        register(new Gleam());
        register(new Gravity());
        register(new Grid());
        register(new Pivot());
        register(new Alias());
        register(new Guard());
        register(new Anchor());
        register(new Sidearm());
        register(new Portal());
        register(new Steady());
        register(new Tiptoe());
        register(new Puff());
        register(new Arc());
        register(new Scope());

        register(new Shield());
        register(new Return());
        register(new Beacon());
        register(new ChatPlus());
        register(new Sort());
        register(new Greet());
        register(new Alert());
        register(new Filter());
        register(new Mute());
        register(new ChatS());

        register(new Idle());
        register(new Gear());
        register(new Feed());
        register(new Leap());
        register(new Totem());
        register(new TabPlus());
        register(new Glimpse());
        register(new Edge());
        register(new Frost());
        register(new BarStack());
        register(new Breadcrumb());
        register(new OutlineDec());
        register(new F5Plus());
        register(new FRC());
        register(new Sun());
        register(new InMove());
        register(new Label());
        register(new OverlayPlus());
        register(new SafeEdge());
        register(new Run());
        register(new Mark());
    }

    public void register(Feature m) {
        modules.add(m);
        byClass.put(m.getClass(), m);
        byId.put(m.id.toLowerCase(), m);
        byId.put(m.name.toLowerCase(), m);
        byId.put(m.getClass().getSimpleName().toLowerCase(), m);
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
