package timotei;

import java.util.Date;

//Class represents log entry when Item is sent
public class SendEvent extends LogEvent {
    private final String  senderName;
    private final String  senderSurname;
    private final String  receiverName;
    private final String  receiverSurname;
    private final String  source;
    private final String  destination;
    private final int     distance;
    private final String  sentItem;
    private final String  packageType;
    private final boolean broke;
    
    public SendEvent( String sName, String sSurname, String rName, String rSurname, 
            String src, String dest, int d, String i, String p, boolean br ){
        super( new Date().toString());
        senderName = sName;
        senderSurname = sSurname;
        receiverName = rName;
        receiverSurname = rSurname;
        source = src;
        destination = dest;
        distance = d;
        sentItem = i;
        packageType = p;
        broke = br;
    }
    
    
    public SendEvent( String sName, String sSurname, String rName, String rSurname, 
            String src, String dest, int d, String i, String p, boolean br, String stamp ){
        super( stamp );
        senderName = sName;
        senderSurname = sSurname;
        receiverName = rName;
        receiverSurname = rSurname;
        source = src;
        destination = dest;
        distance = d;
        sentItem = i;
        packageType = p;
        broke = br;
    }    
    
    @Override
    public String toString(){
        String success = broke ? ", Item broke during transaction!" : "";
        return getTimeStamp() + "\n" + "Sender:   " + senderName + " " + senderSurname 
                + "\nReceiver: " + receiverName + " " + receiverSurname + "\n" 
                + "Source:    \t" + source + "\nDestination: " + destination 
                + "\n" + sentItem
                + ", " + packageType + ", " + distance + " km" 
                + success + "\n ";
    }

    public String getSenderName(){ return senderName; }
    public String getSenderSurname(){ return senderSurname; }
    public String getReceiverName(){ return receiverName; }
    public String getReceiverSurname(){ return receiverSurname; }
    public String getSentLocation(){ return source; }
    public String getReceivedLocation(){ return destination; }
    public String getSentItem(){ return sentItem; }
    public String getPackageInfo(){ return packageType; }
    public int getDistance(){ return distance; }
    public int getBrokeAsInt(){ return broke ? 1 : 0; }
}
