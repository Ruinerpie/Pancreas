package ruinerpie.pancreas.signals;

import net.minecraft.network.chat.Component;
import net.minecraft.world.BossEvent;
import ruinerpie.pancreas.Signal;
import java.util.Collection;

public class RenderBossBar extends Signal {
    public static class BossIterator extends RenderBossBar {
        public Collection<?> bossBars;
        public BossIterator(Collection<?> bossBars) { this.bossBars = bossBars; }
        public BossIterator() {}
    }
    public static class BossSpacing extends RenderBossBar {
        public int spacing;
        public BossSpacing(int spacing) { this.spacing = spacing; }
        public BossSpacing() {}
    }
    public static class BossText extends RenderBossBar {
        public BossEvent bossBar;
        public Component name;
        public BossText(BossEvent bossBar, Component name) { this.bossBar = bossBar; this.name = name; }
        public BossText() {}
    }
}
