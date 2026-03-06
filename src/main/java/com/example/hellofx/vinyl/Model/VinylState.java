package com.example.hellofx.vinyl.Model;
//kazda metoda tak dostane referenciu na vinyl a STATE rozhodne čo s vinylom urobi//

public interface VinylState {

    void reserve(Vinyl vinyl);

    void borrow(Vinyl vinyl);

    void returnVinyl(Vinyl vinyl);

    void remove(Vinyl vinyl);

    String getName();
}