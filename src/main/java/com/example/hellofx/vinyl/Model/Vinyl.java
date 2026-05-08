package com.example.hellofx.vinyl.Model;

import com.example.hellofx.vinyl.Model.State.AvailableState;
import com.example.hellofx.vinyl.Model.State.IVinylState;
import javafx.beans.property.*;

public class Vinyl {

    private final StringProperty title = new SimpleStringProperty();   //bcs I'm creating object not String (inside this object is information for Java Fx)//
    private final StringProperty artist = new SimpleStringProperty();
    private final IntegerProperty year = new SimpleIntegerProperty();

    private final ObjectProperty<IVinylState> state = new SimpleObjectProperty<>();
    //I use ObjectProperty to take advantage of JavaFX’s built-in Observer mechanism, which ensures that the UI automatically reacts to changes in the state object. Additionally, it enables me to maintain a polymorphic implementation of the State pattern rather than relying on a simple enum or String to represent the state.
    private final StringProperty stateName = new SimpleStringProperty();

    // NEW: who reserved / who borrowed
    private final ObjectProperty<User> reservedBy = new SimpleObjectProperty<>(null);
    private final ObjectProperty<User> borrowedBy = new SimpleObjectProperty<>(null);

    private final BooleanProperty removalRequested = new SimpleBooleanProperty(false);
    private final BooleanProperty reservationBlocked = new SimpleBooleanProperty(false);


    public Vinyl(String title, String artist, int year) {
        this.title.set(title);  //when object's string is set JavaFX get notified about it//
        this.artist.set(artist);
        this.year.set(year);
        state.set(new AvailableState());//every new Vinyl object is automatically settled to available//
        stateName.set(state.get().getName());

        state.addListener((obs, oldState, newState) -> {
            stateName.set(newState.getName());
        });
    }




    // CHANGED: reserve/borrow/return need a user
    public void reserve(User user) {
        state.get().reserve(this, user);
    }

    public void borrow(User user) {
        state.get().borrow(this, user);
    }

    public void returnVinyl(User user) {
        state.get().returnVinyl(this, user);
    }

    public void remove() {
        state.get().remove(this);
    }




    // NEW: reservedBy/borrowedBy accessors
    public User getReservedBy() { return reservedBy.get(); }
    public ObjectProperty<User> reservedByProperty() { return reservedBy; }
    public void setReservedBy(User user) { reservedBy.set(user); }

    public User getBorrowedBy() { return borrowedBy.get(); }
    public ObjectProperty<User> borrowedByProperty() { return borrowedBy; }
    public void setBorrowedBy(User user) { borrowedBy.set(user); }

    public boolean isReserved() { return getReservedBy() != null; }
    public boolean isBorrowed() { return getBorrowedBy() != null; }
    public boolean isReservedBy(User user) { return user != null && user.equals(getReservedBy()); }

    public boolean isRemovalRequested() { return removalRequested.get(); }
    public BooleanProperty removalRequestedProperty() { return removalRequested; }
    public void requestRemoval() { removalRequested.set(true); }

    public boolean isReservationBlocked() { return reservationBlocked.get(); }
    public BooleanProperty reservationBlockedProperty() { return reservationBlocked; }
    public void blockReservations() { reservationBlocked.set(true); }

    // HELP WITH REMOVAL - when it needs to be removed physically
    
    public boolean canBeRemovedNow() {
        return !isBorrowed() && !isReserved();
    }



    public StringProperty getTitleProperty() { return title; }
    public StringProperty getArtistProperty() { return artist; }
    public IntegerProperty getYearProperty() { return year; }


    public StringProperty stateNameProperty() {
        return stateName;
    }

    public void setState(IVinylState newState) {
        state.set(newState);
    }


}