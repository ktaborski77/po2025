public class Choinka {
    public static void main(String[] args) {
        // Sprawdzenie argumentów
        if (args.length != 1) {
            System.out.println("Za duzo argumentow");
            return;
        }

        int wysokosc = Integer.parseInt(args[0]);

        if (wysokosc <= 0) {
            System.out.println("Podaj liczbe dodatnia.");
            return;
        }

        int i = 1;
        while (i <= wysokosc) {
            // Wypisanie i gwiazdek
            for (int j = 1; j <= i; j++) {
                System.out.print("*");
            }

            System.out.println(); // nowa linia
            i++;
        }
    }
}
