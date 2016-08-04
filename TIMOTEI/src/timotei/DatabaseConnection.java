package timotei;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

//Connection between program and database.
public class DatabaseConnection {
    private static DatabaseConnection   instance;
    private Connection                  connection;
    private Statement                   statement;
    
    private DatabaseConnection(){
        try {
            Class.forName( "org.sqlite.JDBC" );
            connection = DriverManager.getConnection( "jdbc:sqlite:timotei.db" );
            statement = connection.createStatement();
            connection.setAutoCommit( false );
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Singleton implementation
    public static DatabaseConnection getInstance(){
        if( instance == null ){
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public void closeDatabase(){
        try {
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Initializes new database.
    public void initializeDatabase(){
        createDB();
        makeStorage();
        loadPosts();
        makePackages();
    }
    
    //Creates database from schema.
    private void createDB(){
        String db = "";
        try {
            BufferedReader reader = new BufferedReader( new FileReader( "schema.sql" ));
            String line;
            while(( line = reader.readLine()) != null ){
                db += line + "\n";
                
                //Statement can execute only single command. Check if current command has ended.
                if( db.endsWith( ";\n" )){
                    statement.execute( db );
                    connection.commit();
                    db = "";
                }
            }
        } catch (FileNotFoundException ex ) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println( "Database schema not found! Closing program." );
            System.exit( -1 );
        }
    }
    
    //Makes storage as logical connector between posts and items and packages.
    private void makeStorage(){
        String insert = "INSERT INTO 'Storage' VALUES( NULL )";
        try {
            statement.execute( insert );
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Loads SmartPost data from internet and inserts into database.
    private void loadPosts(){
        XMLLoader loader = new XMLLoader();
        ArrayList<String> data = loader.loadPostInfo();
        String insert1 = "INSERT INTO 'SmartPost'( 'name', 'openData', 'storageID' ) "
                + "VALUES( ?, ?, 1 )";
        String insert2 = "INSERT INTO 'Adress'( 'city', 'street' ) "
                + "VALUES( ?, ? )";
        String insert3 = "INSERT INTO 'PostNumber'( 'areaCode' ) VALUES( ? )";
        String insert4 = "INSERT INTO 'GeoPoint'( latitude, longitude ) VALUES( ?, ? )";
        PreparedStatement prep;
        for( String line : data ){
            
            //[0] - areaCode, [1] - city, [2] - street, [3] - date-date, time - time,
            //[4] - name, [5] - latitude, [6] - longitude
            String[] elements = line.split( ";" );
            try {
                prep = connection.prepareStatement( insert1 );
                prep.setString( 1, elements[4] );
                prep.setString( 2 , elements[3] );
                prep.execute();
                
                prep = connection.prepareStatement( insert2 );
                prep.setString( 1, elements[1] );
                prep.setString( 2, elements[2] );
                prep.execute();
                
                prep = connection.prepareStatement( insert3 );
                prep.setString( 1, elements[0] );
                prep.execute();
                
                prep = connection.prepareStatement( insert4 );
                prep.setDouble( 1, Double.valueOf( elements[5] ));
                prep.setDouble( 2, Double.valueOf( elements[6] ));  
                prep.execute();
                prep.close();
                connection.commit();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println( "Something went wrong!" );
                try {
                    connection.rollback();
                } catch (SQLException ex1) {
                    Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }
    }
    
    //Makes defaul packages.
    private void makePackages(){
        String insert1 = "INSERT INTO 'Package'( 'storageID' ) VALUES( 1 )";
        String insert2 = "INSERT INTO 'MaxData'( 'sizeX', 'sizeY', 'sizeZ', "
                + "'weightLimit', 'distanceLimit', 'packageID' ) VALUES ( ?, ?, ?, ?, ?, ? )";
        
        //Database cannot return null, so zero will represent empty value.
        float[] sizesX   = { 250f, 60f, 0 };
        float[] sizesY   = { 250f, 60f, 0 };
        float[] sizesZ   = { 250f, 60f, 0 };
        float[] weights  = { 2.5f, 0.5f, 0 };
        int[] distances  = { 150, 0, 0 };
        for (int i = 0; i < 3; i++) {
            try {
                statement.executeUpdate( insert1 );

                PreparedStatement prep = connection.prepareStatement( insert2 );
                prep.setFloat( 1, sizesX[ i ]);
                prep.setFloat( 2, sizesY[ i ]);
                prep.setFloat( 3, sizesZ[ i ]);
                prep.setFloat( 4, weights[ i ]);
                prep.setInt( 5, distances[ i ]); 
                prep.setInt( 6, i + 1 );
                prep.executeUpdate();
                connection.commit();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    //Adds Item to database and returns true if succeeded.
    public boolean addItem( Item i ){
        boolean success = true;
        String insert1 = "INSERT INTO 'Item'( 'itemID', 'name', 'description', 'weight', 'breakable', 'storageID' ) "
                + "VALUES( ?, ?, ?, ?, ?, 1 )";
        String insert2 = "INSERT INTO 'SizeData'( 'itemID', 'sizeX', 'sizeY', 'sizeZ' ) " +
                "VALUES( ?, ?, ?, ? )";
        Savepoint save = null;
        try {
            save = connection.setSavepoint();
            PreparedStatement prep = connection.prepareStatement( insert1 );
            prep.setInt( 1, i.getId());
            prep.setString( 2, i.getName());
            prep.setString( 3, i.getDescription());
            prep.setFloat( 4, i.getWeight());
            prep.setInt( 5, i.getBreakableAsInt());
            prep.execute();
            prep = connection.prepareStatement( insert2 );
            prep.setInt( 1, i.getId());
            prep.setDouble( 2, i.getSizeX());
            prep.setDouble( 3, i.getSizeY());
            prep.setDouble( 4, i.getSizeZ());
            prep.execute();
            connection.commit();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
            try {
                connection.rollback( save );
            } catch (SQLException ex1) {
                Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return success;
    }
    
    //Removes item from database and returns true if succeeded.
    public boolean removeItem( Item i ){
        boolean success = true;
        String remove1 = "DELETE FROM 'SizeData' WHERE itemid = ?";
        String remove2 = "DELETE FROM 'Item'     WHERE itemid = ?";
        Savepoint save = null;
        try {
            save = connection.setSavepoint();
            PreparedStatement prep = connection.prepareStatement( remove1 );
            prep.setInt( 1, i.getId());
            prep.executeUpdate();
            prep = connection.prepareStatement( remove2 );
            prep.setInt( 1, i.getId());
            prep.executeUpdate();
            connection.commit();
        } catch (SQLException ex ) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
            try {
                connection.rollback( save );
            } catch (SQLException ex1) {
                Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return success;
    }
    
    //Adds SendEvent to database.
    public void addSendEvent( SendEvent e ){
        String insert1 = "INSERT INTO 'History'( 'historyID', 'timeStamp', 'storageID' ) "
                + "VALUES( ?, ?, 1 )"; 
        String insert2 = "INSERT INTO 'SendEvent'( 'historyID', 'sentFromName', "
                + "'sentFromSurname', 'sentToName', 'sentToSurname', 'sentLocation', " 
                + " 'receivedLocation', 'sentItemName', 'packageInfo', "
                + "'routeLength', 'broke' ) "
                + "VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
        try {
            PreparedStatement prep = connection.prepareStatement( insert1 );
            prep.setInt( 1, e.getID());
            prep.setString( 2, e.getTimeStamp());
            prep.executeUpdate();
            
            prep = connection.prepareStatement( insert2 );
            prep.setInt( 1, e.getID());
            prep.setString( 2, e.getSenderName());
            prep.setString( 3, e.getSenderSurname());
            prep.setString( 4, e.getReceiverName());
            prep.setString( 5, e.getReceiverSurname());
            prep.setString( 6, e.getSentLocation());
            prep.setString( 7, e.getReceivedLocation());
            prep.setString( 8, e.getSentItem());
            prep.setString( 9, e.getPackageInfo());
            prep.setInt( 10, e.getDistance());
            prep.setInt( 11, e.getBrokeAsInt());
            prep.executeUpdate();
            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Adds ItemEvent to database.
    public void addItemEvent( ItemEvent e ){
        String insert1 = "INSERT INTO 'History'( 'historyID', 'timeStamp', 'storageID' ) "
                + "VALUES( ?, ?, 1 )"; 
        String insert2 = "INSERT INTO 'ItemEvent'( 'historyID', 'itemName', 'wasCreated' ) "
                + "VALUES( ?, ?, ? )";
        try {
            PreparedStatement prep = connection.prepareStatement( insert1 );
            prep.setInt( 1, e.getID());
            prep.setString( 2, e.getTimeStamp());
            prep.executeUpdate();
            
            prep = connection.prepareStatement( insert2 );
            prep.setInt( 1, e.getID());
            prep.setString( 2, e.getName());
            prep.setInt( 3, e.getCreatedAsInt());
            prep.executeUpdate();
            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Loads items from database to storage.
    public void initItems( Storage s ){
        String query = "SELECT 'Item'.'itemID', 'Item'.'name', 'Item'.'description',"
                + "'Item'.'weight', 'Item'.'breakable', " 
                + "'SizeData'.'sizeX', 'SizeData'.'sizeY', 'SizeData'.'sizeZ' "
                + "FROM 'Item' INNER JOIN " 
                + "'SizeData' ON 'Item'.'itemID' = 'SizeData'.'itemID' "
                + "ORDER BY 'itemID'";
        try {
            ResultSet results = statement.executeQuery( query );
            while( results.next() ){
                int id              = results.getInt( 1 );
                String name         = results.getString( 2 );
                String description  = results.getString( 3 );
                float weight        = results.getFloat( 4 );
                int breakable       = results.getInt( 5 );
                float sizeX         = results.getFloat( 6 );
                float sizeY         = results.getFloat( 7 );
                float sizeZ         = results.getFloat( 8 );
                boolean br          = ( breakable != 0 );
                Item i = new Item( name, description, weight, sizeX, sizeY, sizeZ, br );
                i.setId( id );
                s.addItem( i );
            }
    } catch (SQLException ex) {
        Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
    
    //Loads smartPosts from database to Post.
    public void initSmartPost( ArrayList<SmartPost> s ){
        String query = "SELECT * FROM 'PostData' ";
        try {
            ResultSet results = statement.executeQuery( query );
            while( results.next()){
                String name = results.getString( 1 );
                String openData = results.getString( 2 );
                double longitude = results.getDouble( 3 );
                double latitude = results.getDouble( 4 );
                String city = results.getString( 5 );
                String adress = results.getString( 6 );
                String areaCode = results.getString( 7 );
                SmartPost p = new SmartPost( name, openData, longitude, latitude, 
                city, adress, areaCode );
                s.add( p ); 
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Loads packages from database to storage.
    public void initPackages( Storage s){
        String query = "SELECT * FROM PackageInfo";
        try {
            ResultSet results = statement.executeQuery( query );
            while( results.next()){
                float x = results.getFloat( 2 );
                float y = results.getFloat( 3 );
                float z = results.getFloat( 4 );
                float weight = results.getFloat( 5 );
                int   dist = results.getInt( 6 );
                switch (results.getInt( 1 )){
                    case 1:
                        s.getPackages().add( new PackageClass1( x, y, z, weight, dist ));
                        break;
                    case 2:
                        s.getPackages().add( new PackageClass2( x, y, z, weight, dist ));
                        break;
                    default:
                        s.getPackages().add( new PackageClass3( x, y, z, weight, dist ));
                        break;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Initialize log with send- and item events.
    public void initLog( LogManager log ){
        String query1 = "SELECT * FROM 'ItemData'";
        String query2 = "SELECT * FROM 'SendData'";
        try {
            ResultSet results = statement.executeQuery( query1 );
            while( results.next()){
                int id          = results.getInt( 1 );
                String stamp    = results.getString( 2 );
                String name     = results.getString( 3 );
                int created     = results.getInt( 4 );
                boolean create  = created != 0;
                ItemEvent e = new ItemEvent( name, create, stamp );
                e.setID( id );
                log.addOldEvent( e );
            }
            
            results = statement.executeQuery( query2 );
            while( results.next()){
                int id          = results.getInt( 1 );
                String stamp    = results.getString( 2 );
                String sName    = results.getString( 3 );
                String sSurname = results.getString( 4 );
                String rName    = results.getString( 5 );
                String rSurname = results.getString( 6 );
                String src      = results.getString( 7 );
                String dest     = results.getString( 8 );
                String iName    = results.getString( 9 );
                int distance    = results.getInt( 10 );
                String pack     = results.getString( 11 );
                int broke       = results.getInt( 12 );
                boolean br      = broke != 0;
                SendEvent e = new SendEvent( sName, sSurname, rName, rSurname, 
                            src, dest, distance, iName, pack, br, stamp );
                e.setID( id );
                log.addOldEvent( e );
            }
        } catch (SQLException ex) {
            System.out.println( "Log entry could not be loaded!" );
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}