package com.example.hellofx.vinyl.Model;

public class BorrowedState implements IVinylState{
    @Override
    public void reserve(Vinyl vinyl) {
        throw new IllegalStateException(
                "Cannot reserve a borrowed vinyl."
        );
    }

    @Override
    public void borrow(Vinyl vinyl) {
        throw new IllegalStateException(
                "Vinyl is already borrowed."
        );
    }

    @Override
    public void returnVinyl(Vinyl vinyl) {
        vinyl.setState(new AvailableState());
    }

    @Override
    public void remove(Vinyl vinyl) {
       vinyl.markForDeletion();
    }

    @Override
    public String getName() {
        return "Borrowed";
    }
}
