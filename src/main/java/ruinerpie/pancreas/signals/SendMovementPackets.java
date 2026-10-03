package ruinerpie.pancreas.signals;

import ruinerpie.pancreas.Signal;

public class SendMovementPackets extends Signal {
    public static class Pre extends SendMovementPackets {}
    public static class Post extends SendMovementPackets {}
}
