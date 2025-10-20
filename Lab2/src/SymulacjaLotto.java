import java.util.*;
import java.time.*;

public class SymulacjaLotto {
    public static void main(String[] args) {
        // Twój kupon Lotto (6 liczb od 1 do 49)
        Set<Integer> mojeLiczby = new HashSet<>(Arrays.asList(3, 12, 25, 33, 42, 47));

        Random rand = new Random();
        long liczbaLosowan = 0;
        boolean trafienie = false;

        // Pomiar czasu start
        Instant start = Instant.now();

        while (!trafienie) {
            liczbaLosowan++;

            // Losowanie 6 różnych liczb
            Set<Integer> los = new HashSet<>();
            while (los.size() < 6) {
                los.add(rand.nextInt(49) + 1); // liczby 1–49
            }

            // Sprawdzenie trafienia
            if (los.equals(mojeLiczby)) {
                trafienie = true;
            }
        }

        // Pomiar czasu koniec
        Instant koniec = Instant.now();
        Duration czas = Duration.between(start, koniec);

        // Wynik
        System.out.println("Pełne trafienie uzyskano!");
        System.out.println("Liczba losowań: " + String.format("%,d", liczbaLosowan));
        System.out.printf("Czas działania programu: %.2f sekundy%n", czas.toMillis() / 1000.0);
    }
}

