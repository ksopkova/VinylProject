package com.example.hellofx.vinyl.Model.State;

import com.example.hellofx.vinyl.Model.User;
import com.example.hellofx.vinyl.Model.Vinyl;

public class BorrowedState implements IVinylState {

    @Override
    public void reserve(Vinyl vinyl, User user) {
        // Podľa zadania: môžeš rezervovať aj borrowed vinyl, ak ešte nie je reserved
        if (vinyl.isMarkedForDeletion()) {
            // toto máš teraz ako "markedForDeletion", ale podľa zadania by to malo znamenať
            // "about to be removed" => nové rezervácie zakázať
            throw new IllegalStateException("Vinyl is marked for removal and cannot be reserved.");
        }

        if (vinyl.isReserved()) {
            throw new IllegalStateException("Vinyl is already reserved.");
        }

        vinyl.setReservedBy(user);
        // stav ostáva Borrowed, len si pamätáme rezerváciu
        // (neskôr môžeš spraviť extra stav BorrowedReservedState, ale nie je nutné)
    }

    @Override
    public void borrow(Vinyl vinyl, User user) {
        throw new IllegalStateException("Vinyl is already borrowed.");
    }

    @Override
    public void returnVinyl(Vinyl vinyl, User user) {
        // voliteľne: kontrola že vracia ten istý user
        // if (!vinyl.isBorrowedBy(user)) throw new IllegalStateException("Not borrowed by you");

        vinyl.setBorrowedBy(null);

        // ak je rezervovaný, po návrate nie je "Available", ale "Reserved"
        if (vinyl.isReserved()) {
            vinyl.setState(new ReservedState());
        } else {
            vinyl.setState(new AvailableState());
        }
    }

    @Override
    public void remove(Vinyl vinyl) {
        // podľa zadania: keď je borrowed, remove má len označiť "pending removal"
        // a nesmie sa hneď odstrániť zo zoznamu.
        // ZATIAĽ to necháme ako markForDeletion, ale POZOR: musíš potom opraviť Library.remove()
        // aby nevymazával hneď, keď je borrowed/reserved.
        vinyl.markForDeletion();
    }

    @Override
    public String getName() {
        return "Borrowed";
    }
}