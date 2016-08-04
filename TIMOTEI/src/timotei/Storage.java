package timotei;

import java.util.ArrayList;

public class Storage {
    private ArrayList<Item>     items;
    private ArrayList<Package>  packages;
    private int                 highestID;
    
    public Storage(){
        items = new ArrayList();
        packages = new ArrayList( 3 );
        highestID = 0;
    }
    
    //Adds new item and updates highest index for database.
    public void addItem( Item i ){
        items.add( i );
        if( i.getId() > highestID ){
            highestID = i.getId();
        }
    }
    
    public void removeItem( Item i ){
        items.remove( i );
    } 
    
    public int getNextID(){ return highestID + 1; }
    public ArrayList<Item> getItems(){ return items; }
    public ArrayList<Package> getPackages(){ return packages; }
}
