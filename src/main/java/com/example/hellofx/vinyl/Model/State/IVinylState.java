package com.example.hellofx.vinyl.Model.State;
//kazda metoda tak dostane referenciu na vinyl a STATE rozhodne čo s vinylom urobi//

import com.example.hellofx.vinyl.Model.Vinyl;

public interface IVinylState {

    void reserve(Vinyl vinyl);

    void borrow(Vinyl vinyl);

    void returnVinyl(Vinyl vinyl);

    void remove(Vinyl vinyl);

    String getName();
}