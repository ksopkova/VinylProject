package com.example.hellofx.vinyl.Model.State;

import com.example.hellofx.vinyl.Model.User;
import com.example.hellofx.vinyl.Model.Vinyl;

public class AvailableState implements IVinylState {

    @Override
    public void reserve(Vinyl vinyl, User user) {
        // after a remove-request, new reservations must be blocked
        if (vinyl.isReservationBlocked()) {
            throw new IllegalStateException("Vinyl is about to be removed and cannot be reserved.");
        }

        vinyl.setReservedBy(user);
        vinyl.setState(new ReservedState());
    }

    @Override
    public void borrow(Vinyl vinyl, User user) {
        vinyl.setBorrowedBy(user);

        // safety: Available should not have a reserver, but clear anyway
        vinyl.setReservedBy(null);

        vinyl.setState(new BorrowedState());
    }

    @Override
    public void returnVinyl(Vinyl vinyl, User user) {
        throw new IllegalStateException("Cannot return a vinyl that is not borrowed.");
    }

    @Override
    public void remove(Vinyl vinyl) {
        // Available + not reserved => can be removed immediately by Library
        vinyl.requestRemoval();
    }

    @Override
    public String getName() {
        return "Available";
    }
}