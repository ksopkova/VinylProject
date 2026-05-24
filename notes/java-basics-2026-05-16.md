# Java poznámky – 2026-05-16

## String vs StringProperty

```java
String meno = "Kika";
```

- `String` je obyčajná textová hodnota.
- `meno` je premenná.
- `"Kika"` je objekt/hodnota typu `String`.
- Klasický `String` nemá listener.

```java
StringProperty meno = new SimpleStringProperty("Kika");
```

- `StringProperty` je JavaFX objekt, ktorý drží text.
- Vie upozorniť listenerov, keď sa hodnota zmení.
- Hodí sa na binding s UI.

Príklad:

```java
label.textProperty().bind(meno);
```

Keď sa zmení `meno`, automaticky sa zmení aj text v `Label`.

## Premenná vs objekt

```java
String meno = "Kika";
```

Rozdelenie:

```text
String meno = "Kika";
typ    premenná objekt/hodnota
```

Premenná `meno` ukazuje na objekt `"Kika"`:

```text
meno -> "Kika"
```

Keď spravíš:

```java
meno = "Anna";
```

premenná `meno` začne ukazovať na inú hodnotu:

```text
meno -> "Anna"
```

Objekt `"Kika"` sa nezmenil.

## Trieda vs objekt

```java
class Vinyl {
    String title;
}
```

- `class Vinyl` je trieda, teda šablóna/typ.
- Objekt vznikne až cez `new`.

```java
Vinyl vinyl = new Vinyl();
```

Rozdelenie:

```text
Vinyl vinyl = new Vinyl();
typ   premenná   objekt
```

## Immutable

`immutable` znamená nemeniteľný.

`String` je immutable:

```java
String meno = "Kika";
meno = "Anna";
```

Nezmení sa text `"Kika"` na `"Anna"`.

Stane sa toto:

```text
meno -> "Kika"
meno -> "Anna"
```

Premenná začne ukazovať na inú hodnotu.

Jednoduchá predstava:

- `String` je papierik s textom.
- Papierik neprepisuješ.
- Keď chceš iný text, vezmeš nový papierik.

## Mutable

`mutable` znamená meniteľný.

Príklad mutable objektu:

```java
StringBuilder meno = new StringBuilder("Kika");
meno.replace(0, meno.length(), "Anna");
```

Tu premenná stále ukazuje na ten istý objekt:

```text
meno -> objekt s textom "Kika"
meno -> ten istý objekt, ale text je "Anna"
```

Jednoduchá predstava:

- `StringBuilder` je tabuľka.
- Vieš zotrieť starý text.
- Vieš napísať nový text na tú istú tabuľku.

## Čo sa stane so starým Stringom

```java
String meno = "Kika";
meno = "Anna";
```

Po zmene už `meno` neukazuje na `"Kika"`.

Ak na `"Kika"` neukazuje nič iné, Java ho môže neskôr upratať cez garbage collector.

Pri textoch v úvodzovkách, napríklad `"Kika"`, Java často používa špeciálne miesto nazývané String pool.

```java
String a = "Kika";
String b = "Kika";
```

`a` aj `b` môžu ukazovať na ten istý String objekt.

## Final

`final` znamená, že premenná sa už nedá presmerovať na inú hodnotu alebo objekt.

```java
final String meno = "Kika";
```

Toto už nejde:

```java
meno = "Anna";
```

Pri obyčajnom `String` to vyzerá ako konštanta, lebo `String` je immutable.

Pri objekte je rozdiel dôležitejší:

```java
final StringBuilder meno = new StringBuilder("Kika");

meno.append(" Anna");              // ide
meno = new StringBuilder("Eva");   // nejde
```

`final` chráni premennú, nie vždy obsah objektu.

## User a final id

Ak má používateľ ID, ktoré sa po vytvorení nemá meniť:

```java
class User {
    private final int id;
    private String name;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

V `main`:

```java
User user = new User(1, "Kika");

user.setName("Anna"); // ide
user.setId(2);        // nejde, metóda neexistuje
```

Keby si sa pokúsil spraviť setter pre `id`:

```java
public void setId(int id) {
    this.id = id; // chyba, lebo id je final
}
```

Zhrnutie:

- `final` = nedá sa zmeniť po vytvorení.
- `private` = nedá sa meniť priamo zvonku.
- setter = povolená cesta, ako meniť hodnotu zvonku.

## Final StringProperty

V JavaFX sa často píše:

```java
private final StringProperty userName = new SimpleStringProperty();
```

Tu `final` znamená, že nevymeníme samotnú property:

```java
userName = new SimpleStringProperty("Anna"); // nejde
```

Ale môžeme meniť hodnotu vnútri property:

```java
userName.set("Anna"); // ide
```

Prečo je to dobré:

- UI môže byť napojené na tú istú property.
- Listenery ostanú napojené.
- Binding sa nerozbije výmenou celej property.

Krátko:

```text
final StringProperty = nemeníme krabičku
.set(...)           = meníme obsah krabičky
```

