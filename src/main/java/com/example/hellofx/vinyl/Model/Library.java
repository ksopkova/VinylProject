package com.example.hellofx.vinyl.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Library {

    private final ObservableList<Vinyl> vinyls =
            FXCollections.observableArrayList();

    public Library() {}

    public ObservableList<Vinyl> getVinyls() {
        return vinyls;
    }

    public void addVinyl(Vinyl vinyl) {
        vinyls.add(vinyl);
    }

    public void reserve(Vinyl vinyl) {
        vinyl.reserve();
    }

    public void borrow(Vinyl vinyl) {
        vinyl.borrow();
    }

    public void returnVinyl(Vinyl vinyl) {
        vinyl.returnVinyl();
    }

    public void remove(Vinyl vinyl) {
        vinyl.remove();

        if (vinyl.isMarkedForDeletion()) {
            vinyls.remove(vinyl);
        }
    }

    public void deleteFromLibrary(Vinyl vinyl) {
        vinyls.remove(vinyl);
    }

}