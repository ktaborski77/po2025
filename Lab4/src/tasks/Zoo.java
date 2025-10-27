package tasks;

import animals.*;
import java.util.Random;

public class Zoo {

    private Animal[] animals = new Animal[100];

    public Zoo() {
        fillAnimals();
    }

    // Wypełnia tablicę losowymi zwierzętami
    private void fillAnimals() {
        Random rand = new Random();

        for (int i = 0; i < animals.length; i++) {
            int choice = rand.nextInt(3); // losujemy 0, 1 lub 2
            switch (choice) {
                case 0:
                    animals[i] = new Dog("Dog" + i);
                    break;
                case 1:
                    animals[i] = new Parrot("Parrot" + i);
                    break;
                case 2:
                    animals[i] = new Snake("Snake" + i);
                    break;
            }
        }
    }

    // Oblicza sumę nóg wszystkich zwierząt
    public int getTotalLegs() {
        int sum = 0;
        for (Animal a : animals) {
            sum += a.getLegs(); // używamy getter
        }
        return sum;
    }

    // Wypisuje opis wszystkich zwierząt
    public void printAnimals() {
        for (Animal a : animals) {
            int legs = a.getLegs();
            System.out.println(a.getDescription());
        }
    }

    // Test działania
    public static void main(String[] args) {
        Zoo zoo = new Zoo();
        zoo.printAnimals();
        System.out.println("Łączna liczba nóg w zoo: " + zoo.getTotalLegs());
    }
}



