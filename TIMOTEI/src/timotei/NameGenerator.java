package timotei;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

//Class for generating namses for workers.
public class NameGenerator {
    private static NameGenerator    instance;
    private ArrayList<String>       forename;
    private ArrayList<String>       surname;
    
    
    private NameGenerator(){
        forename = new ArrayList(10);
        surname = new ArrayList(10);
        
        //Reads files with names.
        try {
            BufferedReader reader = new BufferedReader( new FileReader("Names.txt"));
            String line;
            while(( line = reader.readLine()) != null ){
                forename.add(line);
            }
            reader.close();
            reader = new BufferedReader( new FileReader("Surnames.txt"));
            while(( line = reader.readLine()) != null ){
                surname.add( line );
            }
            reader.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NameGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(NameGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    //Singleton implementation.
    public static NameGenerator getInstance(){
        if( instance == null ){
            instance = new NameGenerator();
        }
        return instance;
    }
    
    //Returns generated name.
    public String generateName(){
        Random rand = new Random();
        int forenameIndex = rand.nextInt( forename.size() - 1 );
        int surnameIndex = rand.nextInt( surname.size());
        return forename.get( forenameIndex ) + " " + surname.get( surnameIndex );
    }
    
}
