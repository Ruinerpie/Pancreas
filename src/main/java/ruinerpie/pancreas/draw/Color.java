package ruinerpie.pancreas.draw;



public class Color {
    public static final Color WHITE = new Color(255, 255, 255, 255);
    public static final Color BLACK = new Color(0, 0, 0, 255);
    public static final Color RED = new Color(255, 0, 0, 255);
    public static final Color GREEN = new Color(0, 255, 0, 255);
    public static final Color BLUE = new Color(0, 0, 255, 255);

    public int r, g, b, a;

    public Color(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public Color(int r, int g, int b) {
        this(r, g, b, 255);
    }

    public Color(int packed) {
        this.a = (packed >> 24) & 0xFF;
        this.r = (packed >> 16) & 0xFF;
        this.g = (packed >> 8) & 0xFF;
        this.b = packed & 0xFF;
    }

    public Color(Color other) {
        this(other != null ? other.r : 255, other != null ? other.g : 255, other != null ? other.b : 255, other != null ? other.a : 255);
    }

    public Color() {
        this(255, 255, 255, 255);
    }

    public Color set(Color color) {
        this.r = color.r;
        this.g = color.g;
        this.b = color.b;
        this.a = color.a;
        return this;
    }

    public Color a(int a) {
        this.a = a;
        return this;
    }

    public int getPacked() {
        return ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
    }
}
