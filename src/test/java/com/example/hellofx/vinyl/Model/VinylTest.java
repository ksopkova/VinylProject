package com.example.hellofx.vinyl.Model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VinylTest {

    @Test
    void newVinylStartsAvailableWithoutUsers() {
        Vinyl vinyl = new Vinyl("Kind of Blue", "Miles Davis", 1959);

        assertEquals("Kind of Blue", vinyl.getTitleProperty().get());
        assertEquals("Miles Davis", vinyl.getArtistProperty().get());
        assertEquals(1959, vinyl.getYearProperty().get());
        assertEquals("Available", vinyl.stateNameProperty().get());
        assertNull(vinyl.getReservedBy());
        assertNull(vinyl.getBorrowedBy());
    }

    @Test
    void reserveStoresReservedUserAndChangesState() {
        Vinyl vinyl = new Vinyl("IGOR", "Tyler, The Creator", 2019);
        User user = new User("u-1", "User One");

        vinyl.reserve(user);

        assertEquals("Reserved", vinyl.stateNameProperty().get());
        assertEquals(user, vinyl.getReservedBy());
        assertNull(vinyl.getBorrowedBy());
        assertTrue(vinyl.isReservedBy(new User("u-1", "Same ID")));
    }

    @Test
    void borrowAvailableVinylStoresBorrowerAndClearsReservation() {
        Vinyl vinyl = new Vinyl("Discovery", "Daft Punk", 2001);
        User user = new User("u-1", "User One");

        vinyl.borrow(user);

        assertEquals("Borrowed", vinyl.stateNameProperty().get());
        assertEquals(user, vinyl.getBorrowedBy());
        assertNull(vinyl.getReservedBy());
    }

    @Test
    void stateNamePropertyFollowsStateChanges() {
        Vinyl vinyl = new Vinyl("Dummy", "Portishead", 1994);
        User user = new User("u-1", "User One");

        vinyl.reserve(user);
        assertEquals("Reserved", vinyl.stateNameProperty().get());

        vinyl.borrow(user);
        assertEquals("Borrowed", vinyl.stateNameProperty().get());

        vinyl.returnVinyl(user);
        assertEquals("Available", vinyl.stateNameProperty().get());
    }
}
