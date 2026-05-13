package com.example.hellofx.vinyl.Model.State;

import com.example.hellofx.vinyl.Model.User;
import com.example.hellofx.vinyl.Model.Vinyl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VinylStateTest {

    @Test
    void stateNamesAreStableForUi() {
        assertEquals("Available", new AvailableState().getName());
        assertEquals("Reserved", new ReservedState().getName());
        assertEquals("Borrowed", new BorrowedState().getName());
    }

    @Test
    void availableVinylCannotBeReturned() {
        Vinyl vinyl = new Vinyl("Rumours", "Fleetwood Mac", 1977);
        User user = new User("u-1", "User One");

        assertThrows(IllegalStateException.class, () -> vinyl.returnVinyl(user));
    }

    @Test
    void reservedVinylCanOnlyBeBorrowedByReserver() {
        Vinyl vinyl = new Vinyl("Currents", "Tame Impala", 2015);
        User reserver = new User("u-1", "Reserver");
        User otherUser = new User("u-2", "Other User");

        vinyl.reserve(reserver);

        assertThrows(IllegalStateException.class, () -> vinyl.borrow(otherUser));

        vinyl.borrow(reserver);
        assertEquals("Borrowed", vinyl.stateNameProperty().get());
        assertEquals(reserver, vinyl.getBorrowedBy());
        assertNull(vinyl.getReservedBy());
    }

    @Test
    void borrowedVinylCanBeReservedForNextUserAndBecomesReservedAfterReturn() {
        Vinyl vinyl = new Vinyl("Untrue", "Burial", 2007);
        User borrower = new User("u-1", "Borrower");
        User nextUser = new User("u-2", "Next User");

        vinyl.borrow(borrower);
        vinyl.reserve(nextUser);
        vinyl.returnVinyl(borrower);

        assertEquals("Reserved", vinyl.stateNameProperty().get());
        assertEquals(nextUser, vinyl.getReservedBy());
        assertNull(vinyl.getBorrowedBy());
    }

    @Test
    void borrowedVinylCannotBeBorrowedAgain() {
        Vinyl vinyl = new Vinyl("Punisher", "Phoebe Bridgers", 2020);
        User borrower = new User("u-1", "Borrower");
        User otherUser = new User("u-2", "Other User");

        vinyl.borrow(borrower);

        assertThrows(IllegalStateException.class, () -> vinyl.borrow(otherUser));
    }
}
