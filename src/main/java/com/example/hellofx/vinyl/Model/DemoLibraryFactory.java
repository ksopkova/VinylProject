package com.example.hellofx.vinyl.Model;

public final class DemoLibraryFactory {
    private DemoLibraryFactory() {
    }

    public static Library createLibrary() {
        Library library = new Library();

        library.addVinyl(new Vinyl("IGOR", "Tyler, The Creator", 2019));
        library.addVinyl(new Vinyl("My Beautiful Dark Twisted Fantasy", "Kanye West", 2010));
        library.addVinyl(new Vinyl("good kid, m.A.A.d city", "Kendrick Lamar", 2012));
        library.addVinyl(new Vinyl("Madvillainy", "Madvillain", 2004));
        library.addVinyl(new Vinyl("The Miseducation of Lauryn Hill", "Lauryn Hill", 1998));

        library.addVinyl(new Vinyl("Mezzanine", "Massive Attack", 1998));
        library.addVinyl(new Vinyl("Dummy", "Portishead", 1994));
        library.addVinyl(new Vinyl("Selected Ambient Works 85-92", "Aphex Twin", 1992));
        library.addVinyl(new Vinyl("Discovery", "Daft Punk", 2001));
        library.addVinyl(new Vinyl("Untrue", "Burial", 2007));
        library.addVinyl(new Vinyl("Since I Left You", "The Avalanches", 2000));

        library.addVinyl(new Vinyl("Unknown Pleasures", "Joy Division", 1979));
        library.addVinyl(new Vinyl("London Calling", "The Clash", 1979));
        library.addVinyl(new Vinyl("The Queen Is Dead", "The Smiths", 1986));
        library.addVinyl(new Vinyl("Rumours", "Fleetwood Mac", 1977));
        library.addVinyl(new Vinyl("Hounds of Love", "Kate Bush", 1985));

        library.addVinyl(new Vinyl("Blue Train", "John Coltrane", 1957));
        library.addVinyl(new Vinyl("Kind of Blue", "Miles Davis", 1959));
        library.addVinyl(new Vinyl("Time Out", "The Dave Brubeck Quartet", 1959));

        library.addVinyl(new Vinyl("Currents", "Tame Impala", 2015));
        library.addVinyl(new Vinyl("Melodrama", "Lorde", 2017));
        library.addVinyl(new Vinyl("Punisher", "Phoebe Bridgers", 2020));

        return library;
    }
}
