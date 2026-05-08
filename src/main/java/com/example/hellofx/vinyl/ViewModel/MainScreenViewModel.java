package com.example.hellofx.vinyl.ViewModel;

import com.example.hellofx.vinyl.Model.User;
import com.example.hellofx.vinyl.Model.Vinyl;
import com.example.hellofx.vinyl.Model.Library;
import javafx.collections.ObservableList;

public class MainScreenViewModel {

    private final Library library;
    //new user
    private final User currentUser = new User("qqqqq","User");

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
            library.reserve(vinyl, currentUser);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    public void borrow(Vinyl vinyl) {
        try {
            library.borrow(vinyl,currentUser);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    public void returnVinyl(Vinyl vinyl) {
        try {
            library.returnVinyl(vinyl,currentUser);
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

    // voliteľné: aby simulátor vedel volať akcie s iným userom
    public void reserveAs(Vinyl vinyl, User user) { library.reserve(vinyl, user); }
    public void borrowAs(Vinyl vinyl, User user) { library.borrow(vinyl, user); }
    public void returnAs(Vinyl vinyl, User user) { library.returnVinyl(vinyl, user); }
}