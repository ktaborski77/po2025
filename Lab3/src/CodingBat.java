public class CodingBat {

    // Zadanie z CodingBat: nearHundred
    public boolean nearHundred(int n) {
        // zwraca true, jeśli n jest w odległości 10 od 100 lub 200
        return (Math.abs(100 - n) <= 10 || Math.abs(200 - n) <= 10);
    }
    public void countAndCheck() { //do testowania petli i warunkow w debugowaniu
        int sum = 0;

        for (int i = 1; i <= 5; i++) {
            sum += i; // sum = sum + i
            System.out.println("Iteracja: " + i + ", suma = " + sum);

            // Instrukcja warunkowa wewnątrz pętli
            if (sum % 2 == 0) {
                System.out.println("Suma " + sum + " jest parzysta.");
            } else {
                System.out.println("Suma " + sum + " jest nieparzysta.");
            }
        }
    }

    public static void main(String[] args) {
        CodingBat cb = new CodingBat();

        // Testowanie nearHundred
        System.out.println("Test nearHundred(93): " + cb.nearHundred(93)); // true
        System.out.println("Test nearHundred(89): " + cb.nearHundred(89)); // false
        System.out.println("Test nearHundred(210): " + cb.nearHundred(210)); // true
        System.out.println("Test nearHundred(150): " + cb.nearHundred(150)); // false

        cb.countAndCheck();
    }
}

