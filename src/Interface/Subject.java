package Interface;


import java.util.ArrayList;
import java.util.List;

public interface Subject {

    List<Listener> Listeners = new ArrayList<>();

    public default void registerListener(Listener listener) {
        Listeners.add(listener);
    }

    public default void removeListener(Listener listener) {
        Listeners.remove(listener);
    }

    public default void notifyListeners() {
        for (Listener listener : Listeners) {
            listener.update();
        }
    }
}
