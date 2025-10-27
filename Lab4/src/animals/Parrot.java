package animals;

public class Parrot extends Animal {

    public Parrot(String name) {
        super(name, 2); // papuga ma 2 nogi
    }

    @Override
    public String getDescription() {
        return "Papuga o imieniu " + name + " ma " + legs + " nogi.";
    }
}

