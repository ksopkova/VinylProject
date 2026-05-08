package com.example.hellofx.vinyl.Model.State;

import com.example.hellofx.vinyl.Model.User;
import com.example.hellofx.vinyl.Model.Vinyl;

public class ReservedState implements IVinylState {

    @Override
    public void reserve(Vinyl vinyl, User user) {
        throw new IllegalStateException("Vinyl is already reserved.");
    }

    @Override
    public void borrow(Vinyl vinyl, User user) {
        // kľúčové pravidlo zo zadania:
        // požičať môže iba ten, kto má rezerváciu
        if (!vinyl.isReservedBy(user)) {
            throw new IllegalStateException("Cannot borrow: vinyl is reserved by another user.");
        }

        vinyl.setBorrowedBy(user);

        // rezervácia sa po požičaní "spotrebuje"
        vinyl.setReservedBy(null);

        vinyl.setState(new BorrowedState());
    }

    @Override
    public void returnVinyl(Vinyl vinyl, User user) {
        throw new IllegalStateException("Cannot return a vinyl that is not borrowed.");
    }

    @Override
    public void remove(Vinyl vinyl) {
        // Najjednoduchšie riešenie: remove rieši Library.remove()
        // takže tu nič nerob (alebo môžeš hodiť výnimku, aby bolo jasné že sa to nepoužíva)
        throw new IllegalStateException("Use Library.remove(vinyl) to remove from library.");
    }

    @Override
    public String getName() {
        return "Reserved";
    }
}

