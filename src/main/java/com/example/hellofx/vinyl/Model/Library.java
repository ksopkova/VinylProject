package com.example.hellofx.vinyl.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Library {

    private final ObservableList<Vinyl> vinyls = FXCollections.observableArrayList();

    public Library() {}

    public ObservableList<Vinyl> getVinyls() {
        return vinyls;
    }

    public void addVinyl(Vinyl vinyl) {
        vinyls.add(vinyl);
    }

    // CHANGED: adding User parameter
    public void reserve(Vinyl vinyl, User user) {
        if (vinyl == null) return; // alebo throw, ako chceš
        vinyl.reserve(user);
    }

    public void borrow(Vinyl vinyl, User user) {
        if (vinyl == null) return;
        vinyl.borrow(user);
    }

    public void returnVinyl(Vinyl vinyl, User user) {
        if (vinyl == null) return;
        vinyl.returnVinyl(user);
    }

    // remove môže zostať bez usera
    public void remove(Vinyl vinyl) {
        if (vinyl == null) return;

        vinyl.remove();

        // POZOR: toto je podľa zadania zatiaľ zlé správanie (mažeš hneď)
        // ale nechávam to tak, aby si najprv rozbehala kompiláciu po zmene na User.
        if (vinyl.isMarkedForDeletion()) {
            vinyls.remove(vinyl);
        }
    }

    public void deleteFromLibrary(Vinyl vinyl) {
        vinyls.remove(vinyl);
    }
}