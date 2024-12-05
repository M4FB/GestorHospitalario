package packageObserver;

import java.util.ArrayList;
import java.util.List;

public class NotificacionManager {
    private List<Observer> observers = new ArrayList<>();

    // Método para agregar observadores
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    // Método para eliminar observadores
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    // Notificar a todos los observadores
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }
}
