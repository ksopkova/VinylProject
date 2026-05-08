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
        // can borrow only if it is reserved by the same user
        if (!vinyl.isReservedBy(user)) {
            throw new IllegalStateException("Cannot borrow: vinyl is reserved by another user.");
        }

        vinyl.setBorrowedBy(user);

        // reservation is consumed by borrowing
        vinyl.setReservedBy(null);

        vinyl.setState(new BorrowedState());
    }

    @Override
    public void returnVinyl(Vinyl vinyl, User user) {
        throw new IllegalStateException("Cannot return a vinyl that is not borrowed.");
    }

    @Override
    public void remove(Vinyl vinyl) {
        // Reserved => cannot remove immediately; mark pending removal
        // Block NEW reservations; keep existing reserver so they can still borrow.
        vinyl.requestRemoval();
        vinyl.blockReservations();
    }

    @Override
    public String getName() {
        return "Reserved";
    }
}
