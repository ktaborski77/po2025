import tasks.Zoo;  // importujemy klasę Zoo

    public class Main {
    public static void main(String[] args) {
        // Tworzymy zoo
        Zoo myZoo = new Zoo();

        // Wypisujemy wszystkie zwierzęta
        myZoo.printAnimals();

        // Wyświetlamy sumę nóg
        System.out.println("Łączna liczba nóg w zoo: " + myZoo.getTotalLegs());
    }
}

