package timotei;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

//Keeps log of users actions.
public class LogManager {
    private ArrayList<LogEvent> oldEvents;
    private ArrayList<LogEvent> newEvents;
    private int                 highestID;
    
    public LogManager(){
        oldEvents = new ArrayList();
        newEvents = new ArrayList();
        highestID = 0;
    }
    
    //Adds event to recent events-log.
    public void addEvent( LogEvent event ){
        newEvents.add( event );
        if( event.getID() > highestID ){
            highestID = event.getID();
        }
    }
    
    //Adds event to old events-log.
    public void addOldEvent( LogEvent event ){
        oldEvents.add( event );
        if( event.getID() > highestID ){
            highestID = event.getID();
        }
    }    
    
    //Outputs revcent events to a file.
    public void writeCheck(){
        try {
            BufferedWriter writer = new BufferedWriter( new FileWriter( "check.txt" ));
            for( LogEvent e : newEvents ){
                writer.write( e.toString() + "\n" );
            }
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(LogManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public int getNextID(){ return highestID + 1; }
    public ArrayList<LogEvent> getLog(){ return oldEvents; }
}
