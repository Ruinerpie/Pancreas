package ruinerpie.pancreas.kit;

import ruinerpie.pancreas.*;
import ruinerpie.pancreas.mixin.*;
import ruinerpie.pancreas.mixin.accessor.*;
import ruinerpie.pancreas.values.*;
import ruinerpie.pancreas.signals.*;
import ruinerpie.pancreas.kit.*;
import ruinerpie.pancreas.draw.*;

import java.util.*;

public class EchoTable {
    private static final List<EchoEntity> PLAYERS = new ArrayList<>();

    public static void add(EchoEntity player) {
        PLAYERS.add(player);
    }

    public static void remove(EchoEntity player) {
        PLAYERS.remove(player);
    }

    public static List<EchoEntity> getPlayers() {
        return PLAYERS;
    }
}
