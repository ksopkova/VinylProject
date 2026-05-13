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

    public String reserve(Vinyl vinyl) {
        if (vinyl == null) return "Select a vinyl first.";

        try {
            library.reserve(vinyl, currentUser);
            return "Vinyl reserved.";
        } catch (IllegalStateException e) {
            return e.getMessage();
        }
    }

    public String borrow(Vinyl vinyl) {
        if (vinyl == null) return "Select a vinyl first.";

        try {
            library.borrow(vinyl,currentUser);
            return "Vinyl borrowed.";
        } catch (IllegalStateException e) {
            return e.getMessage();
        }
    }

    public String returnVinyl(Vinyl vinyl) {
        if (vinyl == null) return "Select a vinyl first.";

        try {
            library.returnVinyl(vinyl,currentUser);
            return "Vinyl returned.";
        } catch (IllegalStateException e) {
            return e.getMessage();
        }
    }

    public String remove(Vinyl vinyl) {
        if (vinyl == null) return "Select a vinyl first.";

        try {
            boolean wasInLibrary = library.getVinyls().contains(vinyl);
            library.remove(vinyl);
            if (wasInLibrary && !library.getVinyls().contains(vinyl)) {
                return "Vinyl removed.";
            }
            return "Vinyl marked for removal.";

        } catch (IllegalStateException e) {
            return e.getMessage();
        }
    }

    // voliteľné: aby simulátor vedel volať akcie s iným userom
    public void reserveAs(Vinyl vinyl, User user) { library.reserve(vinyl, user); }
    public void borrowAs(Vinyl vinyl, User user) { library.borrow(vinyl, user); }
    public void returnAs(Vinyl vinyl, User user) { library.returnVinyl(vinyl, user); }
}
