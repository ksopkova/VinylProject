package com.example.hellofx.vinyl.Model.State;

import com.example.hellofx.vinyl.Model.User;
import com.example.hellofx.vinyl.Model.Vinyl;

public class AvailableState implements IVinylState {

    @Override
    public void reserve(Vinyl vinyl, User user) {
        if (vinyl.isMarkedForDeletion()) {
            throw new IllegalStateException(
                    "Vinyl is marked for removal and cannot be reserved."
            );
        }

        // NEW: store who reserved it
        vinyl.setReservedBy(user);
        vinyl.setState(new ReservedState());
    }

    @Override
    public void borrow(Vinyl vinyl, User user) {
        // NEW: store who borrowed it
        vinyl.setBorrowedBy(user);

        // ak bol predtým rezervovaný (nemalo by sa stať v Available state),
        // môžeš pre istotu vymazať rezerváciu:
        vinyl.setReservedBy(null);

        vinyl.setState(new BorrowedState());
    }

    @Override
    public void returnVinyl(Vinyl vinyl, User user) {
        throw new IllegalStateException(
                "Cannot return a vinyl that is not borrowed."
        );
    }

    @Override
    public void remove(Vinyl vinyl) {
        // podľa zadania: z Available + bez rezervácie sa môže odstrániť hneď.
        // Ty zatiaľ používaš markForDeletion => a Library to hneď vymaže.
        // To je OK len pre Available stav.
        vinyl.markForDeletion();
    }

    @Override
    public String getName() {
        return "Available";
    }
}