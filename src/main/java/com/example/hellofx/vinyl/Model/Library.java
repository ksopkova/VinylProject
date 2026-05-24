package com.example.hellofx.vinyl.Model;

import com.example.hellofx.vinyl.Model.Observer.LibraryEvent;
import com.example.hellofx.vinyl.Model.Observer.LibraryEventType;
import com.example.hellofx.vinyl.Model.Observer.LibraryObserver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;


public class Library {

    private final ObservableList<Vinyl> vinyls = FXCollections.observableArrayList();
    private final List<LibraryObserver> observers = new CopyOnWriteArrayList<>();

    public ObservableList<Vinyl> getVinyls() {
        return vinyls;
    }

    public synchronized List<Vinyl> getVinylSnapshot() {
        return List.copyOf(vinyls);
    }

    public synchronized Optional<Vinyl> findById(String id) {
        return vinyls.stream()
                .filter(vinyl -> vinyl.getId().equals(id))
                .findFirst();
    }

    public void addObserver(LibraryObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(LibraryObserver observer) {
        observers.remove(observer);
    }

    public void addVinyl(Vinyl vinyl) {
        if (vinyl == null) return;
        synchronized (this) {
            vinyls.add(vinyl);
        }
        notifyObservers(new LibraryEvent(LibraryEventType.VINYL_ADDED, vinyl, "Vinyl added."));
    }

    public void reserve(Vinyl vinyl, User user) {
        if (vinyl == null || user == null) return;
        synchronized (this) {
            vinyl.reserve(user);
        }
        notifyObservers(new LibraryEvent(LibraryEventType.VINYL_CHANGED, vinyl, "Vinyl reserved."));
    }

    public void borrow(Vinyl vinyl, User user) {
        if (vinyl == null || user == null) return;
        synchronized (this) {
            vinyl.borrow(user);
        }
        notifyObservers(new LibraryEvent(LibraryEventType.VINYL_CHANGED, vinyl, "Vinyl borrowed."));
    }

    public void returnVinyl(Vinyl vinyl, User user) {
        if (vinyl == null || user == null) return;
        boolean removed;
        synchronized (this) {
            vinyl.returnVinyl(user);

            // NEW: po return skús fyzicky odstrániť, ak je pending removal a už je to dovolené
            removed = vinyl.isRemovalRequested() && vinyl.canBeRemovedNow();
            if (removed) {
                vinyls.remove(vinyl);
            }
        }

        if (removed) {
            notifyObservers(new LibraryEvent(LibraryEventType.VINYL_REMOVED, vinyl, "Vinyl removed after return."));
        } else {
            notifyObservers(new LibraryEvent(LibraryEventType.VINYL_CHANGED, vinyl, "Vinyl returned."));
        }
    }

    public void remove(Vinyl vinyl) {
        if (vinyl == null) return;
        boolean removed;
        synchronized (this) {
            // nech State nastaví flagy / zmení správanie
            vinyl.remove();

            // NEW: fyzicky vymaž iba ak je to už dovolené
            removed = vinyl.isRemovalRequested() && vinyl.canBeRemovedNow();
            if (removed) {
                vinyls.remove(vinyl);
            }
        }

        if (removed) {
            notifyObservers(new LibraryEvent(LibraryEventType.VINYL_REMOVED, vinyl, "Vinyl removed."));
        } else {
            notifyObservers(new LibraryEvent(LibraryEventType.VINYL_CHANGED, vinyl, "Vinyl marked for removal."));
        }
    }

    private void notifyObservers(LibraryEvent event) {
        for (LibraryObserver observer : observers) {
            observer.onLibraryChanged(event);
        }
    }
}
