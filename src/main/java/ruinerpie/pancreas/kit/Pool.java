package ruinerpie.pancreas.kit;

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

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Queue;
import java.util.function.Supplier;

public class Pool<T> {
    private final Queue<T> pool = new ArrayDeque<>();
    private final Supplier<T> factory;

    public Pool(Supplier<T> factory) {
        this.factory = factory;
    }

    public T get() {
        T item = pool.poll();
        return item != null ? item : factory.get();
    }

    public void free(T item) {
        if (item != null) pool.offer(item);
    }

    public void freeAll(Collection<T> items) {
        if (items != null) {
            for (T item : items) free(item);
            items.clear();
        }
    }
}