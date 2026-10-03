package ruinerpie.pancreas.kit;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class TargetKit {
    public static void getList(List<Entity> targetList, Predicate<Entity> filter, SortBy priority, int maxTargets) {
        Minecraft mc = Minecraft.getInstance();
        var level = mc.level;
        var player = mc.player;
        if (level == null || player == null || targetList == null || filter == null) return;

        List<Entity> valid = new ArrayList<>();
        for (Entity entity : level.entitiesForRendering()) {
            if (entity != null && filter.test(entity)) {
                valid.add(entity);
            }
        }

        valid.sort((e1, e2) -> {
            if (e1 == null || e2 == null) return 0;
            double d1 = player.distanceToSqr(e1);
            double d2 = player.distanceToSqr(e2);
            return Double.compare(d1, d2);
        });

        for (int i = 0; i < Math.min(maxTargets, valid.size()); i++) {
            Entity e = valid.get(i);
            if (e != null) {
                targetList.add(e);
            }
        }
    }
}