package com.example.hellofx.vinyl.Model.State;

import com.example.hellofx.vinyl.Model.User;
import com.example.hellofx.vinyl.Model.Vinyl;

public interface IVinylState {

    void reserve(Vinyl vinyl, User user);

    void borrow(Vinyl vinyl, User user);

    void returnVinyl(Vinyl vinyl, User user);

    void remove(Vinyl vinyl);

    String getName();
}