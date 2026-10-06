package ruinerpie.pancreas;



public class Toast {
    public enum Type {
        INFO, WARNING, ERROR, SUCCESS
    }

    public final String title;
    public final String message;
    public final Type type;
    public final long timestamp;

    public Toast(String title, String message, Type type) {
        this.title = title;
        this.message = message;
        this.type = type;
        this.timestamp = System.currentTimeMillis();
    }
}
