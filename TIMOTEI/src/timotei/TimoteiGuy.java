package timotei;

import java.util.Random;

//Represents Smart post worker.
public class TimoteiGuy {
    private final String    name;
    private final int       stressLevel;
    
    public TimoteiGuy(){
        Random rand = new Random();
        stressLevel = rand.nextInt( 101 );
        NameGenerator gen = NameGenerator.getInstance();
        name = gen.generateName();
    }
    
    public String getName(){ return name; }
    public int getStressLevel(){ return stressLevel; }
}
