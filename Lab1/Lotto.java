import java.util.Random;

public class Lotto {

    public static void main(String[] args) {
        int[] numbers = new int[6];           // tablica do przechowywania wylosowanych liczb
        Random rand = new Random();

        int count = 0;
        while (count < 6) {
            int num = rand.nextInt(49) + 1;   // losuje liczbę z zakresu 1..49
            boolean alreadyDrawn = false;

            // sprawdzanie czy liczba już została wylosowana
            for (int i = 0; i < count; i++) {
                if (numbers[i] == num) {
                    alreadyDrawn = true;
                    break;
                }
            }

            // jeśli liczba jest unikalna, dodaj ją do tablicy
            if (!alreadyDrawn) {
                numbers[count] = num;
                count++;
            }
        }

        // wypisywanie wyników
        System.out.print("Wylosowane liczby: ");
        for (int i = 0; i < numbers.length; i++) {
            System.out.print(numbers[i]);
            if (i < numbers.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println();
    }
}
