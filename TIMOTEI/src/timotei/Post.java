package timotei;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

//Class that communicates directly with GUI and handles all logic
public class Post {
    private static Post             instance;
    private ArrayList<SmartPost>    smartPost;
    private Storage                 storage;
    private DatabaseConnection      database;
    private LogManager              logger;
    
    private Post(){
        storage = new Storage();
        //logger = new Logger();
        smartPost = new ArrayList();
        logger = new LogManager();
    }
    
    //Singleton implementation
    public static Post getInstance(){
        if( instance == null ){
            instance = new Post();
        }
        return instance;
    }
    
    //Initializes all classes and database, if user chose to.
    public void initialize( boolean newDB ){
        if( newDB ){
            File file = new File( "timotei.db" );
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Post.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        database = DatabaseConnection.getInstance();
        
        //Repeating if-statement, because database  cannot be open when creating new file.
        if( newDB ){
            database.initializeDatabase();
            
            //Make default items.
            Item i = new Item( "Kaalimadon lahjapaketti",
                    "Sisältää lukuisia tuotteita jokaisen makuun", 1.2f,
                    0.67f, 0.73f, 0.94f, true);
            i.setId( 1 );
            database.addItem( i );
            i = new Item( "Markku", "Serkku", 75f, 0.7f, 0.53f, 1.86f, false);
            i.setId( 2 );
            database.addItem( i );
            i = new Item( "Korppu", "Markku Serkun herkkukorppu", 0.1f, 0.1f,
                    0.05f, 0.01f, true );
            i.setId( 3 );
            database.addItem( i );
        }
        
        //Init classes.
        database.initItems( storage );
        database.initSmartPost( smartPost );
        database.initPackages( storage );
        database.initLog( logger );
    }
    
    public void closeDatabaseConnection(){
        database.closeDatabase();
    }
    
    //Adds item to database and storage.
    public boolean addItem( Item i ){
        i.setId( storage.getNextID() );
        if( database.addItem( i )){
            storage.addItem( i );
            return true;
        } else{
            System.out.println( "Item wasn't added succesfully!" );
        }
        return false;
    }
    
    //Removes item from database and storage.
    public boolean removeItem( Item i ){
        if( database.removeItem( i )){
            storage.removeItem( i );
            return true;
        }
        return false;
    }
    
    //Updates log with user-driven event.
    public void addLogEvent( LogEvent e ){
        e.setID( logger.getNextID());
        logger.addEvent( e );
        if( e instanceof SendEvent ){
            database.addSendEvent(( SendEvent )e );
        } else{
            database.addItemEvent(( ItemEvent )e );
        }
    }
    
    //Writes all recent activities of user to a file.
    public void writeCheck(){
        logger.writeCheck();
    }
    
    
    public ArrayList<SmartPost> getSmartPosts(){ return smartPost; }
    public ArrayList<Item> getItems(){ return storage.getItems(); }
    public ArrayList<Package> getPackages(){ return storage.getPackages(); }
    public ArrayList<LogEvent> getLog(){ return logger.getLog(); }
}
