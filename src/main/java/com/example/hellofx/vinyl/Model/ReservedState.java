package com.example.hellofx.vinyl.Model;

public class ReservedState implements IVinylState{
    @Override
    public void reserve(Vinyl vinyl) {
        throw new IllegalStateException(
                "Vinyl is already reserved."
        );
    }

    @Override
    public void borrow(Vinyl vinyl) {
        vinyl.setState(new BorrowedState());
    }

    @Override
    public void returnVinyl(Vinyl vinyl) {
        throw new IllegalStateException(
                "Cannot return a vinyl that is not borrowed."
        );
    }

    @Override
    public void remove(Vinyl vinyl) {
        vinyl.markForDeletion();
    }

    @Override
    public String getName() {
        return "Reserved";
    }
}
