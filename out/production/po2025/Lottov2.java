import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;

public class Lottov2 {
    public static void main(String[] args) {
        if (args.length != 6) {
            System.out.println("Musi byc 6 liczb");
            return;
        }

        ArrayList<Integer> userNumbers = new ArrayList<>();
        for (String arg : args) {
            int num = Integer.parseInt(arg); // zakładamy, że to liczba
            if (num < 1 || num > 49) {
                System.out.println("Liczby muszą być w zakresie 1–49!");
                return;
            }
            if (!userNumbers.contains(num)) {
                userNumbers.add(num);
            } else {
                System.out.println("Nie podawaj duplikatów!");
                return;
            }
        }

        ArrayList<Integer> lottoNumbers = new ArrayList<>();
        Random random = new Random();

        while (lottoNumbers.size() < 6) {
            int num = random.nextInt(49) + 1; // losuje liczbę 1–49
            if (!lottoNumbers.contains(num)) {
                lottoNumbers.add(num);
            }
        }

        Collections.sort(userNumbers);
        Collections.sort(lottoNumbers);

        int hits = 0;
        for (int n : userNumbers) {
            if (lottoNumbers.contains(n)) {
                hits++;
            }
        }

        System.out.println("Twoje typy: " + userNumbers);
        System.out.println("Wylosowane liczby: " + lottoNumbers);
        System.out.println("Liczba trafień: " + hits);
    }
}

