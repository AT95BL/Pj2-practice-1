import java.util.Random;

public abstract class Naoruzanje{

    public Random random = new Random();
    public int jacina;

    public Naoruzanje(){
        this.jacina = random.nextInt();
    }
}