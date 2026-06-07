# Multimodalni Sistem za Pronalaženje Optimalne Rute unutar Države

Ovaj projekat predstavlja rješenje projektnog zadatka u okviru predmeta **Programski jezici 2** na Elektrotehničkom fakultetu Banja Luka. Aplikacija omogućava pronalaženje i vizualizaciju optimalne rute između gradova kombinovanjem autobuskog i željezničkog saobraćaja.

## Karakteristike aplikacije

* **Modelovanje države:** Država je predstavljena kao matrica gradova dimenzija $n \times m$. Svaki grad posjeduje autobusku ($A\_X\_Y$) i željezničku ($Z\_X\_Y$) stanicu koje čine čvorove u grafu saobraćajne mreže.
* **Višekriterijumska optimizacija:** Pronalaženje top 5 ruta na osnovu:
  * Najkraćeg vremena putovanja (uzimajući u obzir minimalno vrijeme čekanja na presjedanje),
  * Najniže cijene karte,
  * Najmanjeg broja presjedanja.
* **Grafički interfejs (JavaFX):** Interaktivni prikaz mape, forme za izbor parametara, tabelarni pregled optimalne rute i vizualizacija grafa pomoću GraphStream biblioteke.
* **Perzistencija podataka:** Učitavanje topologije iz JSON fajla i perzistentno čuvanje izdatih računa u tekstualnom formatu, uz analizu ukupnog prihoda pri pokretanju.

## Tehnologije i biblioteke
* **Jezik:** Java (verzija: 17)
* **GUI:** JavaFX 
* **Vizualizacija grafova:** JavaFX Canvas
* **Parsiranje podataka:** Jackson 2.17.0
* **Dokumentacija:** JavaDoc

## Kako pokrenuti aplikaciju

1. Klonirajte repozitorijum:
```bash
   git clone https://github.com/DajanaPopovic/TransportApp.git
```

2. Uđite u folder projekta:
```bash
   cd TransportApp
```

3. Pokrenite aplikaciju pomoću Mavena:
```bash
   mvn javafx:run
```

   Ili otvorite projekat u IntelliJ IDEA i pokrenite `Main.java`.

## Struktura projekta
src/main/java/com/transportapp/
├── Main.java
├── generator/       # Generisanje JSON podataka o transportnoj mreži
├── graph/           # Graf, čvorovi, grane i kriterijumi pretrage
├── gui/             # Grafički interfejs (JavaFX)
├── model/           # Modeli podataka (City, Station, Departure...)
└── routing/         # Algoritmi za pronalaženje optimalne rute


## Autor

Dajana Popović
