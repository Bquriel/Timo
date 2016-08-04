package timotei;

import java.util.Date;

//Class represents log entry when item is deleted or created.
public class ItemEvent extends LogEvent {
    private final String    item;
    private final boolean   wasCreated; //True - created, false - deleted
    
    public ItemEvent( String i, boolean created ){
        super(new Date().toString());
        item = i;
        wasCreated = created;
    }
    
    public ItemEvent( String i, boolean created, String stamp ){
        super( stamp );
        item = i;
        wasCreated = created;
    }
    
    @Override
    public String toString(){
        String action = wasCreated ? "Created new" : "Deleted";
        return getTimeStamp() + " - " + action + " item: " + item;
    }
    
    public String getName(){ return item; }
    public int getCreatedAsInt(){ return wasCreated ? 1 : 0; }
}
