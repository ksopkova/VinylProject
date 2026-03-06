package com.example.hellofx.vinyl.ViewModel;

import com.example.hellofx.vinyl.Model.Vinyl;
import com.example.hellofx.vinyl.Model.Library;
import javafx.collections.ObservableList;

public class MainScreenViewModel {

    private final Library library;

    public MainScreenViewModel(Library library) {
        this.library = library;
    }

    public ObservableList<Vinyl> getVinyls() {
        return library.getVinyls();
    }

    public void addVinyl(Vinyl vinyl) {
        library.addVinyl(vinyl);
    }

    public void reserve(Vinyl vinyl) {
        try {
            library.reserve(vinyl);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    public void borrow(Vinyl vinyl) {
        try {
            library.borrow(vinyl);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    public void returnVinyl(Vinyl vinyl) {
        try {
            library.returnVinyl(vinyl);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    public void remove(Vinyl vinyl) {
        try {
            library.remove(vinyl);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }
}