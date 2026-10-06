package ruinerpie.pancreas;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Toasts {
    private static final Toasts INSTANCE = new Toasts();
    private static final Logger LOG = LoggerFactory.getLogger("Pancreas-Notifications");

    public static Toasts get() {
        return INSTANCE;
    }

    private final List<Toast> notifications = new ArrayList<>();

    public void add(String title, String message, Toast.Type type) {
        Toast notification = new Toast(title, message, type);
        synchronized (notifications) {
            notifications.add(notification);
            if (notifications.size() > 50) {
                notifications.remove(0);
            }
        }

        switch (type) {
            case INFO, SUCCESS -> LOG.info("[{}] {}", title, message);
            case WARNING -> LOG.warn("[{}] {}", title, message);
            case ERROR -> LOG.error("[{}] {}", title, message);
        }
    }

    public void info(String title, String message) {
        add(title, message, Toast.Type.INFO);
    }

    public void warning(String title, String message) {
        add(title, message, Toast.Type.WARNING);
    }

    public void error(String title, String message) {
        add(title, message, Toast.Type.ERROR);
    }

    public void success(String title, String message) {
        add(title, message, Toast.Type.SUCCESS);
    }

    public List<Toast> getNotifications() {
        synchronized (notifications) {
            return Collections.unmodifiableList(new ArrayList<>(notifications));
        }
    }
}
