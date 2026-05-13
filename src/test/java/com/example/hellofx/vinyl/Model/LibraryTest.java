package com.example.hellofx.vinyl.Model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LibraryTest {

    @Test
    void addVinylAddsItToObservableList() {
        Library library = new Library();
        Vinyl vinyl = new Vinyl("Madvillainy", "Madvillain", 2004);

        library.addVinyl(vinyl);

        assertEquals(1, library.getVinyls().size());
        assertTrue(library.getVinyls().contains(vinyl));
    }

    @Test
    void removeAvailableVinylRemovesItImmediately() {
        Library library = new Library();
        Vinyl vinyl = new Vinyl("Blue Train", "John Coltrane", 1957);
        library.addVinyl(vinyl);

        library.remove(vinyl);

        assertFalse(library.getVinyls().contains(vinyl));
    }

    @Test
    void removeBorrowedVinylWaitsUntilItIsReturned() {
        Library library = new Library();
        Vinyl vinyl = new Vinyl("Mezzanine", "Massive Attack", 1998);
        User borrower = new User("u-1", "Borrower");
        library.addVinyl(vinyl);

        library.borrow(vinyl, borrower);
        library.remove(vinyl);

        assertTrue(library.getVinyls().contains(vinyl));
        assertTrue(vinyl.isRemovalRequested());
        assertTrue(vinyl.isReservationBlocked());
        assertThrows(IllegalStateException.class,
                () -> library.reserve(vinyl, new User("u-2", "Next User")));

        library.returnVinyl(vinyl, borrower);

        assertFalse(library.getVinyls().contains(vinyl));
    }

    @Test
    void removeReservedVinylAllowsReserverToBorrowThenRemovesAfterReturn() {
        Library library = new Library();
        Vinyl vinyl = new Vinyl("London Calling", "The Clash", 1979);
        User reserver = new User("u-1", "Reserver");
        library.addVinyl(vinyl);

        library.reserve(vinyl, reserver);
        library.remove(vinyl);

        assertTrue(library.getVinyls().contains(vinyl));
        assertTrue(vinyl.isRemovalRequested());
        assertThrows(IllegalStateException.class,
                () -> library.reserve(vinyl, new User("u-2", "Next User")));

        library.borrow(vinyl, reserver);
        assertEquals("Borrowed", vinyl.stateNameProperty().get());

        library.returnVinyl(vinyl, reserver);

        assertFalse(library.getVinyls().contains(vinyl));
    }
}
