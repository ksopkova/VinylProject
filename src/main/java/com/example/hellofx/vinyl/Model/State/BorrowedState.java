package com.example.hellofx.vinyl.Model.State;

import com.example.hellofx.vinyl.Model.User;
import com.example.hellofx.vinyl.Model.Vinyl;

public class BorrowedState implements IVinylState {

    @Override
    public void reserve(Vinyl vinyl, User user) {
        // Borrowed vinyl can be reserved if it is not already reserved,
        // but after a remove-request we must block NEW reservations.
        if (vinyl.isReservationBlocked()) {
            throw new IllegalStateException("Vinyl is about to be removed and cannot be reserved.");
        }

        if (vinyl.isReserved()) {
            throw new IllegalStateException("Vinyl is already reserved.");
        }

        vinyl.setReservedBy(user);
        // state stays Borrowed; after returnVinyl it will go to ReservedState
    }

    @Override
    public void borrow(Vinyl vinyl, User user) {
        throw new IllegalStateException("Vinyl is already borrowed.");
    }

    @Override
    public void returnVinyl(Vinyl vinyl, User user) {
        // optional: enforce only the borrower can return
        // if (!vinyl.isBorrowedBy(user)) throw new IllegalStateException("Not borrowed by you");

        vinyl.setBorrowedBy(null);

        // if reserved, after return it becomes Reserved; otherwise Available
        if (vinyl.isReserved()) {
            vinyl.setState(new ReservedState());
        } else {
            vinyl.setState(new AvailableState());
        }
    }

    @Override
    public void remove(Vinyl vinyl) {
        // Borrowed => cannot be removed right now; mark pending removal
        // and block new reservations (existing reservation, if any, stays)
        vinyl.requestRemoval();
        vinyl.blockReservations();
    }

    @Override
    public String getName() {
        return "Borrowed";
    }
}