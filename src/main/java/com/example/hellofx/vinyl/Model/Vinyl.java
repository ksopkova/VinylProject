package com.example.hellofx.vinyl.Model;

import javafx.beans.property.*;

public class Vinyl {

    private final StringProperty title = new SimpleStringProperty();   //bcs I'm creating object not String (inside this object is information for Java Fx)//
    private final StringProperty artist = new SimpleStringProperty();
    private final IntegerProperty year = new SimpleIntegerProperty();
    private final ObjectProperty<VinylState> state = new SimpleObjectProperty<>(); //I use ObjectProperty to take advantage of JavaFX’s built-in Observer mechanism, which ensures that the UI automatically reacts to changes in the state object. Additionally, it enables me to maintain a polymorphic implementation of the State pattern rather than relying on a simple enum or String to represent the state.
    private boolean markedForDeletion = false;
    private final StringProperty stateName = new SimpleStringProperty();

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


    public void reserve() {       // delegate the reserve(this) call to the current state object because I am implementing the State pattern. In the State pattern, behavior depends on the current state of the object, and each state encapsulates its own rules and transitions. Instead of using conditional logic inside the Vinyl class (such as multiple if-else statements), the Vinyl object delegates the responsibility to the current VinylState. This ensures that behavior changes dynamically when the state changes and keeps the Vinyl class clean and compliant with the Open/Closed Principle
        state.get().reserve(this);
    }

    public void borrow() {
        state.get().borrow(this);
    }

    public void returnVinyl() {
        state.get().returnVinyl(this);
    }

    public void remove() {
        state.get().remove(this);
    }
    
    public void markForDeletion() {
        this.markedForDeletion = true;
    }

    public boolean isMarkedForDeletion() {
        return markedForDeletion;
    }

    public StringProperty getTitleProperty() { return title; }
    public StringProperty getArtistProperty() { return artist; }
    public IntegerProperty getYearProperty() { return year; }


    public StringProperty stateNameProperty() {
        return stateName;
    }

    public void setState(VinylState newState) {
        state.set(newState);
    }


}