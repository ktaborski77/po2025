package animals;

public class Snake extends Animal {

    public Snake(String name) {
        super(name, 0); // wąż ma 0 nóg
    }

    @Override
    public String getDescription() {
        return "Wąż o imieniu " + name + " ma " + legs + " nóg.";
    }
}

