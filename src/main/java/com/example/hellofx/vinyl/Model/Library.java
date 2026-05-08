package com.example.hellofx.vinyl.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class Library {

    private final ObservableList<Vinyl> vinyls = FXCollections.observableArrayList();

    public ObservableList<Vinyl> getVinyls() {
        return vinyls;
    }

    public void addVinyl(Vinyl vinyl) {
        vinyls.add(vinyl);
    }

    public void reserve(Vinyl vinyl, User user) {
        if (vinyl == null || user == null) return;
        vinyl.reserve(user);
    }

    public void borrow(Vinyl vinyl, User user) {
        if (vinyl == null || user == null) return;
        vinyl.borrow(user);
    }

    public void returnVinyl(Vinyl vinyl, User user) {
        if (vinyl == null || user == null) return;
        vinyl.returnVinyl(user);

        // NEW: po return skús fyzicky odstrániť, ak je pending removal a už je to dovolené
        if (vinyl.isRemovalRequested() && vinyl.canBeRemovedNow()) {
            vinyls.remove(vinyl);
        }
    }

    public void remove(Vinyl vinyl) {
        if (vinyl == null) return;

        // nech State nastaví flagy / zmení správanie
        vinyl.remove();

        // NEW: fyzicky vymaž iba ak je to už dovolené
        if (vinyl.isRemovalRequested() && vinyl.canBeRemovedNow()) {
            vinyls.remove(vinyl);
        }
    }
}